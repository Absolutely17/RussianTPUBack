package ru.tpu.russian.back.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.user.BaseUserRequest;
import ru.tpu.russian.back.dto.user.UserProfileResponse;
import ru.tpu.russian.back.dto.user.UserResponse;
import ru.tpu.russian.back.dto.user.UserTableRow;
import ru.tpu.russian.back.dto.user.calendarEvent.CalendarEventCreateRequest;
import ru.tpu.russian.back.dto.user.calendarEvent.CalendarEventResponse;
import ru.tpu.russian.back.entity.CalendarEvent;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.repository.language.LanguageRepository;
import ru.tpu.russian.back.repository.notification.MailingTokenRepository;

import java.util.UUID;

@Component
public class UserMapper {

    private final LanguageRepository languageRepository;

    private final MailingTokenRepository mailingTokenRepository;

    public UserMapper(LanguageRepository languageRepository,
                      MailingTokenRepository mailingTokenRepository) {
        this.languageRepository = languageRepository;
        this.mailingTokenRepository = mailingTokenRepository;
    }

    public UserResponse convertToResponse(User user) {
        String languageName = languageRepository.getById(user.getLanguage()).getShortName();
        return new UserResponse(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getGender(),
                user.getPhoneNumber(),
                user.getGroupName(),
                user.getLanguage(),
                languageName
        );
    }

    public UserTableRow convertToTableRow(User user) {
        String languageName = languageRepository.getById(user.getLanguage()).getShortName();
        boolean activeFcmToken = mailingTokenRepository.isActiveByUserId(user.getId());
        return new UserTableRow(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getGender() == null ? null : user.getGender().getValue(),
                user.getPhoneNumber(),
                user.getGroupName(),
                user.getLanguage(),
                languageName,
                activeFcmToken
        );
    }

    public User convertToUserFromRegistrationRequest(BaseUserRequest request) {
        return new User(
                request.getFirstName(),
                request.getLastName(),
                request.getMiddleName(),
                request.getGender(),
                request.getLanguageId(),
                request.getPhoneNumber(),
                request.getEmail(),
                request.getProvider(),
                request.getGroupName()
        );
    }

    public UserProfileResponse convertToProfile(User user) {
        String languageName = languageRepository.getById(user.getLanguage()).getShortName();
        return new UserProfileResponse(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getGender(),
                user.getPhoneNumber(),
                user.getGroupName(),
                user.getLanguage(),
                languageName,
                user.isConfirm()
        );
    }

    public CalendarEvent convertToCalendarEventFromRequest(CalendarEventCreateRequest request) {
        return new CalendarEvent(
                UUID.randomUUID().toString(),
                request.getTitle(),
                request.getDescription(),
                request.getDate(),
                request.getGroupTarget(),
                request.isSendNotification()
        );
    }

    public CalendarEventResponse convertCalendarEventToResponse(CalendarEvent event) {
        return new CalendarEventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getTimestamp(),
                event.getTargetEnum()
        );
    }
}
