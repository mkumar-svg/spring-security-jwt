package com.security_jwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class RegisterRequest {
    private String username;
    private String password;
}
