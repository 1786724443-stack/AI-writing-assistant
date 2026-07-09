package com.example.aiwriter.repository;

import com.example.aiwriter.entity.ApiCallLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApiCallLogRepository extends JpaRepository<ApiCallLog, Long> {

    Page<ApiCallLog> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<ApiCallLog> findByUserIdAndEndpointOrderByCreatedAtDesc(Long userId, String endpoint, Pageable pageable);

    @Query("SELECT a FROM ApiCallLog a WHERE a.userId = :userId AND a.createdAt >= :startTime ORDER BY a.createdAt DESC")
    List<ApiCallLog> findByUserIdAndTimeRange(@Param("userId") Long userId, @Param("startTime") LocalDateTime startTime);

    long countByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}
