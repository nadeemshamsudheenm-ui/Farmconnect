package com.farmconnect.dto;

import com.farmconnect.entity.SupportStatus;
import com.farmconnect.entity.SupportTopic;
import com.farmconnect.entity.SupportUrgency;

import java.time.LocalDateTime;
import java.util.List;

public class SupportTicketResponse {
    private Long id;
    private Long farmerId;
    private String farmName;
    private String anonymizedRegionCode;
    private SupportTopic topic;
    private String title;
    private String description;
    private SupportUrgency urgency;
    private String cropType;
    private SupportStatus status;
    private LocalDateTime createdAt;
    private List<SupportAdviceResponse> responses;

    public SupportTicketResponse() {
    }

    public SupportTicketResponse(Long id, Long farmerId, String farmName, String anonymizedRegionCode,
                                 SupportTopic topic, String title, String description,
                                 SupportUrgency urgency, String cropType, SupportStatus status,
                                 LocalDateTime createdAt, List<SupportAdviceResponse> responses) {
        this.id = id;
        this.farmerId = farmerId;
        this.farmName = farmName;
        this.anonymizedRegionCode = anonymizedRegionCode;
        this.topic = topic;
        this.title = title;
        this.description = description;
        this.urgency = urgency;
        this.cropType = cropType;
        this.status = status;
        this.createdAt = createdAt;
        this.responses = responses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getAnonymizedRegionCode() {
        return anonymizedRegionCode;
    }

    public void setAnonymizedRegionCode(String anonymizedRegionCode) {
        this.anonymizedRegionCode = anonymizedRegionCode;
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

    public SupportStatus getStatus() {
        return status;
    }

    public void setStatus(SupportStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<SupportAdviceResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<SupportAdviceResponse> responses) {
        this.responses = responses;
    }
}
