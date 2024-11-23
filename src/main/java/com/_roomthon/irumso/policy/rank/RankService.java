package com._roomthon.irumso.policy.rank;

import com._roomthon.irumso.policy.SupportPolicyDto;
import com._roomthon.irumso.policy.supportPolicy.SupportPolicy;
import com._roomthon.irumso.policy.supportPolicy.SupportPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RankService {
    private final SupportPolicyService supportPolicyService;

    public List<SupportPolicyDto> getSupportPolicyRankByView() {
        List<SupportPolicy> supportPolicies = supportPolicyService.getPolicyRankByView();

        return supportPolicies.stream()
                .map(SupportPolicyDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<SupportPolicyDto> getSupportPolicyRankByLike() {
        List<SupportPolicy> supportPolicies = supportPolicyService.getPolicyRankByLike();

        return supportPolicies.stream()
                .map(SupportPolicyDto::fromEntity)
                .collect(Collectors.toList());
    }
}
