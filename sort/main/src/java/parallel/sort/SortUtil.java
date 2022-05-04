package parallel.sort;

import java.util.stream.IntStream;

public class SortUtil {
	public static final int SEQUENTIAL_THRESHOLD = 5000;

	/**
	 * Check if an input array is sorted
	 * @param input - integer array
	 * @return true or false
	 */
	public static boolean isSorted(int[] input) {
		if (input.length < SEQUENTIAL_THRESHOLD) {
			return isSortedSequential(input);
		}
		return isSortedParallel(input);
	}

	static boolean isSortedSequential(int[] input) {
		for (int i = 0; i < input.length - 1; i++) {
			if (input[i] > input[i + 1]) {
				return false;
			}
		}
		return true;
	}

	static boolean isSortedParallel(int[] input) {
		return IntStream.range(0, input.length - 1)
				.parallel()
				.allMatch(i -> input[i] <= input[i + 1]);
	}
}
