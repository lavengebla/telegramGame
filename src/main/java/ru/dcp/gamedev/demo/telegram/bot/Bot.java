package ru.dcp.gamedev.demo.telegram.bot;

import com.google.common.io.ByteSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVenue;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dcp.gamedev.demo.configuration.configLoader;
import ru.dcp.gamedev.demo.models.model.DocumentNotification;
import ru.dcp.gamedev.demo.models.model.Notification;
import ru.dcp.gamedev.demo.models.model.Role;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.service.NotificationService;
import ru.dcp.gamedev.demo.service.UserService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@EnableAsync
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {

    //инициализация конфига
    final static configLoader cfg = ConfigFactory.create(configLoader.class,
            System.getProperties(),
            System.getenv());

    private final UpdateReceiver updateReceiver;

    private final UserService userService;
    private final NotificationService notificationService;

    @Override
    public String getBotUsername() {
       return cfg.botName();
    }

    @Override
     public String getBotToken() {
        return cfg.botToken();
    }

    @PostConstruct
    public void startBot() {
        createOrUpdateSuperUser();
        sendStartReport();
    }

    @PreDestroy
    public void stopBot() {
        sendStopReport();
    }

    @Override
    public void onUpdateReceived(Update update) {
        List<BotApiMethod<Message>> messagesToSend = updateReceiver.handle(update);

        //обработка обычных сообщений
        if (messagesToSend != null && !messagesToSend.isEmpty()) {
            messagesToSend.forEach(response -> {
                if (response instanceof SendMessage) {
                    executeWithExceptionCheck((SendMessage) response);
                }
            });
        }

        List<SendDocument> documentsToSend = updateReceiver.handleDocument(update);
        //обработка сообщений документов
        if (documentsToSend != null && !documentsToSend.isEmpty()) {
            documentsToSend.forEach(response -> {
                if (response instanceof SendDocument) {
                    executeWithExceptionCheck(response);
                }
            });
        }


        List<SendVenue> venuesToSend = updateReceiver.handleVenue(update);
        //обработка сообщений документов
        if (venuesToSend != null && !venuesToSend.isEmpty()) {
            venuesToSend.forEach(response -> {
                if (response instanceof SendVenue) {
                    executeWithExceptionCheck(response);
                }
            });
        }

        //обработка сообщений, которые редактируют существующие
        List<EditMessageText> messagesToUpdate = updateReceiver.handleEdit(update);

        if (messagesToUpdate != null && !messagesToUpdate.isEmpty()) {
            messagesToUpdate.forEach(response -> {
                if (response instanceof EditMessageText) {
                    executeWithExceptionCheck((EditMessageText) response);
                }
            });
        }

        //обработка удаляемых сообщений
        List<DeleteMessage> messagesToDelete = updateReceiver.handleDelete(update);

        if (messagesToDelete != null && !messagesToDelete.isEmpty()){
            messagesToDelete.forEach(response -> {
                if (response instanceof DeleteMessage) {
                    executeWithExceptionCheck((DeleteMessage) response);
                }
            });
        }

    }

    private void executeWithExceptionCheck(BotApiMethod<Document> sendDocument) {
        try {
            execute(sendDocument);
            log.debug("Выполнено {}", sendDocument);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения {} пользователю: {}", sendDocument, e.getMessage());
        }
    }

    private void executeWithExceptionCheck(SendMessage sendMessage) {
        try {
            execute(sendMessage);
            log.debug("Выполнено {}", sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения {} пользователю: {}", sendMessage, e.getMessage());
        }
    }

    private void executeWithExceptionCheck(SendDocument sendDocument) {
        try {
            execute(sendDocument);
            log.debug("Выполнено {}", sendDocument);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения {} пользователю: {}", sendDocument, e.getMessage());
        }
    }

    private void executeWithExceptionCheck(SendVenue sendVenue) {
        try {
            execute(sendVenue);
            log.debug("Выполнено {}", sendVenue);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения {} пользователю: {}", sendVenue, e.getMessage());
        }
    }

    private void executeWithExceptionCheck(DeleteMessage deleteMessage) {
        try {
            execute(deleteMessage);
            log.debug("Выполнено {}", deleteMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения {} пользователю: {}", deleteMessage, e.getMessage());
        }
    }

    private void executeWithExceptionCheck(EditMessageText editMessageText) {
        try {
            execute(editMessageText);
            log.debug("Выполнено {}", editMessageText);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения {} пользователю: {}", editMessageText, e.getMessage());
        }
    }



    @Scheduled(fixedDelay = 5000)
    private void sendNotifications() {
        Iterable<Notification> notify = notificationService.getAllQueued();

        Iterator<Notification> it = notify.iterator();
        while (it.hasNext()) {
            log.debug("есть сообщения для отправки");
            Notification entity = it.next();
            log.debug("подготавливаю сообщение для отправки: [{}]:[{}]", entity.getType(), entity.getMessage());
            SendMessage notificationMsg = new SendMessage();
                notificationMsg.setText(String.format("*[%s]* %s", entity.getType(), entity.getMessage()));
                notificationMsg.enableMarkdown(true);
                notificationMsg.setChatId(String.valueOf(entity.getChatId()));
                notificationMsg.setReplyMarkup(setCloseButton());
            executeWithExceptionCheck(notificationMsg);
            entity.setState("SENDED");
            notificationService.changeState(entity);
        }
    }

    @Scheduled(fixedDelay = 5000)
    private void sendDocNotifications() throws IOException {
        Iterable<DocumentNotification> notify = notificationService.getAllQueuedDocuments();

        Iterator<DocumentNotification> it = notify.iterator();

        while (it.hasNext()){
            log.debug("есть документы для отправки");
            DocumentNotification entity = it.next();
            log.debug("подготавливаю для отправки: [{}]:[{}]", entity.getType(), entity.getMessage());

            InputStream targetStream = ByteSource.wrap(entity.getData()).openStream();

            InputFile inputFile = new InputFile();
            inputFile.setMedia(targetStream, entity.getFilename());

            SendDocument notificationMsg = new SendDocument();
                notificationMsg.setCaption(String.format("<b>[%s]</b> %s", entity.getType(), entity.getMessage()));
                notificationMsg.setChatId(String.valueOf(entity.getChatId()));
                notificationMsg.setDocument(inputFile);
                notificationMsg.setParseMode("html");
                notificationMsg.setReplyMarkup(setCloseButton());
            executeWithExceptionCheck(notificationMsg);
            entity.setState("SENDED");
            notificationService.changeStateDoc(entity);
        }

    }

    /**
     * Сообщение пользователю администратору, что бот успешно запущен
     */
    private void sendStartReport() {
        SendMessage startUpMsg = new SendMessage();
            startUpMsg.setText("Бот запущен");
            startUpMsg.setChatId(cfg.adminChat());
            startUpMsg.setReplyMarkup(setCloseButton());
        executeWithExceptionCheck(startUpMsg);
        log.debug("Оповоещение о старте направлено администратору");
    }

    /**
     * Сообщение пользователю администратору, что бот остановлен
     */
    private void sendStopReport() {
        SendMessage stopMsg = new SendMessage();
        stopMsg.setText("<b>ALARM!!!</b> Боту стало плохо, он отключается");
        stopMsg.setChatId(cfg.adminChat());
        stopMsg.setParseMode("html");
        stopMsg.setReplyMarkup(setCloseButton());
        executeWithExceptionCheck(stopMsg);
        log.debug("Оповоещение о старте направлено администратору");
    }

    private void createOrUpdateSuperUser() {
        //todo не статичный сеттинг
        User superuser = userService.getOrCreate(cfg.superUserId(), cfg.superUserName(), cfg.superUserUsername(), cfg.superUserSurname());
        String roleValue = "АДМИНИСТРАТОР";
        final Role roleToUpdate = Role.valueOf(roleValue);
        Set<Role> userRoles = superuser.getRoles();
        if (!superuser.getRoles().contains(roleToUpdate)) {
            userRoles.add(roleToUpdate);
        }
        superuser.setRoles(userRoles);
        userService.update(superuser);
    }


    private InlineKeyboardMarkup setCloseButton(){

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Закрыть");
        inlineKeyboardButton.setCallbackData("/DELETE");
        row.add(inlineKeyboardButton);

        keyboard.add(row);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }



}
