package com._roomthon.irumso.youthPolicy;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/youth-policy")
public class YouthPolicyController {

    private final YouthPolicyApiService youthPolicyApiService;

    public YouthPolicyController(YouthPolicyApiService youthPolicyApiService) {
        this.youthPolicyApiService = youthPolicyApiService;
    }

    @PostMapping("/fetch-and-save")
    public String fetchAndSaveYouthPolicies(@RequestParam(name = "apiKey") String apiKey,
                                            @RequestParam(name = "display", defaultValue = "100") int display,
                                            @RequestParam(name = "pageIndex", defaultValue = "1") int pageIndex,
                                            @RequestParam(name = "bizTycdSel") String serviceType) {
        try {
            youthPolicyApiService.fetchAndSaveYouthPolicies(apiKey, display, pageIndex, serviceType);
            return "Data fetched and saved successfully!";
        } catch (Exception e) {
            return "Error occurred: " + e.getMessage();
        }
    }
}