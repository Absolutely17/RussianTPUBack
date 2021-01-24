package ru.tpu.russian.back.repository.user;

import ru.tpu.russian.back.dto.user.*;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.entity.calendarEvent.CalendarEvent;

import java.util.*;

public interface IUserRepository {

    /**
     * Добавляем нового пользователя
     */
    User saveUser(UserRegisterRequest user);

    /**
     * Редактируем секрет для рефреш токена у пользователя
     */
    void editRefreshSalt(String email, String salt);

    /**
     * Получаем пользователя по email
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Меняем статус подтверждения аккаунта
     */
    int editRegisteredStatus(String email, boolean status);

    /**
     * Изменяем данные пользователя
     */
    void editUser(UserProfileEditRequest requestDto);

    /**
     * Сбрасываем и устанавливаем новый пароль
     */
    int editPassword(String email, String newPassword, String token);

    /**
     * Генерируем токен для сброса пароля
     */
    void addResetToken(String email, String token);

    /**
     * Получаем ID группы (не общеизвестный, к примеру, 8В7Б, а 35089(обозначение внутри системы ТПУ)
     */
    Optional<String> getGroupId(String email);

    /**
     * Получить события, отображаемые в календаре для пользователя
     */
    List<CalendarEvent> getCalendarEventsByEmail(String email);

    /**
     * Получить событие по его ID
     */
    Optional<CalendarEvent> getCalendarEventById(String id);

    /**
     * Удалить пользователя из системы.
     */
    void deleteUserById(String id);

    /**
     * Редактирование пользователя из админки
     */
    void editUserByAdmin(String id, UserProfileEditRequest request);
}
