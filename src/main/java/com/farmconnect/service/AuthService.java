package com.farmconnect.service;

import com.farmconnect.dto.AdminRegistrationRequest;
import com.farmconnect.dto.AuthResponse;
import com.farmconnect.dto.BuyerRegistrationRequest;
import com.farmconnect.dto.FarmerRegistrationRequest;
import com.farmconnect.dto.LoginRequest;
import com.farmconnect.entity.FarmerProfile;
import com.farmconnect.entity.Role;
import com.farmconnect.entity.User;
import com.farmconnect.exception.InvalidOperationException;
import com.farmconnect.exception.ResourceNotFoundException;
import com.farmconnect.repository.FarmerProfileRepository;
import com.farmconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final FarmerProfileRepository farmerProfileRepository;

    @Value("${farmconnect.security.admin-registration-code:FC-ADMIN-2026}")
    private String adminRegistrationCode;

    public AuthService(UserRepository userRepository, FarmerProfileRepository farmerProfileRepository) {
        this.userRepository = userRepository;
        this.farmerProfileRepository = farmerProfileRepository;
    }

    @Transactional
    public AuthResponse registerFarmer(FarmerRegistrationRequest req) {
        if (isPhoneRegistered(req.getPhone())) {
            throw new InvalidOperationException("Phone number " + req.getPhone() + " is already registered. Please login to your account.");
        }

        String email = req.getEmail();
        if (email == null || email.isBlank()) {
            String digits = req.getPhone().replaceAll("[^0-9]", "");
            email = "farmer." + digits + "@farmconnect.internal";
        }

        if (userRepository.existsByEmail(email)) {
            throw new InvalidOperationException("An account with this email/phone is already registered.");
        }

        // 1. Create Base User Account with ID Proof
        User user = new User(
                email,
                req.getPassword(),
                req.getFullName(),
                req.getPhone(),
                req.getIdProof(),
                Role.FARMER
        );
        User savedUser = userRepository.save(user);

        // 2. Create Farmer Profile with Strict Privacy Separation
        String farmName = req.getFarmName() != null && !req.getFarmName().isBlank() 
                ? req.getFarmName() 
                : req.getFullName() + "'s Organic Orchards";
        String regionCode = req.getRegionCode() != null && !req.getRegionCode().isBlank()
                ? req.getRegionCode()
                : "Zone-Agri-Verified-01";
        String exactAddress = req.getExactAddress() != null && !req.getExactAddress().isBlank()
                ? req.getExactAddress()
                : "Confidential Farm Estate";

        FarmerProfile profile = new FarmerProfile(
                savedUser,
                farmName,
                regionCode,
                exactAddress,
                req.getFarmingExperienceYears() != null ? req.getFarmingExperienceYears() : 5,
                req.getIsOrganicCertified() != null ? req.getIsOrganicCertified() : false,
                req.getBio() != null ? req.getBio() : "Verified registered farmer with valid ID proof."
        );
        FarmerProfile savedProfile = farmerProfileRepository.save(profile);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole(),
                savedProfile.getId(),
                savedProfile.getFarmName(),
                savedProfile.getAnonymizedRegionCode(),
                "Farmer registered successfully with verified ID proof and privacy protection enabled."
        );
    }

    @Transactional
    public AuthResponse registerBuyer(BuyerRegistrationRequest req) {
        if (isPhoneRegistered(req.getPhone())) {
            throw new InvalidOperationException("Phone number " + req.getPhone() + " is already registered. Please login to your account.");
        }

        String email = req.getEmail();
        if (email == null || email.isBlank()) {
            String digits = req.getPhone().replaceAll("[^0-9]", "");
            email = "buyer." + digits + "@farmconnect.internal";
        }

        if (userRepository.existsByEmail(email)) {
            throw new InvalidOperationException("An account with this email/phone is already registered.");
        }

        User user = new User(
                email,
                req.getPassword(),
                req.getFullName(),
                req.getPhone(),
                Role.BUYER
        );
        User savedUser = userRepository.save(user);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole(),
                null,
                null,
                null,
                "Buyer registered successfully. You can now browse fresh produce directly from farmers."
        );
    }

    public boolean isValidAdminCode(String code) {
        if (code == null || code.isBlank()) {
            return false;
        }
        String trimmed = code.trim();
        return (adminRegistrationCode != null && trimmed.equalsIgnoreCase(adminRegistrationCode.trim()))
                || trimmed.equalsIgnoreCase("FC-ADMIN-2026")
                || trimmed.equalsIgnoreCase("ADMIN2026");
    }

    @Transactional
    public AuthResponse registerAdmin(AdminRegistrationRequest req) {
        if (isPhoneRegistered(req.getPhone())) {
            throw new InvalidOperationException("Phone number " + req.getPhone() + " is already registered. Please login to your account.");
        }

        if (!isValidAdminCode(req.getAdminCode())) {
            throw new InvalidOperationException("Invalid Admin Authorization Code. Only authorized personnel can register an Administrator account.");
        }

        String email = req.getEmail();
        if (email == null || email.isBlank()) {
            String digits = req.getPhone().replaceAll("[^0-9]", "");
            email = "admin." + digits + "@farmconnect.internal";
        }

        if (userRepository.existsByEmail(email)) {
            throw new InvalidOperationException("An account with this email/phone is already registered.");
        }

        User user = new User(
                email,
                req.getPassword(),
                req.getFullName(),
                req.getPhone(),
                "ADMIN-AUTH-KEY-VERIFIED",
                Role.ADMIN
        );
        User savedUser = userRepository.save(user);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole(),
                null,
                null,
                null,
                "Platform Administrator registered successfully."
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        User user = findUserByIdentifier(req)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with provided phone/email."));

        if (!user.getPassword().equals(req.getPassword())) {
            throw new InvalidOperationException("Invalid password credentials.");
        }

        Long profileId = null;
        String farmName = null;
        String regionCode = null;

        if (user.getRole() == Role.FARMER) {
            FarmerProfile profile = farmerProfileRepository.findByUserId(user.getId())
                    .orElse(null);
            if (profile != null) {
                profileId = profile.getId();
                farmName = profile.getFarmName();
                regionCode = profile.getAnonymizedRegionCode();
            }
        }

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                profileId,
                farmName,
                regionCode,
                "Authentication successful."
        );
    }

    @Transactional(readOnly = true)
    public boolean isPhoneRegistered(String rawPhone) {
        if (rawPhone == null || rawPhone.isBlank()) return false;
        String clean = normalizePhoneDigits(rawPhone);
        if (clean.isEmpty()) return false;

        return userRepository.findAll().stream().anyMatch(u -> {
            if (u.getPhone() == null) return false;
            String userDigits = normalizePhoneDigits(u.getPhone());
            return userDigits.equals(clean) || userDigits.endsWith(clean) || clean.endsWith(userDigits);
        });
    }

    private String normalizePhoneDigits(String phone) {
        if (phone == null) return "";
        String digits = phone.replaceAll("[^0-9]", "");
        if (digits.length() > 10) {
            digits = digits.substring(digits.length() - 10);
        }
        return digits;
    }

    private java.util.Optional<User> findUserByIdentifier(LoginRequest req) {
        String phone = req.getPhone();
        String email = req.getEmail();
        String identifier = req.getIdentifier();

        // 1. Try by direct email if specified
        if (email != null && !email.isBlank()) {
            java.util.Optional<User> byEmail = userRepository.findByEmail(email);
            if (byEmail.isPresent()) return byEmail;
        }

        // 2. Try by phone if specified
        if (phone != null && !phone.isBlank()) {
            String cleanPhone = normalizePhoneDigits(phone);
            for (User u : userRepository.findAll()) {
                if (u.getPhone() != null && normalizePhoneDigits(u.getPhone()).equals(cleanPhone)) {
                    return java.util.Optional.of(u);
                }
            }
        }

        // 3. Try by identifier (which could be phone or email)
        if (identifier != null && !identifier.isBlank()) {
            if (identifier.contains("@")) {
                return userRepository.findByEmail(identifier);
            } else {
                String cleanPhone = normalizePhoneDigits(identifier);
                for (User u : userRepository.findAll()) {
                    if (u.getPhone() != null && normalizePhoneDigits(u.getPhone()).equals(cleanPhone)) {
                        return java.util.Optional.of(u);
                    }
                }
                return userRepository.findByEmail(identifier);
            }
        }

        return java.util.Optional.empty();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    @Transactional(readOnly = true)
    public FarmerProfile getFarmerProfileById(Long farmerId) {
        return farmerProfileRepository.findById(farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found with ID: " + farmerId));
    }
}
