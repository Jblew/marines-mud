/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.system.Config;

/**
 * PL: Klasa słownika, która także zarządza slownikami
 * EN: Dictionary class, which also manages the dictionaries
 * @author jblew
 */
public class Dictionary {

	private final String name;
	private Map<String, String> words = new HashMap<String, String>();
	private static final Map<String, Dictionary> dictionaries = new HashMap<String, Dictionary>();
	private static final Dictionary mainDictionary = getDictionary("main");

        /**
         *
         * @param name_ name of dictionary; nazwa słownika
         * @return dictionary; słownik
         */
        public static Dictionary getDictionary(String name_) {
		try {
			if (dictionaries.containsKey(name_)) {
				return dictionaries.get(name_);
			} else {
				return new Dictionary(name_);
			}
		} catch (IOException ex) {
			Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

        /**
         *
         * @return main dictionary, główny słownik
         */
        public static Dictionary getMainDictionary() {
		return mainDictionary;
	}

	private Dictionary(String name_) throws IOException {
		name = name_;
                Map<String, Map<String, String>> map_ = (Map<String, Map<String, String>>) Config.getMap("dictionaries.dictionaries");
                if(!map_.containsKey(name)) {
                    throw new NoSuchElementException("Dictionary (name="+name+")");
                }

                Map<String, String> map = (Map<String, String>) map_.get(name);

		String dict = null;
		
		for (String k : map.keySet()) {
			String v = map.get(k);
			words.put(k, v);
		}
		words = Collections.unmodifiableMap(words);
		dictionaries.put(name, this);
	}

        /**
         *
         * @param key - key of entry; klucz wpisu
         * @return entry from dictionary; wpis ze słownika
         */
        public String getElement(String key) {
		if (!words.containsKey(key)) {
			throw new NoSuchElementException(key);
		} else {
			return words.get(key);
		}
	}

        /**
         *
         * @return all entries and keys from dictionary; wszystkie klucze i wpisy ze słownika
         */
        public Map<String, String> getElements() {
		return Collections.unmodifiableMap(words);
	}

        /**
         *
         * @param key - key of element
         * @return if element exists; czy element istnieje
         */
        public boolean hasElement(String key) {
		return words.containsKey(key);
	}

        /**
         *
         * @return all keys; wszystkie klucze
         */
        public String[] getKeys() {
		String [] out = new String[words.size()];

		int i = 0;
		for(String elem : words.keySet()) {
			out[i] = elem;
			i++;
		}

		return out;
	}

        /**
         *
         * @return all entries; wszystkie wpisy
         */
        public String[] getValues() {
		String [] out = new String[words.size()];

		int i = 0;
		for(String elem : words.values()) {
			out[i] = elem;
			i++;
		}

		return out;
	}
}
