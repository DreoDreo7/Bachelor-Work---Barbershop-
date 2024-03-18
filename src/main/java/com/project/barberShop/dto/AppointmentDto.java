package com.project.barberShop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    private Long id;
    private String service;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private LocalTime time;
}