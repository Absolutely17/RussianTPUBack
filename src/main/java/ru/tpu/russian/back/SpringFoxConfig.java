package ru.tpu.russian.back;

import org.springframework.context.annotation.*;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    public static final String ARTICLE_REST = "ArticleRest";

    public static final String MEDIA_REST = "MediaRest";

    public static final String MENU_REST = "MenuRest";

    public static final String USER_REST = "UserRest";

    public static final String TEST_REST = "TestRest";

    public static final String AUTH_REST = "AuthRest";

    public static final String MAIL_REST = "MailRest";

    public static final String TOKEN_REST = "TokenRest";

    public static final String DOCUMENT_REST = "DocumentRest";

    public static final String DICTS_REST = "DictsRest";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.tpu.russian.back"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .tags(new Tag(ARTICLE_REST, "Получение статей"))
                .tags(new Tag(MEDIA_REST, "Получение изображений"))
                .tags(new Tag(MENU_REST, "Получение меню"))
                .tags(new Tag(USER_REST, "Получение пользователей"))
                .tags(new Tag(AUTH_REST, "Аутентификация/регистрация пользователя"))
                .tags(new Tag(MAIL_REST, "Операции с почтой"))
                .tags(new Tag(TOKEN_REST, "Токен пользователя"))
                .tags(new Tag(DOCUMENT_REST, "Документы пользователя"))
                .tags(new Tag(DICTS_REST, "Получение словарей"));
    }

    private static ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("RussianTPU API").version("1.0.0").build();
    }
}
