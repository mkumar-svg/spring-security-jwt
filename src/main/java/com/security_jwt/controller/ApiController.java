package com.security_jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/user/profile")
    public String user() {
        return "USER ACCESS";
    }

    @GetMapping("/admin/dashboard")
    public String admin() {
        return "ADMIN ACCESS";
    }
}