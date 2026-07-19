package io.github.logith.billing_application.service.serviceImpl;

import io.github.logith.billing_application.entity.User;
import io.github.logith.billing_application.exception.ResourceNotFoundException;
import io.github.logith.billing_application.repository.UserRepository;
import io.github.logith.billing_application.request.UpdateProfileRequest;
import io.github.logith.billing_application.response.UserResponse;
import io.github.logith.billing_application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ─── User operations ────────────────────────────────────────────────────

    @Override
    public UserResponse getMyProfile(Long userId) {
        log.debug("Fetching profile for userId: {}", userId);
        User user = findUserById(userId);
        return UserResponse.fromEntity(user);
    }

    @Override
    @Transactional
    public UserResponse updateMyProfile(Long userId, UpdateProfileRequest request) {
        log.info("Update profile request for userId: {}", userId);
        User user = findUserById(userId);

        user.setName(request.name());

        // Password is optional — only update if provided
        if (request.password() != null && !request.password().isBlank()) {
            log.debug("Password change requested for userId: {}", userId);
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        user.setUpdatedAt(LocalDateTime.now());
        User saved = userRepository.save(user);
        log.info("Profile updated for userId: {}", userId);
        return UserResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public void deleteMyAccount(Long userId) {
        log.info("Delete account request for userId: {}", userId);
        User user = findUserById(userId);
        userRepository.delete(user);
        log.info("Account deleted for userId: {}", userId);
    }

    // ─── Admin operations ────────────────────────────────────────────────────

    @Override
    public List<UserResponse> getAllUsers() {
        log.debug("Admin: fetching all users");
        List<UserResponse> users = userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
        log.debug("Admin: found {} users", users.size());
        return users;
    }

    @Override
    public UserResponse getUserById(Long userId) {
        log.debug("Admin: fetching user by id: {}", userId);
        return UserResponse.fromEntity(findUserById(userId));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("Admin: deleting userId: {}", userId);
        User user = findUserById(userId);
        userRepository.delete(user);
        log.info("Admin: userId: {} deleted", userId);
    }

    // ─── Private helper ──────────────────────────────────────────────────────

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }
}
