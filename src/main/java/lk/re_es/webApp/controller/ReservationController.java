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
import java.util.ArrayList;
import java.util.List;



@RestController
@CrossOrigin
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ReservationController() {
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty printing
    }

    // Helper method to get the file where users are stored
    private File getFile() throws IOException {
        File file = new File("database/reservation.json");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            objectMapper.writeValue(file, new ArrayList<User>());
        }

        return file;
    }

    private List<Reservation> readReservationsFromFile() {
        try {
            File file = getFile();
            if (!file.exists()) {
                return new ArrayList<>();
            }
            List<Reservation> reservations = objectMapper.readValue(file, new TypeReference<List<Reservation>>() {
            });
            System.out.println("reservations read from file: " + reservations);
            return reservations;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void writeReservationsToFile(List<User> users) {
        try {
            objectMapper.writeValue(getFile(), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
