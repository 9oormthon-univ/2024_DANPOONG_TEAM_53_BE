package com._roomthon.irumso.targetAudience;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TargetAudienceRepository extends JpaRepository<TargetAudience, Long> {
    Optional<TargetAudience> findByServiceId(String serviceId);


}