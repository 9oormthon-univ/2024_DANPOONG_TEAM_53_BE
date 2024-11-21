package com._roomthon.irumso.board;

import com._roomthon.irumso.user.User;
import com._roomthon.irumso.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardPostService {

    private final UserService userService;
    private final BoardPostRepository boardPostRepository;

    @Value("${image.upload.dir}")
    private String uploadDir;

    public BoardPost createBoardPost(String nickname, String title, String content, MultipartFile image) throws IOException {
        User user = userService.findByNickname(nickname);

        String imageUrl = (image != null && !image.isEmpty()) ? uploadImage(image) : null;

        BoardPost boardPost = BoardPost.builder()
                .title(title)
                .content(content)
                .createdBy(user)
                .authorNickname(user.getNickname())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .imageUrl(imageUrl)
                .build();

        return boardPostRepository.save(boardPost);
    }

    public BoardPost updateBoardPost(Long postId, String title, String content, MultipartFile image) throws IOException {
        BoardPost existingPost = boardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        existingPost.setTitle(title);
        existingPost.setContent(content);

        String imageUrl = (image != null && !image.isEmpty()) ? uploadImage(image) : existingPost.getImageUrl();
        existingPost.setImageUrl(imageUrl);

        existingPost.setUpdatedAt(LocalDateTime.now());

        return boardPostRepository.save(existingPost);
    }

    public void deleteBoardPost(Long postId) {
        boardPostRepository.deleteById(postId);
    }

    public List<BoardPost> searchBoardPosts(String keyword) {
        return boardPostRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
    }

    public void addLikeToPost(Long postId) {
        BoardPost boardPost = boardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (boardPost.getLikes() == null) {
            boardPost.setLikes(0);
        }

        boardPost.setLikes(boardPost.getLikes() + 1);
        boardPostRepository.save(boardPost);
    }

    public void removeLikeFromPost(Long postId) {
        BoardPost boardPost = boardPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (boardPost.getLikes() > 0) {
            boardPost.setLikes(boardPost.getLikes() - 1);
        }

        boardPostRepository.save(boardPost);
    }

    public String uploadImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return null;
        }

        String fileName = image.getOriginalFilename();
        Path path = Paths.get("C:/Users/space/OneDrive/바탕 화면/changhwan space/DEVELOP/PROJECT(9room)/board_image/" + fileName);

        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return "http://localhost:8080/uploads/" + fileName;
    }
}
