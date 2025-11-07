package com.example.soap_demo.model;

import jakarta.xml.bind.annotation.*;


@XmlRootElement(name = "customer", namespace = "http://example.com/soap_demo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customer", propOrder = { "id", "name" })
public class Customer {
    @XmlElement(namespace = "http://example.com/soap_demo")
    private int id;

    @XmlElement(namespace = "http://example.com/soap_demo")
    private String name;

    // Required: no-arg constructor and getters/setters
    public Customer() {}
    public Customer(int id, String name) { this.id = id; this.name = name; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

