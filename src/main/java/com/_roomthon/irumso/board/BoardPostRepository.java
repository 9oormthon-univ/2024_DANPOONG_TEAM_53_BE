package com._roomthon.irumso.board;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {

    // 제목 또는 내용에 키워드가 포함된 게시글 검색
    List<BoardPost> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword, String contentKeyword);

    // 조회수가 많은 상위 5개 게시글
    List<BoardPost> findTop5ByOrderByViewCountDesc();

    // 좋아요가 많은 상위 5개 게시글
    List<BoardPost> findTop5ByOrderByLikesDesc();
}
