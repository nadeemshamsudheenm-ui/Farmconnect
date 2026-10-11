package com.farmconnect.service;

import com.farmconnect.dto.*;
import com.farmconnect.entity.*;
import com.farmconnect.exception.ResourceNotFoundException;
import com.farmconnect.repository.CultivationSupportRequestRepository;
import com.farmconnect.repository.FarmerProfileRepository;
import com.farmconnect.repository.FarmingTipRepository;
import com.farmconnect.repository.SupportResponseRepository;
import com.farmconnect.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CultivationSupportService {

    private final CultivationSupportRequestRepository supportRequestRepository;
    private final SupportResponseRepository supportResponseRepository;
    private final FarmerProfileRepository farmerProfileRepository;
    private final UserRepository userRepository;
    private final FarmingTipRepository farmingTipRepository;

    public CultivationSupportService(CultivationSupportRequestRepository supportRequestRepository,
                                     SupportResponseRepository supportResponseRepository,
                                     FarmerProfileRepository farmerProfileRepository,
                                     UserRepository userRepository,
                                     FarmingTipRepository farmingTipRepository) {
        this.supportRequestRepository = supportRequestRepository;
        this.supportResponseRepository = supportResponseRepository;
        this.farmerProfileRepository = farmerProfileRepository;
        this.userRepository = userRepository;
        this.farmingTipRepository = farmingTipRepository;
    }

    @Transactional
    public SupportTicketResponse createSupportRequest(SupportTicketRequest req) {
        FarmerProfile farmer = farmerProfileRepository.findById(req.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found with ID: " + req.getFarmerId()));

        CultivationSupportRequest ticket = new CultivationSupportRequest(
                farmer,
                req.getTopic(),
                req.getTitle(),
                req.getDescription(),
                req.getUrgency(),
                req.getCropType()
        );

        CultivationSupportRequest saved = supportRequestRepository.save(ticket);
        return mapToTicketResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SupportTicketResponse> getFarmerRequests(Long farmerId) {
        return supportRequestRepository.findByFarmerIdOrderByCreatedAtDesc(farmerId).stream()
                .map(this::mapToTicketResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SupportTicketResponse> getAllRequests(SupportStatus status, SupportTopic topic) {
        List<CultivationSupportRequest> list;
        if (status != null) {
            list = supportRequestRepository.findByStatusOrderByCreatedAtDesc(status);
        } else if (topic != null) {
            list = supportRequestRepository.findByTopicOrderByCreatedAtDesc(topic);
        } else {
            list = supportRequestRepository.findAllByOrderByCreatedAtDesc();
        }

        return list.stream()
                .map(this::mapToTicketResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SupportTicketResponse getRequestById(Long id) {
        CultivationSupportRequest ticket = supportRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Support ticket not found with ID: " + id));
        return mapToTicketResponse(ticket);
    }

    @Transactional
    public SupportTicketResponse replyToRequest(Long requestId, SupportAdviceReplyRequest replyReq) {
        CultivationSupportRequest ticket = supportRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Support ticket not found with ID: " + requestId));

        User expert = userRepository.findById(replyReq.getResponderId())
                .orElseThrow(() -> new ResourceNotFoundException("Agronomist/Responder user not found with ID: " + replyReq.getResponderId()));

        SupportResponse response = new SupportResponse(
                ticket,
                expert,
                replyReq.getGuidanceText(),
                replyReq.getRecommendedResources()
        );

        ticket.addResponse(response);
        ticket.setStatus(SupportStatus.RESOLVED);
        supportResponseRepository.save(response);
        CultivationSupportRequest updated = supportRequestRepository.save(ticket);

        return mapToTicketResponse(updated);
    }

    @Transactional(readOnly = true)
    public List<FarmingTipResponse> getFarmingTips(Season season) {
        List<FarmingTip> tips;
        if (season != null && season != Season.ALL_SEASON) {
            tips = farmingTipRepository.findBySeasonOrderByPublishedAtDesc(season);
        } else {
            tips = farmingTipRepository.findAllByOrderByPublishedAtDesc();
        }

        return tips.stream()
                .map(t -> new FarmingTipResponse(
                        t.getId(),
                        t.getTitle(),
                        t.getCategory(),
                        t.getSeason(),
                        t.getContent(),
                        t.getAuthor(),
                        t.getPublishedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public FarmingTipResponse createFarmingTip(FarmingTip tip) {
        FarmingTip saved = farmingTipRepository.save(tip);
        return new FarmingTipResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getCategory(),
                saved.getSeason(),
                saved.getContent(),
                saved.getAuthor(),
                saved.getPublishedAt()
        );
    }

    private SupportTicketResponse mapToTicketResponse(CultivationSupportRequest ticket) {
        List<SupportAdviceResponse> adviceList = ticket.getResponses().stream()
                .map(r -> new SupportAdviceResponse(
                        r.getId(),
                        r.getResponder().getId(),
                        r.getResponder().getFullName(),
                        r.getResponder().getRole(),
                        r.getGuidanceText(),
                        r.getRecommendedResources(),
                        r.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new SupportTicketResponse(
                ticket.getId(),
                ticket.getFarmer().getId(),
                ticket.getFarmer().getFarmName(),
                ticket.getFarmer().getAnonymizedRegionCode(),
                ticket.getTopic(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getUrgency(),
                ticket.getCropType(),
                ticket.getStatus(),
                ticket.getCreatedAt(),
                adviceList
        );
    }
}
