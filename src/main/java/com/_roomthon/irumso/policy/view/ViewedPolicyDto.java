package com._roomthon.irumso.policy.view;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewedPolicyDto {
    private Long userId;
    private String userName;

    public ViewedPolicyDto(ViewedPolicy viewedPolicy) {
        this.userId = viewedPolicy.getUser().getId();
        this.userName = viewedPolicy.getUser().getNickname();
    }
}
