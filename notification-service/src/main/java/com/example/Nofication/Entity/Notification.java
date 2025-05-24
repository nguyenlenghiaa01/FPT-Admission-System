package com.example.Nofication.Entity;

import com.example.Nofication.Enum.StatusEnum;
import com.example.Nofication.Enum.TypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Notification {

    @Id
    @Column(name="notification_uuid")
    private String uuid;

    @Email(message = "Invalid Email!")
    private String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone!")
    private String phone;

    private String description;

    private TypeEnum type;

    private StatusEnum status;

    @Column(name = "send_At")
    private LocalDateTime sendAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    public Notification() {
        this.createdAt = LocalDateTime.now();
    }
}
