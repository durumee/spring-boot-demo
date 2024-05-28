package com.nrzm.demo.controller;

import com.nrzm.demo.dto.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    @GetMapping("/product")
    public ResponseEntity<List<Product>> getData() {
        List<Product> products = Arrays.asList(
                new Product(1, "상품1", "상품1 설명", 10000),
                new Product(2, "상품2", "상품2 설명", 20000),
                new Product(3, "상품3", "상품3 설명", 30000)
        );

        return ResponseEntity.ok(products);
    }
}
