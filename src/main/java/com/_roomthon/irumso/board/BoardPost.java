package com._roomthon.irumso.board;

import com._roomthon.irumso.user.User;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false) // 주
    private BoardCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")  // 외래 키 설정
    private User createdBy;  // 작성자

    @ElementCollection
    @CollectionTable(name = "board_post_images", joinColumns = @JoinColumn(name = "board_post_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    @Builder.Default
    private Integer likes = 0;

    @Builder.Default
    private Integer viewCount = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(name = "author_nickname", nullable = false)
    private String authorNickname;  // 작성자 닉네임 필드 추가
}
