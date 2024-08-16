package com.soap.controller;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ws.client.WebServiceTransportException;

import com.soap.client.SoapClient;
import com.soap.wsdl.CustomerRequest;
import com.soap.wsdl.CustomerResponse;

public class SoapControllerTest {
   @Mock
    private SoapClient soapClient;

    @InjectMocks
    private SoapController soapController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCustomer_Success() {
        CustomerRequest request = new CustomerRequest();
        request.setDocumentType("C");
        request.setDocumentNumber("23445322");

        CustomerResponse mockResponse = new CustomerResponse();
        // Configurar mockResponse con datos de prueba

        when(soapClient.getclient(request)).thenReturn(mockResponse);

        ResponseEntity<?> response = soapController.getCustomer(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockResponse);
    }

    @Test
    public void testGetCustomer_BadRequest_MissingDocumentType() {
        CustomerRequest request = new CustomerRequest();
        request.setDocumentNumber("23445322");

        ResponseEntity<?> response = soapController.getCustomer(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("El Tipo de documento es obligatorio");
    }

    @Test
    public void testGetCustomer_BadRequest_InvalidDocumentType() {
        CustomerRequest request = new CustomerRequest();
        request.setDocumentType("X");
        request.setDocumentNumber("23445322");

        ResponseEntity<?> response = soapController.getCustomer(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Tipo de documento inválido, únicamente válidos los tipos C (cédula de ciudadanía) y P (Pasaporte)");
    }

    @Test
    public void testGetCustomer_InternalServerError() {
        CustomerRequest request = new CustomerRequest();
        request.setDocumentType("C");
        request.setDocumentNumber("23445322");

        when(soapClient.getclient(request)).thenThrow(new WebServiceTransportException("Error"));

        ResponseEntity<?> response = soapController.getCustomer(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Error interno del servidor");
    }
}
