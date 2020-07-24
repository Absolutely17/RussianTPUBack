package ru.tpu.russian.back.entity.security;

public interface OAuthUserInfo {

    String getEmail();

    String getFirstName();

    String getLastName();

    String getProvider();
}
