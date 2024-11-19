package com.groomthon.irumso.service;

import com.groomthon.irumso.entity.BoardPost;
import com.groomthon.irumso.entity.BoardComment;
import com.groomthon.irumso.repository.BoardPostRepository;
import com.groomthon.irumso.repository.BoardCommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardPostRepository boardPostRepository;
    private final BoardCommentRepository boardCommentRepository;

    public BoardService(BoardPostRepository boardPostRepository, BoardCommentRepository boardCommentRepository) {
        this.boardPostRepository = boardPostRepository;
        this.boardCommentRepository = boardCommentRepository;
    }

    // 게시글 생성
    public BoardPost createPost(BoardPost post) {
        return boardPostRepository.save(post);
    }

    // 댓글 생성
    public BoardComment createComment(BoardComment comment) {
        return boardCommentRepository.save(comment);
    }

    // 제목으로 게시글 검색
    public List<BoardPost> searchPostsByTitle(String keyword) {
        return boardPostRepository.findByTitleContaining(keyword);
    }

    // 모든 게시글 조회
    public List<BoardPost> getAllPosts() {
        return boardPostRepository.findAll();
    }

    // 특정 게시글에 달린 댓글 조회
    public List<BoardComment> getCommentsByPostId(Long postId) {
        return boardCommentRepository.findByPostId(postId);
    }

    // 게시글에 첨부된 이미지를 바이트 배열로 처리하여 저장
    // 이미지를 byte[] 형태로 받아서 db에 저장
    public BoardPost savePostWithImage(BoardPost post, byte[] imageBytes) {
        post.setImage(imageBytes);  // 이미지 데이터를 게시글에 설정
        return boardPostRepository.save(post);  // 이미지와 함께 게시글 저장
    }
}
