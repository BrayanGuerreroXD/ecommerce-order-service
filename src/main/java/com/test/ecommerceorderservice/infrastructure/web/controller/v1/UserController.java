package com.test.ecommerceorderservice.infrastructure.web.controller.v1;

import com.test.ecommerceorderservice.application.dto.request.UserCreateRequest;
import com.test.ecommerceorderservice.application.dto.request.UserUpdateRequest;
import com.test.ecommerceorderservice.application.dto.response.UserResponse;
import com.test.ecommerceorderservice.application.service.UserApplicationService;
import com.test.ecommerceorderservice.infrastructure.annotation.RoleVerify;
import com.test.ecommerceorderservice.infrastructure.enums.Role;
import com.test.ecommerceorderservice.infrastructure.web.dto.DefaultResponse;
import com.test.ecommerceorderservice.infrastructure.web.dto.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserApplicationService userApplicationService;

    @RoleVerify(Role.ADMIN)
    @PostMapping
    public DefaultResponse<UserResponse> createUser(
            @RequestBody @Valid UserCreateRequest request
    ) {
        UserResponse response = userApplicationService.createUser(request);
        return new DefaultResponse<>(response);
    }

    @RoleVerify({Role.ADMIN, Role.CUSTOMER})
    @GetMapping("/{id}")
    public DefaultResponse<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = userApplicationService.getUserById(id);
        return new DefaultResponse<>(response);
    }

    @RoleVerify(Role.ADMIN)
    @GetMapping
    public DefaultResponse<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userApplicationService.getAllUsers();
        return new DefaultResponse<>(users);
    }

    @RoleVerify(Role.ADMIN)
    @PutMapping("/{id}")
    public DefaultResponse<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        UserResponse response = userApplicationService.updateUser(id, request);
        return new DefaultResponse<>(response);
    }

    @RoleVerify(Role.ADMIN)
    @DeleteMapping("/{id}")
    public DefaultResponse<SuccessResponse> deleteUser(@PathVariable Long id) {
        boolean deleted = userApplicationService.deleteUser(id);
        return new DefaultResponse<>(new SuccessResponse(deleted));
    }
}

