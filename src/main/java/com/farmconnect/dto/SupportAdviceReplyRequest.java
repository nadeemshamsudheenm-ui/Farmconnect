package com.farmconnect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SupportAdviceReplyRequest {

    @NotNull(message = "Expert/Responder ID is required")
    private Long responderId;

    @NotBlank(message = "Guidance text is required")
    private String guidanceText;

    private String recommendedResources;

    public SupportAdviceReplyRequest() {
    }

    public Long getResponderId() {
        return responderId;
    }

    public void setResponderId(Long responderId) {
        this.responderId = responderId;
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
}
