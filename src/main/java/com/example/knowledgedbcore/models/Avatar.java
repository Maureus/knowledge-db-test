package com.example.knowledgedbcore.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String fileName;
    private String fileType;
    @Lob
    private byte[] data;
}
