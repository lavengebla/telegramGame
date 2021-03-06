package ru.dcp.gamedev.demo.telegram.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.dcp.gamedev.demo.configuration.configLoader;
import ru.dcp.gamedev.demo.models.model.Classes;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.service.CharacterService;
import ru.dcp.gamedev.demo.service.NotificationService;
import ru.dcp.gamedev.demo.telegram.annotations.BotCommand;
import ru.dcp.gamedev.demo.telegram.builder.EditMessageBuilder;

import java.util.List;
import java.util.Random;


/**
 * Начало новой игры
 * <p>
 * Доступ: любой пользователь
 */

@Component
@Slf4j
@BotCommand(command = {"/NEW_GAME"})
public class NewGameHandler extends AbstractEditHandler {

    //инициализация конфига
    final static configLoader cfg = ConfigFactory.create(configLoader.class,
            System.getProperties(),
            System.getenv());

    private CharacterService characterService;

    public NewGameHandler(NotificationService notificationService, CharacterService characterService) {
        super(notificationService);
        this.characterService = characterService;
    }

    @Override
    protected List<EditMessageText> handle(User user, Long chatId, String message, int id) {
        log.debug("Подготовка /NEW_GAME");

        int base_int = getStatValue(cfg.getBaseStatMax());
        int base_str = getStatValue(cfg.getBaseStatMax() - base_int);
        int base_agi = cfg.getBaseStatMax() - base_str - base_int;

        EditMessageBuilder editMessageBuilder = EditMessageBuilder.create(String.valueOf(chatId), id);
        //пытаемя расписать
        editMessageBuilder
                .line("*Ваш персонаж*:")
                .line("интелект: " + base_int)
                .line("ловкость: " + base_agi)
                .line("сила: " + base_str)
                .row()
                    .button("Принять", String.format("/ACCEPT %d %d %d", base_int, base_agi, base_str))
                    .button("Перераспределить", "/NEW_GAME")
                .row()
                .button("Назад", "/START_EDIT")
                .CloseButton();

        return List.of(editMessageBuilder.build());
    }

    private int getStatValue(int max) {
        Random rand = new Random();
        return rand.nextInt((max - cfg.getBaseStatMin()) + 1) + cfg.getBaseStatMin();
    }

}

