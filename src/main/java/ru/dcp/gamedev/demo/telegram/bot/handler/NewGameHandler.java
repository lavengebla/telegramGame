package ru.dcp.gamedev.demo.telegram.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.dcp.gamedev.demo.models.model.Classes;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.service.ClassService;
import ru.dcp.gamedev.demo.service.NotificationService;
import ru.dcp.gamedev.demo.telegram.annotations.BotCommand;
import ru.dcp.gamedev.demo.telegram.builder.EditMessageBuilder;

import java.util.List;


/**
 * Начало новой игры
 * <p>
 * Доступ: любой пользователь
 */

@Component
@Slf4j
@BotCommand(command = {"/NEW_GAME"})
public class NewGameHandler extends AbstractEditHandler {

    private ClassService classService;

    public NewGameHandler(NotificationService notificationService, ClassService classService) {
        super(notificationService);
        this.classService = classService;
    }

    @Override
    protected List<EditMessageText> handle(User user, Long chatId, String message, int id) {
        log.debug("Подготовка /NEW_GAME");

        EditMessageBuilder editMessageBuilder = EditMessageBuilder.create(String.valueOf(chatId), id);
        //пытаемя расписать
        editMessageBuilder
                .line("*Выберите базовый класс*:");

        for (Classes classEntity:
             classService.getAllClasses()) {

            editMessageBuilder
                    .row()
                        .button(classEntity.getName(), String.format("/PICK %s", classEntity.getId()))
                        .button("?", String.format("/ABOUT %s", classEntity.getId()));
        }

        editMessageBuilder
                .row()
                .button("Назад", "/START_EDIT")
                .CloseButton();

        return List.of(editMessageBuilder.build());
    }
}

