package com.linkedin.post_service.utils;

import com.linkedin.post_service.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    private final String ID_KEY = "x-user-id";
    private final String EMAIL_KEY = "x-user-email";
    private final String IS_ADMIN_KEY = "x-user-admin";

    public String getUserId(HttpServletRequest request) {
        String id = request.getHeader(ID_KEY);

        if (id == null || id.isEmpty()) throw new ApiException(HttpStatus.BAD_REQUEST, ID_KEY + " not found");

        return id;
    }

    public String getUserEmail(HttpServletRequest request) {
        String email = request.getHeader(EMAIL_KEY);

        if (email == null || email.isEmpty()) throw new ApiException(HttpStatus.BAD_REQUEST, EMAIL_KEY + " not found");

        return email;
    }

    public boolean isAdmin(HttpServletRequest request) {
        return Boolean.parseBoolean(request.getHeader(IS_ADMIN_KEY));
    }

    public void shouldBeAdmin(HttpServletRequest request) {
        boolean isAdmin = Boolean.parseBoolean(request.getHeader(IS_ADMIN_KEY));
        if (!isAdmin) throw new ApiException(HttpStatus.FORBIDDEN, "Only admins are allowed to access this endpoint.");
    }
}
