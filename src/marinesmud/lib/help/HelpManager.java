/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.help;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import marinesmud.world.World;
import pl.jblew.code.jutils.data.containers.ObjectHolder;

/**
 *
 * @author jblew
 */
public class HelpManager {

    private HelpManager() {
    }


    public static HelpTopic[] findTopics(String in) {
        Map<HelpTopic, Integer> tmp_ = new HashMap<HelpTopic, Integer>();
        for (String inWord : in.split(" ")) {
            inWord = inWord.trim().toLowerCase();
            for (HelpTopic entry : World.getInstance().helpTopics.values()) {
                for (String word : entry.getName().split(" ")) {
                    word = word.trim().toLowerCase();
                    if (word.contains(inWord) || word.equalsIgnoreCase(inWord) || word.startsWith(inWord) || word.endsWith(inWord)) {
                        if (tmp_.containsKey(entry)) {
                            tmp_.put(entry, tmp_.get(entry) + 1);
                        } else {
                            tmp_.put(entry, 1);
                        }
                    }
                }
            }
        }
        //System.out.println(tmp_.size());

        SortedMap<ObjectHolder<Integer>, HelpTopic> _tmp = new TreeMap<ObjectHolder<Integer>, HelpTopic>(new Comparator<ObjectHolder<Integer>>() {

            public int compare(ObjectHolder<Integer> t, ObjectHolder<Integer> t1) {
                if (t.getObject() > t1.getObject()) {
                    return -1;
                } else if (t.getObject() == t1.getObject()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        for (HelpTopic entry : tmp_.keySet()) {
            int n = tmp_.get(entry);
            _tmp.put(new ObjectHolder<Integer>(n), entry);
            //System.out.println(entry.getName()+": "+n);
        }

        //System.out.println(_tmp.size());
        HelpTopic[] out = new HelpTopic[_tmp.size()];
        int i = 0;
        for (ObjectHolder<Integer> ohi : _tmp.keySet()) {
            HelpTopic h = _tmp.get(ohi);
            out[i] = h;
            i++;
        }
        return out;
    }

    public static HelpManager getInstance() {
        return HelpManagerHolder.INSTANCE;
    }

    private static class HelpManagerHolder {
        private static final HelpManager INSTANCE = new HelpManager();
    }
}
