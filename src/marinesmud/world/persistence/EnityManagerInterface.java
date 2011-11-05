/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.world.persistence;

import java.util.Collection;

/**
 *
 * @author jblew
 */
public interface EnityManagerInterface<T extends WorldEnity> {
    public Collection<T> getElements();

    public boolean elementExists(int id);

    public T getElement(int id);

    public void addElement(T elem);

    public boolean hasId(int id);

    public int size();
}
