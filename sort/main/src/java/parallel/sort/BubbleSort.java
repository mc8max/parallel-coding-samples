package parallel.sort;

import java.util.concurrent.Phaser;

public class BubbleSort {
	/**
	 * Perform the bubble sort in traditional sequential method
	 * @param input - integer array to be sported
	 * @return sorted integer array
	 */
	public static int[] sortSequential(int[] input) {
		for (int i = input.length - 1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (input[j] > input[j + 1]) {
					int temp = input[j];
					input[j] = input[j + 1];
					input[j + 1] = temp;
				}
			}
		}
		return input;
	}

	/**
	 * Perform Odd-Even Bubble Sort algorithm to sort the input array in sequential order. 
	 * @param input - integer array to be sported
	 * @return sorted integer array
	 */
	public static int[] sortOddEven(int[] input) {
		for (int turn = 0; turn < input.length; turn++) {
			if (turn % 2 == 0) {
				// Even Turn
				for (int i = 0; i < input.length - 1; i += 2) {
					if (input[i] > input[i + 1]) {
						int temp = input[i];
						input[i] = input[i + 1];
						input[i + 1] = temp;
					}
				}
			} else {
				// Odd Turn
				for (int i = 1; i < input.length - 1; i += 2) {
					if (input[i] > input[i + 1]) {
						int temp = input[i];
						input[i] = input[i + 1];
						input[i + 1] = temp;
					}
				}
			}
		}
		return input;
	}

	/**
	 * Perform Odd-Even Bubble Sort algorithm to sort the input array in parallel manner.
	 * This API split the sorting into threads and run them in parallel. 
	 *  
	 * @param input - integer array to be sported
	 * @param tasks - number of tasks that can be performed in parallel
	 * @return sorted integer array
	 */
	public static int[] sortOddEvenParallel(int[] input, int tasks) {
		Phaser ph = new Phaser(0);
		ph.bulkRegister(tasks);

		final int chunkSize = computeChunkSize(tasks, input.length);

		Thread[] threads = new Thread[tasks];

		for (int t = 0; t < tasks; t++) {
			final int left = computeLeftIndex(tasks, input.length, t, chunkSize);
			final int rightExclusive = computeRightIndexExclusive(tasks, input.length, t, chunkSize);

			threads[t] = new Thread(() -> {
				for (int turn = 0; turn < input.length; turn++) {
					// Note: chunk size is always even, hence all left indices of tasks are even
					// These even indices ensure non-overlapping data races between threads for each Odd / Even phase. 
					if (turn % 2 == 0) {
						// Even case
						for (int i = left; i < rightExclusive; i += 2) {
							if (input[i] > input[i + 1]) {
								int temp = input[i];
								input[i] = input[i + 1];
								input[i + 1] = temp;
							}
						}
					} else {
						// Odd Case
						for (int i = left + 1; i < rightExclusive; i += 2) {
							if (input[i] > input[i + 1]) {
								int temp = input[i];
								input[i] = input[i + 1];
								input[i + 1] = temp;
							}
						}
					}

					// Barrier to synchronize the threads
					ph.arriveAndAwaitAdvance();
				}
			});

			threads[t].start();
		}

		for (int t = 0; t < tasks; t++) {
			try {
				threads[t].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return input;
	}
	
	/**
	 * Return the chunk size to be allocated into tasks.
	 * The return amount is always even to support for Odd-Even algorithm in Parallel mode.
	 * 
	 * @param tasks - number of tasks (or chunks)
	 * @param n - input size (or number of elements) to be split in chunks
	 * @return the chunk size.
	 */
	static int computeChunkSize(int tasks, int n) {
		// Make sure size is always even
		int d = n / tasks;
		return d % 2 == 0 ? d : d + 1;
	}
	
	/**
	 * Return the left index of a chunk, which is identified by index.
	 * 
	 * @param tasks - number of tasks (or chunks)
	 * @param n - input size (or number of elements) to be split in chunks
	 * @param i - index order of the specified task
	 * @param chunkSize - chunk size
	 * @return left index of an chunk, which is identified by index.
	 */
	static int computeLeftIndex(int tasks, int n, int i, int chunkSize) {
		return i * chunkSize;
	}
	
	/**
	 * Return the right index (exclusive) of a chunk, which is identified by index.
	 * 
	 * @param tasks - number of tasks (or chunks)
	 * @param n - input size (or number of elements) to be split in chunks
	 * @param i - index order of the specified task
	 * @param chunkSize - chunk size
	 * @return the right index (exclusive) of a chunk, which is identified by index.
	 */
	static int computeRightIndexExclusive(int tasks, int n, int i, int chunkSize) {
		// The last chunk will take the last element to:
		// - cover the rest of array, which can be bigger than the chunkSize  
		// - not to throw ArrayOutOfBoundException during Odd-Even swap
		if (i == tasks - 1) {
			return n - 1;
		}
		return i * chunkSize + chunkSize;
	}
}
