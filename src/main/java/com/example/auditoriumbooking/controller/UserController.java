package com.example.auditoriumbooking.controller;

import com.example.auditoriumbooking.model.Booking;
import com.example.auditoriumbooking.model.Auditorium;
import com.example.auditoriumbooking.model.User;
import com.example.auditoriumbooking.service.AuditoriumService;
import com.example.auditoriumbooking.service.BookingService;
import com.example.auditoriumbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired private AuditoriumService audService;
    @Autowired private BookingService bookService;
    @Autowired private UserService userService;
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @GetMapping("/auditoriums")
    public String list(Model m) {
        m.addAttribute("auds", audService.listAll());
        return "user/list-auditoriums";
    }

    @GetMapping("/book/{id}")
    public String showForm(@PathVariable Long id, Model m) {
        Auditorium a = audService.get(id);
        m.addAttribute("aud", a);
        m.addAttribute("booking", new Booking());
        return "user/view-availability";
    }

    @PostMapping("/book")
    public String doBook(@AuthenticationPrincipal UserDetails ud,
                         @ModelAttribute Booking booking,
                         @RequestParam Long audId,
                         @RequestParam String start,
                         @RequestParam String end,
                         Model m) {
        try {
            User user = userService.findByUsername(ud.getUsername())
                    .orElseThrow(() -> new IllegalStateException("User not found"));
            booking.setAuditorium(audService.get(audId));
            booking.setUser(user);
            booking.setStartTime(LocalDateTime.parse(start, fmt));
            booking.setEndTime(LocalDateTime.parse(end, fmt));
            bookService.book(booking);
            return "redirect:/user/bookings";
        } catch (DateTimeParseException e) {
            m.addAttribute("error", "Invalid date format. Please use YYYY-MM-DD HH:MM.");
            m.addAttribute("aud", audService.get(audId));
            return "user/view-availability";
        } catch (Exception e) {
            m.addAttribute("error", "Booking failed: " + e.getMessage());
            m.addAttribute("aud", audService.get(audId));
            return "user/view-availability";
        }
    }

    @GetMapping("/bookings")
    public String myBookings(@AuthenticationPrincipal UserDetails ud, Model m) {
        User user = userService.findByUsername(ud.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        m.addAttribute("books", bookService.listByUser(user.getId()));
        return "user/my-bookings";
    }
}