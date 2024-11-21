package com._roomthon.irumso.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글이 작성되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CreateCommentRequest request) {
        boardCommentService.createBoardComment(request.getNickname(), request.getPostId(), request.getContent(), request.getParentCommentId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글에 달린 댓글 조회", description = "게시글에 달린 댓글들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<BoardComment>> getCommentsByPostId(@PathVariable Long postId) {
        List<BoardComment> comments = boardCommentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "대댓글 조회", description = "부모 댓글에 달린 대댓글들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대댓글 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "부모 댓글을 찾을 수 없음")
    })
    @GetMapping("/replies/{parentCommentId}")
    public ResponseEntity<List<BoardComment>> getRepliesByParentCommentId(@PathVariable Long parentCommentId) {
        List<BoardComment> replies = boardCommentService.getRepliesByParentCommentId(parentCommentId);
        return ResponseEntity.ok(replies);
    }

    @Operation(summary = "댓글 수정", description = "특정 댓글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long commentId, @RequestBody UpdateCommentRequest request) {
        boardCommentService.updateBoardComment(commentId, request.getContent());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "대댓글 수정", description = "대댓글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "대댓글 수정 성공"),
            @ApiResponse(responseCode = "404", description = "대댓글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/replies/{commentId}")
    public ResponseEntity<Void> updateReply(@PathVariable Long commentId, @RequestBody UpdateCommentRequest request) {
        boardCommentService.updateBoardComment(commentId, request.getContent());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        boardCommentService.deleteBoardComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "대댓글 삭제", description = "대댓글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "대댓글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "대댓글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/replies/{commentId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long commentId) {
        boardCommentService.deleteBoardComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
