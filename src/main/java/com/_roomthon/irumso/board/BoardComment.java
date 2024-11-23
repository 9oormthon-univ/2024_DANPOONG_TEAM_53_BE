package com._roomthon.irumso.board;

import com._roomthon.irumso.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "board_comment")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_post_id", nullable = false)
    private BoardPost boardPost;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardComment> replies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BoardComment parentComment;

    @Builder
    public BoardComment(BoardPost boardPost, User createdBy, String content, BoardComment parentComment, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.boardPost = boardPost;
        this.createdBy = createdBy;
        this.content = content;
        this.parentComment = parentComment;
        this.createdAt = (createdAt == null) ? LocalDateTime.now() : createdAt;
        this.updatedAt = (updatedAt == null) ? LocalDateTime.now() : updatedAt;
    }
}
