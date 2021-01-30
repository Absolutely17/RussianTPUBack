package ru.tpu.russian.back.enums;

import lombok.Getter;

/**
 * Перечисление всех возможных групп рассылки (кроме языков, они отдельно)
 */
public enum NotificationTargetGroup {
    ALL("all");

    @Getter
    String value;

    NotificationTargetGroup(String value) {
        this.value = value;
    }
}
