package com.example.AuthenticationService.Config;

import com.example.AuthenticationService.Service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.io.IOException;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.example.AuthenticationService.Exception.AuthException;

@Component
public class Filter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver resolver;

    private final List<String> PUBLIC_AUTH_SERVICE_APIS = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/forgot-password",
            "/api/auth/getEmail",
            "/actuator/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public boolean checkIsPublicAPI(String uri) {
        return PUBLIC_AUTH_SERVICE_APIS.stream().anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        System.out.println("[Filter] Incoming request: " + requestURI + ", full URL: " + request.getRequestURL());
        boolean isPublicAPI = checkIsPublicAPI(requestURI);
        System.out.println("[Filter] isPublicAPI? " + isPublicAPI);
        if (isPublicAPI) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);
        if (token == null) {
            resolver.resolveException(request, response, null, new AuthException("JWT token is missing!"));
            return;
        }

        try {
            Claims claims = tokenService.extractAllClaims(token);
            String email = claims.getSubject();
            String rolesString = (String) claims.get("roles");

            Collection<SimpleGrantedAuthority> authorities = Arrays.stream(rolesString.split(","))
                    .map(String::trim)
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    authorities
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            resolver.resolveException(request, response, null, new AuthException("JWT token has expired!"));
        } catch (MalformedJwtException e) {
            resolver.resolveException(request, response, null, new AuthException("Invalid JWT token format!"));
        } catch (Exception e) {
            resolver.resolveException(request, response, null, new AuthException("JWT token processing error: " + e.getMessage()));
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}