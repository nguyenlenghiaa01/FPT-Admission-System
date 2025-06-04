package com.example.Nofication.Service;

import com.example.Nofication.Entity.Notification;
import com.example.Nofication.Enum.StatusEnum;
import com.example.Nofication.Enum.TypeEnum;
import com.example.Nofication.Model.EmailDetail;
import com.example.Nofication.Model.Request.NotificationRequest;
import com.example.Nofication.Repository.NotificationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    }

    public void createNotificationSubmitApplication(NotificationRequest request) {
    }
}
