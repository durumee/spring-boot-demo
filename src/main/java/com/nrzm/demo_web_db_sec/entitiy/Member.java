package com.nrzm.demo_web_db_sec.entitiy;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

    @Id
    private Long id;
    private String memberName;
    private String memberPassword;
    private String role;
}
