package com._roomthon.irumso.policy;

import com._roomthon.irumso.policy.supportPolicy.SupportPolicy;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupportPolicyDto {
    private Long id;
    private String serviceId;
    private String detailUrl;
    private String serviceName;
    private String serviceField;
    private String criteria;
    private Integer views;
    private Integer likes;
    private String supportContent;
    private String purpose;
    private String applyTarget;
    private String applicationUrl;

    public static SupportPolicyDto fromEntity(SupportPolicy supportPolicy) {
        return SupportPolicyDto.builder()
                .id(supportPolicy.getId())
                .serviceId(supportPolicy.getServiceId())
                .serviceName(supportPolicy.getServiceName())
                .serviceField(supportPolicy.getServiceField())
                .criteria(supportPolicy.getCriteria())
                .views(supportPolicy.getViews())
                .likes(supportPolicy.getLikes())
                .supportContent(supportPolicy.getSupportContent())
                .purpose(supportPolicy.getPurpose())
                .applyTarget(supportPolicy.getApplyTarget())
                .applicationUrl(supportPolicy.getApplicationUrl())
                .build();
    }
}
