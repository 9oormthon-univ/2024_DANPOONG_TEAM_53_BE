package com._roomthon.irumso.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequest {
    private String nickname;
    private Long postId;
    private String content;
    private Long parentCommentId;  // 대댓글일 경우 부모 댓글 ID
}
