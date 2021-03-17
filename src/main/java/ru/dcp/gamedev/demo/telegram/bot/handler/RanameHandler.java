package ru.dcp.gamedev.demo.telegram.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.dcp.gamedev.demo.models.model.Characters;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.service.CharacterService;
import ru.dcp.gamedev.demo.telegram.annotations.BotCommand;
import ru.dcp.gamedev.demo.telegram.builder.MessageBuilder;
import ru.dcp.gamedev.demo.util.ParsingUtil;

import java.util.List;
import java.util.Optional;


/**
 * Главное меню
 * <p>
 * Доступ: любой пользователь
 */

@Component
@Slf4j
@BotCommand(command = {"/RENAME"})
public class RanameHandler extends AbstractBaseHandler {

    private CharacterService characterService;

    public RanameHandler(CharacterService characterService) {
        this.characterService = characterService;
    }

    @Override
    protected List<BotApiMethod<Message>> handle(User user, Long chatId, String message) {
        //todo необходима модерация и защита от изменения чужих чаров

        final String[] arguments = ParsingUtil.extractArguments(message).split(" ");

        int charId = Integer.parseInt(arguments[0]);

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < arguments.length; i++) {
            sb.append(arguments[i]).append(" ");
        }

        Optional<Characters> charactersOptional = characterService.getById(charId);

        if (charactersOptional.isPresent()) {
            Characters characters = charactersOptional.get();
            if (characters.getUser().getUserId() == user.getUserId()) {
                characters.setName(sb.toString());
                characterService.updateCharacter(characters);
            } else {
                log.warn("Какой-то петрушка хотел переименовать не своего персонажа");
            }
        }

        return List.of(
                MessageBuilder.create(chatId.toString())
                    .line("*Запрос отравлен*")
                    .row()
                        .CloseButton()
                    .build());

    }
}

