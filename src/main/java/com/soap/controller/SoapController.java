package com.soap.controller;

import com.soap.client.SoapClient;
import com.soap.wsdl.CustomerRequest;
import com.soap.wsdl.CustomerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ws.client.WebServiceTransportException;


@RestController
@CrossOrigin(originPatterns = "*")
public class SoapController {

    @Autowired
    private SoapClient soapClient;

    @PostMapping("/getCustomer")
    public ResponseEntity<?> getCustomer(@RequestBody CustomerRequest request) {
        if (request == null || request.getDocumentType() == null || request.getDocumentType() == "" ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El Tipo de documento es obligatorio");
        }
        if (request.getDocumentNumber() == null || request.getDocumentNumber().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El número de documento es obligatorio");
        }



        String documentType = request.getDocumentType();
        if (!"C".equals(documentType) && !"P".equals(documentType)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tipo de documento inválido, únicamente válidos los tipos C (cédula de ciudadanía) y P (Pasaporte)");
        }

        try {
            CustomerResponse customerResponse = soapClient.getclient(request);
            if (customerResponse != null) {
                return ResponseEntity.ok(customerResponse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
            }
        } catch (WebServiceTransportException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }




    }

}
