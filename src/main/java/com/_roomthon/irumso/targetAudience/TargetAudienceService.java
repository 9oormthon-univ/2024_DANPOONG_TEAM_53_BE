package com._roomthon.irumso.targetAudience;

import com._roomthon.irumso.policy.Gender;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class TargetAudienceService {

    private final TargetAudienceRepository targetAudienceRepository;

    @Transactional
    public void saveTargetAudienceIfValid(Map<String, Object> apiResponse) {
        // 필수 조건: JA0320, JA0326, JA0327 중 하나라도 "Y"이어야 저장
        if (!("Y".equals(apiResponse.get("JA0320")) ||
                "Y".equals(apiResponse.get("JA0326")) ||
                "Y".equals(apiResponse.get("JA0327")))) {
            return; // 조건에 맞지 않으면 저장하지 않음
        }

        TargetAudience targetAudience = new TargetAudience();

        Gender gender = null;
        if ("Y".equals(apiResponse.get("JA0101"))) {
            gender = Gender.MALE;
        } else if ("Y".equals(apiResponse.get("JA0102"))) {
            gender = Gender.FEMALE;
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
        targetAudience.setGender(gender);
        targetAudience.setFromAge(parseInt(apiResponse.get("JA0110")));
        targetAudience.setToAge(parseInt(apiResponse.get("JA0111")));

        targetAudience.setServiceId(String.valueOf(apiResponse.get("서비스ID")));

        targetAudienceRepository.save(targetAudience);
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