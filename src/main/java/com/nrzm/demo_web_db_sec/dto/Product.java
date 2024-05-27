package com.nrzm.demo_web_db_sec.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Product {
    private int id;
    private String name;
    private String description;
    private int price;
}
