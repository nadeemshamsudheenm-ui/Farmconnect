package com.farmconnect.repository;

import com.farmconnect.entity.SupportResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportResponseRepository extends JpaRepository<SupportResponse, Long> {
    List<SupportResponse> findByRequestIdOrderByCreatedAtAsc(Long requestId);
}
