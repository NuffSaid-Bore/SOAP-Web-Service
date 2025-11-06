package com.example.soap_demo.client;

import com.example.soap_demo.model.Customer;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import com.example.soap_demo.model.GetCustomerRequest;
import com.example.soap_demo.model.GetCustomerResponse;

@Component
public class CustomerClient {

    private final WebServiceTemplate webServiceTemplate;

    public CustomerClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }


    public Customer getCustomer(int id) {
        GetCustomerRequest request = new GetCustomerRequest();
        request.setId(id);

        GetCustomerResponse response = (GetCustomerResponse)
                webServiceTemplate.marshalSendAndReceive(request);

        return response.getCustomer();
    }

}
