package com.demo.auth.infrastructure.adapter.in.mapper;

import com.demo.auth.domain.model.User;
import com.demo.auth.infrastructure.adapter.in.dto.UserResponse;

public class AuthMapper {

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}
