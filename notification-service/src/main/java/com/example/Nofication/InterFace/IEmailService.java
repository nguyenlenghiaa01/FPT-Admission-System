package com.example.Nofication.InterFace;

import com.example.Nofication.Model.EmailDetail;

public interface IEmailService {
    void sendEmail(EmailDetail emailDetail);
    void sendEmailNotiSubmitApplication(EmailDetail emailDetail);
}
