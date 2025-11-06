package com.example.soap_demo.controller;

import com.example.soap_demo.model.Customer;
import com.example.soap_demo.model.GetCustomerRequest;
import com.example.soap_demo.model.GetCustomerResponse;
import com.example.soap_demo.service.CustomerService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
public class CustomerController {

    private static final String NAMESPACE_URI = "http://example.com/soap_demo";

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCustomerRequest")
    @ResponsePayload
    public GetCustomerResponse getCustomer(@RequestPayload GetCustomerRequest request) {
        Customer customer = customerService.getCustomerById(request.getId());

        GetCustomerResponse response = new GetCustomerResponse();
        response.setCustomer(customer);

        return response;
    }


}
