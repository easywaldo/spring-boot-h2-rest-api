package com.approval.document.documentapproval.config.security;

import com.approval.document.documentapproval.domain.service.AuthService;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    //@Autowired
    //private JwtUtils jwtUtils;

    @Autowired
    private AuthService authService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            String userId = authService.getUserIdFromJwtCookie(request);
            UserDetailsImpl userDetails = null;

            if (!Strings.isNullOrEmpty(userId)) {
                userDetails = new UserDetailsImpl(authService.getUserDetails(request));
                logger.debug("[userDetails " + request.getRequestURI() + " ] =>" + userDetails);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            // token 이 없거나 인증정보가 없으면 filter를 통과해서 security쪽으로 가서 401로
            logger.debug("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }
}
