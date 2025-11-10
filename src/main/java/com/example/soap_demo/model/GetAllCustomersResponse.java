package com.example.soap_demo.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "getAllCustomersResponse", namespace = "http://example.com/soap_demo")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetAllCustomersResponse {

    @XmlElement(name = "customers", namespace = "http://example.com/soap_demo")
    private List<Customer> customers = new ArrayList<>();

    public GetAllCustomersResponse() {
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}
