package com.nrzm.demo.repository;

import com.nrzm.demo.entitiy.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByName(String name);

    // 복합 조건 쿼리
    List<User> findByAgeBetweenAndStatus(int startAge, int endAge, String status);
}