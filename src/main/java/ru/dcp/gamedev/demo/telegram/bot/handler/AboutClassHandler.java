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
import ru.dcp.gamedev.demo.util.ParsingUtil;

import java.util.List;
import java.util.Optional;


/**
 * Показывает информацию основную информацию о классе
 * <p>
 * Доступ: любой пользователь
 */

@Component
@Slf4j
@BotCommand(command = {"/ABOUT"})
public class AboutClassHandler extends AbstractEditHandler {

    private ClassService classService;

    public AboutClassHandler(NotificationService notificationService, ClassService classService) {
        super(notificationService);
        this.classService = classService;
    }


    @Override
    protected List<EditMessageText> handle(User user, Long chatId, String message, int id) {
        log.debug("Подготовка /ABOUT");

        final String[] arguments = ParsingUtil.extractArguments(message).split(" ");

        Optional<Classes> classEntityOptional  = classService.getById(Integer.parseInt(arguments[0]));

        //пытаемя расписать

        if (classEntityOptional.isPresent()){
            Classes classEntity = classEntityOptional.get();
            return List.of(EditMessageBuilder.create(String.valueOf(chatId), id)
                    .line(String.format("*%s*", classEntity.getName().toUpperCase()))
                    .line()
                    .line(classEntity.getDescription())
                    .line()
                    .line("*Базовые характеристики*")
                    .line("интелект: " + classEntity.getBase_int())
                    .line("ловкость: " + classEntity.getBase_agi())
                    .line("сила: " + classEntity.getBase_str())
                    .row()
                    .button("Назад", "/NEW_GAME")
                    .CloseButton()
                    .build());
        } else {
            return null;
        }

    }
}

