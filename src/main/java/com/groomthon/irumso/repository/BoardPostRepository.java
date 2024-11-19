package com.groomthon.irumso.repository;

import com.groomthon.irumso.entity.BoardPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {

    // 제목에 특정 문자열이 포함된 게시글을 검색
    List<BoardPost> findByTitleContaining(String titleKeyword);
}
