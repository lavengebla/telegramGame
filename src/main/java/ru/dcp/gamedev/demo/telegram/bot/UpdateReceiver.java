package ru.dcp.gamedev.demo.telegram.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendVenue;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.dcp.gamedev.demo.service.UserService;
import ru.dcp.gamedev.demo.telegram.annotations.BotCommand;
import ru.dcp.gamedev.demo.telegram.bot.handler.*;
import ru.dcp.gamedev.demo.util.ParsingUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Основной класс, обрабатывающий входящие апдейты.
 * Выбирает подходящего наследника AbstractBaseHandler для обработки
 */
@Component
@Slf4j
@EnableAsync
@RequiredArgsConstructor
public class UpdateReceiver {
    private final List<AbstractBaseHandler> handlers;
    private final List<AbstractDocumentHandler> DocumentHandlers;
    private final List<AbstractVenueHandler> VenueHandlers;
    private final List<AbstractDeleteHandler> DeleteHandlers;
    private final List<AbstractEditHandler> EditHandlers;
    private final UserService userService;

    /**
     * Анализирует полученный update на наличие исполняемой команды
     *
     * @param update полученный от пользователя
     * @return список SendMessages для выполнения
     */public List<BotApiMethod<Message>> handle(Update update) {
        try {
            int userId = 0;
            String name = "unknown";
            String username = "unknown";
            String surname = "unknown";
            String text = null;
            Long chatId = null;

            if (isMessageWithText(update)) {
                final Message message = update.getMessage();
                userId = message.getFrom().getId();
                name = message.getFrom().getFirstName();
                username = message.getFrom().getUserName();
                surname = message.getFrom().getLastName();
                text = message.getText();
                chatId = message.getChatId();
                log.debug("Update is text message {} from {} chat {}", text, userId, chatId);
            } else if (update.hasCallbackQuery()) {
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                userId = callbackQuery.getFrom().getId();
                name = callbackQuery.getFrom().getFirstName();
                username = callbackQuery.getFrom().getUserName();
                surname = callbackQuery.getFrom().getLastName();
                text = callbackQuery.getData();
                chatId = callbackQuery.getMessage().getChatId();
                log.debug("Update is callbackquery {} from {}", text, userId);
            }
            if (text != null && userId != 0) {
                return getHandler(text).authorizeAndHandle(userService.getOrCreate(userId, name, username, surname), chatId,text);
            }
            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException e) {
            log.debug("Command: {} is unsupported", update.toString());
            return Collections.emptyList();
        }
    }

    public List<SendDocument> handleDocument(Update update) {
        try {
            int userId = 0;
            String name = "unknown";
            String username = "unknown";
            String surname = "unknown";
            String text = null;
            Long chatId = null;

            if (isMessageWithText(update)) {
                final Message message = update.getMessage();
                userId = message.getFrom().getId();
                name = message.getFrom().getFirstName();
                username = message.getFrom().getUserName();
                surname = message.getFrom().getLastName();
                text = message.getText();
                chatId = message.getChatId();
                log.debug("Update is text message {} from {} chat {}", text, userId, chatId);
            } else if (update.hasCallbackQuery()) {
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                userId = callbackQuery.getFrom().getId();
                name = callbackQuery.getFrom().getFirstName();
                username = callbackQuery.getFrom().getUserName();
                surname = callbackQuery.getFrom().getLastName();
                text = callbackQuery.getData();
                chatId = callbackQuery.getMessage().getChatId();
                log.debug("Update is callbackquery {} from {}", text, userId);
            }
            if (text != null && userId != 0) {
                return getDocumentHandler(text).authorizeAndHandle(userService.getOrCreate(userId, name, username, surname), chatId,text);
            }
            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException e) {
            log.debug("Command: {} is unsupported", update.toString());
            return Collections.emptyList();
        }
    }

    public List<SendVenue> handleVenue(Update update) {
        try {
            int userId = 0;
            String name = "unknown";
            String username = "unknown";
            String surname = "unknown";
            String text = null;
            Long chatId = null;

            if (isMessageWithText(update)) {
                final Message message = update.getMessage();
                userId = message.getFrom().getId();
                name = message.getFrom().getFirstName();
                username = message.getFrom().getUserName();
                surname = message.getFrom().getLastName();
                text = message.getText();
                chatId = message.getChatId();
                log.debug("Update is text message {} from {} chat {}", text, userId, chatId);
            } else if (update.hasCallbackQuery()) {
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                userId = callbackQuery.getFrom().getId();
                name = callbackQuery.getFrom().getFirstName();
                username = callbackQuery.getFrom().getUserName();
                surname = callbackQuery.getFrom().getLastName();
                text = callbackQuery.getData();
                chatId = callbackQuery.getMessage().getChatId();
                log.debug("Update is callbackquery {} from {}", text, userId);
            }
            if (text != null && userId != 0) {
                return getVenueHandler(text).authorizeAndHandle(userService.getOrCreate(userId, name, username, surname), chatId,text);
            }
            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException e) {
            log.debug("Command: {} is unsupported", update.toString());
            return Collections.emptyList();
        }
    }

    public List<EditMessageText> handleEdit(Update update) {
        try {
            int userId = 0;
            String name = "unknown";
            String username = "unknown";
            String surname = "unknown";
            String text = null;
            Long chatId = null;
            int messageId = 0;

            if (update.hasCallbackQuery()) {
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                userId = callbackQuery.getFrom().getId();
                name = callbackQuery.getFrom().getFirstName();
                username = callbackQuery.getFrom().getUserName();
                surname = callbackQuery.getFrom().getLastName();
                text = callbackQuery.getData();
                messageId = callbackQuery.getMessage().getMessageId();
                chatId = callbackQuery.getMessage().getChatId();
                log.debug("Update is callbackquery {} from {}", text, userId);
            }
            if (text != null && userId != 0) {
                return getEditHandler(text).authorizeAndHandle(userService.getOrCreate(userId, name, username, surname), chatId,text, messageId);
            }
            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException e) {
            log.debug("Command: {} is unsupported", update.toString());
            return Collections.emptyList();
        }
    }

    public List<DeleteMessage> handleDelete(Update update) {
        try {
            int userId = 0;
            String name = "unknown";
            String username = "unknown";
            String surname = "unknown";
            String text = null;
            Long chatId = null;
            int messageId = 0;

            if (update.hasCallbackQuery()) {
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                userId = callbackQuery.getFrom().getId();
                name = callbackQuery.getFrom().getFirstName();
                username = callbackQuery.getFrom().getUserName();
                surname = callbackQuery.getFrom().getLastName();
                text = callbackQuery.getData();
                messageId = callbackQuery.getMessage().getMessageId();
                chatId = callbackQuery.getMessage().getChatId();
                log.debug("Update is callbackquery {} from {}", text, userId);
            }
            if (text != null && userId != 0) {
                return getDeleteHandler(text).authorizeAndHandle(userService.getOrCreate(userId, name, username, surname), chatId,text, messageId);
            }
            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException e) {
            log.debug("Command: {} is unsupported", update.toString());
            return Collections.emptyList();
        }
    }

    /**
     * Selects handler which can handle received command
     *
     * @param text content of received message
     * @return handler suitable for command
     */
    private AbstractBaseHandler getHandler(String text) {
        return handlers.stream()
                .filter(h -> h.getClass()
                        .isAnnotationPresent(BotCommand.class))
                .filter(h -> Stream.of(h.getClass()
                        .getAnnotation(BotCommand.class)
                        .command())
                        .anyMatch(c -> c.equalsIgnoreCase(ParsingUtil.extractCommand(text))))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private AbstractDocumentHandler getDocumentHandler(String text) {
        return DocumentHandlers.stream()
                .filter(h -> h.getClass()
                        .isAnnotationPresent(BotCommand.class))
                .filter(h -> Stream.of(h.getClass()
                        .getAnnotation(BotCommand.class)
                        .command())
                        .anyMatch(c -> c.equalsIgnoreCase(ParsingUtil.extractCommand(text))))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private AbstractVenueHandler getVenueHandler(String text) {
        return VenueHandlers.stream()
                .filter(h -> h.getClass()
                        .isAnnotationPresent(BotCommand.class))
                .filter(h -> Stream.of(h.getClass()
                        .getAnnotation(BotCommand.class)
                        .command())
                        .anyMatch(c -> c.equalsIgnoreCase(ParsingUtil.extractCommand(text))))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private AbstractDeleteHandler getDeleteHandler(String text) {
        return DeleteHandlers.stream()
                .filter(h -> h.getClass()
                        .isAnnotationPresent(BotCommand.class))
                .filter(h -> Stream.of(h.getClass()
                        .getAnnotation(BotCommand.class)
                        .command())
                        .anyMatch(c -> c.equalsIgnoreCase(ParsingUtil.extractCommand(text))))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private AbstractEditHandler getEditHandler(String text) {
        return EditHandlers.stream()
                .filter(h -> h.getClass()
                        .isAnnotationPresent(BotCommand.class))
                .filter(h -> Stream.of(h.getClass()
                        .getAnnotation(BotCommand.class)
                        .command())
                        .anyMatch(c -> c.equalsIgnoreCase(ParsingUtil.extractCommand(text))))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }
}
