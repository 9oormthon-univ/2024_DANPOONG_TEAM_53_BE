package com._roomthon.irumso.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCommentRequest {
    private String content;  // 수정할 댓글 내용
}
