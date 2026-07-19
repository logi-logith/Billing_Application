package io.github.logith.billing_application.controller;

import io.github.logith.billing_application.request.UpdateProfileRequest;
import io.github.logith.billing_application.response.UserResponse;
import io.github.logith.billing_application.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    // ─── User endpoints (any authenticated user) ─────────────────────────────

    // GET /api/users/me — view own profile
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        log.debug("GET /api/users/me — userId: {}", userId);
        return ResponseEntity.ok(userService.getMyProfile(userId));
    }

    // PUT /api/users/me — update own name / password
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        log.info("PUT /api/users/me — userId: {}", userId);
        return ResponseEntity.ok(userService.updateMyProfile(userId, request));
    }

    // DELETE /api/users/me — delete own account
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        log.info("DELETE /api/users/me — userId: {}", userId);
        userService.deleteMyAccount(userId);
        return ResponseEntity.noContent().build();
    }

    // ─── Admin endpoints ──────────────────────────────────────────────────────

    // GET /api/users — list all users (admin only)
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.debug("GET /api/users — admin listing all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET /api/users/{id} — get any user by id (admin only)
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.debug("GET /api/users/{} — admin fetch", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // DELETE /api/users/{id} — delete any user (admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/users/{} — admin delete", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
