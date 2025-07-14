package com.example.AuthenticationService.Service;

import com.example.AuthenticationService.InterFace.IAuthenticationService;
import com.example.AuthenticationService.InterFace.UserClient;
import com.example.AuthenticationService.Exception.AuthException;
import com.example.AuthenticationService.Exception.NotFoundException;
import com.example.AuthenticationService.Model.Request.*;
import com.example.AuthenticationService.Model.Response.AccountResponse;
import com.example.AuthenticationService.Service.redis.RedisTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.AuthenticationService.Enity.ForgotPasswordEvent;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final TokenService tokenService;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UserServiceCaller userServiceCaller;
    private final UserClient userClient;
    private final RedisTokenService redisTokenService;
    private final String TOPIC = "forgot-password-events";



    public AccountResponse register(RegisterRequest registerRequest) {
        return userClient.register(registerRequest);
    }

    public AccountResponse login(LoginRequest loginRequest) {
        try {
            AccountResponse userAuthInfo = userServiceCaller.login(loginRequest);

            if (userAuthInfo == null) {
                throw new AuthException("Invalid username or password.");
            }
            AccountResponse accountResponse = new AccountResponse();
            accountResponse.setEmail(userAuthInfo.getEmail());
            accountResponse.setRole(userAuthInfo.getRole());
            accountResponse.setUuid(userAuthInfo.getUuid());
            accountResponse.setAddress(userAuthInfo.getAddress());
            accountResponse.setImage(userAuthInfo.getImage());
            accountResponse.setPhone(userAuthInfo.getPhone());
            accountResponse.setFullName(userAuthInfo.getFullName());
            accountResponse.setUserName(userAuthInfo.getUserName());

            String jwtToken = tokenService.generateToken(accountResponse);
            accountResponse.setToken(jwtToken);

            // redis
//            String key = "auth:" + accountResponse.getUuid();
//            redisTokenService.saveToken(key, jwtToken, 10);

            return accountResponse;

        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthException("An error occurred during login: " + e.getMessage());
        }
    }

    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        AccountResponse account = userServiceCaller.getAccountByEmail(forgotPasswordRequest.getEmail());
        if (account == null) {
            throw new NotFoundException("Email not found!");
        } else {
            try {

                String jwtToken = tokenService.generateToken(account);
                System.out.println("Generated JWT token: " + jwtToken);
                ForgotPasswordEvent event = new ForgotPasswordEvent();
                event.setEmail(forgotPasswordRequest.getEmail());
                event.setToken(jwtToken);

                String eventJson = objectMapper.writeValueAsString(event);
                kafkaTemplate.send(TOPIC, eventJson);

            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize forgot password event", e);
            }
        }
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest){
        userServiceCaller.resetPassword(resetPasswordRequest);
    }

    public String changePassword(ChangePasswordRequest changePasswordRequest) {
        return userServiceCaller.changePassword(changePasswordRequest);
    }


    public String logout(String token) {
        Claims claims = tokenService.extractAllClaims(token);
        Date exp = claims.getExpiration();
        Date now = new Date(System.currentTimeMillis());
        redisTokenService.blacklistToken(token, exp.getTime() - now.getTime() / 1000);
        return "Logout successfully!";
    }
}
