package ru.tpu.russian.back.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.notification.*;
import ru.tpu.russian.back.entity.notification.Notification;
import ru.tpu.russian.back.entity.user.User;
import ru.tpu.russian.back.enums.NotificationTargetGroup;
import ru.tpu.russian.back.exception.BusinessException;
import ru.tpu.russian.back.repository.user.UserRepository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.HashSet;

/**
 * Маппер для уведомлений
 */
@Component
public class NotificationMapper {

    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public NotificationMapper(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    /**
     * Преобразовываем в сущность из запроса уведомления по определенно выбранным пользователям
     */
    public Notification convertToUserNotification(NotificationRequestUsers request, String status) {
        Notification notification = convertToNotificationByBase(request, status);
        notification.setUsers(new HashSet<>(userRepository.getAllByIds(request.getUsers())));
        return notification;
    }

    /**
     * Преобразовываем в сущность из запроса группового уведомления
     */
    public Notification convertToGroupNotification(NotificationRequestGroup request, String status) {
        Notification notification = convertToNotificationByBase(request, status);
        if (request.getLanguageId() != null) {
            notification.setUsers(new HashSet<>(userRepository.getAllByLanguage(request.getLanguageId())));
        } else if (request.getTargetGroup() != null) {
            notification.setUsers(
                    new HashSet<>(userRepository.getUsersByQuery(getQueryByTargetGroup(request.getTargetGroup()))));
        }
        return notification;
    }

    private Notification convertToNotificationByBase(NotificationBaseRequest request, String status) {
        User admin = userRepository.getUserByEmail(request.getAdminEmail())
                .orElseThrow(() -> new BusinessException("Указанный администратор не найден"));
        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setText(request.getMessage());
        notification.setStatus(status);
        notification.setAdminId(admin.getId());
        return notification;
    }

    /**
     * Запрос выбора пользователей в зависимости группы рассылки
     */
    private CriteriaQuery<User> getQueryByTargetGroup(NotificationTargetGroup targetGroup) {
        if (targetGroup == NotificationTargetGroup.ALL) {
            CriteriaQuery<User> query = entityManager.getCriteriaBuilder().createQuery(User.class);
            Root<User> root = query.from(User.class);
            return query.select(root);
        }
        throw new IllegalArgumentException("Неверная группа рассылки");
    }

    /**
     * Маппим в ДТО ответа на получение всех уведомлений
     */
    public NotificationResponse convertToResponse(Notification notification) {
        return new NotificationResponse(
                notification.getTitle(),
                notification.getText(),
                notification.getLoadDate()
        );
    }
}
