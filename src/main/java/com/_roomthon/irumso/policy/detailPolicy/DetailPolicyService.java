package com._roomthon.irumso.policy.detailPolicy;

import com._roomthon.irumso.policy.supportPolicy.SupportPolicy;
import com._roomthon.irumso.policy.supportPolicy.SupportPolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DetailPolicyService {
    private final WebClient webClient;
    private final SupportPolicyRepository supportPolicyRepository;

    public DetailPolicyService(WebClient.Builder webClientBuilder,
                               SupportPolicyRepository supportPolicyRepository) {
        this.webClient = webClientBuilder.baseUrl("https://api.odcloud.kr/api/gov24/v3").build();
        this.supportPolicyRepository = supportPolicyRepository;
    }

    public void fetchAndSaveSupportPolicy(int page, int perPage) {
        // API 호출하여 데이터를 가져옵니다.
        Map<String, Object> response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/serviceList")
                        .queryParam("page", page)
                        .queryParam("perPage", perPage)
                        .queryParam("serviceKey", "gh415FmQZbT19PwzW6hTUvHqiavk4ThTPC2T8GGjByq9rCcoXU5fNdlYbm8eIfE6GJAeiH9LAm5TMXfeC5D/Fw==")
                        .build())
                .retrieve()
                .bodyToMono(Map.class) // 응답을 Map으로 받기
                .block();

        // 응답에서 data 항목을 List로 추출합니다.
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.get("data");

        // dataList가 비어 있지 않으면 데이터를 처리합니다.
        if (dataList != null && !dataList.isEmpty()) {
            for (Map<String, Object> data : dataList) {
                // 서비스ID가 맞는지 확인
                String fetchedServiceId = (String) data.get("서비스ID");
                Optional<SupportPolicy> supportPolicyOpt = supportPolicyRepository.findByServiceId(fetchedServiceId);

                // supportPolicy가 없으면 넘어가기
                if (supportPolicyOpt.isEmpty()) {
                    continue;
                }

                System.out.println(fetchedServiceId);
                SupportPolicy supportPolicy = supportPolicyOpt.get();
                supportPolicy.setServiceField((String) data.get("서비스명"));
                supportPolicy.setViews(parseInt(data.get("조회수")));

                supportPolicyRepository.save(supportPolicy);
            }
        }
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
