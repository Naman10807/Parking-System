package com.parking.service;

import com.parking.dto.request.LoginRequest;
import com.parking.dto.request.RegisterRequest;
import com.parking.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
