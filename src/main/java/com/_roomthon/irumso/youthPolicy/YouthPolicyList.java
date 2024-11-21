package com._roomthon.irumso.youthPolicy;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name = "youthPolicyList")
public class YouthPolicyList {
    @XmlElement(name = "youthPolicy")
    private List<YouthPolicy> policies; // 정책 리스트
}