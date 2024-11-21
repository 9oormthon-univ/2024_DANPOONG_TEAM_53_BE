package com._roomthon.irumso.recommend;

import com._roomthon.irumso.policy.Gender;
import com._roomthon.irumso.policy.IncomeLevel;
import com._roomthon.irumso.policy.Job;
import com._roomthon.irumso.policy.SupportPolicyDto;
import com._roomthon.irumso.policy.supportPolicy.SupportPolicy;
import com._roomthon.irumso.policy.supportPolicy.SupportPolicyRepository;
import com._roomthon.irumso.targetAudience.TargetAudienceRepository;
import com._roomthon.irumso.user.SurveyRecommendation;
import com._roomthon.irumso.user.SurveyRecommendationRepository;
import com._roomthon.irumso.user.User;
import com._roomthon.irumso.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RecommendService {
    private final UserService userService;
    private final TargetAudienceRepository targetAudienceRepository;
    private final SurveyRecommendationRepository surveyRecommendationRepository;
    private final SupportPolicyRepository supportPolicyRepository;

    public void inputRecommendSurvey(String email, Gender gender, int age, Job job, IncomeLevel incomeLevel) {
        User user = userService.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("사용자 정보가 존재하지 않거나 설문 정보를 찾을 수 없습니다.");
        }

        SurveyRecommendation recommendation = new SurveyRecommendation();
        recommendation.setGender(gender);
        recommendation.setAge(age);
        recommendation.setJob(job);
        recommendation.setIncomeLevel(incomeLevel);

        recommendation.setUser(user);
        user.setSurveyRecommendation(recommendation);

        surveyRecommendationRepository.save(recommendation);

    }

    public List<SupportPolicyDto> getRecommendService(String email) {
        User user = userService.findByEmail(email);
        if (user == null || user.getSurveyRecommendation() == null) {
            throw new IllegalArgumentException("사용자 정보가 존재하지 않거나 설문 정보를 찾을 수 없습니다.");
        }

        SurveyRecommendation survey = user.getSurveyRecommendation();

        // Repository에서 필터링된 대상 조회
        List<SupportPolicy> matchingAudiences = supportPolicyRepository.findMatchingAudiences(
                survey.getGender(),
                survey.getAge(),
                String.valueOf(survey.getJob()),
                String.valueOf(survey.getIncomeLevel()) // Enum의 이름 사용
        );

        // 추천 서비스 DTO로 변환
        return matchingAudiences.stream()
                .map(SupportPolicyDto::fromEntity)
                .collect(Collectors.toList());
    }

}
