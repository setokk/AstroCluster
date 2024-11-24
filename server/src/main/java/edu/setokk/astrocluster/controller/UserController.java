package edu.setokk.astrocluster.controller;

import edu.setokk.astrocluster.auth.JwtUtils;
import edu.setokk.astrocluster.model.dto.UserDto;
import edu.setokk.astrocluster.request.LoginRequest;
import edu.setokk.astrocluster.request.RegisterRequest;
import edu.setokk.astrocluster.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        loginRequest.validate();
        UserDto user = userService.loginUser(loginRequest);
        return ResponseEntity.ok(JwtUtils.generateJWT(user)); // Generate JWT Token
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        registerRequest.validate();
        UserDto user = userService.registerUser(registerRequest);
        return ResponseEntity.ok(JwtUtils.generateJWT(user)); // Generate JWT Token
    }
}
