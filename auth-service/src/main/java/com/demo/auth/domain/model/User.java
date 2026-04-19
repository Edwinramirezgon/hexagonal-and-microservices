package com.demo.auth.domain.model;

public class User {

    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private String role;

    private User() {}

    public static User create(String username, String email, String passwordHash) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("El email es obligatorio.");
        if (passwordHash == null || passwordHash.isBlank())
            throw new IllegalArgumentException("La contraseña es obligatoria.");

        User u        = new User();
        u.username    = username;
        u.email       = email;
        u.passwordHash = passwordHash;
        u.role        = "USER";
        return u;
    }

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }
    public String getUsername()                { return username; }
    public String getEmail()                   { return email; }
    public String getPasswordHash()            { return passwordHash; }
    public String getRole()                    { return role; }
    public void setRole(String role)           { this.role = role; }
}
