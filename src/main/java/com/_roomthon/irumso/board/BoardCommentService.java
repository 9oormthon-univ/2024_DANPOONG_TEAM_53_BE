package com._roomthon.irumso.board;

import com._roomthon.irumso.user.User;
import com._roomthon.irumso.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardCommentService {

    private final UserService userService;
    private final BoardCommentRepository boardCommentRepository;
    private final BoardPostRepository boardPostRepository;

    @Transactional
    public void createBoardComment(String nickname, Long postId, String content, Long parentCommentId) {
        User user = userService.findByNickname(nickname);

        BoardPost post = boardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        BoardComment parentComment = null;
        if (parentCommentId != null) {
            parentComment = boardCommentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));
        }

        BoardComment comment = BoardComment.builder()
                .boardPost(post)
                .content(content)
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .parentComment(parentComment)
                .build();

        boardCommentRepository.save(comment);
    }

    @Transactional
    public void updateBoardComment(Long commentId, String newContent) {
        BoardComment comment = boardCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        comment.setContent(newContent);
        comment.setUpdatedAt(LocalDateTime.now());

        boardCommentRepository.save(comment);
    }

    @Transactional
    public void deleteBoardComment(Long commentId) {
        BoardComment comment = boardCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        boardCommentRepository.delete(comment);
    }

    public List<BoardComment> getCommentsByPostId(Long postId) {
        return boardCommentRepository.findByBoardPostId(postId);
    }

    public List<BoardComment> getRepliesByParentCommentId(Long parentCommentId) {
        return boardCommentRepository.findByParentCommentId(parentCommentId);
    }
}
