package com._roomthon.irumso.policy.supportPolicy;

import com._roomthon.irumso.targetAudience.TargetAudience;
import com._roomthon.irumso.youthPolicy.ServiceType;
import com._roomthon.irumso.like.LikedPolicy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "service_field")
    private String serviceField;

    @Column(name = "applyTarget", columnDefinition = "TEXT")
    private String applyTarget;

    @Column(name = "views")
    private Integer views;

    @Column(name = "support_content", columnDefinition = "TEXT")
    private String supportContent;

    @Column(name = "purpose", columnDefinition = "TEXT")
    private String purpose;

    @Column(name = "application_url")
    private String applicationUrl;

    @Column(name = "service_type")
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "target_audience_id")
    private TargetAudience targetAudience;

    @JsonIgnore
    @OneToMany(mappedBy = "supportPolicy", cascade = CascadeType.ALL)
    private List<LikedPolicy> likedPolicies = new ArrayList<>();

    public void addLikedPolicy(LikedPolicy likedPolicy) {
        likedPolicy.setSupportPolicy(this);
        this.likedPolicies.add(likedPolicy);
    }

}
