package com.groomthon.irumso.controller;

import com.groomthon.irumso.entity.BoardPost;
import com.groomthon.irumso.entity.BoardComment;
import com.groomthon.irumso.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/board")
@Tag(name = "Board API", description = "게시판 관련 API")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @Operation(summary = "게시글 생성", description = "새로운 게시글을 작성하며 이미지를 첨부할 수 있습니다.")
    @PostMapping("/post")
    public BoardPost createPost(@RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        byte[] imageBytes = null;

        // 이미지 파일이 첨부된 경우 byte[]로 변환
        if (image != null && !image.isEmpty()) {
            imageBytes = image.getBytes();
        }

        // 게시글 생성
        BoardPost post = new BoardPost();
        post.setTitle(title);
        post.setContent(content);
        post.setImage(imageBytes);  // 이미지 데이터를 BoardPost에 저장

        return boardService.createPost(post);
    }

    @Operation(summary = "댓글 생성", description = "게시글에 댓글을 작성합니다.")
    @PostMapping("/comment")
    public BoardComment createComment(@RequestBody BoardComment comment) {
        return boardService.createComment(comment);
    }

    @Operation(summary = "모든 게시글 조회", description = "모든 게시글 목록을 조회합니다.")
    @GetMapping("/posts")
    public List<BoardPost> getAllPosts() {
        return boardService.getAllPosts();
    }

    @Operation(summary = "게시글에 달린 댓글 조회", description = "특정 게시글에 달린 댓글 목록을 조회합니다.")
    @GetMapping("/post/{postId}/comments")
    public List<BoardComment> getCommentsByPostId(
            @Parameter(description = "댓글을 조회할 게시글 ID") @PathVariable Long postId) {
        return boardService.getCommentsByPostId(postId);
    }

    @Operation(summary = "제목으로 게시글 검색", description = "제목에 키워드가 포함된 게시글을 검색합니다.")
    @GetMapping("/posts/search")
    public List<BoardPost> searchPostsByTitle(@Parameter(description = "검색 키워드") @RequestParam String keyword) {
        return boardService.searchPostsByTitle(keyword);
    }
}
