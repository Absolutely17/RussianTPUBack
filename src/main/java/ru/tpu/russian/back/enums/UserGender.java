package ru.tpu.russian.back.enums;

import lombok.Getter;

public enum UserGender {

    Male("Мужской"),
    Female("Женский");

    @Getter
    String value;

    UserGender(String value) {
        this.value = value;
    }
}
