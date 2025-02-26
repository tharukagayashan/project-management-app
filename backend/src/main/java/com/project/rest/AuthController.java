package com.project.rest;

import com.project.dto.UserDto;
import com.project.dto.other.AuthResponseDto;
import com.project.dto.other.LoginReqDto;
import com.project.dto.other.RegisterReqDto;
import com.project.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterReqDto registerReqDto) throws RuntimeException {
        return authService.register(registerReqDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginReqDto loginReqDto) throws RuntimeException {
        return authService.login(loginReqDto);
    }

    @GetMapping("/user-details")
    public ResponseEntity<AuthResponseDto> getUserDetails() throws RuntimeException {
        return authService.getUserDetails();
    }

}
