package com._roomthon.irumso.like;

import com._roomthon.irumso.policy.supportPolicy.SupportPolicy;
import com._roomthon.irumso.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikedPolicyRepository extends JpaRepository<LikedPolicy, Long> {
    @Query("SELECT l FROM LikedPolicy l WHERE l.supportPolicy = :supportPolicy AND l.user = :user")
    Optional<LikedPolicy> findBySupportPolicyAndUser(@Param("supportPolicy") SupportPolicy supportPolicy, @Param("user") User user);
}
