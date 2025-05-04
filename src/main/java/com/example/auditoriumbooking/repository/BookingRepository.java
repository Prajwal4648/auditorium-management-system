package com.example.auditoriumbooking.repository;

import com.example.auditoriumbooking.model.Auditorium;
import com.example.auditoriumbooking.model.Booking;
import com.example.auditoriumbooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // ✅ Fetch bookings by user
    List<Booking> findByUser(User user);

    // ✅ Fetch bookings by auditorium
    List<Booking> findByAuditorium(Auditorium auditorium);
}
