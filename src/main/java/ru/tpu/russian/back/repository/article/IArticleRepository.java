package ru.tpu.russian.back.repository.article;

import ru.tpu.russian.back.entity.*;

import java.util.*;

public interface IArticleRepository {

    List<Article> getBriefArticlesFromMenu(String idMenuItem);

    List<Article> getBriefArticles(String id);

}
