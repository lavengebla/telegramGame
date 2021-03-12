package ru.dcp.gamedev.demo.telegram.builder;

import com.google.common.io.ByteSource;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVenue;
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
public final class VenueBuilder {
    @Setter
    private String chatId;
    private final List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
    private List<InlineKeyboardButton> row = null;
    private String title;
    private String address;
    private String foursquareId;
    private String foursquareType;
    private Double latitude;
    private Double longitude;


    private VenueBuilder() {
    }

    /**
     * Creates new MessageBuilder with initialized chatId
     *
     * @param chatId of user that will receive the message
     * @return instance of MessageBuilder with initialized chatId
     */
    public static VenueBuilder create(String chatId) {
        VenueBuilder builder = new VenueBuilder();
        builder.setChatId(chatId);
        return builder;
    }

    /**
     * Creates new MessageBuilder with initialized chatId
     *
     * @param user that will receive the message
     * @return instance of MessageBuilder with initialized chatId
     */
    public static VenueBuilder create(User user) {
        return create(String.valueOf(user.getUserId()));
    }


    /**
     * Creates new {@link InlineKeyboardButton} row
     *
     * @return this
     */
    public VenueBuilder row() {
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
    public VenueBuilder button(String text, String callbackData) {

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callbackData);

        row.add(inlineKeyboardButton);
        return this;
    }

    public VenueBuilder setVenue(String title,
                              String address,
                              String foursquareId,
                              String foursquareType,
                              Double latitude,
                              Double longitude){
        this.title = title;
        this.address = address;
        this.foursquareId = foursquareId;
        this.foursquareType = foursquareType;
        this.latitude = latitude;
        this.longitude = longitude;
        return this;
    }


    public VenueBuilder LinkButton(String text, String url) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setUrl(url);

        row.add(inlineKeyboardButton);
        return this;
    }


    public VenueBuilder CloseButton() {
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
    public VenueBuilder buttonWithArguments(String text, String callbackData) {
        return button(text, callbackData + " " + text);
    }

    /**
     * Builds an instance of {@link SendMessage}
     *
     * @return {@link SendMessage}
     */
    public SendVenue build() {
        SendVenue sendVenue = new SendVenue();

        sendVenue.setChatId(chatId);

        sendVenue.setTitle(title);
        sendVenue.setAddress(address);
        sendVenue.setFoursquareId(foursquareId);
        sendVenue.setFoursquareType(foursquareType);
        sendVenue.setLatitude(latitude);
        sendVenue.setLongitude(longitude);

        addRowToKeyboard();

        if (!keyboard.isEmpty()) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(keyboard);
            sendVenue.setReplyMarkup(inlineKeyboardMarkup);
        }

        return sendVenue;
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

