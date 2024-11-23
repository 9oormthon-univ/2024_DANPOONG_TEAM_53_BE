package com._roomthon.irumso.policy.dataProcess.youthPolicy;

import com._roomthon.irumso.policy.supportPolicy.SupportPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface YouthPolicyRepository extends JpaRepository<YouthPolicy, Long> {
    Optional<SupportPolicy> findByServiceId(String serviceId);

    @Query("""
    SELECT yp
    FROM YouthPolicy yp
    JOIN yp.targetAudience ta
    WHERE (:gender IS NULL OR (
            (:gender = 'FEMALE' AND ta.female = true)
        OR  (:gender = 'MALE' AND ta.male = true)))
      AND ta.fromAge <= :age AND ta.toAge >= :age
      AND (:job IS NULL OR (
           (:job = 'STUDENT' AND ta.student = true) 
        OR (:job = 'WORKER' AND ta.worker = true) 
        OR (:job = 'JOB_SEEKER' AND ta.jobSeeker = true)))
      AND (:incomeLevel IS NULL OR (
           (:incomeLevel = 'BELOW_50' AND ta.below_50 = true)
        OR (:incomeLevel = 'BETWEEN_51_AND_75' AND ta.between_51_and_75 = true)
        OR (:incomeLevel = 'BETWEEN_76_AND_100' AND ta.between_76_and_100 = true)
        OR (:incomeLevel = 'BETWEEN_101_AND_200' AND ta.between_101_and_200 = true)
        OR (:incomeLevel = 'ABOVE_200' AND ta.above_200 = true)))
""")
    Page<YouthPolicy> findMatchingAudiences(
            @Param("gender") String gender,
            @Param("age") int age,
            @Param("job") String job,
            @Param("incomeLevel") String incomeLevel,
            Pageable pageable
    );
}