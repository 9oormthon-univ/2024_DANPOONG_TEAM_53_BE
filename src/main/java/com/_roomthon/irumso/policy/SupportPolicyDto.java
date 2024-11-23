package com._roomthon.irumso.policy;

import com._roomthon.irumso.policy.like.LikedPolicy;
import com._roomthon.irumso.policy.like.LikedPolicyDto;
import com._roomthon.irumso.policy.supportPolicy.SupportPolicy;
import com._roomthon.irumso.policy.dataProcess.youthPolicy.YouthPolicy;
import com._roomthon.irumso.policy.view.ViewedPolicy;
import com._roomthon.irumso.policy.view.ViewedPolicyDto;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
public class SupportPolicyDto {
    private Long id;
    private String serviceId;
    private String serviceName;
    private String serviceField;
    private String supportContent;
    private String purpose;
    private String applyTarget;
    private String applicationUrl;
    private boolean isYouthData;

    private List<LikedPolicyDto> liked;
    private List<ViewedPolicyDto> viewed;

    public static SupportPolicyDto fromEntity(SupportPolicy supportPolicy) {
        SupportPolicyDto dto = SupportPolicyDto.builder()
                .id(supportPolicy.getId())
                .serviceId(supportPolicy.getServiceId())
                .serviceName(supportPolicy.getServiceName())
                .serviceField(supportPolicy.getServiceField())
                .supportContent(supportPolicy.getSupportContent())
                .purpose(supportPolicy.getPurpose())
                .applyTarget(supportPolicy.getApplyTarget())
                .applicationUrl(supportPolicy.getApplicationUrl())
                .isYouthData(false)
                .build();

        dto.liked = new ArrayList<>();
        dto.viewed = new ArrayList<>();

        if (supportPolicy.getLikedPolicies() != null) {
            for (LikedPolicy likedPolicy : supportPolicy.getLikedPolicies()) {
                dto.liked.add(new LikedPolicyDto(likedPolicy));
            }
        }

        if (supportPolicy.getViewedPolicies() != null) {
            for (ViewedPolicy viewedPolicy : supportPolicy.getViewedPolicies()) {
               dto.viewed.add(new ViewedPolicyDto(viewedPolicy));
            }
        }

        return dto;
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
