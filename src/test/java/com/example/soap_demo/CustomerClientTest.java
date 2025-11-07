package com.example.soap_demo;


import com.example.soap_demo.client.CustomerClient;
import com.example.soap_demo.config.SoapClientConfig;
import com.example.soap_demo.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.xml.transform.StringSource;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;
import static org.springframework.ws.test.client.ResponseCreators.withServerOrReceiverFault;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;





@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SoapClientConfig.class})
public class CustomerClientTest {

    @Autowired
    private WebServiceTemplate webServiceTemplate;

    @Autowired
    private Jaxb2Marshaller marshaller;

    private MockWebServiceServer mockServer;
    private CustomerClient customerClient;

    @BeforeEach
    void setup() {
        mockServer = MockWebServiceServer.createServer(webServiceTemplate);
        customerClient = new CustomerClient(webServiceTemplate);
    }

    @Test
    void testGetCustomerSuccess() throws Exception {
        String requestPayload =
                "<ns2:getCustomerRequest xmlns:ns2=\"http://example.com/soap_demo\">" +
                        "<ns2:id>1</ns2:id>" +
                        "</ns2:getCustomerRequest>";

        String responsePayload =
                "<ns2:getCustomerResponse xmlns:ns2=\"http://example.com/soap_demo\">" +
                        "<ns2:customer>" +
                        "<ns2:id>1</ns2:id>" +
                        "<ns2:name>Customer 1</ns2:name>" +
                        "</ns2:customer>" +
                        "</ns2:getCustomerResponse>";

        mockServer.expect(payload(new StringSource(requestPayload)))
                .andRespond(withPayload(new StringSource(responsePayload)));

        Customer customer = customerClient.getCustomer(1);

        assertNotNull(customer);
        assertEquals(1, customer.getId());
        assertEquals("Customer 1", customer.getName());

        mockServer.verify();
    }

    @Test
    void testGetCustomerNotFound() throws Exception {
        String requestPayload =
                "<ns2:getCustomerRequest xmlns:ns2=\"http://example.com/soap_demo\">" +
                        "<ns2:id>999</ns2:id>" +
                        "</ns2:getCustomerRequest>";

        String responsePayload =
                "<ns2:getCustomerResponse xmlns:ns2=\"http://example.com/soap_demo\">" +
                        "<ns2:customer/>" +
                        "</ns2:getCustomerResponse>";

        mockServer.expect(payload(new StringSource(requestPayload)))
                .andRespond(withPayload(new StringSource(responsePayload)));

        Customer customer = customerClient.getCustomer(999);

        assertNotNull(customer);
        assertEquals(0, customer.getId()); // default value
        assertNull(customer.getName());

        mockServer.verify();
    }

    @Test
    void testSoapFault() {
        String requestPayload =
                "<ns2:getCustomerRequest xmlns:ns2=\"http://example.com/soap_demo\">" +
                        "<ns2:id>2</ns2:id>" +
                        "</ns2:getCustomerRequest>";

        mockServer.expect(payload(new StringSource(requestPayload)))
                .andRespond(withServerOrReceiverFault("Internal Server Error", Locale.ENGLISH));

        assertThrows(SoapFaultClientException.class, () -> customerClient.getCustomer(2));


        mockServer.verify();
    }

}

