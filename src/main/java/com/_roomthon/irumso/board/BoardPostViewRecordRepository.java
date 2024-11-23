package com._roomthon.irumso.board;

import com._roomthon.irumso.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface BoardPostViewRecordRepository extends JpaRepository<BoardPostViewRecord, Long> {

    // BoardPostViewRecordRepository에서 날짜가 LocalDateTime으로 비교되는지 확인
    Optional<BoardPostViewRecord> findByUserAndPostAndViewedAtGreaterThanEqual(User user, BoardPost post, LocalDateTime startOfDay);

}
