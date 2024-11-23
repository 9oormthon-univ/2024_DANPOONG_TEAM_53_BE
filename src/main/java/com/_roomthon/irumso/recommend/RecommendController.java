package com._roomthon.irumso.recommend;

import com._roomthon.irumso.global.base.template.BaseResponse;
import com._roomthon.irumso.user.addtionInfo.Gender;
import com._roomthon.irumso.user.addtionInfo.IncomeLevel;
import com._roomthon.irumso.user.addtionInfo.Job;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
@Tag(name = "추천 정책", description = "추천 정책 관리 API")
public class RecommendController {
    private final RecommendService recommendService;
    private final UserService userService;

    @Operation(summary = "추천 정책 조회", description = "추천 정책을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추천 정책 조회 성공",
                    content = @Content(schema = @Schema(implementation = SupportPolicyDto.class))),
            @ApiResponse(responseCode = "404", description = "조회할 수 없음",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping
    public ResponseEntity<?> getRecommendPolicy(@RequestHeader("Authorization") String token) {
        User user = userService.findByAccessToken(token);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseResponse.response("로그인 후 이용해주세요."));
        }

        List<SupportPolicyDto> supportPolicies = recommendService.getRecommendService(user.getNickname());

        if (supportPolicies == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(BaseResponse.response("실시간 인기 랭킹에서 정책을 찾을 수 없습니다."));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.response(supportPolicies));
    }

    @Operation(summary = "사용자 설문조사", description = "추천을 위한 사용자의 정보를 입력합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추천을 위한 사용자의 정보를 입력 성공",
                    content = @Content(schema = @Schema(implementation = SupportPolicyDto.class)))
    })
    @PostMapping("/survey")
    public ResponseEntity<?> inputRecommendSurvey(@RequestHeader("Authorization") String token,
                                                  @RequestParam(name = "gender") Gender gender,
                                                  @RequestParam(name = "age") int age,
                                                  @RequestParam(name = "job") Job job,
                                                  @RequestParam(name = "incomeLevel") IncomeLevel incomeLevel) {
        User user = userService.findByAccessToken(token);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseResponse.response("로그인 후 이용해주세요."));
        }

        try{
            recommendService.inputRecommendSurvey(user.getNickname(), gender, age, job, incomeLevel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.response("사용자의 추천정보가 성공적으로 입력되었습니다."));
    }
}
