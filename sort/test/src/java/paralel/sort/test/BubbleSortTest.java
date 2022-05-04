package paralel.sort.test;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import paralel.TestUtil;
import parallel.sort.BubbleSort;
import parallel.sort.SortUtil;

public class BubbleSortTest {
	private static final int STANDARD_TEST_SIZE = 100000;
	@Test
	public void testSortSequential() {
		int[] input = BubbleSort.sortSequential(TestUtil.createRandomIntArray(STANDARD_TEST_SIZE));
		assert(SortUtil.isSorted(input));
	}
	
	@Test
	public void testOddEven() {
		int[] input = BubbleSort.sortOddEven(TestUtil.createRandomIntArray(STANDARD_TEST_SIZE));
		assert(SortUtil.isSorted(input));
	}
	
	@Test
	public void testOddEvenParallel() {
		int[] input = BubbleSort.sortOddEvenParallel(TestUtil.createRandomIntArray(STANDARD_TEST_SIZE), 4);
		assert(SortUtil.isSorted(input));
	}
	
	@Test
	public void testMultipleSizes() {
		int[] sizes = {1000, 10000, 100000, 500000};
		for (int size : sizes) {
			compare(size);
		}
	}
	
	private void compare(int size) {
		int[] input = TestUtil.createRandomIntArray(size);
		System.out.println("Input Size: " + size);
		
		{
			int[] copy = Arrays.copyOf(input, size);
			long start = System.nanoTime();
			copy = BubbleSort.sortSequential(copy);
			double diff = (System.nanoTime() - start) / 1000_000.0;
			System.out.printf("BubbleSort.sortSequential:\t\t%8.3f msec\n", diff);
			assert(SortUtil.isSorted(copy));
		}
		
		{
			int[] copy = Arrays.copyOf(input, size);
			long start = System.nanoTime();
			copy = BubbleSort.sortOddEven(copy);
			double diff = (System.nanoTime() - start) / 1000_000.0;
			System.out.printf("BubbleSort.sortOddEven:\t\t\t%8.3f msec\n", diff);
			assert(SortUtil.isSorted(copy));
		}
		
		{
			int[] copy = Arrays.copyOf(input, size);
			long start = System.nanoTime();
			copy = BubbleSort.sortOddEvenParallel(copy, 4);
			double diff = (System.nanoTime() - start) / 1000_000.0;
			System.out.printf("BubbleSort.sortOddEvenParallel:\t\t%8.3f msec\n", diff);
			assert(SortUtil.isSorted(copy));
		}
		System.out.println();
	}
}
