
---

# 🧼 Spring Boot SOAP Web Service – Customer Service Demo

This project demonstrates how to build a **SOAP-based web service** and a **SOAP client** using **Spring Boot** and **Spring Web Services (Spring-WS)**.
It shows how to:

* Expose SOAP endpoints with XML request/response payloads
* Define WSDL automatically from an XSD schema
* Send SOAP requests and receive responses using a client (`WebServiceTemplate`)
* Marshal/unmarshal Java objects to and from XML automatically using JAXB

---

## 🏗️ Project Overview

The system simulates a **Customer Service**, where a SOAP request containing a customer ID returns the corresponding `Customer` object (ID + name).

### Components

| Layer                  | Class                                                   | Purpose                                                    |
| ---------------------- | ------------------------------------------------------- | ---------------------------------------------------------- |
| **Application**        | `SoapDemoApplication`                                   | Entry point of the Spring Boot application.                |
| **SOAP Configuration** | `WebServiceConfig`                                      | Configures SOAP servlet, WSDL, and schema.                 |
| **SOAP Client Config** | `SoapClientConfig`                                      | Configures `WebServiceTemplate` for sending SOAP requests. |
| **SOAP Endpoint**      | `CustomerController`                                    | Handles incoming SOAP requests (`@Endpoint`).              |
| **SOAP Client**        | `CustomerClient`                                        | Sends SOAP requests to the endpoint.                       |
| **Service Layer**      | `CustomerService`                                       | Provides customer data.                                    |
| **Data Model**         | `Customer`, `GetCustomerRequest`, `GetCustomerResponse` | JAXB-bound classes for SOAP XML marshalling/unmarshalling. |

---

## ⚙️ Technologies Used

* **Java 17+**
* **Spring Boot 3+**
* **Spring Web Services (Spring-WS)**
* **JAXB (Jakarta XML Binding)** for XML marshalling/unmarshalling
* **Maven** (or Gradle)

---

## 🧩 How It Works

### 1. SOAP Web Service Flow

1. **Client** creates a SOAP request (`GetCustomerRequest`) and sends it to `/ws`.
2. **Spring WS Dispatcher** routes the request to the proper `@Endpoint` method (`CustomerController`).
3. The **endpoint** calls `CustomerService` to fetch customer data.
4. A `GetCustomerResponse` is created and marshalled into an XML SOAP response.
5. **Client** unmarshals the XML into a Java object (`Customer`) automatically.

---

## 📦 Project Structure

```
com.example.soap_demo
├── SoapDemoApplication.java
├── client
│   └── CustomerClient.java
├── config
│   ├── SoapClientConfig.java
│   └── WebServiceConfig.java
├── endpoint
│   └── CustomerController.java
├── model
│   ├── Customer.java
│   ├── GetCustomerRequest.java
│   └── GetCustomerResponse.java
└── service
    └── CustomerService.java
resources
└── customer.xsd
```

---

## 🧠 Key Concepts Explained

### 🧱 JAXB (Java Architecture for XML Binding)

JAXB allows Java objects to be automatically **converted to and from XML**.

* `@XmlRootElement` — defines the XML root tag.
* `@XmlAccessorType(XmlAccessType.FIELD)` — tells JAXB to bind fields directly.
* `@XmlType(propOrder = {"id", "name"})` — defines XML element order.

Example:

```java
@XmlRootElement(name = "customer", namespace = "http://example.com/soap_demo")
@XmlAccessorType(XmlAccessType.FIELD)
public class Customer {
    private int id;
    private String name;
}
```

---

### 🚪 Endpoint (Server-Side)

The `@Endpoint` class handles SOAP requests.
`@PayloadRoot` maps incoming XML to the correct method based on the **namespace** and **root element**.

```java
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
```

---

### 🧭 WSDL Configuration

The `WebServiceConfig` class defines how the WSDL and schema are exposed.

```java
@EnableWs
@Configuration
public class WebServiceConfig {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "customers")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema customerSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("CustomerPort");
        definition.setLocationUri("/ws");
        definition.setTargetNamespace("http://example.com/soap_demo");
        definition.setSchema(customerSchema);
        return definition;
    }

    @Bean
    public XsdSchema customerSchema() {
        return new SimpleXsdSchema(new ClassPathResource("customer.xsd"));
    }
}
```

Once the app runs, the WSDL is available at:
👉 **`http://localhost:8080/ws/customers.wsdl`**

---

### 📬 Client Configuration

The client uses `WebServiceTemplate` to send SOAP requests.

```java
@Configuration
public class SoapClientConfig {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(
            Customer.class,
            GetCustomerRequest.class,
            GetCustomerResponse.class
        );
        return marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller marshaller) {
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMarshaller(marshaller);
        template.setUnmarshaller(marshaller);
        template.setDefaultUri("http://localhost:8080/ws");
        return template;
    }
}
```

---

### 🧑‍💻 The SOAP Client

```java
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
```

---

### ⚙️ Main Application

```java
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
```

When you run the app, it automatically sends a SOAP request at startup.

---

## 🧾 Example SOAP XML

### 📤 Request (`GetCustomerRequest`)

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:demo="http://example.com/soap_demo">
   <soapenv:Header/>
   <soapenv:Body>
      <demo:getCustomerRequest>
         <id>1</id>
      </demo:getCustomerRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### 📥 Response (`GetCustomerResponse`)

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:demo="http://example.com/soap_demo">
   <soapenv:Header/>
   <soapenv:Body>
      <demo:getCustomerResponse>
         <demo:customer>
            <demo:id>1</demo:id>
            <demo:name>Customer 1</demo:name>
         </demo:customer>
      </demo:getCustomerResponse>
   </soapenv:Body>
</soapenv:Envelope>
```

---

## 🧩 Example XSD (`customer.xsd`)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
           targetNamespace="http://example.com/soap_demo"
           xmlns:tns="http://example.com/soap_demo"
           elementFormDefault="qualified">

    <xs:element name="getCustomerRequest">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="id" type="xs:int"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="getCustomerResponse">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="customer" type="tns:customer"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:complexType name="customer">
        <xs:sequence>
            <xs:element name="id" type="xs:int"/>
            <xs:element name="name" type="xs:string"/>
        </xs:sequence>
    </xs:complexType>

</xs:schema>
```

---

## 🧪 Running and Testing

### 1️⃣ Start the Application

```bash
mvn spring-boot:run
```

### 2️⃣ Check the WSDL

Open your browser and go to:

```
http://localhost:8080/ws/customers.wsdl
```

### 3️⃣ Observe the Console Output

```
Sending SOAP request for customer ID 1...
Received response: ID=1, Name=Customer 1
```

### 4️⃣ Optional – Test with SOAP UI or Postman

Use the WSDL URL above and send a request like the sample shown.

---

## 📘 Summary

| Concept                     | Description                                                 |
| --------------------------- | ----------------------------------------------------------- |
| **SOAP**                    | XML-based protocol for exchanging structured data.          |
| **WSDL**                    | Web Service Description Language; defines service contract. |
| **XSD**                     | XML Schema; defines data types used in WSDL.                |
| **Marshaller/Unmarshaller** | Convert between Java objects and XML.                       |
| **@Endpoint**               | Marks a class as a SOAP message handler.                    |
| **WebServiceTemplate**      | Client tool to send/receive SOAP messages.                  |

---

## 🧰 Troubleshooting

| Issue                                                   | Cause                                          | Solution                                                              |
| ------------------------------------------------------- | ---------------------------------------------- | --------------------------------------------------------------------- |
| `404 Not Found`                                         | Wrong endpoint URI or XML root element         | Check that `localPart` matches request element (`getCustomerRequest`) |
| `org.springframework.oxm.UnmarshallingFailureException` | Wrong namespace or missing XSD binding         | Ensure namespaces and JAXB annotations match the XSD                  |
| `Connection refused`                                    | Server not running before client sends request | Start the SOAP service first                                          |

---

## 🏁 Conclusion

This project demonstrates a full **SOAP Web Service lifecycle** using Spring Boot — from WSDL generation to client consumption — and can serve as a strong foundation for integrating with enterprise-grade SOAP systems (like ERP, CRM, or legacy services).

---
