package ru.tpu.russian.back.dto.mapper;

import org.springframework.stereotype.Component;
import ru.tpu.russian.back.dto.response.UserResponseDto;
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
}
