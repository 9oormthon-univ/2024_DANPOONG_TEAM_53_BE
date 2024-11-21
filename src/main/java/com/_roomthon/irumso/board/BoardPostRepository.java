package com._roomthon.irumso.board;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {

    // 제목 또는 내용에 키워드가 포함된 게시글 검색
    List<BoardPost> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword, String contentKeyword);
}
