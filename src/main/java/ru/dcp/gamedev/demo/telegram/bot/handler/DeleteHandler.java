package ru.dcp.gamedev.demo.telegram.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.dcp.gamedev.demo.models.model.Role;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.service.NotificationService;
import ru.dcp.gamedev.demo.telegram.annotations.BotCommand;
import ru.dcp.gamedev.demo.telegram.annotations.RequiredRoles;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.dcp.gamedev.demo.util.ParsingUtil.extractArguments;


/**
 * Удаляет сообщение
 * <p>
 * Available to: АДМИНИСТРАТОР, МОДЕРАТОР
 */

@Component
@Slf4j
@BotCommand(command = "/DELETE")
public class DeleteHandler extends AbstractDeleteHandler {

    public DeleteHandler(NotificationService notificationService) {
        super(notificationService);
    }

    @Override
    @RequiredRoles(roles = {Role.АДМИНИСТРАТОР, Role.МОДЕРАТОР})
    public List<DeleteMessage> handle(User admin, Long chatId, String message, int id) {
        log.debug("Подготовка /DELETE");
        final String[] arguments = extractArguments(message).split(" ");

        List<DeleteMessage> deleteMessages = new ArrayList<>();

        //удаляем изначальное
        DeleteMessage initialDelete = new DeleteMessage();
            initialDelete.setChatId(String.valueOf(chatId));
            initialDelete.setMessageId(id);

        deleteMessages.add(initialDelete);

        for (String arg: arguments) {
            if (arg.equals("/DELETE")) {
                //
            } else {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setMessageId(Integer.parseInt(arg));
                deleteMessage.setChatId(String.valueOf(chatId));
            }
        }
        return deleteMessages;
    }
}

