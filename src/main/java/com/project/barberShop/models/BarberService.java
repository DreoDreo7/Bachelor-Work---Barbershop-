package com.project.barberShop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

//@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
public class BarberService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @JsonBackReference
    @Column(nullable = false, unique = true)
    private EBarberService serviceName;

    public BarberService(EBarberService serviceName) {
        this.serviceName = serviceName;
    }
}