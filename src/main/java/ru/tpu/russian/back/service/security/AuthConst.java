package ru.tpu.russian.back.service.security;

import java.util.regex.Pattern;

public class AuthConst {

    public static final Pattern VALID_EMAIL_ADDRESS = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    public static final Pattern VALID_PASSWORD = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");

    public static final int HTTP_STATUS_REG_NEED_FILL = 210;
}
