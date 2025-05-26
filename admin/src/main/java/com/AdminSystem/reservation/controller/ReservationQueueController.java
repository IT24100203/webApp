package com.AdminSystem.reservation.controller;

import com.AdminSystem.reservation.dsa.ReservationQueue;
import com.AdminSystem.reservation.model.Reservation;
import com.AdminSystem.reservation.model.Table;
import com.AdminSystem.reservation.util.AuditLogger;
import com.AdminSystem.reservation.util.FileHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queue")
@CrossOrigin(origins = "*")
public class ReservationQueueController {

    private final ReservationQueue queue = new ReservationQueue();
    private static final String RES_FILE = "data/reservations.txt";
    private static final String TABLES_FILE = "data/tables.txt";

    //  Add reservation to the queue
    @PostMapping("/add")
    public String addToQueue(@RequestBody Reservation reservation) {
        queue.enqueue(reservation);
        AuditLogger.log("Queued reservation: " + reservation.getCustomerName());
        return "Reservation added to queue.";
    }

    //  Process next reservation in the queue
    @PostMapping("/process")
    public String processNextReservation() {
        Reservation next = queue.dequeue();
        if (next == null) {
            return "⚠️ No reservation in queue.";
        }

        List<Reservation> reservations = FileHelper.readList(RES_FILE, Reservation::fromLine);
        List<Table> tables = FileHelper.readList(TABLES_FILE, Table::fromLine);

        int requiredSeats = next.getGuests();

        // 1. Find tables that exactly match the required seat count
        List<Table> matchingTables = tables.stream()
                .filter(t -> t.getSeats() == requiredSeats)
                .toList();

        // 2. Get reserved table IDs at same date and time
        List<Integer> reservedTableIds = reservations.stream()
                .filter(r -> r.getDate().equals(next.getDate()))
                .filter(r -> r.getTime().equals(next.getTime()))
                .map(Reservation::getTableId)
                .toList();

        // 3. Find an available table
        Table assignedTable = matchingTables.stream()
                .filter(t -> !reservedTableIds.contains(t.getId()))
                .findFirst()
                .orElse(null);

        if (assignedTable == null) {
            AuditLogger.log("❌ No table available for " + requiredSeats + " guests at " + next.getDate() + " " + next.getTime());
            return "❌ No available table with " + requiredSeats + " seats at that time.";
        }

        // Assign table ID and save
        next.setTableId(assignedTable.getId());
        reservations.add(next);
        FileHelper.writeList(RES_FILE, reservations, Reservation::toLine);
        AuditLogger.log("✅ Confirmed reservation: " + next.getCustomerName() + " (Table " + assignedTable.getId() + ")");
        return "✅ Reservation confirmed for " + next.getCustomerName() + " on " + next.getDate() + " at " + next.getTime() +
                " (Table " + assignedTable.getId() + ")";
    }

    //  Get all pending reservations
    @GetMapping("/pending")
    public Reservation[] getPendingReservations() {
        return queue.toArray();
    }

    // Get queue size
    @GetMapping("/count")
    public int getQueueCount() {
        return queue.size();
    }
}
