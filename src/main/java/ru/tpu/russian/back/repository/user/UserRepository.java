package ru.tpu.russian.back.repository.user;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.user.User;
import ru.tpu.russian.back.security.model.CustomUserDetails;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, String>, IUserRepository {

    /**
     * Ищем пользователя по email при каждом обращении к сервису. Вынес сюда, дабы сократить время на запрос
     */
    @Query(value = "select new ru.tpu.russian.back.security.model.CustomUserDetails(u.email, u.password, u.role) " +
            " from User u where u.email = :email")
    Optional<CustomUserDetails> getUserDetailByEmail(@Param("email") String email);

    /**
     * Число пользователей с тем или иным языком
     */
    Long countByLanguage(String language);

    /**
     * Искать пользователей по учебной группе
     *
     * @param groupId ID группы
     * @return список ID пользователей
     */
    @Query("SELECT u.id FROM User u LEFT JOIN StudyGroup gr ON gr.id = :groupId WHERE u.groupName = gr.name")
    List<String> getUsersByGroupId(@Param("groupId") String groupId);

    /**
     * Получить всех пользователей определенного языка
     */
    List<User> getAllByLanguage(String languageId);

    /**
     * Получить всех пользователей по их ID одним запросом
     */
    @Query("SELECT main from User main where main.id in :ids")
    List<User> getAllByIds(@Param(value = "ids") List<String> ids);
}
