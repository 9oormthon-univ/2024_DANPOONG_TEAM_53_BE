package com._roomthon.irumso.youthPolicy;

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

    @Column(name = "policyId")
    private String policyId;

    @Column(name = "organization")
    private String organization;

    @Column(name = "policyName")
    private String policyName;

    @Column(name = "policyIntroduction", columnDefinition = "TEXT")
    private String policyIntroduction;

    @Column(name = "applyContent", columnDefinition = "TEXT")
    private String applyContent;

    @Column(name = "siteUrl")
    private String siteUrl;

    @Column(name = "applyOrganization")
    private String applyOrganization;

    @XmlElement(name = "ageInfo")
    private String ageInfo;

    @XmlElement(name = "employStatus")
    private String employStatus;

    @XmlElement(name = "educationStatus")
    private String educationStatus;

    @XmlElement(name = "major")
    private String major;

    private ServiceType serviceType;

}
