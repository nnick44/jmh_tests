/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sample;

import org.openjdk.jmh.annotations.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class SortingBenchmark {

    @Param({"100", "1000", "10000"})
    private int arraySize;

    private int[] ascendingArray;
    private int[] descendingArray;
    private int[] randomArray;

    @Setup(Level.Invocation)
    public void setup() {
        ascendingArray = new int[arraySize];
        descendingArray = new int[arraySize];
        randomArray = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            ascendingArray[i] = i;
            descendingArray[i] = arraySize - i;
            randomArray[i] = (int) (Math.random() * arraySize);
        }
    }

    @Benchmark
    public void testArraysSortAscending() {
        int[] copy = Arrays.copyOf(ascendingArray, ascendingArray.length);
        Arrays.sort(copy);
    }

    @Benchmark
    public void testArraysSortDescending() {
        int[] copy = Arrays.copyOf(descendingArray, descendingArray.length);
        Arrays.sort(copy);
    }

    @Benchmark
    public void testArraysSortRandom() {
        int[] copy = Arrays.copyOf(randomArray, randomArray.length);
        Arrays.sort(copy);
    }

    @Benchmark
    public void testArraysParallelSortAscending() {
        int[] copy = Arrays.copyOf(ascendingArray, ascendingArray.length);
        Arrays.parallelSort(copy);
    }

    @Benchmark
    public void testArraysParallelSortDescending() {
        int[] copy = Arrays.copyOf(descendingArray, descendingArray.length);
        Arrays.parallelSort(copy);
    }

    @Benchmark
    public void testArraysParallelSortRandom() {
        int[] copy = Arrays.copyOf(randomArray, randomArray.length);
        Arrays.parallelSort(copy);
    }

    @Benchmark
    public void testMergeSortAscending() {
        int[] copy = Arrays.copyOf(ascendingArray, ascendingArray.length);
        mergeSort(copy);
    }

    @Benchmark
    public void testMergeSortDescending() {
        int[] copy = Arrays.copyOf(descendingArray, descendingArray.length);
        mergeSort(copy);
    }

    @Benchmark
    public void testMergeSortRandom() {
        int[] copy = Arrays.copyOf(randomArray, randomArray.length);
        mergeSort(copy);
    }

    private void mergeSort(int[] array) {
        if (array.length < 2) return;
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);

        mergeSort(left);
        mergeSort(right);
        merge(array, left, right);
    }

    private void merge(int[] array, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                array[k++] = left[i++];
            } else {
                array[k++] = right[j++];
            }
        }
        while (i < left.length) array[k++] = left[i++];
        while (j < right.length) array[k++] = right[j++];
    }
}