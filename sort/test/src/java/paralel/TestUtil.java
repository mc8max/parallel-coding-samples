package paralel;

import java.util.Random;

public class TestUtil {
	/**
	 * Return an array of random integers.
	 * @param inputSize - number of elements to be created
	 * @return an array of random integers
	 */
	public static int[] createRandomIntArray(int inputSize) {
		Random r = new Random();
		int[] array = new int[inputSize];
		for (int i = 0; i < inputSize; i++) {
			array[i] = r.nextInt();
		}
		return array;
	}
	
	/**
	 * Return an array of integers in descending orders.
	 * @param inputSize - number of elements to be created
	 * @return an array of integers in descending orders
	 */
	public static int[] createIntDescendingArray(int inputSize) {
		int[] array = new int[inputSize];
		for (int i = 0; i < inputSize; i++) {
			array[i] = inputSize - i;
		}
		return array;
		
	}
}
