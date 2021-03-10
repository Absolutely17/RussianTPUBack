package ru.tpu.russian.back.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.user.*;
import ru.tpu.russian.back.entity.user.User;
import ru.tpu.russian.back.repository.language.LanguageRepository;
import ru.tpu.russian.back.repository.notification.MailingTokenRepository;


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
                mailingTokenRepository.getByUserIdAndActive(user.getId(), true).isPresent()
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
                user.isConfirm(),
                user.getAcademicPlanUrl()
        );
    }
}
