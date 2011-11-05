/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 *
 * @author jblew
 */
public abstract class EnityManager<T extends WorldEnity> {
    private final HashMap<Integer, T> elements = new HashMap<Integer, T>();
    private final String enityName;
    private final int id;
    private final int firstId;

    protected EnityManager(Class<T> cls, String enityName, int id, int firstId) {
        this.enityName = enityName;
        this.id = id;
        this.firstId = firstId;

        T[] _elements = (T[]) WorldEnity.loadAll(cls, this.enityName);
        for (T elem : _elements) {
            elements.put(elem.getId(), elem);
        }
        WorldPersistence.getInstance().registerManager(this);
    }

    protected EnityManager(Caster caster, String enityName, int id, int firstId) {
        this.enityName = enityName;
        this.id = id;
        this.firstId = firstId;
        
        T[] _elements = (T[]) WorldEnity.loadAllWithCasting(this.enityName, caster);
        for (T elem : _elements) {
            elements.put(elem.getId(), elem);
        }
        WorldPersistence.getInstance().registerManager(this);
    }

    public synchronized Collection<T> getElements() {
        return elements.values();
    }

    public synchronized boolean elementExists(int id) {
        return elements.containsKey(id);
    }

    public synchronized T getElement(int id) {
        if (!elementExists(id)) {
            throw new NoSuchElementException(enityName + "^" + id);
        }
        return elements.get(id);
    }

    public synchronized void addElement(T elem) {
        elements.put(elem.getId(), elem);
    }

    public synchronized boolean hasId(int id) {
        return elements.containsKey(id);
    }

    public synchronized int size() {
        return elements.size();
    }

    @SuppressWarnings({"unchecked", "unchecked"})
    protected synchronized void _addElement(WorldEnity elem) {
        elements.put(elem.getId(), (T) elem);
    }

    public synchronized void save() {
        //System.out.println("Saving MEN!");
        for (T elem : elements.values()) {
            elem.save();
        }
    }

    public synchronized void _remove(WorldEnity elem) {
        elements.remove(elem.getId());
    }
    
    public synchronized int getId() {
        return id;
    }

    public synchronized int getFirstId() {
        return firstId;
    }

    public synchronized T getFirst() {
        return getElement(firstId);
    }

    public synchronized void init() {
        if(!hasId(getFirstId())) {
            createFirst();
        }
    }

    protected abstract void createFirst();
}
