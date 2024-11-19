package com._roomthon.irumso.targetAudience;

import com._roomthon.irumso.policy.Gender;
import com._roomthon.irumso.policy.SupportPolicy;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "target_audience")
@NoArgsConstructor
@Data
@Entity
public class TargetAudience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "service_id")
    private String serviceId;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "from_age")
    private int fromAge;

    @Column(name = "to_age")
    private int toAge;

    @Column(name = "student")
    private boolean student;

    @Column(name = "worker")
    private boolean worker;

    @Column(name = "job_seeker")
    private boolean jobSeeker;

    @Column(name = "below_50")
    private boolean below_50;

    @Column(name = "between_51_and_75")
    private boolean between_51_and_75;

    @Column(name = "between_76_and_100")
    private boolean between_76_and_100;

    @Column(name = "between_101_and_200")
    private boolean between_101_and_200;

    @Column(name = "above_200")
    private boolean above_200;

    @OneToOne(mappedBy = "targetAudience") // SupportPolicy에서 관리
    private SupportPolicy supportPolicy;
}
