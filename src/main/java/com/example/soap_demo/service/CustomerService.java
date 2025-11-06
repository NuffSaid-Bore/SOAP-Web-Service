package com.example.soap_demo.service;


import com.example.soap_demo.model.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    public Customer getCustomerById(int id) {
        // Dummy data
        return new Customer(id, "Customer " + id);
    }
}
