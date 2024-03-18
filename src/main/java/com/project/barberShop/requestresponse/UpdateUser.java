package com.project.barberShop.requestresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUser {

    @Pattern(regexp = "^[a-zA-Zа-яА-Я]{3,255}$")
    private String firstName;
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]{3,255}$")
    private String lastName;
    @Pattern(regexp = "^\\d{10}$")
    private String phoneNumber;
    @Email
    private String email;
    private String currentPassword;
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$",
            message = "Password should only contain numbers and letters")
    private String newPassword;

//    public UpdateUser() {}
//
//    public UpdateUser(String firstName, String lastName, String phoneNumber,
//                      String email, String currentPassword, String newPassword) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.phoneNumber= phoneNumber;
//        this.email = email;
//        this.currentPassword = currentPassword;
//        this.newPassword = newPassword;
//    }
}
