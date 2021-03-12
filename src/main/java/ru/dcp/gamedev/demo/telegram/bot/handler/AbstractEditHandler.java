package ru.dcp.gamedev.demo.telegram.bot.handler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.dcp.gamedev.demo.configuration.configLoader;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.service.NotificationService;
import ru.dcp.gamedev.demo.telegram.annotations.RequiredRoles;
import ru.dcp.gamedev.demo.telegram.security.AuthorizationService;

import java.util.List;

/**
 * Абстрактный класс для delete обработчиков
 * <p>
 * Наследники промаркированы {@link BotCommand} аннотацией, что комманда поддерживается.
 * <p>
 * Обращение к {@link #handle(User, Long,String, int)} может быть защищена антотацией
 * {@link RequiredRoles}
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEditHandler {

    //инициализация конфига
    final static configLoader cfg = ConfigFactory.create(configLoader.class,
            System.getProperties(),
            System.getenv());

    protected String botAdmin = cfg.adminChat();

    @Autowired
    protected AuthorizationService authorizationService;

    private final NotificationService notificationService;


    /**
     * Performs authorization of user and handling of the command
     *
     * @param user    {@link} User that sent update to the bot
     * @param message {@link} content of the update
     * @return List of {@link BotApiMethod < Message >} that bot will send
     */
    public final List<EditMessageText> authorizeAndHandle(User user, Long chatId, String message, int id) {

        return (this.authorizationService.authorize(this.getClass(), user) || chatId.intValue() == user.getUserId()) ?
                handle(user,chatId ,message, id) : handleUnauthorized(user,chatId,message);

    }
    /**
     * Обрабатываем команду, если пользователь авторизован
     */
    protected abstract List<EditMessageText> handle(User user, Long chatId ,String message, int id);

    /**
     * Обрабатываем команду, если пользователь не авторизован
     */
    private List<EditMessageText> handleUnauthorized(User user, Long chatId ,String message) {
        log.info("Несанкционированный доступ: {} {} {}", user, message, chatId);
        String userChatId = String.valueOf(user.getUserId());
        String userMessage = String.format("Ваш токен %s %nСвяжитесь с администратором, чтобы получить доступ к: [%s]", userChatId, message.replaceAll("_", "-"));
        String adminMessage = String.format("*Несанкционированный доступ* {пользователь: %s, чат: %s)%n", userChatId, chatId)
        + String.format("*Сообщение:* %s", message.replaceAll("_", "-"));

        notificationService.createNotification("security", userMessage, Long.valueOf(userChatId));
        notificationService.createNotification("security", adminMessage, Long.valueOf(botAdmin));

        return List.of();
    }

}
