package com._roomthon.irumso.board;

import com._roomthon.irumso.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "board_post_view_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardPostViewRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // 게시글을 조회한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private BoardPost post;  // 조회한 게시글

    private LocalDateTime viewedAt;  // 조회 시간
}
