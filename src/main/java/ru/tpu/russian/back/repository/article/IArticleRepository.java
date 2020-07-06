package ru.tpu.russian.back.repository.article;

import ru.tpu.russian.back.entity.ArticleBrief;

import java.util.*;

public interface IArticleRepository {

    List<ArticleBrief> getBriefArticles(Map<String, Object> params);

}
