package com._roomthon.irumso.policy.supportPolicy;

import com._roomthon.irumso.user.addtionInfo.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SupportPolicyRepository extends JpaRepository<SupportPolicy, Long> {
    Optional<SupportPolicy> findByServiceId(String serviceId);
    List<SupportPolicy> findTop10ByOrderByViewsDesc();
    @Query("""
    SELECT sp
    FROM SupportPolicy sp
    JOIN sp.targetAudience ta
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
    Page<SupportPolicy> findMatchingAudiencesWithLimit(
            @Param("gender") String gender,
            @Param("age") int age,
            @Param("job") String job,
            @Param("incomeLevel") String incomeLevel,
            Pageable pageable
    );

}