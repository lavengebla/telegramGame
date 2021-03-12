package ru.dcp.gamedev.demo.telegram.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.dcp.gamedev.demo.configuration.configLoader;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.telegram.annotations.BotCommand;
import ru.dcp.gamedev.demo.telegram.annotations.RequiredRoles;
import ru.dcp.gamedev.demo.telegram.builder.MessageBuilder;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.dcp.gamedev.demo.models.model.Role.АДМИНИСТРАТОР;


/**
 * Показывает время на сервере, где развернут бот
 * <p>
 * Available to: Admin
 */

@Component
@Slf4j
@BotCommand(command = "/ВРЕМЯ", message = "Показать серверное время")
public class AdminTimeHandler extends AbstractBaseHandler {
    @Value("+3")
    private String serverHourOffset;

    @Override
    @RequiredRoles(roles = АДМИНИСТРАТОР)
    public List<BotApiMethod<Message>> handle(User admin, Long chatId, String message) {
        log.debug("Подготовка /ВРЕМЯ");

        //инициализация конфига
        final configLoader cfg = ConfigFactory.create(configLoader.class,
                System.getProperties(),
                System.getenv());

        return List.of(MessageBuilder.create(cfg.adminChat())
                .line("*Текущее время на сервере*:")
                .line(LocalDateTime.now().toString())
                .line("Часовой пояс %s", serverHourOffset)
                .row()
                .CloseButton()
                .build());
    }
}

