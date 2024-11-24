package com.fconlinelogger.repository;

import com.fconlinelogger.domain.MatchSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchSummaryRepository extends JpaRepository<MatchSummary, String> {
}
