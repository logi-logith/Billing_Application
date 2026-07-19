package io.github.logith.billing_application.service;

import io.github.logith.billing_application.request.UpdateProfileRequest;
import io.github.logith.billing_application.response.UserResponse;

import java.util.List;

public interface UserService {

    // User — view own profile
    UserResponse getMyProfile(Long userId);

    // User — update own name / password
    UserResponse updateMyProfile(Long userId, UpdateProfileRequest request);

    // User — delete own account
    void deleteMyAccount(Long userId);

    // Admin — list all users
    List<UserResponse> getAllUsers();

    // Admin — get any user by id
    UserResponse getUserById(Long userId);

    // Admin — delete any user
    void deleteUser(Long userId);
}
