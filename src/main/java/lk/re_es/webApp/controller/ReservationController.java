package lk.re_es.webApp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lk.re_es.webApp.dto.LoginResponse;
import lk.re_es.webApp.model.Reservation;
import lk.re_es.webApp.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@CrossOrigin
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ReservationController() {
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    private File getFile() throws IOException {
        File file = new File("database/reservation.json");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            objectMapper.writeValue(file, new ArrayList<Reservation>());
        }

        return file;
    }

    private List<Reservation> readReservationsFromFile() {
        try {
            File file = getFile();
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Reservation>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void writeReservationsToFile(List<Reservation> reservations) {
        try {
            objectMapper.writeValue(getFile(), reservations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        try {
            List<Reservation> reservations = readReservationsFromFile();
            
            // Generate a unique ID for the reservation
            reservation.setId(idGenerator.getAndIncrement());
            
            // Set the status to PENDING if not provided
            if (reservation.getStatus() == null) {
                reservation.setStatus("PENDING");
            }
            
            // Add the new reservation
            reservations.add(reservation);
            
            // Save to file
            writeReservationsToFile(reservations);
            
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating reservation: " + e.getMessage());
        }
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<?> getUserReservations(@PathVariable String email) {
        try {
            List<Reservation> allReservations = readReservationsFromFile();
            List<Reservation> userReservations = allReservations.stream()
                    .filter(r -> r.getUserId().equals(email))
                    .toList();
            return ResponseEntity.ok(userReservations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching reservations: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllReservations() {
        try {
            List<Reservation> reservations = readReservationsFromFile();
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching reservations: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Long id, @RequestBody Reservation updatedReservation) {
        try {
            List<Reservation> reservations = readReservationsFromFile();
            
            for (int i = 0; i < reservations.size(); i++) {
                if (reservations.get(i).getId().equals(id)) {
                    updatedReservation.setId(id); // Ensure ID remains the same
                    reservations.set(i, updatedReservation);
                    writeReservationsToFile(reservations);
                    return ResponseEntity.ok(updatedReservation);
                }
            }
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Reservation not found with id: " + id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating reservation: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        try {
            List<Reservation> reservations = readReservationsFromFile();
            
            boolean removed = reservations.removeIf(r -> r.getId().equals(id));
            
            if (removed) {
                writeReservationsToFile(reservations);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Reservation not found with id: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting reservation: " + e.getMessage());
        }
    }
}
