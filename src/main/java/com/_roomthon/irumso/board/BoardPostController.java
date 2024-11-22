package com._roomthon.irumso.board;

import com._roomthon.irumso.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class BoardPostController {

    private final BoardPostService boardPostService;
    private final UserService userService;

    @Operation(summary = "게시글 작성", description = "게시글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPost(@RequestParam String title,
                                                          @RequestParam String content,
                                                          @RequestParam(required = false) MultipartFile image) throws IOException {
        String nickname = userService.getAuthenticatedUserNickname();
        BoardPost boardPost = boardPostService.createBoardPost(nickname, title, content, image);

        // 응답에 author, createdAt, updatedAt 추가
        Map<String, Object> response = Map.of(
                "author", boardPost.getCreatedBy().getNickname(),
                "createdAt", boardPost.getCreatedAt(),
                "updatedAt", boardPost.getUpdatedAt()  // 응답에 시간 추가
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> updatePost(@PathVariable Long postId,
                                                          @RequestParam String title,
                                                          @RequestParam String content,
                                                          @RequestParam(required = false) MultipartFile image) throws IOException {
        BoardPost updatedPost = boardPostService.updateBoardPost(postId, title, content, image);

        // 응답에 제목, 내용, createdAt, updatedAt 추가
        Map<String, Object> response = Map.of(
                "title", updatedPost.getTitle(),
                "content", updatedPost.getContent(),
                "createdAt", updatedPost.getCreatedAt(),
                "updatedAt", updatedPost.getUpdatedAt()  // 응답에 시간 추가
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        boardPostService.deleteBoardPost(postId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시글 검색", description = "게시글을 제목 또는 내용으로 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 검색 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/search")
    public ResponseEntity<List<BoardPost>> searchPosts(@RequestParam String keyword) {
        List<BoardPost> boardPosts = boardPostService.searchBoardPosts(keyword);
        return ResponseEntity.ok(boardPosts);
    }

    @Operation(summary = "좋아요 달기", description = "게시글에 좋아요를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 추가 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> addLike(@PathVariable Long postId) {
        String nickname = userService.getAuthenticatedUserNickname();  // 닉네임 가져오기
        boardPostService.addLikeToPost(postId, nickname);  // 닉네임을 함께 전달
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "좋아요 취소", description = "게시글에서 좋아요를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 취소 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> removeLike(@PathVariable Long postId) {
        String nickname = userService.getAuthenticatedUserNickname();  // 닉네임 가져오기
        boardPostService.removeLikeFromPost(postId, nickname);  // 닉네임을 함께 전달
        return ResponseEntity.ok().build();
    }

    // 조회수 증가
    @Operation(summary = "조회수 증가", description = "게시글 조회수를 증가시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회수 증가 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })

    @PostMapping("/{postId}/view")
    public ResponseEntity<Void> increaseViewCount(@PathVariable Long postId) {
        String nickname = userService.getAuthenticatedUserNickname();  // 닉네임 가져오기
        boardPostService.increaseViewCount(postId, nickname);  // 닉네임을 함께 전달
        return ResponseEntity.ok().build();
    }

    // 조회수가 많은 상위 5개 게시글
    @Operation(summary = "조회수가 많은 상위 5개 게시글", description = "조회수가 많은 게시글을 상위 5개 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상위 5개 게시글 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/top-views")
    public ResponseEntity<List<BoardPost>> getTopPostsByViews() {
        List<BoardPost> topPosts = boardPostService.getTop5PostsByViews();
        return ResponseEntity.ok(topPosts);
    }

    // 좋아요가 많은 상위 5개 게시글
    @Operation(summary = "좋아요가 많은 상위 5개 게시글", description = "좋아요가 많은 게시글을 상위 5개 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상위 5개 게시글 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/top-likes")
    public ResponseEntity<List<BoardPost>> getTopPostsByLikes() {
        List<BoardPost> topPosts = boardPostService.getTop5PostsByLikes();
        return ResponseEntity.ok(topPosts);
    }
}
