/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.remote;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.lib.HelloObject;
import marinesmud.world.Enities;
import marinesmud.world.World;
import marinesmud.world.WorldInterface;
import marinesmud.world.persistence.EnityManagerInterface;
import pl.jblew.code.jutils.IdUseMap;
import pl.jblew.code.jutils.utils.TypeUtils;

/**
 *
 * @author jblew
 */
public class RemoteMethodsDispatcher extends Listener {
    private final MyObjectSpace objectSpace;

    private RemoteMethodsDispatcher() {
        objectSpace = new MyObjectSpace();

        for(Enities e : Enities.values()) {
            objectSpace.register(e.manager.getId(), e.manager);
        }
    }

    public synchronized void registerClasses(Kryo kryo) {
        MyObjectSpace.registerClasses(kryo);
    }

    @Override
    public void connected(Connection conn) {
        objectSpace.addConnection(conn);
    }

    @Override
    public void disconnected(Connection conn) {
        objectSpace.removeConnection(conn);
    }

    public synchronized void registerRemoteObject(int id, Object o) {
        System.out.println("Registering remoteObject("+o.getClass()+") under id "+id);
        objectSpace.register(id, o);
    }

    public synchronized void unregisterRemoteObject(int id) {
        System.out.println("Unregistering remoteObject under id "+id);
        objectSpace.unregister(id);
    }

    @Override
    public void received(Connection conn, Object o) {
        if (o instanceof MethodRequest) {
            try {
                MethodRequest mr = (MethodRequest) o;
                Method m = getClass().getMethod(mr.getMethodName(), mr.getTypes());
                if (m.getAnnotation(Remote.class) != null) {
                    conn.sendTCP(m.invoke(this, mr.getParameters()));
                } else {
                    conn.sendTCP(new NotRemoteMethodException());
                }
            } catch (NoSuchMethodException ex) {
                conn.sendTCP(ex);
                Logger.getLogger(RemoteMethodsDispatcher.class.getName()).log(Level.SEVERE, "Sent exception to connection", ex);
            } catch (SecurityException ex) {
                conn.sendTCP(ex);
                Logger.getLogger(RemoteMethodsDispatcher.class.getName()).log(Level.SEVERE, "Sent exception to connection", ex);
            } catch (IllegalAccessException ex) {
                conn.sendTCP(ex);
                Logger.getLogger(RemoteMethodsDispatcher.class.getName()).log(Level.SEVERE, "Sent exception to connection", ex);
            } catch (IllegalArgumentException ex) {
                conn.sendTCP(ex);
                Logger.getLogger(RemoteMethodsDispatcher.class.getName()).log(Level.SEVERE, "Sent exception to connection", ex);
            } catch (InvocationTargetException ex) {
                conn.sendTCP(ex);
                Logger.getLogger(RemoteMethodsDispatcher.class.getName()).log(Level.SEVERE, "Sent exception to connection", ex);
            }
        }
    }

    @Remote public synchronized HelloObject hello() {
        return new HelloObject();
    }

    @Remote public synchronized Class<?>[] getClassesToRegister() {
        return new Class<?>[]{TypeUtils.getClassArrayClass(), TypeUtils.getObjectArrayClass(), Class.class, MethodRequest.class, HelloObject.class,
        ObjectSpace.InvokeMethod.class, MyObjectSpace.InvokeMethod.class, ObjectSpace.InvokeMethodResult.class, MyObjectSpace.InvokeMethodResult.class,
        WorldInterface.class, EnityManagerInterface.class};
    }

    public static RemoteMethodsDispatcher getInstance() {
        return RemoteMethodsDispatcherHolder.INSTANCE;
    }

    private static class RemoteMethodsDispatcherHolder {
        private static final RemoteMethodsDispatcher INSTANCE = new RemoteMethodsDispatcher();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    private static @interface Remote {
    }
}
