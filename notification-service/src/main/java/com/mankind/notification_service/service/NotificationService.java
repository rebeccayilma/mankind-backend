package com.mankind.notification_service.service;

import com.mankind.notification_service.model.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;
    private final SmsNotificationService smsNotificationService;

    public void sendNotification(NotificationRequest request) {
        if ("EMAIL".equalsIgnoreCase(request.getType())) {
            sendEmail(request);
        } else if ("SMS".equalsIgnoreCase(request.getType())) {
            // For SMS notifications, we expect the userEmail field to contain a phone number
            // This is a temporary solution; in a real-world scenario, you might want to add a dedicated phone number field
            smsNotificationService.sendSms(request.getUserEmail(), request.getMessage());
            log.info("SMS notification sent to {}", request.getUserEmail());
        } else {
            log.warn("Notification type {} not supported yet", request.getType());
        }
    }

    private void sendEmail(NotificationRequest request) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getUserEmail());
            message.setSubject(request.getSubject());
            message.setText(request.getMessage());

            mailSender.send(message);
            log.info("Email notification sent to {}", request.getUserEmail());
        } catch (Exception e) {
            log.error("Failed to send email notification to {}: {}", request.getUserEmail(), e.getMessage());
            throw new RuntimeException("Failed to send email notification", e);
        }
    }
}
