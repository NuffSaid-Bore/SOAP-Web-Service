package com.example.soap_demo.service;


import com.example.soap_demo.exceptions.CustomerNotFoundException;
import com.example.soap_demo.model.Customer;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerService {

    private final Map<Integer, Customer> customerMap = new HashMap<>();
    private final Random random = new Random();

    public CustomerService() {

        List<String> names = Arrays.asList(
                "Alice", "Bob", "Charlie", "Diana", "Eve", "Frank", "Grace", "Hank", "Ivy", "Jack",
                "Kara", "Liam", "Mona", "Nina", "Oscar", "Paul", "Quincy", "Rita", "Sam", "Tina",
                "Uma", "Victor", "Wendy", "Xander", "Yara", "Zane", "Amber", "Brian", "Cathy", "Derek",
                "Ella", "Fred", "Gina", "Harold", "Isla", "Jon", "Kurt", "Lara", "Mike", "Nora",
                "Owen", "Penny", "Quinn", "Ralph", "Sara", "Tom", "Ursula", "Vince", "Will", "Zoe"
        );

        for (int i = 1; i <= 100; i++) {
            // Pick a random name from the list
            String name = names.get(random.nextInt(names.size()));

            // Optionally, add a suffix to make it more unique
            String uniqueName = name + " " + i;

            // Add to the map
            customerMap.put(i, new Customer(i, uniqueName));
        }
    }

    public Customer getCustomersById(int id) {
        Customer customer = customerMap.get(id);
        if (customer == null) {
            throw new CustomerNotFoundException(id);
        }
        return customer;
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    public Customer addCustomer(Customer customer) {
        customerMap.put(customer.getId(), customer);
        return customer;
    }

    public void deleteCustomer(int id) {
        customerMap.remove(id);
    }

    public Customer getCustomerById(int id) {
        // Dummy data
        return new Customer(id, "Customer " + id);
    }
}
