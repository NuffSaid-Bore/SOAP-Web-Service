package com.example.soap_demo.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "getCustomerRequest", namespace = "http://example.com/soap_demo")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetCustomerRequest {

    @XmlElement(namespace = "http://example.com/soap_demo")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GetCustomerRequest(int id) {
        this.id = id;
    }

    public GetCustomerRequest(){}
}