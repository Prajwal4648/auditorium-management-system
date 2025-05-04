package com.example.auditoriumbooking.repository;

import com.example.auditoriumbooking.model.Auditorium;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriumRepository extends JpaRepository<Auditorium, Long> {
}