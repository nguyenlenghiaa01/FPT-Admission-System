package com.example.Nofication.InterFace;

import com.example.Nofication.Model.Request.NotificationRequest;
import com.example.Nofication.Model.Request.SubmissionApplicationNotificationRequest;

public interface INotificationService {
    void createNotificationForgotPassword(NotificationRequest notificationRequest);
    void createNotificationSubmitApplication(SubmissionApplicationNotificationRequest request);
}
