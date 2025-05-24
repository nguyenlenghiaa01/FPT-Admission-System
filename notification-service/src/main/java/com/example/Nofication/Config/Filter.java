package com.example.Nofication.Config;

import com.example.Nofication.Exception.AuthException;
import com.example.Nofication.Service.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class Filter extends OncePerRequestFilter {
    @Autowired
    private TokenUtil tokenService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver resolver;

    private final List<String> AUTH_PERMISSION = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**"
    );

    public boolean checkIsPublicAPI(String uri) {
        // nếu gặp những cái api trong list ở trên => cho phép truy cập luôn => true
        AntPathMatcher patchMatch = new AntPathMatcher();
        // check token => false
        return AUTH_PERMISSION.stream().anyMatch(pattern -> patchMatch.match(pattern, uri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // check xem cái api mà người dùng yêu cầu có phải là 1 public api?
        boolean isPublicAPI = checkIsPublicAPI(request.getRequestURI());
        if (isPublicAPI) {
            filterChain.doFilter(request, response);
        } else {
            String token = tokenService.getToken(request);
            if (token == null) {
                // ko được phép truy cập
                resolver.resolveException(request, response, null, new AuthException("Empty token!"));
                return;
            }

            // => có token
            // check xem token có đúng hay ko => lấy thông tin account từ token
            try {
                String email = tokenService.extractEmail(token);
                String role = tokenService.extractRole(token);

                // Tạo danh sách quyền từ role sau setup @PreAuthorize("hasRole('ADMIN')")
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + role)
                );

                // => token chuẩn
                // => cho phép truy cập
                // => lưu lại thông tin account
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        email, // principal (username)
                        null,  // credentials (mật khẩu, thường để null khi xác thực bằng token)
                        authorities  // danh sách quyền
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                // token ok, cho vào
                filterChain.doFilter(request, response);

            } catch (ExpiredJwtException e) {
                // response token hết hạn
                resolver.resolveException(request, response, null, new AuthException("Expired token!"));
                return;
            } catch (MalformedJwtException malformedJwtException) {
                // response token sai
                resolver.resolveException(request, response, null, new AuthException("Invalid token!"));
                return;
            } catch (Exception e) {
                resolver.resolveException(request, response, null, new AuthException("Token validation error: " + e.getMessage()));
                return;
            }
        }
    }

}