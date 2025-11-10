package com.example.soap_demo.controller;

import com.example.soap_demo.model.*;
import com.example.soap_demo.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
public class CustomerController {

    private static final String NAMESPACE_URI = "http://example.com/soap_demo";

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCustomerRequest")
    @ResponsePayload
    public GetCustomerResponse getCustomer(@RequestPayload GetCustomerRequest request) {
        Customer customer = customerService.getCustomersById(request.getId());
        LOGGER.info("Sending request for customer ID: {}", request.getId());
        GetCustomerResponse response = new GetCustomerResponse();
        response.setCustomer(customer);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllCustomersRequest")
    @ResponsePayload
    public GetAllCustomersResponse getAllCustomers(@RequestPayload GetAllCustomersRequest request) {
        LOGGER.info("Received SOAP request to get all customers");
        GetAllCustomersResponse response = new GetAllCustomersResponse();
        response.getCustomers().addAll(customerService.getAllCustomers());
        return response;
    }



}
