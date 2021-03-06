package ru.tpu.russian.back.repository.user;

import com.google.api.client.util.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.user.*;
import ru.tpu.russian.back.entity.user.User;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.util.*;

@Slf4j
public class UserRepositoryImpl implements IUserRepository {

    private static final String ADD_USER = "AddUser";

    private static final String EDIT_REFRESH_SALT = "EditUserRefreshSalt";

    private static final String GET_USER_BY_EMAIL = "GetUserByEmail";

    private static final String SET_REGISTERED_STATUS = "EditRegisteredStatus";

    private static final String EDIT_USER = "EditUser";

    private static final String RESET_AND_EDIT_PASS = "EditPasswordUser";

    private static final String ADD_RESET_PASSWORD_TOKEN = "AddResetPasswordRequest";

    private static final String GET_GROUP_SCHEDULE_URL = "GetGroupScheduleUrl";

    private static final String DELETE_USER_BY_ID = "DeleteUser";

    private static final String EDIT_USER_BY_ADMIN = "EditUserByAdmin";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public User saveUser(UserRegisterRequest user) {
        Optional<User> userRegistered = em.createNativeQuery(
                "exec " + ADD_USER + " :firstName, :lastName" +
                        ", :role, :password, :sex, :languageId, :provider, :groupName, :email, :confirmed, :phoneNumber",
                User.class
        )
                .setParameter("firstName", user.getFirstName())
                .setParameter("lastName", user.getLastName())
                .setParameter("role", "ROLE_USER")
                .setParameter("password", user.getPassword())
                .setParameter("sex", user.getGender())
                .setParameter("languageId", user.getLanguageId())
                .setParameter("provider", user.getProvider().toString())
                .setParameter("groupName", user.getGroupName())
                .setParameter("email", user.getEmail())
                .setParameter("confirmed", false)
                .setParameter("phoneNumber", user.getPhoneNumber())
                .getResultStream().findFirst();
        if (userRegistered.isPresent()) {
            return userRegistered.get();
        } else {
            throw new IllegalStateException("Не удалось создать пользователя в БД");
        }
    }

    @Override
    @Transactional
    public void editRefreshSalt(String email, String salt) {
        em.createNativeQuery("exec " + EDIT_REFRESH_SALT + " :refreshSalt, :email")
                .setParameter("refreshSalt", salt)
                .setParameter("email", email)
                .executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return em.createNativeQuery("exec " + GET_USER_BY_EMAIL + " :email", User.class)
                .setParameter("email", email)
                .getResultStream().findFirst();
    }

    @Override
    @Transactional
    public int editRegisteredStatus(String email, boolean status) {
        return em.createNativeQuery("exec " + SET_REGISTERED_STATUS + " :email, :status")
                .setParameter("email", email)
                .setParameter("status", status)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void editUser(UserProfileEditRequest requestDto) {
        em.createNativeQuery("exec " + EDIT_USER + " :firstName, :lastName" +
                ", :password, :sex, :languageId, :groupName, :email, :phoneNumber")
                .setParameter("firstName", requestDto.getFirstName())
                .setParameter("lastName", requestDto.getLastName())
                .setParameter("password", requestDto.getNewPassword())
                .setParameter("sex", requestDto.getGender())
                .setParameter("languageId", requestDto.getLanguageId())
                .setParameter("groupName", requestDto.getGroupName())
                .setParameter("email", requestDto.getEmail())
                .setParameter("phoneNumber", requestDto.getPhoneNumber())
                .executeUpdate();
    }

    @Override
    @Transactional
    public int editPassword(String email, String newPassword, String token) {
        return em.createNativeQuery("exec " + RESET_AND_EDIT_PASS + " :email, :newPassword, :token")
                .setParameter("email", email)
                .setParameter("newPassword", newPassword)
                .setParameter("token", token)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void addResetToken(String email, String token) {
        em.createNativeQuery("exec " + ADD_RESET_PASSWORD_TOKEN + " :email, :token")
                .setParameter("email", email)
                .setParameter("token", token)
                .executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<String> getGroupScheduleUrl(String email) {
        return Optional.ofNullable((String)em.createNativeQuery("exec " + GET_GROUP_SCHEDULE_URL + " :email")
                .setParameter("email", email)
                .getSingleResult());
    }

    @Override
    @Transactional
    public void deleteUserById(String id) {
        em.createNativeQuery("exec " + DELETE_USER_BY_ID + " :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Transactional
    @Override
    public void editUserByAdmin(String id, UserProfileEditRequest request) {
        em.createNativeQuery("exec " + EDIT_USER_BY_ADMIN + " :id, :firstName, :lastName" +
                ", :password, :sex, :languageId, :groupName, :email, :phoneNumber")
                .setParameter("id", id)
                .setParameter("firstName", request.getFirstName())
                .setParameter("lastName", request.getLastName())
                .setParameter("password", request.getNewPassword())
                .setParameter("sex", request.getGender())
                .setParameter("languageId", request.getLanguageId())
                .setParameter("groupName", request.getGroupName())
                .setParameter("email", request.getEmail())
                .setParameter("phoneNumber", Strings.isNullOrEmpty(request.getPhoneNumber())
                        ? null
                        : request.getPhoneNumber()
                )
                .executeUpdate();
    }

    @Override
    public List<User> getUsersByQuery(CriteriaQuery query) {
        return em.createQuery(query).getResultList();
    }
}
