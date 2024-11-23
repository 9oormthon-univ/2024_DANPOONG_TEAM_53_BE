package com._roomthon.irumso.policy.dataProcess.youthPolicy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class YouthPolicyApiService {

    private final YouthPolicyRepository youthPolicyRepository;
    private final YouthPolicyXmlParser xmlParser;

    @Transactional
    public void fetchAndSaveYouthPolicies(String apiKey, int display, int pageIndex, String serviceType) {
        String url = UriComponentsBuilder.fromHttpUrl("https://www.youthcenter.go.kr/opi/youthPlcyList.do")
                .queryParam("openApiVlak", apiKey)
                .queryParam("display", display)
                .queryParam("pageIndex", pageIndex)
                .queryParam("bizTycdSel", serviceType)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();

        try {
            // API 호출
            String response = restTemplate.getForObject(url, String.class);

            // XML 응답 파싱
            YouthPolicyListResponse policyListResponse = xmlParser.parseResponse(response);

            // 데이터 저장
            List<YouthPolicy> policies = policyListResponse.getPolicies()
                    .stream()
                    .map(policy -> policy.toEntity(serviceType))
                    .collect(Collectors.toList());
            youthPolicyRepository.saveAll(policies);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}