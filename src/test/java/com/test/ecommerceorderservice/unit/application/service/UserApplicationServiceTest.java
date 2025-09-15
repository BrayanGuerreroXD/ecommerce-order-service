package com.test.ecommerceorderservice.unit.application.service;

import com.test.ecommerceorderservice.application.dto.request.UserCreateRequest;
import com.test.ecommerceorderservice.application.dto.request.UserUpdateRequest;
import com.test.ecommerceorderservice.application.dto.response.UserResponse;
import com.test.ecommerceorderservice.application.service.UserApplicationService;
import com.test.ecommerceorderservice.domain.model.Role;
import com.test.ecommerceorderservice.domain.model.User;
import com.test.ecommerceorderservice.domain.repository.RoleRepository;
import com.test.ecommerceorderservice.domain.repository.UserRepository;
import com.test.ecommerceorderservice.infrastructure.enums.exceptions.ExceptionCodeEnum;
import com.test.ecommerceorderservice.infrastructure.web.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserApplicationService Test")
class UserApplicationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserApplicationService userApplicationService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setKey("ADMIN");
        role.setName("Administrator");
    }

    @Test
    void createUser_success_unitTest() {
        UserCreateRequest request = new UserCreateRequest();
        request.setFullName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("1234");
        request.setRoleId(1L);

        User savedUser = new User();
        savedUser.setId(100L);
        savedUser.setFullName("John Doe");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword("encoded");
        savedUser.setRole(role);

        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userApplicationService.createUser(request);

        assertNotNull(response);
        assertEquals("John Doe", response.getFullName());
        assertEquals("john@example.com", response.getEmail());
        assertEquals(1L, response.getRoleId());

        verify(roleRepository, times(1)).findById(eq(1L)); // ver que se llamó con 1L
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void mocksAreInjected_check() throws Exception {
        assertTrue(Mockito.mockingDetails(roleRepository).isMock(), "roleRepository debe ser mock");
        assertTrue(Mockito.mockingDetails(userRepository).isMock(), "userRepository debe ser mock");

        Field f = userApplicationService.getClass().getDeclaredField("roleRepository");
        f.setAccessible(true);
        Object repoInsideService = f.get(userApplicationService);
        assertSame(roleRepository, repoInsideService, "El roleRepository dentro del service debe ser el mismo mock");
    }

    @Test
    void createUser_roleNotFound_throwsException() {
        UserCreateRequest request = new UserCreateRequest();
        request.setFullName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("1234");
        request.setRoleId(99L);

        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userApplicationService.createUser(request));

        assertNotNull(ex.getErrorResponse());
        assertNotNull(ex.getErrorResponse().getErrors());
        assertEquals(ExceptionCodeEnum.C01ROL01.name(),
                ex.getErrorResponse().getErrors().getCode().toString());
    }

    @Test
    void updateUser_success() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setFullName("Jane Doe");
        request.setEmail("jane@example.com");
        request.setPassword("abcd");
        request.setRoleId(1L);

        User existingUser = new User();
        existingUser.setId(200L);
        existingUser.setFullName("Old Name");
        existingUser.setEmail("old@example.com");
        existingUser.setPassword("oldPassword");
        existingUser.setRole(role);

        when(userRepository.findById(200L)).thenReturn(Optional.of(existingUser));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        System.out.println("DEBUG request.email=" + request.getEmail() + " request.password=" + request.getPassword());

        UserResponse response = userApplicationService.updateUser(200L, request);

        assertNotNull(response);
        assertEquals("Jane Doe", response.getFullName());
        assertEquals("jane@example.com", response.getEmail());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void getUserById_userExists() {
        User user = new User();
        user.setId(300L);
        user.setFullName("User Test");
        user.setEmail("test@example.com");
        user.setRole(role);

        when(userRepository.findById(300L)).thenReturn(Optional.of(user));

        UserResponse response = userApplicationService.getUserById(300L);

        assertNotNull(response);
        assertEquals("User Test", response.getFullName());
        assertEquals("test@example.com", response.getEmail());
    }

    @Test
    void getUserById_userNotFound() {
        when(userRepository.findById(400L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userApplicationService.getUserById(400L));

        assertNotNull(ex.getErrorResponse(), "ErrorResponse no debe ser null");
        assertNotNull(ex.getErrorResponse().getErrors(), "ErrorDetailResponse no debe ser null");

        assertEquals(ExceptionCodeEnum.C01USR01.name(),
                ex.getErrorResponse().getErrors().getCode().toString());
    }


    @Test
    void getAllUsers_returnsList() {
        User u1 = new User(1L, "User One", "one@example.com", "pass", role);
        User u2 = new User(2L, "User Two", "two@example.com", "pass", role);

        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<UserResponse> responses = userApplicationService.getAllUsers();

        assertEquals(2, responses.size());
        assertEquals("User One", responses.get(0).getFullName());
    }

    @Test
    void deleteUser_success() {
        User u = new User(500L, "Delete Me", "del@example.com", "pwd", role);

        when(userRepository.findById(500L)).thenReturn(Optional.of(u));
        doNothing().when(userRepository).deleteById(500L);

        boolean result = userApplicationService.deleteUser(500L);

        assertTrue(result);
        verify(userRepository, times(1)).deleteById(500L);
    }
}
