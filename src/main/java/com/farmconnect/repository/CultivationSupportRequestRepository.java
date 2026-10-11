package com.farmconnect.repository;

import com.farmconnect.entity.CultivationSupportRequest;
import com.farmconnect.entity.SupportStatus;
import com.farmconnect.entity.SupportTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CultivationSupportRequestRepository extends JpaRepository<CultivationSupportRequest, Long> {

    List<CultivationSupportRequest> findByFarmerIdOrderByCreatedAtDesc(Long farmerId);

    List<CultivationSupportRequest> findByStatusOrderByCreatedAtDesc(SupportStatus status);

    List<CultivationSupportRequest> findByTopicOrderByCreatedAtDesc(SupportTopic topic);

    List<CultivationSupportRequest> findAllByOrderByCreatedAtDesc();
}
