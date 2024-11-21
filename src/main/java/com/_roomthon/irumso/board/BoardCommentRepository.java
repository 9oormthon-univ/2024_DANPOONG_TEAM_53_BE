package com._roomthon.irumso.board;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    // 게시글에 달린 댓글을 조회
    List<BoardComment> findByBoardPostId(Long postId);

    // 부모 댓글을 기준으로 대댓글을 조회
    List<BoardComment> findByParentCommentId(Long parentCommentId);
}
