package ru.tpu.russian.back.repository.user;

import ru.tpu.russian.back.dto.user.*;
import ru.tpu.russian.back.entity.user.User;

import javax.persistence.criteria.CriteriaQuery;
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
     * Получаем ссылку на расписание для группы
     */
    Optional<String> getGroupScheduleUrl(String email);

    /**
     * Удалить пользователя из системы.
     */
    void deleteUserById(String id);

    /**
     * Редактирование пользователя из админки
     */
    void editUserByAdmin(String id, UserProfileEditRequest request);

    /**
     * Искать пользователей по кастомному запросу
     */
    List<User> getUsersByQuery(CriteriaQuery query);
}
