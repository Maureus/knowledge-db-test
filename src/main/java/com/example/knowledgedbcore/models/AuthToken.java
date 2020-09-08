package com.example.knowledgedbcore.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class AuthToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String jwtToken;

}
