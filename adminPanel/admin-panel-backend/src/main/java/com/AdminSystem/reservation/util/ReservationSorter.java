package com.AdminSystem.reservation.util;

import com.AdminSystem.reservation.model.Reservation;

import java.util.List;
import java.util.ArrayList;

public class ReservationSorter {

    public static List<Reservation> mergeSort(List<Reservation> list) {
        if (list.size() <= 1) return list;

        int mid = list.size() / 2;
        List<Reservation> left = mergeSort(new ArrayList<>(list.subList(0, mid)));
        List<Reservation> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())));

        return merge(left, right);
    }

    private static List<Reservation> merge(List<Reservation> left, List<Reservation> right) {
        List<Reservation> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).getTime().isBefore(right.get(j).getTime())) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }

        while (i < left.size()) result.add(left.get(i++));
        while (j < right.size()) result.add(right.get(j++));

        return result;
    }
}
