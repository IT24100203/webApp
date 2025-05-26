package com.AdminSystem.reservation.util;

import com.AdminSystem.reservation.dsa.ReservationQueue;
import com.AdminSystem.reservation.model.Reservation;

import java.util.function.Function;
import java.util.List;

public class ReservationQueueManager {

    private static final String FILE = "data/reservations.txt";

    // Load reservations from file into custom ReservationQueue
    public static ReservationQueue loadQueueFromFile() {
        List<Reservation> reservationList = FileHelper.readList(FILE, Reservation::fromLine);
        ReservationQueue queue = new ReservationQueue();

        for (Reservation res : reservationList) {
            queue.enqueue(res);
        }

        return queue;
    }

    //  Save queue contents back to file (overwrite existing file)
    public static void saveQueueToFile(ReservationQueue queue) {
        Reservation[] array = queue.toArray();

        FileHelper.writeList(FILE, List.of(array), Reservation::toLine);
        AuditLogger.log("Queue written to file with " + array.length + " reservations.");
    }

    // Add a reservation directly to file (append)
    public static void appendReservationToFile(Reservation reservation) {
        List<Reservation> existing = FileHelper.readList(FILE, Reservation::fromLine);
        existing.add(reservation);
        FileHelper.writeList(FILE, existing, Reservation::toLine);
        AuditLogger.log("Reservation saved directly to file: " + reservation.getCustomerName());
    }
}
