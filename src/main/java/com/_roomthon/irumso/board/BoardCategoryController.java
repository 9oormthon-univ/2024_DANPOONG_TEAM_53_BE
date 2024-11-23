package com._roomthon.irumso.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class BoardCategoryController {

    private final BoardCategoryService boardCategoryService;

    public BoardCategoryController(BoardCategoryService boardCategoryService) {
        this.boardCategoryService = boardCategoryService;
    }

    @Operation(
            summary = "모든 카테고리 조회",
            description = "데이터베이스에 저장된 모든 게시판 카테고리를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<BoardCategory>> getAllCategories() {
        List<BoardCategory> categories = boardCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
