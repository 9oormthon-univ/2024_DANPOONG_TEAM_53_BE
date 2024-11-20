package com._roomthon.irumso.policy.supportPolicy;

import com._roomthon.irumso.targetAudience.TargetAudience;
import com._roomthon.irumso.targetAudience.TargetAudienceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class SupportPolicyService {

    private final WebClient webClient;
    private final SupportPolicyRepository supportPolicyRepository;
    private final TargetAudienceRepository targetAudienceRepository;

    public SupportPolicyService(WebClient.Builder webClientBuilder,
                                SupportPolicyRepository supportPolicyRepository,
                                TargetAudienceRepository targetAudienceRepository) {
        this.webClient = webClientBuilder.baseUrl("https://api.odcloud.kr/api/gov24/v3").build();
        this.supportPolicyRepository = supportPolicyRepository;
        this.targetAudienceRepository = targetAudienceRepository;
    }

    public void fetchAndSaveSupportPolicy(String serviceId) {
        // TargetAudience 정보를 가져옵니다.
        TargetAudience targetAudience = targetAudienceRepository.findByServiceId(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service ID: " + serviceId));

        // API 호출하여 데이터를 가져옵니다.
        Map<String, Object> response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/serviceDetail")
                        .queryParam("cond[서비스ID::EQ]", serviceId)
                        .queryParam("serviceKey", "gh415FmQZbT19PwzW6hTUvHqiavk4ThTPC2T8GGjByq9rCcoXU5fNdlYbm8eIfE6GJAeiH9LAm5TMXfeC5D/Fw==")
                        .build())
                .retrieve()
                .bodyToMono(Map.class) // 응답을 Map으로 받기
                .block();

        // 응답에서 data 항목을 List로 추출합니다.
        List<Map<String, Object>> newDataList = (List<Map<String, Object>>) response.get("data");

        // dataList가 비어 있지 않으면 데이터를 처리합니다.
        if (newDataList != null && !newDataList.isEmpty()) {
            for (Map<String, Object> data : newDataList) {
                // 서비스ID가 맞는지 확인
                String fetchedServiceId = (String) data.get("서비스ID");
                if (fetchedServiceId != null && !fetchedServiceId.equals(serviceId)) {
                    continue;
                }

                SupportPolicy supportPolicy = new SupportPolicy();
                supportPolicy.setServiceId(serviceId);
                supportPolicy.setServiceName((String) data.get("서비스명"));
                supportPolicy.setCriteria((String) data.get("선정기준"));
                supportPolicy.setApplyTarget((String) data.get("지원대상"));
                supportPolicy.setSupportContent((String) data.get("지원내용"));
                supportPolicy.setPurpose((String) data.get("서비스목적"));
                supportPolicy.setApplicationUrl((String) data.get("온라인신청사이트URL"));
                supportPolicy.setTargetAudience(targetAudience);

                supportPolicyRepository.save(supportPolicy);
            }
        } else {
            throw new IllegalArgumentException("No data found for serviceId: " + serviceId);
        }
    }
}
