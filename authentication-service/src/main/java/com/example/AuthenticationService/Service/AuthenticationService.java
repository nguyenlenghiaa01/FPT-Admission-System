package com.example.AuthenticationService.Service;

import com.example.AuthenticationService.Controller.UserClient;
import com.example.AuthenticationService.Exception.AuthException;
import com.example.AuthenticationService.Exception.NotFoundException;
import com.example.AuthenticationService.Model.Request.*;
import com.example.AuthenticationService.Model.Response.AccountResponse;
import com.example.AuthenticationService.Service.redis.RedisTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.UserService.Entity.ForgotPasswordEvent;
import io.jsonwebtoken.Claims;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import com.example.AuthenticationService.Controller.UserServiceCaller;

import java.util.Date;

@Service
public class AuthenticationService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired

    private UserServiceCaller userServiceCaller;

    @Autowired
    private UserClient userClient;

    @Autowired
    private RedisTokenService redisTokenService;


    public AccountResponse register(RegisterRequest registerRequest) {
        return userClient.register(registerRequest);
    }

    @Autowired
    AuthenticationManager authenticationManager;

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

    private final String TOPIC = "forgot-password-events";



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
