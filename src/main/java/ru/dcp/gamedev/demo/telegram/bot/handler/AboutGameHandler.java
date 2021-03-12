package ru.dcp.gamedev.demo.telegram.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.dcp.gamedev.demo.configuration.configLoader;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.service.NotificationService;
import ru.dcp.gamedev.demo.telegram.annotations.BotCommand;
import ru.dcp.gamedev.demo.telegram.builder.EditMessageBuilder;

import java.util.List;


/**
 * Показывает информацию основную информацию о игре
 * <p>
 * Доступ: любой пользователь
 */

@Component
@Slf4j
@BotCommand(command = {"/ИГРА"})
public class AboutGameHandler extends AbstractEditHandler {


    public AboutGameHandler(NotificationService notificationService) {
        super(notificationService);
    }

    @Override
    protected List<EditMessageText> handle(User user, Long chatId, String message, int id) {
        log.debug("Подготовка /ИГРА");


        //пытаемя расписать
        return List.of(EditMessageBuilder.create(String.valueOf(chatId), id)
                .line("*Наша игра*:")
                .line("Это божетсвенно (q) Атикин")
                .row()
                    .button("Назад", "/START_EDIT")
                    .CloseButton()
                .build());
    }
}

