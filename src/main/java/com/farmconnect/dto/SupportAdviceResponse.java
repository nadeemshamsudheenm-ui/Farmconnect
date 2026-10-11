package com.farmconnect.dto;

import com.farmconnect.entity.Role;

import java.time.LocalDateTime;

public class SupportAdviceResponse {
    private Long id;
    private Long responderId;
    private String responderName;
    private Role responderRole;
    private String guidanceText;
    private String recommendedResources;
    private LocalDateTime createdAt;

    public SupportAdviceResponse() {
    }

    public SupportAdviceResponse(Long id, Long responderId, String responderName,
                                 Role responderRole, String guidanceText,
                                 String recommendedResources, LocalDateTime createdAt) {
        this.id = id;
        this.responderId = responderId;
        this.responderName = responderName;
        this.responderRole = responderRole;
        this.guidanceText = guidanceText;
        this.recommendedResources = recommendedResources;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResponderId() {
        return responderId;
    }

    public void setResponderId(Long responderId) {
        this.responderId = responderId;
    }

    public String getResponderName() {
        return responderName;
    }

    public void setResponderName(String responderName) {
        this.responderName = responderName;
    }

    public Role getRole() {
        return responderRole;
    }

    public void setRole(Role responderRole) {
        this.responderRole = responderRole;
    }

    public String getGuidanceText() {
        return guidanceText;
    }

    public void setGuidanceText(String guidanceText) {
        this.guidanceText = guidanceText;
    }

    public String getRecommendedResources() {
        return recommendedResources;
    }

    public void setRecommendedResources(String recommendedResources) {
        this.recommendedResources = recommendedResources;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
