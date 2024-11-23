package com._roomthon.irumso.board;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardCategoryService {

    private final BoardCategoryRepository boardCategoryRepository;

    public BoardCategoryService(BoardCategoryRepository boardCategoryRepository) {
        this.boardCategoryRepository = boardCategoryRepository;
    }

    public List<BoardCategory> getAllCategories() {
        return boardCategoryRepository.findAll();
    }
}
