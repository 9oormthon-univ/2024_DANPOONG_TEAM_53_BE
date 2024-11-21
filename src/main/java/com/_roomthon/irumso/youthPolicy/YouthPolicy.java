package com._roomthon.irumso.youthPolicy;

import com._roomthon.irumso.targetAudience.TargetAudience;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class YouthPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serviceId")
    private String serviceId;

    @Column(name = "serviceName")
    private String serviceName;

    @Column(name = "serviceField", columnDefinition = "TEXT")
    private String serviceField;

    @Column(name = "supportContent", columnDefinition = "TEXT")
    private String supportContent;

    @Column(name = "applicationUrl")
    private String applicationUrl;

    @XmlElement(name = "ageInfo")
    private String ageInfo;

    @XmlElement(name = "employStatus")
    private String employStatus;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "target_audience_id")
    private TargetAudience targetAudience;

}
