package com._roomthon.irumso.policy.view;

import com._roomthon.irumso.global.base.template.BaseResponse;
import com._roomthon.irumso.policy.SupportPolicyDto;
import com._roomthon.irumso.policy.like.LikedPolicyService;
import com._roomthon.irumso.user.User;
import com._roomthon.irumso.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/view")
@Tag(name = "정책 조회", description = "정책 조회 관리 API")
public class ViewedPolicyController {
    private final UserService userService;
    private final ViewedPolicyService viewedPolicyService;

    @Operation(summary = "정책 조회", description = "정책 조회수 표시합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정책 조회 표시 성공",
                    content = @Content(schema = @Schema(implementation = SupportPolicyDto.class))),
            @ApiResponse(responseCode = "404", description = "정책 조회할 수 없음",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> viewedPolicy(@RequestHeader("Authorization") String token,
                                             @RequestParam(name = "policyId") Long policyId) {

        User user = userService.findByAccessToken(token);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseResponse.response("로그인 후 이용해주세요."));
        }

        ViewedPolicyDto dto;
        try {
            dto = viewedPolicyService.viewedPolicy(user.getNickname(), policyId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.response(dto));
    }

    @Operation(summary = "최근에 조회한 정책", description = "최근에 조회한 정책을 표시합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "최근에 조회한 정책 조회 성공",
                    content = @Content(schema = @Schema(implementation = SupportPolicyDto.class))),
            @ApiResponse(responseCode = "404", description = "최근에 본 정책 조회할 수 없음",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping
    public ResponseEntity<?> myViewed(@RequestHeader("Authorization") String token) {

        User user = userService.findByAccessToken(token);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseResponse.response("로그인 후 이용해주세요."));
        }

        List<SupportPolicyDto> dto;
        try {
            dto = viewedPolicyService.getMyViewed(user.getNickname());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.response(dto));
    }
}
