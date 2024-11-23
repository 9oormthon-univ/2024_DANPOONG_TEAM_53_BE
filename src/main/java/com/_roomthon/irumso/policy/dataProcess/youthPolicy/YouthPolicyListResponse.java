package com._roomthon.irumso.policy.dataProcess.youthPolicy;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "youthPolicyList")
@XmlAccessorType(XmlAccessType.FIELD)
public class YouthPolicyListResponse {

    @XmlElement(name = "pageIndex")
    private int pageIndex;

    @XmlElement(name = "totalCnt")
    private int totalCnt;

    @XmlElement(name = "youthPolicy")
    private List<YouthPolicyXml> policies;

    public List<YouthPolicyXml> getPolicies() {
        return policies;
    }
}
