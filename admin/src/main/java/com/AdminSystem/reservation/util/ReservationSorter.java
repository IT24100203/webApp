package com.AdminSystem.reservation.util;

import com.AdminSystem.reservation.model.Reservation;

public class ReservationSorter {

    // Public method to call from controller or service
    public static Reservation[] mergeSort(Reservation[] array) {
        if (array == null || array.length <= 1) {
            return array;
        }

        return mergeSortRecursive(array, 0, array.length - 1);
    }

    // Recursive merge sort
    private static Reservation[] mergeSortRecursive(Reservation[] array, int left, int right) {
        if (left == right) {
            return new Reservation[]{array[left]};
        }

        int mid = (left + right) / 2;

        Reservation[] leftHalf = mergeSortRecursive(array, left, mid);
        Reservation[] rightHalf = mergeSortRecursive(array, mid + 1, right);

        return merge(leftHalf, rightHalf);
    }

    // Merges two sorted arrays
    private static Reservation[] merge(Reservation[] left, Reservation[] right) {
        Reservation[] result = new Reservation[left.length + right.length];
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            boolean isEarlier =
                    left[i].getDate().isBefore(right[j].getDate()) ||
                            (left[i].getDate().isEqual(right[j].getDate()) &&
                                    left[i].getTime().isBefore(right[j].getTime()));

            if (isEarlier) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }

        while (i < left.length) {
            result[k++] = left[i++];
        }

        while (j < right.length) {
            result[k++] = right[j++];
        }

        return result;
    }
}
