package com.project.barberShop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailedAppointmentDto {
    private Long userId;
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String service;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private LocalTime time;
}
