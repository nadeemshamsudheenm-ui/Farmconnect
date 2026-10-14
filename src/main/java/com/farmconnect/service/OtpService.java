package com.farmconnect.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private static final Logger log = LoggerFactory.getLogger(OtpService.class);
    private static final Duration OTP_VALIDITY = Duration.ofMinutes(10);

    private final SmsService smsService;
    private final SecureRandom secureRandom = new SecureRandom();

    // Map storing clean 10-digit phone -> OtpRecord
    private final Map<String, OtpRecord> otpStore = new ConcurrentHashMap<>();

    public OtpService(SmsService smsService) {
        this.smsService = smsService;
    }

    public static class OtpRecord {
        private final String code;
        private final Instant expiryTime;

        public OtpRecord(String code, Instant expiryTime) {
            this.code = code;
            this.expiryTime = expiryTime;
        }

        public boolean isExpired() {
            return Instant.now().isAfter(expiryTime);
        }

        public String getCode() {
            return code;
        }
    }

    /**
     * Generates a 6-digit OTP, stores it with 10-minute validity, and sends it via Fast2SMS to the phone.
     *
     * @param rawPhone Mobile number
     * @return normalized 10-digit phone number
     */
    public String sendOtp(String rawPhone) {
        String cleanPhone = normalizePhone(rawPhone);
        if (cleanPhone.length() != 10) {
            throw new IllegalArgumentException("Phone number must have at least 10 digits");
        }

        // Generate 6-digit OTP (e.g. 100000 - 999999)
        int randomVal = 100000 + secureRandom.nextInt(900000);
        String otpCode = String.valueOf(randomVal);

        Instant expiry = Instant.now().plus(OTP_VALIDITY);
        otpStore.put(cleanPhone, new OtpRecord(otpCode, expiry));

        log.info("Generated new 6-digit OTP for +91 {}. Valid until {}", cleanPhone, expiry);

        // Dispatch via Fast2SMS
        smsService.sendSmsOtp(cleanPhone, otpCode);

        return cleanPhone;
    }

    /**
     * Verifies the entered OTP against the stored code for the given phone number.
     *
     * @param rawPhone Mobile number
     * @param enteredCode User entered 6-digit code
     * @return true if valid and not expired, false otherwise
     */
    public boolean verifyOtp(String rawPhone, String enteredCode) {
        if (enteredCode == null || enteredCode.isBlank()) {
            return false;
        }

        String cleanPhone = normalizePhone(rawPhone);
        String trimmedCode = enteredCode.trim();

        // 1. Check against active in-memory OTP record
        OtpRecord record = otpStore.get(cleanPhone);
        if (record != null) {
            if (record.isExpired()) {
                otpStore.remove(cleanPhone);
                log.warn("OTP for +91 {} has expired", cleanPhone);
                return false;
            }

            if (record.getCode().equals(trimmedCode)) {
                // One-time use: remove on successful verification
                otpStore.remove(cleanPhone);
                log.info("✓ OTP verified successfully for +91 {}", cleanPhone);
                return true;
            }
        }

        // 2. Testing / Demo fallback code
        if ("123456".equals(trimmedCode)) {
            otpStore.remove(cleanPhone);
            log.info("✓ Master test OTP (123456) accepted for +91 {}", cleanPhone);
            return true;
        }

        log.warn("Invalid OTP entered for +91 {}", cleanPhone);
        return false;
    }

    public String normalizePhone(String raw) {
        if (raw == null) return "";
        String digits = raw.replaceAll("[^0-9]", "");
        if (digits.length() > 10) {
            digits = digits.substring(digits.length() - 10);
        }
        return digits;
    }
}
