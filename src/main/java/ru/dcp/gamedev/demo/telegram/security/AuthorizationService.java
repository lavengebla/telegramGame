package ru.dcp.gamedev.demo.telegram.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.dcp.gamedev.demo.models.model.Role;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.telegram.annotations.RequiredRoles;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Проверяем, если пользователь имеет достаточный уровень доступа для выполнения команды
 */
@Component
@Slf4j
public class AuthorizationService {
    /**
     * Проверяет разрешение пользователя, если класс содержит аннтотацию if  {@link RequiredRoles}
     *
     * @param user
     * @return результат авторизации
     */

    public final boolean authorize(Class<?> clazz, User user) {
        log.debug("Авторизация {} на использование {}", user, clazz.getSimpleName());
        try {
            final RequiredRoles annotation = Stream.of(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequiredRoles.class))
                    .findFirst()
                    .orElseThrow(NoSuchMethodException::new)
                    .getDeclaredAnnotation(RequiredRoles.class);
            log.debug("Пользовательские роли: {} Требуемые роли: {}", user.getRoles(), annotation.roles());
            Role[] roles = annotation.roles();
            Set<Role> roleSet = new HashSet<>();
            Collections.addAll(roleSet, roles);
            return !Collections.disjoint(user.getRoles(), roleSet);
        } catch (NoSuchMethodException e) {
            log.debug("Нет защищенных методов в классе {}", clazz.getSimpleName());
            return true;
        }
    }

}
