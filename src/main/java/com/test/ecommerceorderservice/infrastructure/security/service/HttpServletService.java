package com.test.ecommerceorderservice.infrastructure.security.service;

import com.test.ecommerceorderservice.infrastructure.enums.exceptions.OtherExceptionEnum;
import com.test.ecommerceorderservice.infrastructure.web.exception.ForbiddenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class HttpServletService {

    public String getToken(HttpServletRequest tokenRequest) {
        String authorization = tokenRequest.getHeader("Authorization");
        if (authorization == null || authorization.isEmpty()) {
            throw new ForbiddenException(OtherExceptionEnum.TOKEN_IS_NULL);
        }
        return authorization.split(" ")[1];
    }

}
