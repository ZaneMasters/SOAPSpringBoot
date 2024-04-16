package com.soap.client;

import com.soap.wsdl.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;


@Service
public class SoapClient {

	@Autowired
	private Jaxb2Marshaller marshaller;

	private WebServiceTemplate template;

	public CustomerResponse getclient(CustomerRequest request) {
		template = new WebServiceTemplate(marshaller);
		CustomerResponse customerResponse = (CustomerResponse) template.marshalSendAndReceive("http://localhost:8080/",
				request);

				if ("C".equalsIgnoreCase(request.getDocumentType()) && "23445322".equals(request.getDocumentNumber())) {
					customerResponse.setFirstName("John");
					customerResponse.setMiddleName("");
					customerResponse.setLastName("Doe");
					customerResponse.setSecondLastName("Smith");
					customerResponse.setPhone("123-456-7890");
					customerResponse.setAddress("123 Main St.");
					customerResponse.setCity("New York");
					return customerResponse;
				} else {
					return null;
				}
	}
    
}
