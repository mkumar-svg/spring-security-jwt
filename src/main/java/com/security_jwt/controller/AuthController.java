package com.security_jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security_jwt.config.JwtUtil;
import com.security_jwt.dto.LoginRequest;
import com.security_jwt.dto.RegisterRequest;
import com.security_jwt.dto.TokenResponse;
import com.security_jwt.entity.Role;
import com.security_jwt.entity.User;
import com.security_jwt.repository.RoleRepository;
import com.security_jwt.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepository userRepo;
    @Autowired
    RoleRepository roleRepo;
    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest req) {

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(encoder.encode(req.getPassword()));

        Role role = roleRepo.findByName("ROLE_USER")
                .orElseThrow();

        user.getRoles().add(role);
        userRepo.save(user);

        return "User Registered Successfully";
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest req) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(), req.getPassword()));

        String token = jwtUtil.generateToken(
                (UserDetails) auth.getPrincipal());

        return new TokenResponse(token);
    }
}
