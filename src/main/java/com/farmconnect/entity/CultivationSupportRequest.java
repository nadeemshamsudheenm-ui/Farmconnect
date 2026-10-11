package com.farmconnect.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cultivation_support_requests")
public class CultivationSupportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private FarmerProfile farmer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SupportTopic topic;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 3000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SupportUrgency urgency = SupportUrgency.MEDIUM;

    @Column(nullable = false)
    private String cropType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SupportStatus status = SupportStatus.OPEN;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupportResponse> responses = new ArrayList<>();

    public CultivationSupportRequest() {
    }

    public CultivationSupportRequest(FarmerProfile farmer, SupportTopic topic, String title,
                                     String description, SupportUrgency urgency, String cropType) {
        this.farmer = farmer;
        this.topic = topic;
        this.title = title;
        this.description = description;
        this.urgency = urgency != null ? urgency : SupportUrgency.MEDIUM;
        this.cropType = cropType;
        this.status = SupportStatus.OPEN;
        this.createdAt = LocalDateTime.now();
    }

    public void addResponse(SupportResponse response) {
        responses.add(response);
        response.setRequest(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FarmerProfile getFarmer() {
        return farmer;
    }

    public void setFarmer(FarmerProfile farmer) {
        this.farmer = farmer;
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

    public List<SupportResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<SupportResponse> responses) {
        this.responses = responses;
    }
}
