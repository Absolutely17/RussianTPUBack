package ru.tpu.russian.back.security.model;

public interface OAuthUserInfo {

    String getEmail();

    String getFirstName();

    String getLastName();

    String getProvider();
}
