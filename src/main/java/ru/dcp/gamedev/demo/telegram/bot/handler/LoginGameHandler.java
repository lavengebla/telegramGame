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
import ru.dcp.gamedev.demo.util.ParsingUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Продолжение игры
 * <p>
 * Доступ: любой пользователь
 */

@Component
@Slf4j
@BotCommand(command = {"/LOGIN"})
public class LoginGameHandler extends AbstractEditHandler {

    //инициализация конфига
    final static configLoader cfg = ConfigFactory.create(configLoader.class,
            System.getProperties(),
            System.getenv());

    private CharacterService characterService;

    public LoginGameHandler(NotificationService notificationService, CharacterService characterService) {
        super(notificationService);
        this.characterService = characterService;
    }

    @Override
    protected List<EditMessageText> handle(User user, Long chatId, String message, int id) {
        log.debug("Подготовка /LOGIN");

        final String[] arguments = ParsingUtil.extractArguments(message).split(" ");

        Optional<Characters> CharacterOptional = characterService.getById(Integer.parseInt(arguments[0]));

        EditMessageBuilder editMessageBuilder = EditMessageBuilder.create(String.valueOf(chatId), id);

        if (CharacterOptional.isPresent()) {

            Characters Character = CharacterOptional.get();
            //пытаемя расписать
            editMessageBuilder
                    .line(String.format("*%s*, ждет ваших указаний", Character.getName()))
                    .row()
                    .button("Поиск подземелий", String.format("/LFD %d", Character.getId()))
                    .row()
                    .button("Инвентарь", String.format("/INVENTORY S %d", Character.getId()))
                    .row()
                    .button("Статистика", String.format("/STATS %d", Character.getId()));
        } else {
            editMessageBuilder
                    .line("Персонаж куда-то пропал");
        }

        editMessageBuilder
                .row()
                .button("В меню", "/START_EDIT")
                .CloseButton();

        return List.of(editMessageBuilder.build());
    }

}

