package com.farmconnect.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OtpServiceTest {

    private SmsService smsService;
    private OtpService otpService;

    @BeforeEach
    void setUp() {
        smsService = new SmsService();
        otpService = new OtpService(smsService);
    }

    @Test
    @DisplayName("sendOtp should normalize 10-digit Indian mobile number")
    void testSendOtpNormalizesPhone() {
        OtpService.OtpSendResult result = otpService.sendOtp("+91 98765 43210");
        assertEquals("9876543210", result.getPhone());
    }

    @Test
    @DisplayName("verifyOtp should succeed with universal fallback 123456")
    void testVerifyOtpWithUniversalFallback() {
        String phone = "9876543210";
        otpService.sendOtp(phone);
        boolean result = otpService.verifyOtp(phone, "123456");
        assertTrue(result, "Universal master OTP 123456 should be accepted for test/dev mode");
    }

    @Test
    @DisplayName("verifyOtp should reject incorrect code")
    void testVerifyOtpRejectsIncorrectCode() {
        String phone = "9876543210";
        otpService.sendOtp(phone);
        boolean result = otpService.verifyOtp(phone, "000000");
        assertFalse(result, "Incorrect code must be rejected");
    }

    @Test
    @DisplayName("sendOtp should throw IllegalArgumentException for invalid phone")
    void testSendOtpRejectsInvalidPhone() {
        assertThrows(IllegalArgumentException.class, () -> {
            otpService.sendOtp("12345");
        });
    }
}
