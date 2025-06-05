package com.example.Nofication.Service;

import com.example.Nofication.Entity.Notification;
import com.example.Nofication.Enum.StatusEnum;
import com.example.Nofication.Enum.TypeEnum;
import com.example.Nofication.Model.EmailDetail;
import com.example.Nofication.Model.Request.NotificationRequest;
import com.example.Nofication.Model.Request.SubmissionApplicationNotificationRequest;
import com.example.Nofication.Repository.NotificationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

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
            // Add commentMore actions
            EmailDetail emailDetail = new EmailDetail();
            emailDetail.setReceiver(notificationRequest.getEmail());
            emailDetail.setSubject("Reset Password");
            String token = notificationRequest.getToken();
            tokenUtil.parseToken(token);

            emailDetail.setLink("http://localhost:5173/reset-password?token=" + token);

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

    public void createNotificationSubmitApplication(SubmissionApplicationNotificationRequest request) {
        try {
            System.out.println("Bắt đầu gửi mail cho user!");
            EmailDetail emailDetail = modelMapper.map(request, EmailDetail.class);
            emailDetail.setReceiver(request.getEmail());
            String submitApplication_subject = "[FPT-University] Đăng kí tư vấn tuyển sinh thành công";
            emailDetail.setSubject(submitApplication_subject + " " + LocalDateTime.now());
            System.out.println("User Info: " + emailDetail.toString());

            // create log noti
            Notification newNotification = new Notification();
            newNotification.setEmail(request.getEmail());
            newNotification.setType(TypeEnum.REGISTER_ADMISSION);
            newNotification.setSendAt(LocalDateTime.now());
            newNotification.setDescription("none");

            try {
                emailService.sendEmailNotiSubmitApplication(emailDetail);
                newNotification.setStatus(StatusEnum.SEND);
                System.out.println("Đã gửi mail thành công cho user!");
            } catch (Exception e) {
                newNotification.setStatus(StatusEnum.FAIL);
                System.out.println("Lỗi: " + e.getMessage());
            }

            notificationRepository.save(newNotification);

        } catch (Exception e) {
            throw new RuntimeException("Tạo thông báo nộp đơn tư vấn thất bại: " + e);
        }
    }
}
