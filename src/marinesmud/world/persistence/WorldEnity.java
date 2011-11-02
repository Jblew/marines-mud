/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import pl.jblew.code.jutils.utils.FileUtils;
import pl.jblew.code.jutils.utils.SerializationUtils;

/**
 *
 * @author jblew
 */
public abstract class WorldEnity implements Comparable<WorldEnity>, Serializable {
    private static final String DIRNAME = "./world/";
    private static final String EXTENSION = ".wes";
    private transient final int id;
    private transient final boolean created;

    public WorldEnity(int id, File file) {
        created = false;
        FileInputStream fis = null;
        try {
            this.id = id;
            fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            loadVariables(ois);

            ois.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public WorldEnity() {
        created = true;
        int maxId = 0;
        for (WorldEnity e : getManager().getElements()) {
            if (e.getId() > maxId) {
                maxId++;
            }
        }
        this.id = maxId + 1;

        getManager()._addElement(this);
    }

    public WorldEnity(int id) {
        created = true;
        this.id = id;

        getManager()._addElement(this);
    }

    public static synchronized WorldEnity[] loadAll(Class<? extends WorldEnity> cls, String enityName) {
        File dir = new File(DIRNAME + enityName + "/");
        if (!dir.exists()) {
            throw new RuntimeException("Dir for this enity files (name=" + enityName + ") does not exist!");
        }

        WorldEnity[] out = new WorldEnity[dir.list().length];
        int i = 0;
        for (File f : dir.listFiles()) {
            try {
                int id = Integer.valueOf(FileUtils.getFileNameWithoutExtension(f));
                Constructor<? extends WorldEnity> constructor = cls.getDeclaredConstructor(Integer.TYPE, File.class);
                constructor.setAccessible(true);
                out[i] = cls.cast(constructor.newInstance(id, f));
                i++;
            } catch (InstantiationException ex) {
                Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return out;
    }

    @SuppressWarnings("unchecked")
    public static synchronized WorldEnity[] loadAllWithCasting(String enityName, Caster caster) {
        File dir = new File(DIRNAME + enityName + "/");
        if (!dir.exists()) {
            throw new RuntimeException("Dir for this enity files (name=" + enityName + ") does not exist!");
        }

        List<WorldEnity> out = new LinkedList<WorldEnity>();
        int i = 0;
        for (File f : dir.listFiles()) {
            try {
                String[] elemsOfName = f.getName().split("\\.");
                if (elemsOfName.length == 3) {
                    int id = Integer.valueOf(elemsOfName[0]);
                    Class<? extends WorldEnity> cls = caster.castTo(elemsOfName[1]);
                    Constructor<? extends WorldEnity> constructor = cls.getDeclaredConstructor(Integer.TYPE, File.class);
                    constructor.setAccessible(true);
                    out.add((WorldEnity) constructor.newInstance(id, f));
                } else {
                    Logger.getLogger(WorldEnity.class.getName()).log(Level.WARNING, "Element of enity {0} with incorrect name ({1}) was skipped!", new Object[]{enityName, f.getName()});
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchElementException ex) {
                Logger.getLogger(WorldEnity.class.getName()).log(Level.WARNING, "Caster cannot find cast for enity ({0}). Skipping.", f.getName());
            }
        }
        return out.toArray(new WorldEnity[]{});
    }

    public synchronized final int loadVariables(ObjectInput ois) throws IOException {
        Class<?> cls = getClass();

        if (!SerializationUtils.readString(ois).equals(enityName())) {
            throw new RuntimeException("Enity names doesn't match!");
        }

        int elemid = ois.readInt();

        long version = ois.readLong();

        if (version > version()) {
            throw new RuntimeException("Trying to load newer save to older object.");
        }

        while (true) {
            byte b = ois.readByte();
            if (b == 15) {
                String fname = SerializationUtils.readString(ois);
                try {

                    Object value = ois.readObject();
                    _loadField(fname, value, cls);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (b == 25) {
                break;
            } else {
                throw new RuntimeException("Unrecognized type of next entry!");
            }
        }

        return elemid;
    }

    private void _loadField(String fname, Object value, Class<?> baseCls) {
        try {
            Field f = baseCls.getDeclaredField(fname);
            f.setAccessible(true);
            if (f.getAnnotation(WorldEnity.Persistent.class) != null) {
                f.set(this, value);
            } else {
                Logger.getLogger(WorldEnity.class.getName()).log(Level.WARNING, "Cannot set value of not persisten field!");
            }
        } catch (NoSuchFieldException ex) {
            if(baseCls.getSuperclass() != Object.class) {
                _loadField(fname, value, baseCls.getSuperclass());
            }
            else Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, "No such field ''{0}''. Skipping.", fname);
        } catch (SecurityException ex) {
            Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public final synchronized int getId() {
        return id;
    }

    protected final synchronized boolean wasCreated() {
        return created;
    }

    private synchronized void saveVariables(ObjectOutputStream oos) throws IOException {
        SerializationUtils.writeString(oos, enityName());
        oos.writeInt(id);
        oos.writeLong(version());

        Class<?> cls = getClass();
        List<Field> fields = new ArrayList<Field>();
        scanForFields(cls, fields);
        for (Field f : fields) {
            if (!Modifier.isStatic(f.getModifiers())) {
                f.setAccessible(true);
                if (f.getAnnotation(WorldEnity.Persistent.class) != null) {
                    Persistent p = f.getAnnotation(WorldEnity.Persistent.class);
                    try {
                        oos.writeByte(15);
                        SerializationUtils.writeString(oos, f.getName());
                        oos.writeObject(f.get(this));
                    } catch (IllegalArgumentException ex) {
                        throw new RuntimeException("Cannot write field", ex);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException("Cannot write field", ex);
                    }
                } else if (Modifier.isTransient(f.getModifiers())) {
                } else {
                    throw new RuntimeException("Field (name=" + f.getName() + ", class=" + cls + ") is not persistent and not transient!");
                }
            }
        }
        oos.writeByte(25);
    }

    private synchronized void scanForFields(Class<?> cls, List<Field> fields) {
        Class<?> parent = cls.getSuperclass();
        fields.addAll(Arrays.asList(cls.getDeclaredFields()));
        if (parent != Object.class) {
            scanForFields(parent, fields);
        }
    }

    public synchronized final void save() {
        //System.out.println("Saving enity!");
        FileOutputStream fos = null;
        try {
            File file = new File(DIRNAME + enityName() + "/" + id + (needsCasting() ? "." + getCastTo() : "") + EXTENSION);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            saveVariables(oos);

            oos.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(WorldEnity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public synchronized void destruct() {
        File file = new File(DIRNAME + enityName() + "/" + id + EXTENSION);
        if (file.exists()) {
            file.delete();
        }
        getManager()._remove(this);
    }

    @Override
    public int compareTo(WorldEnity e) {
        if (!getClass().isInstance(e)) {
            throw new RuntimeException("Canot compare wnities of different types.");
        }
        return getId() - e.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WorldEnity other = (WorldEnity) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + this.id;
        hash = 19 * hash + (getClass() != null ? getClass().hashCode() : 0);
        return hash;
    }

    public abstract long version();

    public abstract String enityName();

    public abstract boolean needsCasting();

    public abstract String getCastTo();

    public abstract EnityManager<? extends WorldEnity> getManager();

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    protected static @interface Persistent {
    }

    @Aspect
    public static class SettersAspect {
        @Before("set(!transient * WorldEnity.*")
        public void fieldChanged() {
            System.out.println("Field changed.");
        }
    }

    /*public static class ClassInspector {
    private ClassInspector() {}
    private static void inspect(Class<? extends WorldEnity> cls) {
    try {
    cls.getConstructor(Integer.class);
    } catch (NoSuchMethodException ex) {
    throw new ImproperWorldEnityClassException("Class doesn't have constructor with parameters: (int).");
    } catch (SecurityException ex) {
    throw new ImproperWorldEnityClassException("Can't check if class have constructor with parameters: (int). Resson: "+ex);
    }

    try {
    cls.getConstructor(Integer.class, File.class);
    } catch (NoSuchMethodException ex) {
    throw new ImproperWorldEnityClassException("Class doesn't have constructor with parameters: (int, File).");
    } catch (SecurityException ex) {
    throw new ImproperWorldEnityClassException("Can't check if class have constructor with parameters: (int, File). Resson: "+ex);
    }
    }

    public static class ImproperWorldEnityClassException extends RuntimeException {
    private static final long serialVersionUID = -4718257970001324097L;

    public ImproperWorldEnityClassException(String improperiety) {
    super(improperiety);
    }
    }
    }*/
}
