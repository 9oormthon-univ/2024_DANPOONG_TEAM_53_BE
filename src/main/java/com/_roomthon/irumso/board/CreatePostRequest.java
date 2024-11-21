package com._roomthon.irumso.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostRequest {
    private String title;
    private String content;
    private String imageUrl;  // 이미지 URL 필드 추가
}
