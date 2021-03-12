package ru.dcp.gamedev.demo.telegram.annotations;

import ru.dcp.gamedev.demo.models.model.Role;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.telegram.bot.handler.AbstractBaseHandler;
import ru.dcp.gamedev.demo.telegram.bot.handler.AdminTimeHandler;
import ru.dcp.gamedev.demo.telegram.security.AuthorizationService;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Объявляет список ролей {@link Role ru.rtlabs.automation.model.Role} каждый из пользователей {@link User ru.rtlabs.automation.model.User }
 * должен иметь, чтобы получить достук к методу с аннотацией
 * <p>
 * Используется у наслединков {@link AbstractBaseHandler}
 *
 * @see AdminTimeHandler пример использования анотацияя
 * @see AuthorizationService пример применения авторизации
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface RequiredRoles {
    /**
     * @return возвращает список ролей, для которых есть доступ
     */
    Role[] roles();
}
