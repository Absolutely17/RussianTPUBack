package ru.tpu.russian.back.repository.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tpu.russian.back.dto.user.*;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.entity.calendarEvent.CalendarEvent;

import javax.persistence.*;
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

    private static final String GET_GROUP_ID = "GetUserGroupID";

    private static final String GET_CALENDAR_EVENTS_BY_EMAIL = "GetCalendarEventsByEmail";

    private static final String DELETE_USER_BY_ID = "DeleteUser";

    private static final String EDIT_USER_BY_ADMIN = "EditUserByAdmin";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public User saveUser(UserRegisterRequest user) {
        Optional<User> userRegistered = em.createNativeQuery(
                "exec " + ADD_USER + " :firstName, :middleName, :lastName" +
                        ", :role, :password, :sex, :languageId, :provider, :groupName, :email, :confirmed, :phoneNumber",
                User.class
        )
                .setParameter("firstName", user.getFirstName())
                .setParameter("middleName", user.getMiddleName())
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
        em.createNativeQuery("exec " + EDIT_USER + " :firstName, :middleName, :lastName" +
                ", :password, :sex, :languageId, :groupName, :email, :phoneNumber")
                .setParameter("firstName", requestDto.getFirstName())
                .setParameter("middleName", requestDto.getMiddleName())
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
    public Optional<String> getGroupId(String email) {
        return Optional.ofNullable((String) em.createNativeQuery("exec " + GET_GROUP_ID + " :email")
                .setParameter("email", email)
                .getSingleResult());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarEvent> getCalendarEventsByEmail(String email) {
        return em.createNativeQuery("exec " + GET_CALENDAR_EVENTS_BY_EMAIL + " :email", CalendarEvent.class)
                .setParameter("email", email)
                .getResultList();
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
        em.createNativeQuery("exec " + EDIT_USER_BY_ADMIN + " :id, :firstName, :middleName, :lastName" +
                ", :password, :sex, :languageId, :groupName, :email, :phoneNumber")
                .setParameter("id", id)
                .setParameter("firstName", request.getFirstName())
                .setParameter("middleName", request.getMiddleName())
                .setParameter("lastName", request.getLastName())
                .setParameter("password", request.getNewPassword())
                .setParameter("sex", request.getGender())
                .setParameter("languageId", request.getLanguageId())
                .setParameter("groupName", request.getGroupName())
                .setParameter("email", request.getEmail())
                .setParameter("phoneNumber", request.getPhoneNumber())
                .executeUpdate();
    }
}
