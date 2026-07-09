package com.example.aiwriter.repository;

import com.example.aiwriter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.usedQuota = u.usedQuota + 1 WHERE u.id = :userId AND (u.dailyQuota - u.usedQuota) > 0")
    int incrementUsedQuota(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE User u SET u.usedQuota = 0, u.quotaResetDate = :resetDate WHERE u.quotaResetDate < :resetDate")
    int resetDailyQuota(@Param("resetDate") LocalDateTime resetDate);
}
