package com.farmconnect.controller;

import com.farmconnect.dto.*;
import com.farmconnect.entity.FarmingTip;
import com.farmconnect.entity.Season;
import com.farmconnect.entity.SupportStatus;
import com.farmconnect.entity.SupportTopic;
import com.farmconnect.service.CultivationSupportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/support")
public class CultivationSupportController {

    private final CultivationSupportService supportService;

    public CultivationSupportController(CultivationSupportService supportService) {
        this.supportService = supportService;
    }

    /**
     * Farmers submit cultivation questions (crop care, fertilizers, pests, etc.)
     */
    @PostMapping("/requests")
    public ResponseEntity<SupportTicketResponse> createRequest(@Valid @RequestBody SupportTicketRequest request) {
        SupportTicketResponse response = supportService.createSupportRequest(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Farmer views their own submitted support inquiries.
     */
    @GetMapping("/requests/farmer/{farmerId}")
    public ResponseEntity<List<SupportTicketResponse>> getFarmerRequests(@PathVariable Long farmerId) {
        List<SupportTicketResponse> requests = supportService.getFarmerRequests(farmerId);
        return ResponseEntity.ok(requests);
    }

    /**
     * Agronomists / Experts view all open cultivation tickets.
     */
    @GetMapping("/requests")
    public ResponseEntity<List<SupportTicketResponse>> getAllRequests(
            @RequestParam(required = false) SupportStatus status,
            @RequestParam(required = false) SupportTopic topic) {
        List<SupportTicketResponse> requests = supportService.getAllRequests(status, topic);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/requests/{id}")
    public ResponseEntity<SupportTicketResponse> getRequestDetails(@PathVariable Long id) {
        SupportTicketResponse response = supportService.getRequestById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Agronomist delivers expert guidance on a support ticket.
     */
    @PostMapping("/requests/{id}/reply")
    public ResponseEntity<SupportTicketResponse> replyToRequest(
            @PathVariable Long id,
            @Valid @RequestBody SupportAdviceReplyRequest replyRequest) {
        SupportTicketResponse response = supportService.replyToRequest(id, replyRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Public / Community knowledge hub: seasonal farming tips and best practices.
     */
    @GetMapping("/tips")
    public ResponseEntity<List<FarmingTipResponse>> getFarmingTips(@RequestParam(required = false) Season season) {
        List<FarmingTipResponse> tips = supportService.getFarmingTips(season);
        return ResponseEntity.ok(tips);
    }

    @PostMapping("/tips")
    public ResponseEntity<FarmingTipResponse> createFarmingTip(@RequestBody FarmingTip tip) {
        FarmingTipResponse response = supportService.createFarmingTip(tip);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/topics")
    public ResponseEntity<List<SupportTopic>> getSupportTopics() {
        return ResponseEntity.ok(Arrays.asList(SupportTopic.values()));
    }
}
