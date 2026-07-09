package com.example.aiwriter.service;

import com.example.aiwriter.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(String username, String password, String email);

    AuthResponse login(String username, String password);
}
