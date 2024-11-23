package com._roomthon.irumso.policy.rank;

import com._roomthon.irumso.global.base.template.BaseResponse;
import com._roomthon.irumso.policy.SupportPolicyDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/rank")
@Tag(name = "실시간 인기 랭킹", description = "실시간 인기 랭킹 관리 API")
public class RankController {
    private final RankService rankService;

    @Operation(summary = "조회수로 실시간 인기 랭킹 조회", description = "조회수로 실시간 인기 랭킹을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "실시간 인기 랭킹 조회 성공",
                    content = @Content(schema = @Schema(implementation = SupportPolicyDto.class))),
            @ApiResponse(responseCode = "404", description = "조회할 수 없음",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/view")
    public ResponseEntity<?> getRank() {
        List<SupportPolicyDto> supportPolicies = rankService.getSupportPolicyRankByView();

        if (supportPolicies == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(BaseResponse.response("실시간 인기 랭킹에서 정책을 찾을 수 없습니다."));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.response(supportPolicies));
    }
}
