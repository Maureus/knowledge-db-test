package com.example.knowledgedbcore.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Data
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private String name;
    private String semester;
    private String year;
    private String points;

    @ManyToMany(mappedBy = "subjects")
    private Set<User> users = new HashSet<>();


    public Subject(@NotBlank String name, String semester, String year, String points) {
        this.name = name;
        this.semester = semester;
        this.year = year;
        this.points = points;
    }

    public Subject(int id, String name, String semester, String year, String points) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.year = year;
        this.points = points;
    }


    public Subject() {

    }
}
