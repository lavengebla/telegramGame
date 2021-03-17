package ru.dcp.gamedev.demo.telegram.bot.handler;

import com.google.common.collect.EnumMultiset;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.dcp.gamedev.demo.configuration.configLoader;
import ru.dcp.gamedev.demo.models.model.Characters;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.models.model.inventory.Inventory;
import ru.dcp.gamedev.demo.models.model.inventory.Item;
import ru.dcp.gamedev.demo.service.CharacterService;
import ru.dcp.gamedev.demo.service.NotificationService;
import ru.dcp.gamedev.demo.telegram.annotations.BotCommand;
import ru.dcp.gamedev.demo.telegram.builder.EditMessageBuilder;
import ru.dcp.gamedev.demo.util.ParsingUtil;

import java.util.Collection;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Optional;


/**
 * Продолжение игры
 * <p>
 * Доступ: любой пользователь
 */

@Component
@Slf4j
@BotCommand(command = {"/INVENTORY"})
public class CharacterInventoryHandler extends AbstractEditHandler {

    //инициализация конфига
    final static configLoader cfg = ConfigFactory.create(configLoader.class,
            System.getProperties(),
            System.getenv());

    private CharacterService characterService;

    public CharacterInventoryHandler(NotificationService notificationService, CharacterService characterService) {
        super(notificationService);
        this.characterService = characterService;
    }

    @Override
    protected List<EditMessageText> handle(User user, Long chatId, String message, int id) {
        log.debug("Подготовка /INVENTORY");

        final String[] arguments = ParsingUtil.extractArguments(message).split(" ");

        boolean stash;

        if (arguments[0].equals("S")) {
            stash = true;
        } else {
            stash = false;
        }

        Optional<Characters> CharacterOptional = characterService.getById(Integer.parseInt(arguments[1]));

        EditMessageBuilder editMessageBuilder = EditMessageBuilder.create(String.valueOf(chatId), id);

        if (CharacterOptional.isPresent()) {

            Characters Character = CharacterOptional.get();

            Iterable<Inventory> charInventory = characterService.getInventory(Character);


            if (size(charInventory) == 0) {
                editMessageBuilder
                        .line("Вы где-то потеряли все вещи ...");
            } else {
                int counter = 0;
                //показывает инвентарь
                editMessageBuilder
                        .line(String.format("*%s*, ваш инвентарь", Character.getName()));

                //блок экипы
                editMessageBuilder
                        .row()
                            .button("Экипированные предметы", "blank")
                        .row()
                            .button("\uD83D\uDC51", "head_command")
                            .button("\uD83C\uDFF9", "weapon_command")
                        .row()
                            .button("🥋", "body_command")
                            .button("\uD83D\uDC8D", "ring1_command")
                        .row()
                            .button("\uD83D\uDC5E", "boot_command")
                            .button("\uD83D\uDC8D", "ring2_command");

                if (stash) {
                    //блок тайника
                    editMessageBuilder
                            .row()
                            .button("Тайник", "blank");

                    for (Inventory inventory : charInventory
                    ) {
                        if (counter % 5 == 0) {
                            editMessageBuilder
                                    .row();
                        }
                        editMessageBuilder
                                .button(getItemIcon(inventory.getItem().getItemType()), String.format("/ITEM_MENU %d", inventory.getItem().getId()));
                        counter++;
                    }
                }
            }
        } else {
            editMessageBuilder
                    .line("Вы где-то потеряли все вещи ...");
        }

        if (stash) {
            editMessageBuilder
                    .row()
                    .button("Скрыть тайник", String.format("/INVENTORY NS %s", arguments[1]));
        } else {
            editMessageBuilder
                    .row()
                    .button("Показать тайник", String.format("/INVENTORY S %s", arguments[1]));
        }

        editMessageBuilder
                .row()
                .button("Назад", String.format("/LOGIN %s", arguments[1]))
                .CloseButton();

        return List.of(editMessageBuilder.build());
    }


    private static int size(Iterable data) {

        if (data instanceof Collection) {
            return ((Collection<?>) data).size();
        }
        int counter = 0;
        for (Object i : data) {
            counter++;
        }
        return counter;
    }

    private static String getItemIcon(Item.item_type item_type)
    {
        switch (item_type) {
            case BODY:
                return "🥋";
            case RING:
                return "\uD83D\uDC8D";
            case HEAD:
                return "\uD83D\uDC51";
            case BOOT:
                return "\uD83D\uDC5E";
            case WEAPON_MELEE:
                return "\uD83D\uDDE1";
            case WEAPON_RANGE:
                return "\uD83C\uDFF9";
            case WEAPON_MAGIC:
                return "\uD83D\uDD2E";
            default: return null;
        }
    }
}

