package com._roomthon.irumso.youthPolicy;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Component;

import java.io.StringReader;

@Component
public class YouthPolicyXmlParser {

    public YouthPolicyListResponse parseResponse(String xmlData) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(YouthPolicyListResponse.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (YouthPolicyListResponse) unmarshaller.unmarshal(new StringReader(xmlData));
    }
}