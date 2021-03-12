package ru.dcp.gamedev.demo.telegram.annotations;


import ru.dcp.gamedev.demo.telegram.bot.handler.AbstractBaseHandler;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Показывает, что это обработчик команды
 * <p>
 * Используется у наследников {@link AbstractBaseHandler}
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface BotCommand {
    /**
     * Возвращает список команд поддерживаемых обработчиком
     *
     * @return список команд поддерживаемых обработчиком
     */
    String[] command();

    /**
     * Возвращает справку о команде
     *
     * @return Возвращает справку о команде
     */
    String message() default "";
}
