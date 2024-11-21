package com._roomthon.irumso.recommend;

import com._roomthon.irumso.user.addtionInfo.Gender;
import com._roomthon.irumso.user.addtionInfo.IncomeLevel;
import com._roomthon.irumso.user.addtionInfo.Job;
import com._roomthon.irumso.policy.SupportPolicyDto;
import com._roomthon.irumso.policy.supportPolicy.SupportPolicy;
import com._roomthon.irumso.policy.supportPolicy.SupportPolicyRepository;
import com._roomthon.irumso.targetAudience.TargetAudienceRepository;
import com._roomthon.irumso.user.suveyInfo.SurveyRecommendation;
import com._roomthon.irumso.user.suveyInfo.SurveyRecommendationRepository;
import com._roomthon.irumso.user.User;
import com._roomthon.irumso.user.UserService;
import com._roomthon.irumso.youthPolicy.YouthPolicy;
import com._roomthon.irumso.youthPolicy.YouthPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class RecommendService {
    private final UserService userService;
    private final SurveyRecommendationRepository surveyRecommendationRepository;
    private final SupportPolicyRepository supportPolicyRepository;
    private final YouthPolicyRepository youthPolicyRepository;

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

        Pageable pageable = PageRequest.of(0, 100);
        List<YouthPolicy> matchingAudiencesWithYouthPolicy = youthPolicyRepository.findMatchingAudiences(
                String.valueOf(survey.getGender()),
                survey.getAge(),
                String.valueOf(survey.getJob()),
                String.valueOf(survey.getIncomeLevel()), // Enum의 이름 사용
                pageable
        ).getContent();

        // Repository에서 필터링된 대상 조회
        List<SupportPolicy> matchingAudiencesWithSupportPolicy = supportPolicyRepository.findMatchingAudiencesWithLimit(
                String.valueOf(survey.getGender()),
                survey.getAge(),
                String.valueOf(survey.getJob()),
                String.valueOf(survey.getIncomeLevel()),
                pageable// Enum의 이름 사용
        ).getContent();

        // YouthPolicy와 SupportPolicy를 DTO로 변환 및 병합
        List<SupportPolicyDto> combinedList = Stream.concat(
                        matchingAudiencesWithYouthPolicy.stream().map(SupportPolicyDto::fromYouthPolicy),
                        matchingAudiencesWithSupportPolicy.stream().map(SupportPolicyDto::fromEntity)
                )
                .distinct() // 중복 제거 (equals/hashCode 기준)
                .collect(Collectors.toList());

        return combinedList;
    }

}
