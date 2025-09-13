package com.test.ecommerceorderservice.infrastructure.web.controller.v1;

import com.test.ecommerceorderservice.application.dto.request.UserCreateRequest;
import com.test.ecommerceorderservice.application.dto.request.UserUpdateRequest;
import com.test.ecommerceorderservice.application.dto.response.UserResponse;
import com.test.ecommerceorderservice.application.service.UserApplicationService;
import com.test.ecommerceorderservice.infrastructure.annotation.RoleVerify;
import com.test.ecommerceorderservice.infrastructure.enums.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserApplicationService userApplicationService;

    @RoleVerify(Role.ADMIN)
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @RequestBody @Valid UserCreateRequest request
    ) {
        UserResponse response = userApplicationService.createUser(request);
        return ResponseEntity.ok(response);
    }

    @RoleVerify({Role.ADMIN, Role.CUSTOMER})
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = userApplicationService.getUserById(id);
        if (response == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(response);
    }

    @RoleVerify(Role.ADMIN)
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userApplicationService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @RoleVerify(Role.ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        UserResponse response = userApplicationService.updateUser(id, request);
        if (response == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(response);
    }

    @RoleVerify(Role.ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userApplicationService.deleteUser(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}

