package com.example.Nofication.Service;

import com.example.Nofication.Entity.Notification;
import com.example.Nofication.Enum.StatusEnum;
import com.example.Nofication.Enum.TypeEnum;
import com.example.Nofication.Model.EmailDetail;
import com.example.Nofication.Model.Request.NotificationRequest;
import com.example.Nofication.Repository.NotificationRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;
import java.time.LocalDateTime;



@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;
    @Autowired
    private TokenUtil tokenUtil;

    public void createNotificationForgotPassword(NotificationRequest notificationRequest) {
        try {
            // Tạo UUID duy nhất
            String notificationUuid;
            do {
                notificationUuid = UUID.randomUUID().toString();
            } while (notificationRepository.findNotificationByUuid(notificationUuid) != null);

            // Tạo notification mới
            Notification newNotification = new Notification();
            newNotification.setUuid(notificationUuid);
            newNotification.setEmail(notificationRequest.getEmail());
            newNotification.setType(TypeEnum.FORGOT_PASSWORD);
            newNotification.setSendAt(LocalDateTime.now());
            newNotification.setDescription("none");

            EmailDetail emailDetail = new EmailDetail();
            emailDetail.setReceiver(notificationRequest.getEmail());
            emailDetail.setSubject("Reset Password");
            String token = notificationRequest.getToken();
            tokenUtil.parseToken(token);

            emailDetail.setLink("https://localhost:8082/reset-password?token=" + token);

            // Gửi email
            try {
                emailService.sendEmail(emailDetail);
                newNotification.setStatus(StatusEnum.SEND);
            } catch (Exception e) {
                newNotification.setStatus(StatusEnum.FAIL);
            }

            notificationRepository.save(newNotification);

        } catch (Exception e) {
            throw new RuntimeException("Tạo thông báo quên mật khẩu thất bại", e);
        }
    }

}
