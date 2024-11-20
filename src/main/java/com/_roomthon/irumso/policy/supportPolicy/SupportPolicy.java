package com._roomthon.irumso.policy.supportPolicy;

import com._roomthon.irumso.targetAudience.TargetAudience;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "support_policy")
@NoArgsConstructor
@Data
@Entity
public class SupportPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "service_id")
    private String serviceId;

    @Column(name = "detail_url")
    private String detailUrl;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "service_field")
    private String serviceField;

    @Column(name = "criteria", columnDefinition = "TEXT")
    private String criteria;

    @Column(name = "views")
    private Integer views;

    @Column(name = "likes")
    private Integer likes;

    @Column(name = "support_content", columnDefinition = "TEXT")
    private String supportContent;

    @Column(name = "purpose", columnDefinition = "TEXT")
    private String purpose;

    @Column(name = "apply_target", columnDefinition = "TEXT")
    private String applyTarget;

    @Column(name = "application_url")
    private String applicationUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "target_audience_id")
    private TargetAudience targetAudience;
}
