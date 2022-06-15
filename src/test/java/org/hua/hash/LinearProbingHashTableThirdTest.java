/**
 * This code is part of the lab exercises for the Data Structures course at Harokopio
 * University of Athens, Dept. of Informatics and Telematics.
 */
package org.hua.hash;

import static org.junit.Assert.assertTrue;


import java.util.ArrayList;
import java.util.Random;

import org.hua.exercise2.hash.Dictionary;
import org.hua.exercise2.hash.LinearProbingHashTable;
import org.junit.Test;

public class LinearProbingHashTableThirdTest {

	private static final int SIZE = 10000;

	@Test
	public void testLinearProbingHashTable() {
		Dictionary<String, String> dict = new LinearProbingHashTable<>();		

		ArrayList<String> keys = new ArrayList<>();
		ArrayList<String> values = new ArrayList<>();	

		Random random = new Random();	

		assertTrue(dict.isEmpty());

		/**
		 * Testing hash table with elements of type String
		 * Generate random alphanumeric Strings of length 0 to 20 and put them in the queue
		 * then remove them
		 */
		for (int i = 0; i < SIZE; i++) {
			String randomStringKey = random.ints(48, 122)
					.filter(j -> (j <= 57 || j >= 65) && (j <= 90 || j >= 97))
					.limit(random.nextInt(10))
					.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
					.toString();			
			
			String randomStringValue = random.ints(48, 122)
					.filter(j -> (j <= 57 || j >= 65) && (j <= 90 || j >= 97))
					.limit(random.nextInt(10))
					.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
					.toString();
			
			if (!keys.contains(randomStringKey)) { //ensure the same key wont be put into hash table
				dict.put(randomStringKey, randomStringValue);
				
				keys.add(randomStringKey);
				values.add(randomStringValue);
			}
		}		
		
		//check if key value pair is correct
		for (int i = 0; i <  keys.size(); i++) {			
			assertTrue(dict.get(keys.get(i)).equals(values.get(i)));
		}
	}
}
