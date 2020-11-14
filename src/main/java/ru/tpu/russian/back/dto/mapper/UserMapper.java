package ru.tpu.russian.back.dto.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.request.BaseUserRequestDto;
import ru.tpu.russian.back.dto.response.*;
import ru.tpu.russian.back.entity.User;
import ru.tpu.russian.back.repository.dicts.IDictRepository;

@Component
public class UserMapper {

    private final IDictRepository dictRepository;

    public UserMapper(IDictRepository dictRepository) {
        this.dictRepository = dictRepository;
    }

    public UserResponseDto convertToResponse(User user) {
        String languageName = dictRepository.getLanguageById(user.getLanguage()).getShortName();
        return new UserResponseDto(
                user.getId(),
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

    public User convertToUserFromRegistrationRequest(BaseUserRequestDto request) {
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
                user.isConfirm()
        );
    }
}
