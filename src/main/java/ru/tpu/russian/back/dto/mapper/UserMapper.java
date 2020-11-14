package ru.tpu.russian.back.dto.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.user.*;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.repository.dicts.IDictRepository;
import ru.tpu.russian.back.repository.notification.MailingTokenRepository;

@Component
public class UserMapper {

    private final IDictRepository dictRepository;

    private final MailingTokenRepository mailingTokenRepository;

    public UserMapper(IDictRepository dictRepository,
                      MailingTokenRepository mailingTokenRepository) {
        this.dictRepository = dictRepository;
        this.mailingTokenRepository = mailingTokenRepository;
    }

    public UserResponse convertToResponse(User user) {
        String languageName = dictRepository.getLanguageById(user.getLanguage()).getShortName();
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
        String languageName = dictRepository.getLanguageById(user.getLanguage()).getShortName();
        boolean activeFcmToken = mailingTokenRepository.isActiveByUserId(user.getId());
        return new UserTableRow(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getGender(),
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
        String languageName = dictRepository.getLanguageById(user.getLanguage()).getShortName();
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
}
