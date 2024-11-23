package com._roomthon.irumso.policy.like;

import com._roomthon.irumso.global.base.template.BaseResponse;
import com._roomthon.irumso.policy.SupportPolicyDto;
import com._roomthon.irumso.user.User;
import com._roomthon.irumso.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
@Tag(name = "정책 좋아요", description = "정책 좋아요 관리 API")
public class LikedPolicyController {
    private final UserService userService;
    private final LikedPolicyService likedPolicyService;

    @Operation(summary = "정책에 좋아요 누르기", description = "정책에 좋아요를 누릅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정책에 좋아요 성공",
                    content = @Content(schema = @Schema(implementation = LikedPolicyDto.class))),
            @ApiResponse(responseCode = "404", description = "좋아요를 누를 수 없음",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> setLikeToPolicy(@RequestHeader("Authorization") String token,
                                             @RequestParam(name = "policyId") Long policyId) {
        User user = userService.findByAccessToken(token);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseResponse.response("로그인 후 이용해주세요."));
        }

        LikedPolicyDto dto;
        try {
            dto = likedPolicyService.setLikeToPolicy(user.getNickname(), policyId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.response(dto));
    }
}
