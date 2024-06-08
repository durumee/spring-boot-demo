package com.nrzm.demo.config;

import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class CustomMongoConfig {

    @Bean
    public MongoTemplate mongoTemplate() {
        try {
            return new MongoTemplate(MongoClients.create("mongodb://localhost:27017/mydatabase"), "mydatabase");
        } catch (Exception e) {
            // MongoDB 연결 실패 시 예외 처리
            return null;
        }
    }
}