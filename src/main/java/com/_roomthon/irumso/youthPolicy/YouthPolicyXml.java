package com._roomthon.irumso.youthPolicy;

import com._roomthon.irumso.targetAudience.TargetAudience;
import jakarta.persistence.Column;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        TargetAudience targetAudience = new TargetAudience();

        // YouthPolicyXml의 필드를 YouthPolicy 엔티티로 변환
        policy.setServiceId(this.bizId);
        policy.setServiceName(this.polyBizSjnm);
        policy.setServiceField(this.polyItcnCn);
        policy.setSupportContent(this.sporCn);
        policy.setApplicationUrl(this.rqutUrla);
        policy.setEmployStatus(empmSttsCn);

        if (this.ageInfo != null) {
            Pattern pattern = Pattern.compile("만 (\\d+)세 ~ (\\d+)세");
            Matcher matcher = pattern.matcher(this.ageInfo);

            if (matcher.find()) {
                int fromAge = Integer.parseInt(matcher.group(1));
                int toAge = Integer.parseInt(matcher.group(2));
                targetAudience.setFromAge(fromAge);
                targetAudience.setToAge(toAge);
            }

            if (this.ageInfo.contains("제한없음")) {
                targetAudience.setFromAge(0);
                targetAudience.setToAge(100);
            }
        }

        if (this.empmSttsCn != null) {
            if (this.empmSttsCn.contains("미취업자")) {
                targetAudience.setJobSeeker(true);
            }
            if (this.empmSttsCn.contains("창업자") || this.empmSttsCn.contains("프리랜서")|| this.empmSttsCn.contains("근로자")) {
                targetAudience.setWorker(true); // 창업자를 근로자로 설정
            }
            if (this.empmSttsCn.contains("학생")) {
                targetAudience.setStudent(true); // 창업자를 근로자로 설정
            }
            if (this.empmSttsCn.contains("제한없음")) {
                targetAudience.setJobSeeker(true);
                targetAudience.setWorker(true);
                targetAudience.setStudent(true);
            }
        }

        targetAudience.setBelow_50(true);
        targetAudience.setBetween_51_and_75(true);
        targetAudience.setBetween_76_and_100(true);
        targetAudience.setBetween_101_and_200(true);
        targetAudience.setAbove_200(true);

        policy.setTargetAudience(targetAudience);
        targetAudience.setYouthPolicy(policy);

        if (serviceType.equals("023040")) {  // Assuming 1 represents 의료
            policy.setServiceType(ServiceType.MEDICAL_SEVICE);
        } else if (serviceType.equals("023020")) {  // Assuming 1 represents 의료
            policy.setServiceType(ServiceType.HOUSING_SERVICE);
        } else if (serviceType.equals("023030")) {  // Assuming 1 represents 의료
            policy.setServiceType(ServiceType.EDUCATION_SERVICE);
        }

        return policy;
    }
}
