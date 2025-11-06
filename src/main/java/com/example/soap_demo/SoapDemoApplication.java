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
    @Bean
    CommandLineRunner lookup(CustomerClient client) {
        return args -> {
            System.out.println("Sending SOAP request for customer ID 1...");
            Customer customer = client.getCustomer(1);
            System.out.println("Received response: ID=" + customer.getId() + ", Name=" + customer.getName());
        };
    }

}
