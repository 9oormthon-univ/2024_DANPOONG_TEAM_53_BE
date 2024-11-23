package com._roomthon.irumso.policy.targetAudience;

import com._roomthon.irumso.policy.supportPolicy.SupportPolicyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TargetAudienceService {

    private final TargetAudienceRepository targetAudienceRepository;
    private final SupportPolicyService supportPolicyService;

    @Transactional
    public void saveTargetAudienceIfValid(Map<String, Object> apiResponse) {
        String serviceID = String.valueOf(apiResponse.get("서비스ID"));
        Optional<TargetAudience> targetAudienceByServiceId = targetAudienceRepository.findByServiceId(serviceID);

        if (targetAudienceByServiceId.isPresent()) {
            return;
        }

        // 필수 조건: JA0320, JA0326, JA0327 중 하나라도 "Y"이어야 저장
        if (!("Y".equals(apiResponse.get("JA0320")) ||
                "Y".equals(apiResponse.get("JA0326")) ||
                "Y".equals(apiResponse.get("JA0327")))) {
            return; // 조건에 맞지 않으면 저장하지 않음
        }

        TargetAudience targetAudience = new TargetAudience();

        if ("Y".equals(apiResponse.get("JA0101"))) {
            targetAudience.setMale(true);
        } else if ("Y".equals(apiResponse.get("JA0102"))) {
            targetAudience.setFemale(true);
        }

        // 소득 구간 매핑
        if ("Y".equals(apiResponse.get("JA0201"))) {
            targetAudience.setBelow_50(true);
        }
        if ("Y".equals(apiResponse.get("JA0202"))) {
            targetAudience.setBetween_51_and_75(true);
        }
        if ("Y".equals(apiResponse.get("JA0203"))) {
            targetAudience.setBetween_76_and_100(true);
        }
        if ("Y".equals(apiResponse.get("JA0204"))) {
            targetAudience.setBetween_101_and_200(true);
        }
        if ("Y".equals(apiResponse.get("JA0205"))) {
            targetAudience.setAbove_200(true);
        }


        if ("Y".equals(apiResponse.get("JA0320"))) {
            targetAudience.setStudent(true);
        }

        if ("Y".equals(apiResponse.get("JA0326"))) {
            targetAudience.setWorker(true);
        }

        if ("Y".equals(apiResponse.get("JA0327"))) {
            targetAudience.setJobSeeker(true);
        }

        // 엔티티 생성 및 저장
        targetAudience.setFromAge(parseInt(apiResponse.get("JA0110")));
        targetAudience.setToAge(parseInt(apiResponse.get("JA0111")));

        targetAudience.setServiceId(serviceID);

        targetAudienceRepository.save(targetAudience);
    }

    @Transactional
    public void processAllTargetAudiences() {
        // 1. TargetAudience 전체 조회
        List<TargetAudience> targetAudiences = targetAudienceRepository.findAll();

        // 2. 각 TargetAudience의 serviceId로 fetchAndSaveSupportPolicy 호출
        targetAudiences.forEach(targetAudience -> {
            String serviceId = targetAudience.getServiceId();
            if (serviceId != null && !serviceId.isEmpty()) {
                try {
                    supportPolicyService.fetchAndSaveSupportPolicy(serviceId);
                } catch (Exception e) {
                    // 예외 처리 (로그 출력 등)
                    System.err.println("Error processing serviceId: " + serviceId + ", " + e.getMessage());
                }
            }
        });
    }

    private int parseInt(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                // 잘못된 형식 처리 (기본값 반환 등)
                return 0;  // 예시로 기본값 0을 반환
            }
        }
        return 0;  // 기본값 반환
    }
}