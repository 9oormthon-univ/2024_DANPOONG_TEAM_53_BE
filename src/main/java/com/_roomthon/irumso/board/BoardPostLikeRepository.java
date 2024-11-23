package com._roomthon.irumso.board;

import com._roomthon.irumso.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BoardPostLikeRepository extends JpaRepository<BoardPostLike, Long> {

    // 특정 게시글에 대해 사용자가 좋아요를 눌렀는지 확인
    Optional<BoardPostLike> findByPostAndUser(BoardPost post, User user);

    // 특정 게시글에 대해 사용자가 좋아요를 이미 눌렀는지 체크
    boolean existsByPostAndUser(BoardPost post, User user);
}
