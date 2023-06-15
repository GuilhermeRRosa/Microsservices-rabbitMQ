package gg.code.productapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StatusController {

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus(){
        var response = new HashMap<String, Object>();

        response.put("service", "Product-API");
        response.put("status", "up");
        response.put("httpStatus", HttpStatus.OK);

        return ResponseEntity.ok(response);
    }

}
