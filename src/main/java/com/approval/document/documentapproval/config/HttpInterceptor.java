package com.approval.document.documentapproval.config;

import com.approval.document.documentapproval.domain.service.AuthService;
import com.approval.document.documentapproval.domain.service.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

@Component
public class HttpInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    @Autowired
    public HttpInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Value("${api.key}")
    private String apiKey;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws IllegalAccessException {


        if (request.getRequestURI().equals("")) {
            return true;
        }

        if (request.getRequestURI().contains("swagger") ||
                request.getRequestURI().contains("api-docs") ||
                request.getRequestURI().equals("/") ||
                request.getRequestURI().contains("error") ||
                request.getRequestURI().contains("h2-console") ||
                request.getRequestURI().contains("csrf") ||
                request.getRequestURI().contains("favicon.ico") ||
                request.getRequestURI().contains("/member/userLogin") ||
                request.getRequestURI().contains("/member/userJoin") ||
                request.getRequestURI().contains("/actuator/prometheus") ||
                request.getRequestURI().contains("/actuator/shutdown")) {
            return true;
        }

        Optional<String> clientApiKey = Optional.ofNullable(request.getHeader("Authorization"));
        if (!clientApiKey.isPresent()) {
            throw new IllegalAccessException("no auth");
        }

        // Header 를 이용한 jwt 인증방식
        //String userJwt = request.getHeader("Authorization");
        //String userId = userJwt.trim().replace("Bearer", "");

        // Cookie 를 이용한 jwt 인증방식
        String userJwt = CookieService.getToken(request);

        if (userJwt.isEmpty()) throw new IllegalAccessException("no auth");
        Map<String, String> userInfo = authService.validateToken(userJwt);

        if (userInfo.get("name").isEmpty()) {
            throw new IllegalAccessException("no auth");
        }

        return apiKey.equals(clientApiKey.get());
    }
}
