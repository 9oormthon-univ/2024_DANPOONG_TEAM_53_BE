package com._roomthon.irumso.policy.like;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikedPolicyDto {
    private Long userId;
    private String userName;

    public LikedPolicyDto(LikedPolicy likedPolicy) {
        this.userId = likedPolicy.getUser().getId();
        this.userName = likedPolicy.getUser().getNickname();
    }
}