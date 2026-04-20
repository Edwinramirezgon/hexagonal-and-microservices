package com.demo.auth.infrastructure.adapter.out;

import com.demo.auth.application.port.out.UserRepository;
import com.demo.auth.domain.model.User;
import com.demo.auth.infrastructure.adapter.out.persistence.JpaUserRepository;
import com.demo.auth.infrastructure.adapter.out.persistence.UserEntity;

import java.util.Optional;

public class SqlServerUserAdapter implements UserRepository {

    private final JpaUserRepository jpa;

    public SqlServerUserAdapter(JpaUserRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity saved  = jpa.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpa.findByUsername(username).map(this::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpa.existsByUsername(username);
    }

    private UserEntity toEntity(User user) {
        UserEntity e = new UserEntity();
        e.setId(user.getId());
        e.setUsername(user.getUsername());
        e.setEmail(user.getEmail());
        e.setPasswordHash(user.getPasswordHash());
        e.setRole(user.getRole());
        return e;
    }

    private User toDomain(UserEntity e) {
        User u = User.create(e.getUsername(), e.getEmail(), e.getPasswordHash());
        u.setId(e.getId());
        u.setRole(e.getRole());
        return u;
    }
}
