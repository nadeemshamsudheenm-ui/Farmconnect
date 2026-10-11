package com.farmconnect.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "support_responses")
public class SupportResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private CultivationSupportRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responder_id", nullable = false)
    private User responder;

    @Column(nullable = false, length = 4000)
    private String guidanceText;

    @Column(length = 1000)
    private String recommendedResources;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public SupportResponse() {
    }

    public SupportResponse(CultivationSupportRequest request, User responder, String guidanceText, String recommendedResources) {
        this.request = request;
        this.responder = responder;
        this.guidanceText = guidanceText;
        this.recommendedResources = recommendedResources;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CultivationSupportRequest getRequest() {
        return request;
    }

    public void setRequest(CultivationSupportRequest request) {
        this.request = request;
    }

    public User getResponder() {
        return responder;
    }

    public void setResponder(User responder) {
        this.responder = responder;
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
