package com.project.barberShop.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phoneNumber")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]{3,255}$")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Column(nullable = false)
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]{3,255}$")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$", message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @Column(nullable = false)
    @Pattern(regexp = "^\\d{10}$")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Column(nullable = false)
    @Size(max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "Password should only contain numbers and letters")
            @NotBlank(message = "Password is required")private String password;
            @ManyToMany(fetch = FetchType.EAGER)@JsonManagedReference
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))private Set<Role>role = new HashSet<>();
            @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
            private List<Appointment>appointments;
}