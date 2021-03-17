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
import java.util.Random;
import java.util.UUID;


/**
 * Начало новой игры (подтверждение создания персонажа)
 * <p>
 * Доступ: любой пользователь
 */

@Component
@Slf4j
@BotCommand(command = {"/ACCEPT"})
public class AcceptGameHandler extends AbstractEditHandler {

    //инициализация конфига
    final static configLoader cfg = ConfigFactory.create(configLoader.class,
            System.getProperties(),
            System.getenv());

    private CharacterService characterService;

    public AcceptGameHandler(NotificationService notificationService, CharacterService characterService) {
        super(notificationService);
        this.characterService = characterService;
    }

    @Override
    protected List<EditMessageText> handle(User user, Long chatId, String message, int id) {
        log.debug("Подготовка /ACCEPT");

        final String[] arguments = ParsingUtil.extractArguments(message).split(" ");

        Characters freshCharacter = characterService.create(user,
                UUID.randomUUID().toString(),
                Integer.parseInt(arguments[0]),
                Integer.parseInt(arguments[1]),
                Integer.parseInt(arguments[2]));

        EditMessageBuilder editMessageBuilder = EditMessageBuilder.create(String.valueOf(chatId), id);
        //пытаемя расписать
        editMessageBuilder
                .line(String.format("*%s*, ждет ваших указаний", freshCharacter.getName()))
                .line()
                .line(String.format("*Используйте команду* /rename %d <НОВОЕ ИМЯ ПЕРСОНАЖА> *чтобы задать новое имя*", freshCharacter.getId()))
                .line("*Пример:*")
                .line(String.format("/rename %d Алёшка", freshCharacter.getId()))
                .row()
                    .button("Поиск подземелий", String.format("/LFD %d", freshCharacter.getId()))
                .row()
                    .button("Инвентарь", String.format("/INVENTORY S %d", freshCharacter.getId()))
                .row()
                    .button("Статистика", String.format("/STATS %d", freshCharacter.getId()))
                .row()
                .button("В меню", "/START_EDIT")
                .CloseButton();

        return List.of(editMessageBuilder.build());
    }

}

