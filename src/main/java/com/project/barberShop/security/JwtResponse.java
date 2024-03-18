package com.project.barberShop.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//@Getter
//@Setter
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber; // Add this line
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String email, String firstName,
                       String lastName, String phoneNumber, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTokenType() {
        return type;
    }

    public String getAccessToken() {
        return token;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        System.out.print(getTokenType());
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
