package com.farmconnect.controller;

import com.farmconnect.dto.AdminRegistrationRequest;
import com.farmconnect.dto.AuthResponse;
import com.farmconnect.dto.BuyerRegistrationRequest;
import com.farmconnect.dto.FarmerRegistrationRequest;
import com.farmconnect.dto.LoginRequest;
import com.farmconnect.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/farmer")
    public ResponseEntity<AuthResponse> registerFarmer(@Valid @RequestBody FarmerRegistrationRequest request) {
        AuthResponse response = authService.registerFarmer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/buyer")
    public ResponseEntity<AuthResponse> registerBuyer(@Valid @RequestBody BuyerRegistrationRequest request) {
        AuthResponse response = authService.registerBuyer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody AdminRegistrationRequest request) {
        AuthResponse response = authService.registerAdmin(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/validate-admin-code")
    public ResponseEntity<java.util.Map<String, Object>> validateAdminCode(@RequestParam String code) {
        boolean valid = authService.isValidAdminCode(code);
        return ResponseEntity.ok(java.util.Map.of(
                "valid", valid,
                "message", valid ? "Admin authorization code verified." : "Invalid Admin Authorization Code."
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-phone")
    public ResponseEntity<java.util.Map<String, Object>> checkPhone(@RequestParam String phone) {
        boolean registered = authService.isPhoneRegistered(phone);
        return ResponseEntity.ok(java.util.Map.of(
                "registered", registered,
                "phone", phone,
                "message", registered ? "Phone number is already registered. Please login to your account." : "Phone number is available."
        ));
    }
}
