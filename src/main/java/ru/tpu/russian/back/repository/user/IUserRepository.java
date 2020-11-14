package ru.tpu.russian.back.repository.user;

import ru.tpu.russian.back.dto.request.BaseUserRequestDto;
import ru.tpu.russian.back.entity.User;

import java.util.Optional;

public interface IUserRepository {

    /**
     * Добавляем нового пользователя
     */
    void saveUser(User user);

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
     *
     * @return
     */
    int editRegisteredStatus(String email, boolean status);

    /**
     * Изменяем данные пользователя
     */
    void editUser(BaseUserRequestDto requestDto);

    /**
     * Сбрасываем и устанавливаем новый пароль
     */
    int editPassword(String email, String newPassword, String token);

    /**
     * Генерируем токен для сброса пароля
     */
    void addResetToken(String email, String token);

    /**
     * Получаем ID группы (не общеизвестный, к примеру, 8В7Б, а его обозначение внутри системы ТПУ - 35089
     */
    Optional<String> getGroupId(String email);
}
