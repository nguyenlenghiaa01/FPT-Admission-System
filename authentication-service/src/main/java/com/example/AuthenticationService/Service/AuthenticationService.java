package com.example.AuthenticationService.Service;

import com.example.AuthenticationService.Controller.UserClient;
import com.example.AuthenticationService.Exception.AuthException;
import com.example.AuthenticationService.Exception.NotFoundException;
import com.example.AuthenticationService.Model.Request.*;
import com.example.AuthenticationService.Model.Response.AccountResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.UserService.Entity.ForgotPasswordEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private UserClient userClient;

    public AccountResponse register(RegisterRequest registerRequest) {
        return userClient.register(registerRequest);
    }

    @Autowired
    AuthenticationManager authenticationManager;

    public AccountResponse login(LoginRequest loginRequest) {
        try {
            AccountResponse userAuthInfo = userClient.login(loginRequest); // <-- GIẢ SỬ userClient.authenticateUser() trả về UserAuthResponse

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

            return accountResponse;

        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthException("An error occurred during login: " + e.getMessage());
        }
    }

    private final String TOPIC = "forgot-password-events";



    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        AccountResponse account = userClient.getAccountByEmail(forgotPasswordRequest.getEmail());
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
         userClient.resetPassword(resetPasswordRequest);
    }

    public String changePassword(ChangePasswordRequest changePasswordRequest) {
        return userClient.changePassword(changePasswordRequest);
    }

//    public UserResponse loginGoogle(String token){
//        try {
//            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
//            String email = decodedToken.getEmail();
//            Account account = accountRepository.findAccountByEmail(email);
//            if(account == null) {
//                Account newAccount = new Account();
//                newAccount.setUserName(decodedToken.getEmail());
//                newAccount.setEmail(decodedToken.getEmail());
//                newAccount.setFullName(decodedToken.getName());
//                newAccount.setImage(decodedToken.getPicture());
//                newAccount.setRole(Role.CONSULTANT);
//                account=accountRepository.save(newAccount);
//            }
//            UserResponse userResponse = new UserResponse();
//            userResponse.setToken(token);
//            userResponse.setFullName(account.getFullName());
//            userResponse.setImage(account.getImage());
//            userResponse.setRole(account.getRole());
//
//            return userResponse;
//
//        } catch (Exception e) {
//            throw new RuntimeException("Google token verification failed", e);
//        }
//
//    }


}
