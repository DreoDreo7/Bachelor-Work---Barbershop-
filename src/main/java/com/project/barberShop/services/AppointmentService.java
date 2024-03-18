package com.project.barberShop.services;

import com.project.barberShop.dto.AppointmentDto;
import com.project.barberShop.dto.DetailedAppointmentDto;
import com.project.barberShop.models.Appointment;
import com.project.barberShop.models.User;
import com.project.barberShop.requestresponse.AvailableSlotsRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {
    Appointment createAppointment(AppointmentDto appointmentDto);
    List<AppointmentDto> getAppointmentsForUser(User user);
    List<DetailedAppointmentDto> getAllAppointmentsForDate(LocalDate date);
    void cancelAppointmentByUser(Long appointmentId);
    void cancelAppointmentByAdmin(Long appointmentId);
    List<LocalTime> getAvailableSlotsForDateAndService(AvailableSlotsRequest availableSlotsRequest);
}