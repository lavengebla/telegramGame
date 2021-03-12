package ru.dcp.gamedev.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dcp.gamedev.demo.models.model.DocumentNotification;
import ru.dcp.gamedev.demo.models.model.Notification;
import ru.dcp.gamedev.demo.repository.JpaDocumentNotificationRepository;
import ru.dcp.gamedev.demo.repository.JpaNotificationRepository;



@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JpaNotificationRepository notificationRepository;
    private final JpaDocumentNotificationRepository documentNotificationRepository;


    public Iterable<DocumentNotification> getAllQueuedDocuments(){
        return documentNotificationRepository.findAllByState("QUEUED");
    }

    public void changeStateDoc(DocumentNotification notification){
        documentNotificationRepository.save(notification);
    }

    public void createDocNotification (String type, String message, byte[]  data, Long chatId, String filename){
        documentNotificationRepository.save(new DocumentNotification(type, message, data, chatId, filename));
    }

    public Iterable<Notification> getAllQueued(){
        return notificationRepository.findAllByState("QUEUED");
    }

    public void changeState (Notification notification){
        notificationRepository.save(notification);
    }

    public void createNotification (String type, String message, Long chatId){
        notificationRepository.save(new Notification(type, message, chatId));
    }
}
