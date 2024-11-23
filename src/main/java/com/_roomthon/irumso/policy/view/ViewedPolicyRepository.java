package com._roomthon.irumso.policy.view;

import com._roomthon.irumso.policy.supportPolicy.SupportPolicy;
import com._roomthon.irumso.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ViewedPolicyRepository extends JpaRepository<ViewedPolicy, Long> {
    @Query("SELECT l FROM ViewedPolicy l WHERE l.supportPolicy = :supportPolicy AND l.user = :user")
    Optional<ViewedPolicy> findBySupportPolicyAndUser(@Param("supportPolicy") SupportPolicy supportPolicy, @Param("user") User user);

    List<ViewedPolicy> findByUser(User user);
}
