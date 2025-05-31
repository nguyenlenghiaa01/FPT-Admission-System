package com.example.Nofication.Repository;

import com.example.Nofication.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,String> {

    Notification findNotificationByUuid(String uuid);
}
