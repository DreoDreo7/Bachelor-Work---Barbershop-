package com.project.barberShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BarberShopApplication {
	public static void main(String[] args) {
		SpringApplication.run(BarberShopApplication.class, args);
	}
}
//INSERT INTO barbershoptest.role (role_name) VALUES ('ROLE_ADMIN'), ('ROLE_USER');
//INSERT INTO barbershoptest.barber_service (service_name) VALUES ('HAIR'), ('BEARD'), ('HAIR_AND_BEARD');