package com.project.barberShop.controllers;

import com.project.barberShop.dto.AppointmentDto;
import com.project.barberShop.dto.UserDto;
import com.project.barberShop.exceptions.ConflictException;
import com.project.barberShop.models.Appointment;
import com.project.barberShop.models.User;
import com.project.barberShop.repositories.UserRepository;
import com.project.barberShop.requestresponse.AvailableSlotsRequest;
import com.project.barberShop.requestresponse.LoginRequest;
import com.project.barberShop.requestresponse.UpdateUser;
import com.project.barberShop.security.JwtResponse;
import com.project.barberShop.security.jwt.JwtUtils;
import com.project.barberShop.security.services.UserDetailsImpl;
import com.project.barberShop.services.AppointmentService;
import com.project.barberShop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/registration")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto) {
        User user = userService.register(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getEmail(),
                    userDetails.getFirstName(), userDetails.getLastName(), userDetails.getPhoneNumber(), roles));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email and / or password");
        }
    }

    @PutMapping("/updateProfile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> updateProfile(@Valid @RequestBody UpdateUser updateUser) {
        userService.updateUserProfile(updateUser);
        return ResponseEntity.ok("Profile updated successfully.");
    }

    @DeleteMapping("/deleteProfile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteProfile(@Valid @RequestBody User user) {
        userService.deleteUser(user);
        return ResponseEntity.ok("Profile deleted successfully.");
    }

    @PostMapping("/appointments/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Appointment> createAppointment(@RequestBody
                                                         AppointmentDto appointmentDto) {
        Appointment appointment = appointmentService.createAppointment(appointmentDto);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @PostMapping("/appointments/available")
    public ResponseEntity<?> getAvailableSlots(@RequestBody AvailableSlotsRequest availableSlotsRequest) {
        List<LocalTime> availableSlots = appointmentService.getAvailableSlotsForDateAndService(availableSlotsRequest);
        return ResponseEntity.ok(availableSlots);
    }

    @GetMapping("/appointments")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<AppointmentDto>> getUserAppointments() {
        User user = userService.getCurrentAuthenticatedUser();
        List<AppointmentDto> appointments = appointmentService.getAppointmentsForUser(user);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @DeleteMapping("/appointments/{appointmentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> cancelAppointmentAsUser(@PathVariable Long
                                                                  appointmentId) {
        try {
            User user = userService.getCurrentAuthenticatedUser();
            appointmentService.cancelAppointmentByUser(appointmentId);
            return ResponseEntity.ok("Appointment cancelled successfully");
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);
    }
}