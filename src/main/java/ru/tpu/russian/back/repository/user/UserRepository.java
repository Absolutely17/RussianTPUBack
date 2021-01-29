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
    Optional<CustomUserDetails> findByEmail(@Param("email") String email);

    @Query(value = "select u.id from User u where u.email = :email")
    String getUserIdByEmail(@Param("email") String email);

    Long countByLanguage(String language);

    @Query("SELECT u.id FROM User u LEFT JOIN StudyGroup gr ON gr.id = :groupId WHERE u.groupName = gr.name")
    List<String> getUsersByGroupId(@Param("groupId") String groupId);
}
