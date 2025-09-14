package com.test.ecommerceorderservice.application.service;

import com.test.ecommerceorderservice.application.dto.request.UserCreateRequest;
import com.test.ecommerceorderservice.application.dto.request.UserUpdateRequest;
import com.test.ecommerceorderservice.application.dto.response.UserResponse;
import com.test.ecommerceorderservice.domain.model.Role;
import com.test.ecommerceorderservice.domain.model.User;
import com.test.ecommerceorderservice.domain.repository.RoleRepository;
import com.test.ecommerceorderservice.domain.repository.UserRepository;
import com.test.ecommerceorderservice.infrastructure.enums.exceptions.ExceptionCodeEnum;
import com.test.ecommerceorderservice.infrastructure.web.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserApplicationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        Role role = roleRepository.findById(request.getRoleId()).orElseThrow(
                () -> new NotFoundException(ExceptionCodeEnum.C01ROL01)
        );
        String encodedPassword = new BCryptPasswordEncoder().encode(request.getPassword());

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(role);

        User saved = userRepository.save(user);

        return toResponse(saved);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = this.handleGetById(id);

        Role role = roleRepository.findById(request.getRoleId()).orElseThrow(
                () -> new NotFoundException(ExceptionCodeEnum.C01ROL01)
        );

        String encodedPassword = new BCryptPasswordEncoder().encode(request.getPassword());

        user.setFullName(request.getFullName());
        user.setPassword(encodedPassword);
        user.setEmail(request.getEmail());
        user.setRole(role);

        User updated = userRepository.save(user);

        return toResponse(updated);
    }

    public UserResponse getUserById(Long id) {
        User user = this.handleGetById(id);
        return toResponse(user);
    }

    public User getUserModelById(Long id) {
        return this.handleGetById(id);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public boolean deleteUser(Long id) {
        User user = this.handleGetById(id);
        userRepository.deleteById(user.getId());
        return true;
    }

    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        if (user.getRole() != null) {
            response.setRoleId(user.getRole().getId());
            response.setRoleKey(user.getRole().getKey());
            response.setRoleName(user.getRole().getName());
        }
        return response;
    }

    private User handleGetById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ExceptionCodeEnum.C01USR01)
        );
    }
}
