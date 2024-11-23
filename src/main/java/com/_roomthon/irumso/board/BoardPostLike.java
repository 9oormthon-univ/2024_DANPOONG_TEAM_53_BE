package com._roomthon.irumso.board;

import com._roomthon.irumso.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "board_post_likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardPostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // 좋아요를 누른 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private BoardPost post;  // 좋아요를 누른 게시글

    private LocalDateTime createdAt;  // 좋아요 누른 시간
}
