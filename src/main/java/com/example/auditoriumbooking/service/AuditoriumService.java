package com.example.auditoriumbooking.service;

import com.example.auditoriumbooking.model.Auditorium;
import com.example.auditoriumbooking.model.Booking;
import com.example.auditoriumbooking.repository.AuditoriumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriumService {

    @Autowired
    private AuditoriumRepository auditoriumRepository;

    @Autowired
    private BookingService bookingService;

    // Custom exception for auditorium errors
    public static class AuditoriumException extends RuntimeException {
        public AuditoriumException(String message) {
            super(message);
        }
    }

    // Fetch all auditoriums
    public List<Auditorium> getAllAuditoriums() {
        return auditoriumRepository.findAll();
    }

    // Alias for getAllAuditoriums (for UserController)
    public List<Auditorium> listAll() {
        return getAllAuditoriums();
    }

    // Save or update an auditorium with validation
    public Auditorium saveAuditorium(Auditorium auditorium) {
        if (auditorium == null) {
            throw new AuditoriumException("Auditorium cannot be null");
        }
        if (auditorium.getName() == null || auditorium.getName().trim().isEmpty()) {
            throw new AuditoriumException("Auditorium name cannot be empty");
        }
        if (auditorium.getName().length() > 100) {
            throw new AuditoriumException("Auditorium name cannot exceed 100 characters");
        }
        if (auditorium.getCapacity() <= 0) {
            throw new AuditoriumException("Capacity must be positive");
        }
        return auditoriumRepository.save(auditorium);
    }

    // Fetch a single auditorium by ID
    public Auditorium getAuditoriumById(Long id) {
        if (id == null) {
            throw new AuditoriumException("Auditorium ID cannot be null");
        }
        return auditoriumRepository.findById(id)
                .orElseThrow(() -> new AuditoriumException("Auditorium not found with ID: " + id));
    }

    // Alias for getAuditoriumById (for UserController)
    public Auditorium get(Long id) {
        return getAuditoriumById(id);
    }

    // Delete an auditorium by ID
    public void deleteAuditorium(Long id) {
        if (id == null) {
            throw new AuditoriumException("Auditorium ID cannot be null");
        }
        Auditorium auditorium = getAuditoriumById(id);
        List<Booking> bookings = bookingService.getBookingsByAuditorium(auditorium);
        if (!bookings.isEmpty()) {
            throw new AuditoriumException("Cannot delete auditorium with existing bookings");
        }
        auditoriumRepository.deleteById(id);
    }
}