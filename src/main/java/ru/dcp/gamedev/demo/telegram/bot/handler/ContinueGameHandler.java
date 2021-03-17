package ru.dcp.gamedev.demo.telegram.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.dcp.gamedev.demo.configuration.configLoader;
import ru.dcp.gamedev.demo.models.model.Characters;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.service.CharacterService;
import ru.dcp.gamedev.demo.service.NotificationService;
import ru.dcp.gamedev.demo.telegram.annotations.BotCommand;
import ru.dcp.gamedev.demo.telegram.builder.EditMessageBuilder;

import java.util.List;
import java.util.Random;


/**
 * Продолжение игры
 * <p>
 * Доступ: любой пользователь
 */

@Component
@Slf4j
@BotCommand(command = {"/CONTINUE"})
public class ContinueGameHandler extends AbstractEditHandler {

    //инициализация конфига
    final static configLoader cfg = ConfigFactory.create(configLoader.class,
            System.getProperties(),
            System.getenv());

    private CharacterService characterService;

    public ContinueGameHandler(NotificationService notificationService, CharacterService characterService) {
        super(notificationService);
        this.characterService = characterService;
    }

    @Override
    protected List<EditMessageText> handle(User user, Long chatId, String message, int id) {
        log.debug("Подготовка /NEW_GAME");

        EditMessageBuilder editMessageBuilder = EditMessageBuilder.create(String.valueOf(chatId), id);
        //пытаемя расписать
        editMessageBuilder
                .line("*Ваши персонаж*:");

        for (Characters character:
                characterService.getMyCharacters(user)
             ) {
            editMessageBuilder
                    .row()
                        .button(character.getName(), String.format("/LOGIN %s", character.getId()));
        }

        editMessageBuilder
                .row()
                    .button("Назад", "/START_EDIT")
                    .CloseButton();

        return List.of(editMessageBuilder.build());
    }

}

