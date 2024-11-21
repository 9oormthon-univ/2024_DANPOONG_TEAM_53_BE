package com._roomthon.irumso.board;

import com._roomthon.irumso.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "board_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")  // 외래 키 설정
    private User createdBy;  // 작성자

    @Column(name = "image_url")  // imageUrl 컬럼을 image_url로 변경
    private String imageUrl;  // 이미지 URL

    private Integer likes = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(name = "author_nickname", nullable = false)
    private String authorNickname;  // 작성자 닉네임 필드 추가
}
