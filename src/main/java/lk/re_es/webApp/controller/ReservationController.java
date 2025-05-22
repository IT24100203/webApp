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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");

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

    private ResponseEntity<?> validateReservation(Reservation reservation) {
        // Check if all required fields are present
        if (reservation.getUserId() == null || reservation.getUserId().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("User ID is required");
        }
        if (reservation.getName() == null || reservation.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Name is required");
        }
        if (reservation.getPhone() == null || reservation.getPhone().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Phone number is required");
        }
        if (reservation.getEmail() == null || reservation.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        if (reservation.getDate() == null) {
            return ResponseEntity.badRequest().body("Date and time is required");
        }

        // Validate email format
        if (!EMAIL_PATTERN.matcher(reservation.getEmail()).matches()) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }

        // Validate phone number format (10 digits)
        if (!PHONE_PATTERN.matcher(reservation.getPhone()).matches()) {
            return ResponseEntity.badRequest().body("Phone number must be 10 digits");
        }

        // Validate number of guests (between 1 and 10)
        if (reservation.getGuests() < 1 || reservation.getGuests() > 10) {
            return ResponseEntity.badRequest().body("Number of guests must be between 1 and 10");
        }

        // Validate reservation date (must be in the future)
        LocalDateTime now = LocalDateTime.now();
        if (reservation.getDate().isBefore(now)) {
            return ResponseEntity.badRequest().body("Reservation date must be in the future");
        }

        // Check for overlapping reservations
        List<Reservation> existingReservations = readReservationsFromFile();
        for (Reservation existing : existingReservations) {
            if (existing.getDate().equals(reservation.getDate())) {
                return ResponseEntity.badRequest().body("A reservation already exists for this date and time");
            }
        }

        return null; // No validation errors
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        try {
            // Validate the reservation
            ResponseEntity<?> validationResponse = validateReservation(reservation);
            if (validationResponse != null) {
                return validationResponse;
            }

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
