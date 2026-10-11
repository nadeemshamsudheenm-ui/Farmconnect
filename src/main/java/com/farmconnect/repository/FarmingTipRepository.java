package com.farmconnect.repository;

import com.farmconnect.entity.FarmingTip;
import com.farmconnect.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FarmingTipRepository extends JpaRepository<FarmingTip, Long> {
    List<FarmingTip> findBySeasonOrderByPublishedAtDesc(Season season);
    List<FarmingTip> findAllByOrderByPublishedAtDesc();
}
