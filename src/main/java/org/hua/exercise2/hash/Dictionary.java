/**
 * This code is part of the lab exercises for the Data Structures course at Harokopio
 * University of Athens, Dept. of Informatics and Telematics.
 */
package org.hua.exercise2.hash;

import java.util.Iterator;

/**
 * A dictionary
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public interface Dictionary<K, V> extends Iterable<Dictionary.Entry<K, V>> {

	/**
	 * Put a key value pair
	 * 
	 * @param key   a key
	 * @param value a value
	 */
	void put(K key, V value);

	/**
	 * Remove a mapping
	 * 
	 * @param key the key
	 * @return the value
	 */
	V remove(K key);

	/**
	 * Get the value associated with a key
	 * 
	 * @param key the key
	 * @return the value or null
	 */
	V get(K key);

	/**
	 * Check if a key exists
	 * 
	 * @param key the key
	 * @return true if it exists, false otherwise
	 */
	boolean contains(K key);

	/**
	 * Check if empty
	 * 
	 * @return true if empty, false otherwise
	 */
	boolean isEmpty();

	/**
	 * Get the number of elements
	 * 
	 * @return the number of elements
	 */
	int size();

	/**
	 * Clear
	 */
	void clear();

	/**
	 * Get an iterator.
	 * 
	 * @return an iterator
	 */
	Iterator<Entry<K, V>> iterator();

	/**
	 * A dictionary entry.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 */
	interface Entry<K, V> {
		/**
		 * Get the key
		 * @return the key
		 */
		K getKey();

		/**
		 * Get the value
		 * @return the value
		 */
		V getValue();
	}
}
