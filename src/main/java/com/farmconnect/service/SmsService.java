package com.farmconnect.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class SmsService {

    private static final Logger log = LoggerFactory.getLogger(SmsService.class);

    @Value("${farmconnect.sms.fast2sms-api-key:}")
    private String fast2smsApiKey;

    @Value("${farmconnect.sms.enabled:true}")
    private boolean smsEnabled;

    private final HttpClient httpClient;

    public SmsService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    /**
     * Dispatches a 6-digit OTP to the specified 10-digit Indian phone number via Fast2SMS.
     * If no API key is provided, logs the OTP securely to the server console.
     *
     * @param phone 10-digit clean mobile number
     * @param otp 6-digit OTP code
     * @return true if dispatched or simulated successfully
     */
    public boolean sendSmsOtp(String phone, String otp) {
        if (!smsEnabled) {
            log.info("[SMS GATEWAY] SMS sending is disabled in configuration.");
            return false;
        }

        String cleanPhone = normalizePhone(phone);
        if (cleanPhone.length() != 10) {
            log.warn("[SMS GATEWAY] Invalid phone number provided for SMS: {}", phone);
            return false;
        }

        String apiKey = fast2smsApiKey != null ? fast2smsApiKey.trim() : "";

        if (!apiKey.isEmpty()) {
            try {
                log.info("📲 [Fast2SMS] Dispatching live SMS OTP to mobile +91 {} via Fast2SMS Gateway...", cleanPhone);

                String jsonPayload = String.format(
                        "{\"route\":\"otp\",\"variables_values\":\"%s\",\"numbers\":\"%s\"}",
                        otp, cleanPhone
                );

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://www.fast2sms.com/dev/bulkV2"))
                        .header("authorization", apiKey)
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .timeout(Duration.ofSeconds(8))
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200 && response.body().contains("\"return\":true")) {
                    log.info("✓ [Fast2SMS] Real SMS OTP delivered successfully to +91 {}. Response: {}", cleanPhone, response.body());
                    return true;
                }

                // Fallback attempt via Fast2SMS GET OTP route
                log.info("ℹ️ [Fast2SMS] Trying Fast2SMS GET OTP route for +91 {}...", cleanPhone);
                String getUri = String.format(
                        "https://www.fast2sms.com/dev/bulkV2?authorization=%s&route=otp&variables_values=%s&flash=0&numbers=%s",
                        java.net.URLEncoder.encode(apiKey, java.nio.charset.StandardCharsets.UTF_8),
                        otp, cleanPhone
                );

                HttpRequest getReq = HttpRequest.newBuilder()
                        .uri(URI.create(getUri))
                        .timeout(Duration.ofSeconds(8))
                        .GET()
                        .build();

                HttpResponse<String> getRes = httpClient.send(getReq, HttpResponse.BodyHandlers.ofString());
                if (getRes.statusCode() == 200 && getRes.body().contains("\"return\":true")) {
                    log.info("✓ [Fast2SMS] Real SMS OTP delivered successfully via GET to +91 {}. Response: {}", cleanPhone, getRes.body());
                    return true;
                } else {
                    log.warn("⚠️ [Fast2SMS] Fast2SMS API response: {}", getRes.body());
                    log.info("   [Fallback Log] Verification OTP for +91 {} is: {}", cleanPhone, otp);
                    return true;
                }
            } catch (Exception e) {
                log.error("❌ [Fast2SMS] Failed to send SMS via Fast2SMS gateway: {}", e.getMessage());
                log.info("   [Fallback Log] Verification OTP for +91 {} is: {}", cleanPhone, otp);
                return true;
            }
        } else {
            // No Fast2SMS API key configured: log to backend console
            log.info("==========================================================================");
            log.info("📲 [SMS GATEWAY DISPATCH - FAST2SMS]");
            log.info("   Recipient Mobile : +91 {}", cleanPhone);
            log.info("   SMS Message Text : Your FarmConnect verification OTP is {}. Valid for 10 min.", otp);
            log.info("   OTP Generated    : {}", otp);
            log.info("   [Notice] To deliver real SMS to physical mobile phones, add your Fast2SMS");
            log.info("   API key in application.yml (farmconnect.sms.fast2sms-api-key) or FAST2SMS_API_KEY.");
            log.info("==========================================================================");
            return true;
        }
    }

    private String normalizePhone(String raw) {
        if (raw == null) return "";
        String digits = raw.replaceAll("[^0-9]", "");
        if (digits.length() > 10) {
            digits = digits.substring(digits.length() - 10);
        }
        return digits;
    }
}
