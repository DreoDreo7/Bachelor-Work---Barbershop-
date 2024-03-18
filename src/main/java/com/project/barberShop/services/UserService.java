package com.project.barberShop.services;

import com.project.barberShop.dto.UserDto;
import com.project.barberShop.models.User;
import com.project.barberShop.requestresponse.UpdateUser;

public interface UserService {
    User register(UserDto userDto);
    void updateUserProfile(UpdateUser updateUser);
    User getCurrentAuthenticatedUser();
    void deleteUser(User user);
}