package com._roomthon.irumso.policy;

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

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "age")
    private int age;

    @Column(name = "job")
    private String job;

    @Column(name = "income_level")
    private IncomeLevel incomeLevel;
}
