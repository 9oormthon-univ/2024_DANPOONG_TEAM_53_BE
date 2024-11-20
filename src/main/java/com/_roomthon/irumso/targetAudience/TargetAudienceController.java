package com._roomthon.irumso.targetAudience;

import com._roomthon.irumso.policy.detailPolicy.DetailPolicyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/target-audience")
public class TargetAudienceController {

    private final TargetAudienceService targetAudienceService;
    private final DetailPolicyService detailPolicyService;
    private final WebClient webClient;

    public TargetAudienceController(TargetAudienceService targetAudienceService,
                                    DetailPolicyService detailPolicyService,
                                    WebClient.Builder webClientBuilder) {
        this.targetAudienceService = targetAudienceService;
        this.detailPolicyService = detailPolicyService;
        this.webClient = webClientBuilder.baseUrl("https://api.odcloud.kr/api").build();
    }

    @GetMapping("/fetch-and-save")
    public Mono<String> fetchAndSaveData(@RequestParam(name = "page") int page, @RequestParam(name = "perPage") int perPage) {
        String apiUrl = String.format("/gov24/v3/supportConditions?page=%d&perPage=%d&serviceKey=gh415FmQZbT19PwzW6hTUvHqiavk4ThTPC2T8GGjByq9rCcoXU5fNdlYbm8eIfE6GJAeiH9LAm5TMXfeC5D/Fw==", page, perPage);

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

    @PostMapping("/process-all")
    public ResponseEntity<String> processAll() {
        targetAudienceService.processAllTargetAudiences();
        return ResponseEntity.ok("Processing all TargetAudience entities started.");
    }

    @PostMapping("/process-detail")
    public ResponseEntity<String> processDetail(@RequestParam("page") int page,
                                                @RequestParam("perPage") int perPage) {
        detailPolicyService.fetchAndSaveSupportPolicy(page, perPage);
        return ResponseEntity.ok("Processing all TargetAudience entities started.");
    }
}