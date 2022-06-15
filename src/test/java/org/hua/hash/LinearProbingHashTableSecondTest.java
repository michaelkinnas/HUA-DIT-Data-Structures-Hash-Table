/**
 * This code is part of the lab exercises for the Data Structures course at Harokopio
 * University of Athens, Dept. of Informatics and Telematics.
 */
package org.hua.hash;

import static org.junit.Assert.assertTrue;

import org.hua.exercise2.hash.Dictionary;
import org.hua.exercise2.hash.LinearProbingHashTable;
import org.junit.Test;

public class LinearProbingHashTableSecondTest {

	private static final int SIZE = 10000;

	@Test
	public void testLinearProbingHashTable() {
		Dictionary<Integer, Integer> dict = new LinearProbingHashTable<>();		
		
		
		for (int i=0; i < SIZE; i++) {
			dict.put(i, i);
			assertTrue(dict.get(i).equals(i));
			assertTrue(dict.size() == i + 1);
		}
		
		assertTrue(dict.size() == SIZE);
		
		//create lots of deleted elements
		for (int i=0; i < SIZE; i++) {
			dict.remove(i);
			assertTrue(dict.size() == SIZE - 1 - i);
		}		
		
		
		for (int i=0; i < SIZE; i++) {
			dict.put(i, i);
			assertTrue(dict.size() == 1);
			dict.remove(i);
			assertTrue(dict.size() == 0);
		}		
	}
}
