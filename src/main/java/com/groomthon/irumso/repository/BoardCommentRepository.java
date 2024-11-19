package com.groomthon.irumso.repository;

import com.groomthon.irumso.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    // 게시글 ID로 댓글을 조회
    List<BoardComment> findByPostId(Long postId);
}
