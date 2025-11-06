package com.example.soap_demo.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "customer")
@XmlType(propOrder = { "id", "name" })
public class Customer {

    private int id;
    private String name;
}
