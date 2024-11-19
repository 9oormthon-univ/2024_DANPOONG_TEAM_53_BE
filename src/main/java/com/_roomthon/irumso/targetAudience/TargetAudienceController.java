package com._roomthon.irumso.targetAudience;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/target-audience")
public class TargetAudienceController {

    private final TargetAudienceService targetAudienceService;
    private final WebClient webClient;

    public TargetAudienceController(TargetAudienceService targetAudienceService, WebClient.Builder webClientBuilder) {
        this.targetAudienceService = targetAudienceService;
        this.webClient = webClientBuilder.baseUrl("https://api.odcloud.kr/api").build();
    }

    @GetMapping("/fetch-and-save")
    public Mono<String> fetchAndSaveData() {
        String apiUrl = "/gov24/v3/supportConditions?page=1&perPage=10&serviceKey=gh415FmQZbT19PwzW6hTUvHqiavk4ThTPC2T8GGjByq9rCcoXU5fNdlYbm8eIfE6GJAeiH9LAm5TMXfeC5D/Fw==";

        return webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    if (response != null && response.containsKey("data")) {
                        @SuppressWarnings("unchecked")
                        var dataList = (List<Map<String, Object>>) response.get("data");
                        dataList.forEach(targetAudienceService::saveTargetAudienceIfValid);
                        return Mono.just("Data processed and saved successfully.");
                    } else {
                        return Mono.just("No data found or invalid response.");
                    }
                })
                .onErrorResume(e -> {
                    // 에러 처리
                    return Mono.just("Error occurred: " + e.getMessage());
                });
    }
}