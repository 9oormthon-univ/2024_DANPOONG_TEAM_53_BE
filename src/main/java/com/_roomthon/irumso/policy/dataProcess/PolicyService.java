package com._roomthon.irumso.policy.dataProcess;

import com._roomthon.irumso.policy.dataProcess.youthPolicy.YouthPolicy;
import com._roomthon.irumso.policy.dataProcess.youthPolicy.YouthPolicyRepository;
import com._roomthon.irumso.policy.supportPolicy.SupportPolicy;
import com._roomthon.irumso.policy.supportPolicy.SupportPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final SupportPolicyRepository supportPolicyRepository;
    private final YouthPolicyRepository youthPolicyRepository;

    public void migrateYouthPolicyToSupportPolicy() {
        List<YouthPolicy> youthPolicies = youthPolicyRepository.findAll();

        // 각 YouthPolicy를 SupportPolicy로 변환하여 저장
        for (YouthPolicy youthPolicy : youthPolicies) {

            // applyTarget 생성 (연령과 고용 상태 정보를 합침)
            String applyTarget = String.format("연령: %s, 고용 상태: %s",
                    youthPolicy.getAgeInfo(),
                    youthPolicy.getEmployStatus());

            // 새로운 SupportPolicy 객체 생성
            SupportPolicy supportPolicy = new SupportPolicy();

            // YouthPolicy의 데이터를 SupportPolicy로 매핑
            supportPolicy.setServiceId(youthPolicy.getServiceId());
            supportPolicy.setServiceName(youthPolicy.getServiceName());
            supportPolicy.setServiceField(youthPolicy.getServiceField());
            supportPolicy.setSupportContent(youthPolicy.getSupportContent());
            supportPolicy.setApplicationUrl(youthPolicy.getApplicationUrl());
            supportPolicy.setServiceType(youthPolicy.getServiceType());
            supportPolicy.setTargetAudience(youthPolicy.getTargetAudience());
            supportPolicy.setApplyTarget(applyTarget);

            // SupportPolicy 저장
            supportPolicyRepository.save(supportPolicy);
        }
    }
}