package com.example.soap_demo.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "getCustomerResponse", namespace = "http://example.com/soap_demo")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetCustomerResponse {

    @XmlElement(namespace = "http://example.com/soap_demo")
    private Customer customer;

    public GetCustomerResponse(Customer customer) {
        this.customer = customer;
    }

    public GetCustomerResponse(){}

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
