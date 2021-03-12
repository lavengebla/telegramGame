package ru.dcp.gamedev.demo.telegram.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.dcp.gamedev.demo.configuration.configLoader;
import ru.dcp.gamedev.demo.models.model.Role;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.service.UserService;
import ru.dcp.gamedev.demo.telegram.annotations.BotCommand;
import ru.dcp.gamedev.demo.telegram.annotations.RequiredRoles;
import ru.dcp.gamedev.demo.telegram.builder.MessageBuilder;
import ru.dcp.gamedev.demo.util.ParsingUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.dcp.gamedev.demo.models.model.Role.АДМИНИСТРАТОР;


/**
 * Позволяет администратору менять роли в боте
 * Синтакс: /ADMIN_PROMOTE userId role
 * <p>
 * Доступ: Admin
 */
@Component
@Slf4j
@BotCommand(command = "/ВЫДАТЬ_РОЛЬ")
public class AdminPromoteHandler extends AbstractUserHandler {

    public AdminPromoteHandler(UserService userService) {
        super(userService);
    }

    @Override
    @RequiredRoles(roles = АДМИНИСТРАТОР)
    protected List<BotApiMethod<Message>> handle(User admin, Long chatId, String message) {
        log.debug("подготавливаем команду /ВЫДАТЬ_РОЛЬ");
        final String[] arguments = ParsingUtil.extractArguments(message).split(" ");
        final int userId = Integer.parseInt(arguments[0]);

        //инициализация конфига
        final configLoader cfg = ConfigFactory.create(configLoader.class,
                System.getProperties(),
                System.getenv());


        final User toUpdate = userService.get(userId).orElse(null);
        String roleValue = "";

        if (toUpdate != null) {
            try {
                roleValue = arguments[1].trim();
                final Role roleToUpdate = Role.valueOf(roleValue);
                Set<Role> userRoles = toUpdate.getRoles();

                if (toUpdate.getRoles().contains(roleToUpdate)) {
                    userRoles.remove(roleToUpdate);
                } else {
                    userRoles.add(roleToUpdate);
                }

                toUpdate.setRoles(userRoles);
                userService.update(toUpdate);

                return List.of(
                        MessageBuilder.create(cfg.adminChat())
                                .line("Обновлен пользователь: %s", toUpdate.toString())
                                .build(),
                        MessageBuilder.create(toUpdate)
                                .line("Ваши роли были обновлены: %s", userRoles.stream()
                                        .map(Role::toString)
                                        .collect(Collectors.joining(", ")))
                                .build());
            } catch (IllegalArgumentException e) {
                return List.of(MessageBuilder.create(cfg.adminChat())
                        .line("Роль задана некорректно: %s", roleValue)
                        .build());
            }
        } else {
            return List.of(MessageBuilder.create(cfg.adminChat())
                    .line("Не смог найти пользователя: %d", userId)
                    .build());
        }
    }
}