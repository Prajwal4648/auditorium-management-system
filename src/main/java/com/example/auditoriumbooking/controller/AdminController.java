package com.example.auditoriumbooking.controller;

import com.example.auditoriumbooking.model.Auditorium;
import com.example.auditoriumbooking.model.Booking;
import com.example.auditoriumbooking.service.AuditoriumService;
import com.example.auditoriumbooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AuditoriumService auditoriumService;

    @Autowired
    private BookingService bookingService;

    // ✅ Manage Auditoriums
    @GetMapping("/auditoriums")
    public String manageAuditoriums(Model model) {
        List<Auditorium> auditoriums = auditoriumService.getAllAuditoriums();
        model.addAttribute("auditoriums", auditoriums);
        return "admin/manage-auditoriums";
    }

    @PostMapping("/auditoriums")
    public String addAuditorium(Auditorium auditorium) {
        auditoriumService.saveAuditorium(auditorium);
        return "redirect:/admin/auditoriums";
    }

    // ✅ Manage All Bookings (Admin View)
    @GetMapping("/bookings")
    public String manageBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings(); // Show all bookings
        model.addAttribute("bookings", bookings);
        return "admin/manage-bookings";
    }

    // ✅ Approve Booking
    @PostMapping("/bookings/approve")
    public String approveBooking(@RequestParam("bookingId") Long bookingId) {
        Booking booking = bookingService.getBookingById(bookingId);
        booking.setStatus("CONFIRMED");
        bookingService.saveBooking(booking);
        return "redirect:/admin/bookings";
    }
}
