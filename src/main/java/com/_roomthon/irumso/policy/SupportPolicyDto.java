package com._roomthon.irumso.policy;

import com._roomthon.irumso.policy.supportPolicy.SupportPolicy;
import com._roomthon.irumso.youthPolicy.YouthPolicy;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class SupportPolicyDto {
    private Long id;
    private String serviceId;
    private String serviceName;
    private String serviceField;
    private Integer views;
    private Integer likes;
    private String supportContent;
    private String purpose;
    private String applyTarget;
    private String applicationUrl;
    private boolean isYouthData;

    public static SupportPolicyDto fromEntity(SupportPolicy supportPolicy) {
        return SupportPolicyDto.builder()
                .id(supportPolicy.getId())
                .serviceId(supportPolicy.getServiceId())
                .serviceName(supportPolicy.getServiceName())
                .serviceField(supportPolicy.getServiceField())
                .views(supportPolicy.getViews())
                .likes(supportPolicy.getLikedPolicies().size())
                .supportContent(supportPolicy.getSupportContent())
                .purpose(supportPolicy.getPurpose())
                .applyTarget(supportPolicy.getApplyTarget())
                .applicationUrl(supportPolicy.getApplicationUrl())
                .isYouthData(false)
                .build();
    }

    public static SupportPolicyDto fromYouthPolicy(YouthPolicy youthPolicy) {
        String applyTarget = String.format("연령: %s, 고용 상태: %s",
                youthPolicy.getAgeInfo(),
                youthPolicy.getEmployStatus());

        return SupportPolicyDto.builder()
                .id(youthPolicy.getId())
                .serviceId(youthPolicy.getServiceId())
                .serviceName(youthPolicy.getServiceName())
                .serviceField(youthPolicy.getServiceField())
                .views(youthPolicy.getViews())
                .likes(youthPolicy.getLikes())
                .supportContent(youthPolicy.getSupportContent())
                .purpose(null)
                .applyTarget(applyTarget)
                .applicationUrl(youthPolicy.getApplicationUrl())
                .isYouthData(true)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupportPolicyDto that = (SupportPolicyDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
