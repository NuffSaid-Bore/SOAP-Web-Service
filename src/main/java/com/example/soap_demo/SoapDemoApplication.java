package com.example.soap_demo;

import com.example.soap_demo.model.Customer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.soap_demo.client.CustomerClient;

@SpringBootApplication
public class SoapDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoapDemoApplication.class, args);
	}

//    Cab be trigger by running:
//    Start and trigger the client manually
//    mvn spring-boot:run -Dspring-boot.run.arguments=client

    @Bean
    CommandLineRunner lookup(CustomerClient client) {
        return args -> {
            if (args.length > 0 && args[0].equalsIgnoreCase("client")) {
                System.out.println("Sending SOAP request for customer ID 1...");
                Customer customer = client.getCustomer(1);
                System.out.println("Received response: ID=" + customer.getId() + ", Name=" + customer.getName());
            } else {
                System.out.println("SOAP service started. Waiting for external requests (e.g., from SOAP UI)...");
            }
        };
    }

}
