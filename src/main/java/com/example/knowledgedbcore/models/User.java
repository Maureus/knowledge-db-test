package com.example.knowledgedbcore.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 120)
    private String password;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    private boolean active;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private String firstName;
    private String lastName;
    private String phoneNumber;

    @OneToOne(
            cascade = CascadeType.ALL
    )
    private Avatar avatar;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_subjects",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "subject_id")}
    )
    private Set<Subject> subjects = new HashSet<>();

    public User() {
    }

    public User(String username, String email, String pass) {
        this.username = username;
        this.email = email;
        this.password = pass;
    }

    public User(@NotBlank @Size(max = 20) String username,
                @NotBlank @Size(max = 120) String password,
                @NotBlank @Size(max = 50) @Email String email,
                Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

}
