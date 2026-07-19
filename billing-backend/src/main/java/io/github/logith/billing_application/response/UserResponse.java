package io.github.logith.billing_application.response;

import io.github.logith.billing_application.entity.User;
import io.github.logith.billing_application.entity.enums.UserRole;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
