package com.example.soap_demo.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "getAllCustomersRequest", namespace = "http://example.com/soap_demo")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetAllCustomersRequest {
    // No fields needed — it just triggers the fetch
}
