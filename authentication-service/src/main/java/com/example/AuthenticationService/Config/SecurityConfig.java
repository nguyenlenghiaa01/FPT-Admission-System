package com.example.AuthenticationService.Config;

import com.example.AuthenticationService.Service.AuthenticationService;
import com.example.AuthenticationService.Service.TokenService; // Import TokenService
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerExceptionResolver; // Import này để inject Resolver

@Configuration
@EnableMethodSecurity // Cho phép @PreAuthorize, @PostAuthorize
public class SecurityConfig {

    // Không cần AuthenticationService ở đây để làm UserDetailsService
    // nếu bạn ủy quyền việc xác thực credential hoàn toàn cho User Service
    // @Autowired AuthenticationService authenticationService;

    @Autowired
    Filter filter; // JWT Filter của bạn

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver; // Cần thiết cho filter để ném lỗi

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMap(){
        return new ModelMapper();
    }

    // Bean này vẫn cần nếu bạn có ý định sử dụng AuthenticationManager
    // cho các luồng xác thực khác (ví dụ: testing nội bộ, hoặc bạn có UserDetailsService
    // mà UserDetailsService đó gọi ra User Service để xác thực)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception {
        http
                .cors(cors -> {}) // Cấu hình CORS (thường được cấu hình sâu hơn ở nơi khác)
                .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF cho stateless API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        // Các API CÔNG KHAI của Auth Service (không cần token)
                        .requestMatchers(
                                "/api/users/login",
                                "/api/users/register",
                                "/api/users/forgot-password",
                                "/api/users/register/**",
                                "/swagger-ui/**",          // Swagger UI
                                "/v3/api-docs/**",         // OpenAPI docs
                                "/swagger-resources/**"    // Swagger resources
                        ).permitAll()
                        // TẤT CẢ các request khác CẦN xác thực (cần JWT)
                        .anyRequest().authenticated()
                )
                // KHÔNG SỬ DỤNG userDetailsService ở đây nếu bạn ủy quyền xác thực credentials
                // cho User Service. AuthenticationManager vẫn có thể được tạo, nhưng
                // bạn không cần cấu hình UserDetailsService trực tiếp trong SecurityFilterChain
                // theo cách này nếu Auth Service không quản lý user cục bộ.
                // .userDetailsService(...) // Bỏ dòng này

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // REST API là stateless
                )
                // Thêm JWT Filter của bạn vào trước UsernamePasswordAuthenticationFilter
                // Filter của bạn sẽ là nơi xác thực JWT cho các request cần bảo vệ
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean này cần được định nghĩa để handlerExceptionResolver hoạt động trong filter
    // Nếu bạn đã có nó ở nơi khác, bạn có thể bỏ qua
    @Bean(name = "myHandlerExceptionResolver")
    public HandlerExceptionResolver handlerExceptionResolver() {
        return new org.springframework.web.servlet.handler.HandlerExceptionResolverComposite();
    }
}