package com.fconlinelogger.repository;

import com.fconlinelogger.domain.FCOUser;
import com.fconlinelogger.domain.MatchSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchSummaryRepository extends JpaRepository<MatchSummary, String> {
    List<MatchSummary> findAllByUser(FCOUser user);
}
