package com._roomthon.irumso.policy.supportPolicy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupportPolicyRepository extends JpaRepository<SupportPolicy, Long> {
    Optional<SupportPolicy> findByServiceId(String serviceId);
    List<SupportPolicy> findTop10ByOrderByViewsDesc();
}