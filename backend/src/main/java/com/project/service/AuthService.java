package com.project.service;

import com.project.config.HardcodeConstants;
import com.project.config.JWTProvider;
import com.project.dao.UserDao;
import com.project.dto.UserDto;
import com.project.dto.other.AuthResponseDto;
import com.project.dto.other.LoginReqDto;
import com.project.dto.other.RegisterReqDto;
import com.project.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AuthService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsImpl customUserDetails;

    public AuthService(UserDao userDao, PasswordEncoder passwordEncoder, CustomUserDetailsImpl customUserDetails) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetails = customUserDetails;
    }

    public ResponseEntity<UserDto> register(RegisterReqDto registerReqDto) throws RuntimeException {
        try {
            Optional<User> optUser = userDao.findByEmail(registerReqDto.getEmail());
            if (optUser.isPresent()) {
                throw new RuntimeException("User with email: " + registerReqDto.getEmail() + " already exists");
            } else {
                User user = new User();
                user.setEmail(registerReqDto.getEmail());
                user.setFullName(registerReqDto.getFullName());
                user.setPassword(passwordEncoder.encode(registerReqDto.getPassword()));
                user.setProjectSize(HardcodeConstants.PROJECT_SIZE);
                user = userDao.save(user);

                if (user.getUserId() == null) {
                    throw new RuntimeException("User registration request failed");
                } else {
                    UserDto userDto = new UserDto();
                    userDto.setUserId(user.getUserId());
                    userDto.setFullName(user.getFullName());
                    userDto.setEmail(user.getEmail());
                    userDto.setPassword(user.getPassword());
                    userDto.setProjectSize(user.getProjectSize());
                    userDto.setAssignedIssues(new ArrayList<>());

                    return ResponseEntity.ok(userDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<AuthResponseDto> login(LoginReqDto loginReqDto) throws RuntimeException {
        try {
            Optional<User> optUser = userDao.findByEmail(loginReqDto.getEmail());
            if (optUser.isEmpty()) {
                throw new Exception("User with email: " + loginReqDto.getEmail() + " not found");
            } else {
                User user = optUser.get();
                if (!passwordEncoder.matches(loginReqDto.getPassword(), user.getPassword())) {
                    throw new Exception("Invalid password");
                } else {

                    Authentication authentication = authenticate(loginReqDto.getEmail(), loginReqDto.getPassword());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String token = JWTProvider.generateToken(authentication);

                    AuthResponseDto authResponseDto = new AuthResponseDto();
                    authResponseDto.setUserId(user.getUserId());
                    authResponseDto.setFullName(user.getFullName());
                    authResponseDto.setEmail(user.getEmail());
                    authResponseDto.setProjectSize(user.getProjectSize());
                    authResponseDto.setToken(token);
                    return ResponseEntity.ok(authResponseDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private Authentication authenticate(@NotBlank String email, @NotBlank String password) throws RuntimeException {
        UserDetails userDetails = customUserDetails.loadUserByUsername(email);
        if (userDetails == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
    }

    public ResponseEntity<AuthResponseDto> getUserDetails() throws RuntimeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        User user = userDao.findByEmail(authentication.getName()).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + authentication.getName());
        }

        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setUserId(user.getUserId());
        authResponseDto.setFullName(user.getFullName());
        authResponseDto.setEmail(user.getEmail());
        authResponseDto.setProjectSize(user.getProjectSize());
        return ResponseEntity.ok(authResponseDto);
    }
}