package com._roomthon.irumso.user.suveyInfo;

import com._roomthon.irumso.user.User;
import com._roomthon.irumso.user.addtionInfo.Gender;
import com._roomthon.irumso.user.addtionInfo.IncomeLevel;
import com._roomthon.irumso.user.addtionInfo.Job;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "survey_recommendation")
@NoArgsConstructor
@Data
@Entity
public class SurveyRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "income_level")
    private IncomeLevel incomeLevel;

    @Column(name = "age")
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(name = "job")
    private Job job;

    @OneToOne(mappedBy = "surveyRecommendation", fetch = FetchType.LAZY)
    private User user;
}
