/**
 * This code is part of the lab exercises for the Data Structures course at Harokopio
 * University of Athens, Dept. of Informatics and Telematics.
 */

/**
 * With linear probing, the appropriate bucket stores both the value and the literal key. 
 * So when retrieving, you start in the bucket determined by the hash code and check if the key is the same. 
 * If it's not, you move to the next one and check again, and repeat until you find what you're looking for.
 */
package org.hua.exercise2.hash;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class LinearProbingHashTable<K, V> implements Dictionary<K, V> {
	private int b;
	private int[][] matrix;
	private Entry<K,V>[] array;
	private int size;
	private int deleted;


	
	/**
	 * Constructor
	 */
	@SuppressWarnings("unchecked")
	public LinearProbingHashTable() {
		Random rand = new Random();
		b = 4;
		this.matrix = new int[b][32];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = rand.nextInt(2);				
			}			
		}	
		this.size = 0;
		this.deleted = 0;
		this.array = (Entry<K, V>[])new Entry[(int) Math.pow(2, b)];
		for (int i = 0; i < array.length; i++) {
			this.array[i] = null;
		}		
	}


	/**
	 * It calculates a binary vector from an object's hash code
	 * @param Integer, the hash code of an object
	 * @return A 32 size integer array of binary numbers
	 */
	private int[] calculateHashVector(int hashCode) {
		String binaryHashString = Long.toBinaryString(hashCode & 0xffffffffL | 0x100000000L).substring(1);
		int[] inputVector = new int[32];
		for (int i = 0; i < 32; i++) {
			inputVector[i] = Character.getNumericValue(binaryHashString.charAt(i));		
		}		
		return inputVector;
	}

	/**
	 * It calculates the array position from an object's binary vector
	 * using the hash function matrix
	 * @param A 32 size integer array of binary numbers, the object's binary vector
	 * @return An integer, the position of the array
	 */
	private int calculateArrayPosition(int[] inputVector) {
		int[] positionVector = new int[b];
		int sum = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {				
				sum += matrix[i][j] * inputVector[j];			
			}
			positionVector[i] = sum % 2;
			sum = 0;
		}
		String binStr = Arrays.toString(positionVector);		
		binStr = binStr.replaceAll(", ", "").replace("[", "").replace("]", "");
		return Integer.parseInt(binStr, 2);
	}

	/**
	 * It hashes a given key
	 * @param A key element.
	 * @return An integer, the hashed array position
	 */
	private int hashing(K key) {		
		return calculateArrayPosition(calculateHashVector(key.hashCode()));
	}
	
	
	
	/**
	 * It puts a key-value pair into the hash table
	 * @param A K, key element
	 * @param A V, value element
	 */
	@Override
	public void put(K key, V value) {		
		if (size + deleted >= array.length) {
			//increase b by one and in consequence array length * 2
			b++; 
			rehash();
		}
		insert(key, value);
	}
	
	/**
	 * It inserts an entry object into the array
	 * @param key
	 * @param value
	 */
	private void insert(K key, V value) {
		if (key == null) {
			throw new UnsupportedOperationException("Key cannot be null");
		}
		int cursor = hashing(key);
		Entry<K, V> entry = new EntryImpl<>(key, value);		
		while (true) {
			if (array[cursor] == null || array[cursor].getKey() == null) {
				array[cursor] = entry;		
				size++;
				break;
			} else if (array[cursor].getKey().equals(key)) {
				array[cursor] = entry;			
				break;					
			} else {
				cursor = (cursor + 1) % array.length;			
			}
		}
	}
	
	
	/**
	 * It rehashes all elements of an array into a new array
	 */
	@SuppressWarnings("unchecked")
	private void rehash() {	
		size = 0;
		//create new matrix
		Random rand = new Random();		
		this.matrix = new int[b][32];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = rand.nextInt(2);				
			}			
		}
		//keep old array
		Entry<K, V>[] oldArray = array;	
		//create new array with null values and assign to class array
		Entry<K,V>[] newArray = (Entry<K, V>[])new Entry[(int) Math.pow(2, b)];
		for (int i = 0; i < newArray.length; i++) {
			newArray[i] = null;
		}
		array = newArray;
		//put items from old array to new array using new hash matrix
		for (int i = 0; i < oldArray.length; i++) {
			if (oldArray[i] != null && oldArray[i].getKey() != null) {
				insert(oldArray[i].getKey(), oldArray[i].getValue());
			}
		}
	}
	
	
	/**
	 * It removes an element from the hash table
	 * @param A key element
	 * @return A value element of the found key or null if not found
	 */
	@Override
	public V remove(K key) {
		if (key == null) {
			throw new UnsupportedOperationException("Key cannot be null");
		}
		int cursor = hashing(key);
		int counter = 0;
		V value = null;
		while (counter < array.length) {
			if (array[cursor] == null) {
				break;
			} else if (array[cursor].getKey() == null) {
				cursor = (cursor + 1) % array.length;
				counter++;
			} else if (array[cursor].getKey().equals(key)) {
				value = array[cursor].getValue();
				array[cursor] = new EntryImpl<K, V>(null, null);
				size--;
				break;			
			} else {
				cursor = (cursor + 1) % array.length;
				counter++;
			}
		}
		if (4 * size <= array.length && array.length > 16) {
			b--;
			rehash();
		}		    
		return value;
	}
	
	/**
	 * It returns an element from the hash table
	 * @param A key element
	 * @return A value element of the found key or null if not found
	 */
	@Override
	public V get(K key) {
		if (key == null) {
			throw new UnsupportedOperationException("Key cannot be null");
		}
		int cursor = hashing(key);
		int counter = 0;
		V value = null;
		while (counter < array.length) {
			if (array[cursor] == null) {
				break;
			} else if (array[cursor].getKey().equals(key)) {
				value = array[cursor].getValue();
				break;
			} else {
				cursor = (cursor + 1) % array.length;
				counter++;
			}
		}
		return value;
	}

	/**
	 * Returns true if given key is found in the hash table
	 * @param A key element
	 * @return true if found, false otherwise
	 */
	@Override
	public boolean contains(K key) {
		if (key == null) {
			throw new UnsupportedOperationException("Key cannot be null");
		}
		int cursor = hashing(key);
		int counter = 0;

		while (counter < array.length) {			
			if (array[cursor] == null) {
				break;
			} else if (key == array[cursor].getKey()) {
				return true;
			} else {
				cursor = (cursor + 1) % array.length;
				counter++;
			}
		}		
		return false;
	}
	
	/**
	 * Returns true if size is 0. False otherwise
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Returns the size of the hashtable
	 */
	@Override
	public int size() {		
		return size;
	}
	
	/**
	 * Clears the hashtable by creating a new array 
	 * and hash function similar to the constructor
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		Random rand = new Random();
		b = 4;
		this.matrix = new int[b][32];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = rand.nextInt(2);				
			}			
		}	
		this.size = 0;
		this.deleted = 0;
		this.array = (Entry<K, V>[])new Entry[(int) Math.pow(2, b)];
		for (int i = 0; i < array.length; i++) {
			this.array[i] = null;
		}		
	}
	
	/**
	 * Returns an iterator object from the HashIterator class
	 */
	@Override
	public Iterator<Entry<K, V>> iterator() {
		return new HashIterator();		
	}
	
	/**
	 * Iterator implementation 	 
	 */
	private class HashIterator implements Iterator<Entry<K, V>> {
		private int counter;
		private int i;

		public HashIterator() {
			this.counter = size;
			this.i = 0;
		}
		
		/**
		 * Returns true if there are more elements in the hashtable, false otherwise
		 */
		@Override
		public boolean hasNext() {
			//In testing
			return (counter > 0);
		}
		
		/**
		 * Returns the next element in the hashtable each time.
		 * @throws NoSuchElementException if there are no more
		 * elements to return.
		 */
		@Override
		public Entry<K, V> next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Entry<K, V> entry = null;

			while (true) {
				if (array[i] != null && array[i].getKey() != null) {
					entry = array[i];
					i++;
					break;
				} else {
					i++;
				}
			}
			counter--;
			return entry;
		}
	}
	
	
	/**	
	 * Entry implementation
	 * @param <K> The key of the entry
	 * @param <V> The value of the entry
	 */
	private static class EntryImpl<K,V> implements Dictionary.Entry<K, V> {
		private K key;
		private V value;
		
		/**
		 * Constructor
		 * @param key
		 * @param value
		 */
		public EntryImpl(K key, V value) {			
			this.key = key;
			this.value = value;
		}
		
		/**
		 * Returns the key of the entry
		 */
		@Override
		public K getKey() {			
			return key;
		}
		
		/**
		 * Returns the value of the object
		 */
		@Override
		public V getValue() {			
			return value;
		}
	}
}
