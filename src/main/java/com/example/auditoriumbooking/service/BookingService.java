package com.example.auditoriumbooking.service;

import com.example.auditoriumbooking.model.Auditorium;
import com.example.auditoriumbooking.model.Booking;
import com.example.auditoriumbooking.model.User;
import com.example.auditoriumbooking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserService userService;

    // Custom exception for booking errors
    public static class BookingException extends RuntimeException {
        public BookingException(String message) {
            super(message);
        }
    }

    // Get all bookings (used by admin)
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Get booking by ID
    public Booking getBookingById(Long id) {
        if (id == null) {
            throw new BookingException("Booking ID cannot be null");
        }
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingException("Booking not found with ID: " + id));
    }

    // Save or update booking with validation
    public void saveBooking(Booking booking) {
        validateBooking(booking);
        bookingRepository.save(booking);
    }

    // Book a new booking (used by UserController)
    public void book(Booking booking) {
        validateBooking(booking);
        // Ensure status is set to PENDING for new bookings
        if (booking.getStatus() == null) {
            booking.setStatus("PENDING");
        }
        bookingRepository.save(booking);
    }

    // Get bookings for a specific user by User object
    public List<Booking> getBookingsByUser(User user) {
        if (user == null) {
            throw new BookingException("User cannot be null");
        }
        return bookingRepository.findByUser(user);
    }

    // Get bookings for a specific user by user ID (used by UserController)
    public List<Booking> listByUser(Long userId) {
        if (userId == null) {
            throw new BookingException("User ID cannot be null");
        }
        User user = userService.findById(userId)
                .orElseThrow(() -> new BookingException("User not found with ID: " + userId));
        return bookingRepository.findByUser(user);
    }

    // Get bookings for a specific auditorium
    public List<Booking> getBookingsByAuditorium(Auditorium auditorium) {
        if (auditorium == null) {
            throw new BookingException("Auditorium cannot be null");
        }
        return bookingRepository.findByAuditorium(auditorium);
    }

    // Approve a booking (used by admin)
    public void approveBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus("APPROVED");
        bookingRepository.save(booking);
    }

    // Cancel a booking (used by admin or user)
    public void cancelBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus("CANCELED");
        bookingRepository.save(booking);
    }

    // Validate booking details
    private void validateBooking(Booking booking) {
        if (booking == null) {
            throw new BookingException("Booking cannot be null");
        }
        if (booking.getAuditorium() == null) {
            throw new BookingException("Auditorium is required");
        }
        if (booking.getUser() == null) {
            throw new BookingException("User is required");
        }
        if (booking.getStartTime() == null || booking.getEndTime() == null) {
            throw new BookingException("Start and end times are required");
        }
        if (!booking.getEndTime().isAfter(booking.getStartTime())) {
            throw new BookingException("End time must be after start time");
        }
        // Check for overlapping bookings
        List<Booking> existingBookings = bookingRepository.findByAuditorium(booking.getAuditorium());
        for (Booking existing : existingBookings) {
            if (!existing.getId().equals(booking.getId()) && // Skip same booking
                    existing.getStatus().equals("APPROVED") && // Only check approved bookings
                    !(booking.getEndTime().isBefore(existing.getStartTime()) ||
                            booking.getStartTime().isAfter(existing.getEndTime()))) {
                throw new BookingException("Booking conflicts with an existing booking");
            }
        }
    }
}