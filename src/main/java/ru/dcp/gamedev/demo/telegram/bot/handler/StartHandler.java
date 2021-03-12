package ru.dcp.gamedev.demo.telegram.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.service.ClassService;
import ru.dcp.gamedev.demo.service.NotificationService;
import ru.dcp.gamedev.demo.telegram.annotations.BotCommand;
import ru.dcp.gamedev.demo.telegram.builder.EditMessageBuilder;
import ru.dcp.gamedev.demo.telegram.builder.MessageBuilder;

import java.util.List;


/**
 * Главное меню
 * <p>
 * Доступ: любой пользователь
 */

@Component
@Slf4j
@BotCommand(command = {"/START"}, message = "Начать игру")
public class StartHandler extends AbstractBaseHandler {


    @Override
    protected List<BotApiMethod<Message>> handle(User user, Long chatId, String message) {

        return List.of(
                MessageBuilder.create(chatId.toString())
                    .line("*Приветсвую путник*")
                    .row()
                        .button("Новое приключение", "/NEW_GAME")
                    .row()
                        .button("Продолжить", "/CONTINUE")
                    .row()
                        .button("Об игре", "/ИГРА")
                    .row()
                        .CloseButton()
                    .build());

    }
}

