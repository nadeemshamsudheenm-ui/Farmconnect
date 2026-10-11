package com.farmconnect.dto;

import com.farmconnect.entity.SupportTopic;
import com.farmconnect.entity.SupportUrgency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SupportTicketRequest {

    @NotNull(message = "Farmer ID is required")
    private Long farmerId;

    @NotNull(message = "Support topic is required")
    private SupportTopic topic;

    @NotBlank(message = "Question title is required")
    private String title;

    @NotBlank(message = "Detailed description of the cultivation issue is required")
    private String description;

    private SupportUrgency urgency = SupportUrgency.MEDIUM;

    @NotBlank(message = "Crop type is required (e.g., Tomato, Wheat, Mango)")
    private String cropType;

    public SupportTicketRequest() {
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public SupportTopic getTopic() {
        return topic;
    }

    public void setTopic(SupportTopic topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SupportUrgency getUrgency() {
        return urgency;
    }

    public void setUrgency(SupportUrgency urgency) {
        this.urgency = urgency;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }
}
