package ru.dcp.gamedev.demo.telegram.bot.handler;


import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import ru.dcp.gamedev.demo.configuration.configLoader;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.service.NotificationService;
import ru.dcp.gamedev.demo.telegram.annotations.BotCommand;
import ru.dcp.gamedev.demo.telegram.annotations.RequiredRoles;
import ru.dcp.gamedev.demo.telegram.security.AuthorizationService;

import java.util.List;

/**
 * Абстрактный класс для всех обработчиков
 * <p>
 * Наследники промаркированы {@link BotCommand} аннотацией, что комманда поддерживается.
 * <p>
 * Обращение к {@link #handle(User, Long,String)} может быть защищена антотацией
 * {@link RequiredRoles}
 */
@Slf4j
public abstract class AbstractDocumentHandler {

    //инициализация конфига
    final static configLoader cfg = ConfigFactory.create(configLoader.class,
            System.getProperties(),
            System.getenv());

    protected String botAdmin = cfg.adminChat();

    @Autowired
    protected AuthorizationService authorizationService;

    private final NotificationService notificationService;

    protected AbstractDocumentHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Performs authorization of user and handling of the command
     *
     * @param user    {@link} User that sent update to the bot
     * @param message {@link} content of the update
     * @return List of {@link BotApiMethod < Message >} that bot will send
     */
    public final List<SendDocument> authorizeAndHandle(User user, Long chatId, String message) {
        /**
         *someval = (min >= 2) ? 2 : 1;
         *This is called ternary operator, which can be used as if-else. this is equivalent to
         *if((min >= 2) {
         *    someval =2;
         *} else {
         *    someval =1
         *}
         */
        return this.authorizationService.authorize(this.getClass(), user) ?
                handle(user,chatId ,message) : handleUnauthorized(user,chatId,message);
    }
    /**
     * Обрабатываем команду, если пользователь авторизован
     * @return
     */
    protected abstract List<SendDocument> handle(User user, Long chatId , String message);

    /**
     * Обрабатываем команду, если пользователь не авторизован
     */
    private List<SendDocument> handleUnauthorized(User user, Long chatId ,String message) {
        log.info("Несанкционированный доступ: {} {} {}", user, message, chatId);
        String userChatId = String.valueOf(user.getUserId());
        String userMessage = String.format("Ваш токен %s %nСвяжитесь с администратором, чтобы получить доступ к: [%s]", userChatId, message.replaceAll("_", "-"));
        String adminMessage = String.format("*Несанкционированный доступ* %s, чат: %s)%n", userChatId, chatId)
                + String.format("*Сообщение:* %s", message.replaceAll("_", "-"));

        notificationService.createNotification("security", userMessage, Long.valueOf(userChatId));
        notificationService.createNotification("security", adminMessage, Long.valueOf(botAdmin));

        return List.of();
    }

}
