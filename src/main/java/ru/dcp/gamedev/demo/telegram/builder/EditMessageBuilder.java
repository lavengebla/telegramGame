package ru.dcp.gamedev.demo.telegram.builder;

import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.dcp.gamedev.demo.models.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * MessageBuilder is used to build instances of {@link SendMessage}
 * <p>
 * MessageBuilder provides useful methods that simplify creation of bot replies
 */
public final class EditMessageBuilder {
    @Setter
    private String chatId;
    @Setter
    private int messageId;
    private final StringBuilder sb = new StringBuilder();
    private final List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
    private List<InlineKeyboardButton> row = null;

    private EditMessageBuilder() {
    }

    /**
     * Creates new MessageBuilder with initialized chatId
     *
     * @param chatId of user that will receive the message
     * @return instance of MessageBuilder with initialized chatId
     */
    public static EditMessageBuilder create(String chatId, int messageId) {
        EditMessageBuilder builder = new EditMessageBuilder();
        builder.setChatId(chatId);
        builder.setMessageId(messageId);
        return builder;
    }

    /**
     * Creates new MessageBuilder with initialized chatId
     *
     * @param user that will receive the message
     * @return instance of MessageBuilder with initialized chatId
     */
    public static EditMessageBuilder create(User user, int messageId) {
        return create(String.valueOf(user.getUserId()), messageId);
    }

    /**
     * Simplified use of {@link String#format(String, Object...) String.format} that adds new formatted line to the
     * inner instance of {@link StringBuilder}
     *
     * @param text first agrument of {@link String#format(String, Object...) String.format}
     * @param args second and following arguments of {@link String#format(String, Object...) String.format}
     * @return this
     */
    public EditMessageBuilder line(String text, Object... args) {
        sb.append(String.format(text, args));
        return line();
    }

    /**
     * Creates new line break
     *
     * @return this
     */
    public EditMessageBuilder line() {
        sb.append(String.format("%n"));
        return this;
    }

    /**
     * Creates new {@link InlineKeyboardButton} row
     *
     * @return this
     */
    public EditMessageBuilder row() {
        addRowToKeyboard();
        row = new ArrayList<>();
        return this;
    }

    /**
     * Creates new {@link InlineKeyboardButton}
     *
     * @param text         button text
     * @param callbackData on click callback
     * @return this
     */
    public EditMessageBuilder button(String text, String callbackData) {

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callbackData);

        row.add(inlineKeyboardButton);
        return this;
    }

    public EditMessageBuilder LinkButton(String text, String url) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setUrl(url);

        row.add(inlineKeyboardButton);
        return this;
    }

    public EditMessageBuilder CloseButton() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Закрыть");
        inlineKeyboardButton.setCallbackData("/DELETE");
        row.add(inlineKeyboardButton);
        return this;
    }
    /**
     * Creates new {@link InlineKeyboardButton}
     *
     * @param text         button text (and callback argument)
     * @param callbackData on click callback
     * @return this
     */
    public EditMessageBuilder buttonWithArguments(String text, String callbackData) {
        return button(text, callbackData + " " + text);
    }

    /**
     * Builds an instance of {@link SendMessage}
     *
     * @return {@link SendMessage}
     */
    public EditMessageText build() {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(chatId);
        editMessageText.setText(sb.toString());
        editMessageText.enableMarkdown(true);
        addRowToKeyboard();

        if (!keyboard.isEmpty()) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(keyboard);
            editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        }

        return editMessageText;
    }

    /**
     * Adds new row to keyboard.
     * Performs null check of current row
     */
    private void addRowToKeyboard() {
        if (row != null) {
            keyboard.add(row);
        }
    }
}

