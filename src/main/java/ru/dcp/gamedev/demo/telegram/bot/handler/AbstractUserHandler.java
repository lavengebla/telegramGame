package ru.dcp.gamedev.demo.telegram.bot.handler;

import lombok.RequiredArgsConstructor;
import ru.dcp.gamedev.demo.service.UserService;

/**
 * Base class for handlers that affect users
 */
@RequiredArgsConstructor
public abstract class AbstractUserHandler extends AbstractBaseHandler {
    protected final UserService userService;
}

