package com._roomthon.irumso.youthPolicy;

import jakarta.persistence.Column;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class YouthPolicyXml {

    @XmlElement(name = "rnum")
    private int rnum;

    @XmlElement(name = "bizId")
    private String bizId;

    @XmlElement(name = "polyBizSecd")
    private String polyBizSecd;

    @XmlElement(name = "polyBizTy")
    private String polyBizTy;

    @XmlElement(name = "polyBizSjnm")
    private String polyBizSjnm;

    @XmlElement(name = "polyItcnCn")
    private String polyItcnCn;

    @XmlElement(name = "sporCn")
    @Column(columnDefinition = "TEXT")
    private String sporCn;

    @XmlElement(name = "rqutUrla")
    private String rqutUrla;

    @XmlElement(name = "mngtMson")
    private String mngtMson;

    @XmlElement(name = "cherCtpcCn")
    private String cherCtpcCn;

    @XmlElement(name = "cnsgNmor")
    private String cnsgNmor;

    @XmlElement(name = "etct")
    private String etct;

    @XmlElement(name = "polyRlmCd")
    private String polyRlmCd;

    @XmlElement(name = "ageInfo")
    private String ageInfo;

    @XmlElement(name = "empmSttsCn")
    private String empmSttsCn;

    @XmlElement(name = "accrRqisCn")
    private String accrRqisCn;

    @XmlElement(name = "majrRqisCn")
    private String majrRqisCn;

    // YouthPolicy 객체로 변환하는 toEntity 메서드
    public YouthPolicy toEntity(String serviceType) {
        YouthPolicy policy = new YouthPolicy();

        // YouthPolicyXml의 필드를 YouthPolicy 엔티티로 변환
        policy.setPolicyId(this.bizId);
        policy.setOrganization(this.polyBizTy);
        policy.setPolicyName(this.polyBizSjnm);
        policy.setPolicyIntroduction(this.polyItcnCn);
        policy.setPolicyIntroduction(this.sporCn);
        policy.setSiteUrl(this.rqutUrla);
        policy.setApplyOrganization(this.mngtMson);
        policy.setAgeInfo(this.ageInfo);
        policy.setEmployStatus(empmSttsCn);
        policy.setEducationStatus(empmSttsCn);
        policy.setMajor(majrRqisCn);

        if (serviceType.equals(004004001)) {  // Assuming 1 represents 의료
            policy.setServiceType(ServiceType.MEDICAL_SEVICE);
        } else if (serviceType.equals(004003)) {  // Assuming 1 represents 의료
            policy.setServiceType(ServiceType.HOUSING_SERVICE);
        } else if (serviceType.equals(004001001)) {  // Assuming 1 represents 의료
            policy.setServiceType(ServiceType.EDUCATION_SERVICE);
        }

        return policy;
    }
}
