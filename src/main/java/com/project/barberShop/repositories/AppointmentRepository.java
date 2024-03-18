package com.project.barberShop.repositories;

import com.project.barberShop.models.Appointment;
import com.project.barberShop.models.BarberService;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.barberShop.models.User;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUser(User user);
    List<Appointment> findByDate(LocalDate date);
    List<Appointment> findByDateAndService(LocalDate date, BarberService service);
    List<Appointment> findByUserAndDate(User user, LocalDate date);
}
