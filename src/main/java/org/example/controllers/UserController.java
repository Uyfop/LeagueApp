package org.example.controllers;

import org.example.Security.JwtTokenProvider;
import org.example.services.UserService;
import org.example.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User signUpRequest) {
        if (signUpRequest.getPassword() == null || signUpRequest.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password cannot be null or empty.");
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email address already in use.");
        }

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        User newUser = new User();
        newUser.setEmail(signUpRequest.getEmail());
        newUser.setPassword(encodedPassword);
        newUser.setUserType(signUpRequest.getUserType());
        userService.saveUser(newUser);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody User loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        User authenticatedUser = userService.getUserByEmail(loginRequest.getEmail());

        if(authenticatedUser!=null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(loginRequest.getEmail());
            return ResponseEntity.ok(token);
        }else {
            return ResponseEntity.badRequest().body("No one with that email is registered");
        }

    }
}
