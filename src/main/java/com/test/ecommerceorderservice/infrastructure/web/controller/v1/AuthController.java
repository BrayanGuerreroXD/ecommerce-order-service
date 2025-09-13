package com.test.ecommerceorderservice.infrastructure.web.controller.v1;

import com.test.ecommerceorderservice.application.dto.request.LoginRequest;
import com.test.ecommerceorderservice.application.dto.response.LoginResponse;
import com.test.ecommerceorderservice.application.service.AuthApplicationService;
import com.test.ecommerceorderservice.infrastructure.annotation.PublicIngress;
import com.test.ecommerceorderservice.infrastructure.web.dto.DefaultResponse;
import com.test.ecommerceorderservice.infrastructure.web.dto.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PublicIngress
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthApplicationService authApplicationService;

    @PostMapping(path = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public DefaultResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return new DefaultResponse<>(authApplicationService.login(loginRequest));
    }

    /*Metodo utilizado para realizar el cierre de sesion limpiando el token activo de la sesion*/
    @PutMapping(path = "/logout", produces = {MediaType.APPLICATION_JSON_VALUE})
    public DefaultResponse<SuccessResponse> closeSession(HttpServletRequest tokenRequest) {
        return new DefaultResponse<>(authApplicationService.closeSession(tokenRequest));
    }
}