package com.demo.auth.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role;

    public UserEntity() {}

    public Long   getId()           { return id; }
    public String getUsername()     { return username; }
    public String getEmail()        { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole()         { return role; }

    public void setId(Long id)                   { this.id = id; }
    public void setUsername(String username)     { this.username = username; }
    public void setEmail(String email)           { this.email = email; }
    public void setPasswordHash(String hash)     { this.passwordHash = hash; }
    public void setRole(String role)             { this.role = role; }
}
