package com.project.barberShop.services.Impl;

import com.project.barberShop.dto.UserDto;
import com.project.barberShop.exceptions.ConflictException;
import com.project.barberShop.models.ERole;
import com.project.barberShop.models.Role;
import com.project.barberShop.models.User;
import com.project.barberShop.repositories.RoleRepository;
import com.project.barberShop.repositories.UserRepository;
import com.project.barberShop.requestresponse.UpdateUser;
import com.project.barberShop.security.services.UserDetailsImpl;
import com.project.barberShop.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(UserDto registeredUserDto) {
        if (userRepository.existsByEmail(registeredUserDto.getEmail())) {
            throw new ConflictException("Email is already used.");
        }

        if (userRepository.existsByPhoneNumber(registeredUserDto.getPhoneNumber())) {
            throw new ConflictException("Mobile number is already used.");
        }

        User user = User.builder()
                .firstName(registeredUserDto.getFirstName())
                .lastName(registeredUserDto.getLastName())
                .email(registeredUserDto.getEmail())
                .phoneNumber(registeredUserDto.getPhoneNumber())
                .password(passwordEncoder.encode(registeredUserDto.getPassword()))
                .build();

        Role defaultRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
        user.getRole().add(defaultRole);

        return userRepository.save(user);
    }

    @Override
    public void updateUserProfile(UpdateUser updatedUser) {
        User authenticatedUser = getCurrentAuthenticatedUser();

        if (StringUtils.hasText(updatedUser.getFirstName())
                && !updatedUser.getFirstName().equals(authenticatedUser.getFirstName())) {
            authenticatedUser.setFirstName(updatedUser.getFirstName());
        }

        if (StringUtils.hasText(updatedUser.getLastName())
                && !updatedUser.getLastName().equals(authenticatedUser.getLastName())) {
            authenticatedUser.setLastName(updatedUser.getLastName());
        }

        if (StringUtils.hasLength(updatedUser.getPhoneNumber())
                && !updatedUser.getPhoneNumber().equals(authenticatedUser.getPhoneNumber())
                && userRepository.existsByPhoneNumber(updatedUser.getPhoneNumber())) {
            throw new ConflictException("User with this number " + updatedUser.getPhoneNumber() + " already exist");
        } else if (StringUtils.hasLength(updatedUser.getPhoneNumber())
                && !updatedUser.getPhoneNumber().equals(authenticatedUser.getPhoneNumber())) {
            authenticatedUser.setPhoneNumber(updatedUser.getPhoneNumber());
        }

        if (StringUtils.hasLength(updatedUser.getNewPassword())) {
            if (passwordEncoder.matches(updatedUser.getCurrentPassword(),
                    authenticatedUser.getPassword())) {
                authenticatedUser.setPassword(passwordEncoder.encode(updatedUser.getNewPassword()));
            } else {
                throw new SecurityException("Current password is incorrect.");
            }
        }
        userRepository.save(authenticatedUser);
    }

    @Override
    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("No user currently authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetailsImpl)) {
            throw new SecurityException("Current principal is not a UserDetailsImpl");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        String email = userDetails.getUsername();

        return userRepository.findByEmail(email).orElseThrow(() ->
                new SecurityException("Authenticated user not found in database"));
    }

    @Override
    public void deleteUser(User user) {
        User authenticatedUser = getCurrentAuthenticatedUser();

        if (!authenticatedUser.getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can delete only your profile");
        }

        userRepository.delete(user);
    }
}

