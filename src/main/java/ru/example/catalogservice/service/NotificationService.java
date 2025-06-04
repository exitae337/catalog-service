package ru.example.catalogservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNewProduct(String payload) {
        messagingTemplate.convertAndSend("/notifications/new-product", payload);
    }
}
