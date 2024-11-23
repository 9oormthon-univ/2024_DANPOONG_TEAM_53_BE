package com._roomthon.irumso.board;

import com._roomthon.irumso.user.User;
import com._roomthon.irumso.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardPostService {

    private final UserService userService;
    private final BoardPostRepository boardPostRepository;
    private final BoardPostLikeRepository boardPostLikeRepository;
    private final BoardPostViewRecordRepository boardPostViewRecordRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final BoardCategoryRepository boardCategoryRepository;

    @Transactional
    public BoardPost createBoardPost(String nickname, String title, String content, Long categoryId, MultipartFile[] images) throws IOException {
        // 작성자 정보 조회
        User user = userService.findByNickname(nickname);

        // 카테고리 조회
        BoardCategory category = boardCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        // 게시글 객체 생성 (이미지는 빈 상태로 초기화)
        BoardPost boardPost = BoardPost.builder()
                .title(title)
                .content(content)
                .createdBy(user)
                .authorNickname(user.getNickname())
                .category(category)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .imageUrls(new ArrayList<>()) // 초기화된 빈 리스트
                .build();

        // 게시글 저장 (post_id 생성)
        BoardPost savedPost = boardPostRepository.save(boardPost);

        // 이미지 업로드 처리 후 추가
        if (images != null && images.length > 0) {
            if (images.length > 10) {
                throw new IllegalArgumentException("이미지는 최대 10장까지 업로드 가능합니다.");
            }
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String imageUrl = uploadImage(image);
                    if (imageUrl != null) {
                        savedPost.getImageUrls().add(imageUrl); // URL 추가
                    } else {
                        System.out.println("이미지 업로드 실패: " + image.getOriginalFilename());
                    }
                }
            }
        }

        System.out.println("최종 저장된 이미지 URLs: " + savedPost.getImageUrls());
        // 변경된 엔티티를 다시 저장
        return boardPostRepository.save(savedPost);
    }


    public BoardPost getBoardPostById(Long postId) {
        return boardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    public BoardPost updateBoardPost(Long postId, String title, String content, Long categoryId, MultipartFile[] images) throws IOException {
        // 게시글 조회
        BoardPost existingPost = boardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 카테고리 조회
        BoardCategory category = boardCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        // 게시글 정보 수정
        existingPost.setTitle(title);
        existingPost.setContent(content);
        existingPost.setCategory(category);  // 카테고리 수정

        // 이미지 업데이트
        if (images != null && images.length > 0) {
            if (images.length > 10) {
                throw new IllegalArgumentException("이미지는 최대 10장까지 업로드 가능합니다.");
            }

            // 기존 이미지 URL 제거 후 새로 추가
            existingPost.getImageUrls().clear(); // 기존 이미지 목록 삭제
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String imageUrl = uploadImage(image);
                    existingPost.getImageUrls().add(imageUrl); // 새 이미지 추가
                }
            }
        }

        existingPost.setUpdatedAt(LocalDateTime.now());

        return boardPostRepository.save(existingPost);
    }



    @Transactional
    public void deleteBoardPost(Long postId) {
        // 게시글에 연관된 댓글을 먼저 삭제 (대댓글도 포함)
        List<BoardComment> comments = boardCommentRepository.findByBoardPostId(postId);
        if (!comments.isEmpty()) {
            for (BoardComment comment : comments) {
                deleteReplies(comment);  // 대댓글 삭제
            }
            boardCommentRepository.deleteAll(comments);  // 댓글 삭제
        }

        // 게시글 삭제
        boardPostRepository.deleteById(postId);
    }

    // 댓글에 대한 대댓글을 재귀적으로 삭제
    private void deleteReplies(BoardComment parentComment) {
        List<BoardComment> replies = boardCommentRepository.findByParentCommentId(parentComment.getId());
        if (!replies.isEmpty()) {
            for (BoardComment reply : replies) {
                deleteReplies(reply);  // 대댓글의 대댓글을 삭제
            }
        }
        boardCommentRepository.delete(parentComment);  // 대댓글 삭제
    }

    // 게시글 검색
    public List<BoardPost> searchBoardPosts(String keyword) {
        return boardPostRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
    }

    // 좋아요 추가
    public void addLikeToPost(Long postId, String nickname) {
        BoardPost boardPost = boardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        User user = userService.findByNickname(nickname);

        // 이미 좋아요를 눌렀다면 취소
        if (boardPostLikeRepository.existsByPostAndUser(boardPost, user)) {
            throw new IllegalArgumentException("이미 좋아요를 누른 게시글입니다.");
        }

        // 좋아요 추가
        BoardPostLike postLike = BoardPostLike.builder()
                .user(user)
                .post(boardPost)
                .createdAt(LocalDateTime.now())
                .build();

        boardPostLikeRepository.save(postLike);

        // 게시글의 좋아요 수 증가
        boardPost.setLikes(boardPost.getLikes() + 1);
        boardPostRepository.save(boardPost);
    }

    // 좋아요 취소
    public void removeLikeFromPost(Long postId, String nickname) {
        BoardPost boardPost = boardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        User user = userService.findByNickname(nickname);

        BoardPostLike postLike = boardPostLikeRepository.findByPostAndUser(boardPost, user)
                .orElseThrow(() -> new IllegalArgumentException("좋아요가 없습니다."));

        // 좋아요 취소
        boardPostLikeRepository.delete(postLike);

        // 게시글의 좋아요 수 감소
        boardPost.setLikes(boardPost.getLikes() - 1);
        boardPostRepository.save(boardPost);
    }

    // 이미지 업로드
    @Value("${image.upload.dir}")
    private String uploadDir;

    public String uploadImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return null; // 비어 있는 경우 null 반환
        }

        // 파일 이름 고유화 (한글 깨짐 방지)
        String fileName = System.currentTimeMillis() + "_" + java.net.URLEncoder.encode(image.getOriginalFilename(), "UTF-8");
        Path path = Paths.get(uploadDir + fileName);
        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return "http://localhost:8080/uploads/" + fileName;
    }






    // 조회수 증가 (하루에 한 번만)
    public void increaseViewCount(Long postId, String nickname) {
        BoardPost boardPost = boardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        User user = userService.findByNickname(nickname);

        // 오늘 날짜 기준으로 조회 여부 확인 (LocalDateTime으로 변환하여 비교)
        LocalDate today = LocalDate.now();  // LocalDate를 사용하여 날짜만 처리
        LocalDateTime startOfDay = today.atStartOfDay();  // LocalDate를 LocalDateTime으로 변환하여 시작 시간 설정

        // BoardPostViewRecord 조회 (LocalDateTime을 기준으로 조회)
        Optional<BoardPostViewRecord> existingViewRecord = boardPostViewRecordRepository.findByUserAndPostAndViewedAtGreaterThanEqual(user, boardPost, startOfDay);

        // 오늘 이미 조회한 기록이 있다면 조회수 증가하지 않음
        if (existingViewRecord.isPresent()) {
            throw new IllegalStateException("하루에 한 번만 조회수를 증가시킬 수 있습니다.");
        }

        // 조회수 1 증가
        if (boardPost.getViewCount() == null) {
            boardPost.setViewCount(0);  // 조회수가 null일 경우 0으로 초기화
        }
        boardPost.setViewCount(boardPost.getViewCount() + 1);
        boardPostRepository.save(boardPost);

        // 조회 기록 저장
        BoardPostViewRecord viewRecord = BoardPostViewRecord.builder()
                .user(user)
                .post(boardPost)
                .viewedAt(LocalDateTime.now())  // 조회시간 기록 (LocalDateTime)
                .build();
        boardPostViewRecordRepository.save(viewRecord);
    }




    // 조회수가 많은 상위 5개 게시글 가져오기
    public List<BoardPost> getTop5PostsByViews() {
        return boardPostRepository.findTop5ByOrderByViewCountDesc();
    }

    // 좋아요가 많은 상위 5개 게시글 가져오기
    public List<BoardPost> getTop5PostsByLikes() {
        return boardPostRepository.findTop5ByOrderByLikesDesc();
    }
}
