package com.iamhusrev.alican.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iamhusrev.alican.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    @Value("${security.token.headerName}")
    private String tokenHeaderName;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth/");
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws IOException {
        final String authHeader = request.getHeader(tokenHeaderName);

        if (authHeader == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpServletResponse.SC_FORBIDDEN);
            errorResponse.put("message", messageSource.getMessage("error.unauthorized", null, LocaleContextHolder.getLocale()));
            
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        try {
            final String userEmail = jwtService.extractUsername(authHeader);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(authHeader, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpServletResponse.SC_FORBIDDEN);
            errorResponse.put("message", messageSource.getMessage("error.jwt_invalid", null, LocaleContextHolder.getLocale()));
            
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}