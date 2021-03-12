package ru.dcp.gamedev.demo.telegram.bot.handler;


import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.dcp.gamedev.demo.configuration.configLoader;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.telegram.builder.MessageBuilder;
import ru.dcp.gamedev.demo.telegram.security.AuthorizationService;

import java.util.ArrayList;
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
public abstract class AbstractBaseHandler {

    //инициализация конфига
    final static configLoader cfg = ConfigFactory.create(configLoader.class,
            System.getProperties(),
            System.getenv());

    protected String botAdmin = cfg.adminChat();

    @Autowired
    protected AuthorizationService authorizationService;

    /**
     * Performs authorization of user and handling of the command
     *
     * @param user    {@link} User that sent update to the bot
     * @param message {@link} content of the update
     * @return List of {@link BotApiMethod < Message >} that bot will send
     */
    public final List<BotApiMethod<Message>> authorizeAndHandle(User user, Long chatId, String message) {
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
     */
    protected abstract List<BotApiMethod<Message>> handle(User user, Long chatId ,String message);

    /**
     * Обрабатываем команду, если пользователь не авторизован
     */
    private List<BotApiMethod<Message>> handleUnauthorized(User user, Long chatId ,String message) {
        log.info("Несанкционированный доступ: {} {} {}", user, message, chatId);
        String userChatId = String.valueOf(user.getUserId());

        return List.of(MessageBuilder.create(userChatId)
                        .line("Ваш токен *%s*", userChatId)
                        .line("Свяжитесь с администратором, чтобы получить доступ к: [%s]", message.replaceAll("_", "-"))
                        .build(),
                MessageBuilder.create(botAdmin)
                        .line("*Несанкционированный доступ* %s, чат: %s", user.toString(), chatId)
                        .line("*Сообщение:* %s", message.replaceAll("_", "-"))
                        .build());
    }

}
