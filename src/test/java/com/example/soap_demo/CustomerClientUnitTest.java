package com.example.soap_demo;

import com.example.soap_demo.client.CustomerClient;
import com.example.soap_demo.model.Customer;
import com.example.soap_demo.model.GetCustomerRequest;
import com.example.soap_demo.model.GetCustomerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.WebServiceTemplate;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerClientUnitTest {

    @Mock
    private WebServiceTemplate webServiceTemplate;

    private CustomerClient customerClient;

    @BeforeEach
    void setup() {
        customerClient = new CustomerClient(webServiceTemplate);
    }

    @Test
    void testGetCustomerReturnsValidCustomer() {
        Customer expectedCustomer = new Customer(5, "Customer 5");
        GetCustomerResponse mockResponse = new GetCustomerResponse();
        mockResponse.setCustomer(expectedCustomer);

        when(webServiceTemplate.marshalSendAndReceive(any(GetCustomerRequest.class)))
                .thenReturn(mockResponse);

        Customer actualCustomer = customerClient.getCustomer(5);

        assertNotNull(actualCustomer);
        assertEquals(5, actualCustomer.getId());
        assertEquals("Customer 5", actualCustomer.getName());
    }


    @Test
    void testGetCustomerReturnsNullCustomer() {
        GetCustomerResponse mockResponse = new GetCustomerResponse();
        mockResponse.setCustomer(null);

        when(webServiceTemplate.marshalSendAndReceive(any(GetCustomerRequest.class)))
                .thenReturn(mockResponse);

        Customer actualCustomer = customerClient.getCustomer(999);

        assertNull(actualCustomer, "Expected null customer when response contains no customer");
        verify(webServiceTemplate).marshalSendAndReceive(any(GetCustomerRequest.class));
    }


    @Test
    void testGetCustomerThrowsException() {
        when(webServiceTemplate.marshalSendAndReceive(any(GetCustomerRequest.class)))
                .thenThrow(new WebServiceIOException("SOAP Fault"));

        assertThrows(WebServiceIOException.class, () -> customerClient.getCustomer(2));
    }

}

