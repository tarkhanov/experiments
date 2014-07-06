package com.test.java8.javascript;

import java.util.List;

public class QSort {
	
	public static void sort(List<Integer> array) {
		
		quicksort(array);
	}
	
    public static <T extends Comparable<T>> void quicksort(List<T> a) {
                       
        quicksort(a, 0, a.size() - 1);
    }

    // quicksort a[left] to a[right]
    public static <T extends Comparable<T>> void quicksort(List<T> a, int left, int right) {
        if (right <= left) return;
        int i = partition(a, left, right);
        quicksort(a, left, i-1);
        quicksort(a, i+1, right);
    }

    // partition a[left] to a[right], assumes left < right
    private static <T extends Comparable<T>> int partition(List<T> a, int left, int right) {
        int i = left - 1;
        int j = right;
        while (true) {
            while (less(a.get(++i), a.get(right)))      // find item on left to swap
                ;                               // a[right] acts as sentinel
            while (less(a.get(right), a.get(--j)))      // find item on right to swap
                if (j == left) break;           // don't go out-of-bounds
            if (i >= j) break;                  // check if pointers cross
            exch(a, i, j);                      // swap two elements into place
        }
        exch(a, i, right);                      // swap with partition element
        return i;
    }

    // is x < y ?
    private static <T extends Comparable<T>> boolean less(T x, T y) {
        
        return (x.compareTo(y) < 0);
    }

    // exchange a[i] and a[j]
    private static <T> void exch(List<T> a, int i, int j) {
        
        T swap = a.get(i);
        a.set(i, a.get(j));
        a.set(j, swap);
    }

	
}
