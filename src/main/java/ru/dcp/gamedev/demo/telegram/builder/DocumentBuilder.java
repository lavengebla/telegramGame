package ru.dcp.gamedev.demo.telegram.builder;

import com.google.common.io.ByteSource;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.dcp.gamedev.demo.models.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * MessageBuilder is used to build instances of {@link SendDocument}
 * <p>
 * MessageBuilder provides useful methods that simplify creation of bot replies
 */
public final class DocumentBuilder {
    @Setter
    private String chatId;
    private final StringBuilder sb = new StringBuilder();
    private final List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
    private List<InlineKeyboardButton> row = null;
    private InputFile inputFile = new InputFile();

    private DocumentBuilder() {
    }

    /**
     * Creates new MessageBuilder with initialized chatId
     *
     * @param chatId of user that will receive the message
     * @return instance of MessageBuilder with initialized chatId
     */
    public static DocumentBuilder create(String chatId) {
        DocumentBuilder builder = new DocumentBuilder();
        builder.setChatId(chatId);
        return builder;
    }

    /**
     * Creates new MessageBuilder with initialized chatId
     *
     * @param user that will receive the message
     * @return instance of MessageBuilder with initialized chatId
     */
    public static DocumentBuilder create(User user) {
        return create(String.valueOf(user.getUserId()));
    }

    /**
     * Simplified use of {@link String#format(String, Object...) String.format} that adds new formatted line to the
     * inner instance of {@link StringBuilder}
     *
     * @param text first agrument of {@link String#format(String, Object...) String.format}
     * @param args second and following arguments of {@link String#format(String, Object...) String.format}
     * @return this
     */
    public DocumentBuilder line(String text, Object... args) {
        sb.append(String.format(text, args));
        return line();
    }

    /**
     * Creates new line break
     *
     * @return this
     */
    public DocumentBuilder line() {
        sb.append(String.format("%n"));
        return this;
    }

    /**
     * Creates new {@link InlineKeyboardButton} row
     *
     * @return this
     */
    public DocumentBuilder row() {
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
    public DocumentBuilder button(String text, String callbackData) {

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callbackData);

        row.add(inlineKeyboardButton);
        return this;
    }

    public DocumentBuilder LinkButton(String text, String url) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setUrl(url);

        row.add(inlineKeyboardButton);
        return this;
    }

    public DocumentBuilder setMedia(byte[] file, String file_name) throws IOException {
        InputStream targetStream = ByteSource.wrap(file).openStream();
        inputFile.setMedia(targetStream, file_name);
        return this;
    }

    public DocumentBuilder CloseButton() {
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
    public DocumentBuilder buttonWithArguments(String text, String callbackData) {
        return button(text, callbackData + " " + text);
    }

    /**
     * Builds an instance of {@link SendMessage}
     *
     * @return {@link SendMessage}
     */
    public SendDocument build() {
        SendDocument sendDocument = new SendDocument();

        sendDocument.setChatId(chatId);
        sendDocument.setCaption(sb.toString());
        sendDocument.setParseMode("html");
        sendDocument.setDocument(inputFile);
        addRowToKeyboard();

        if (!keyboard.isEmpty()) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(keyboard);
            sendDocument.setReplyMarkup(inlineKeyboardMarkup);
        }

        return sendDocument;
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

