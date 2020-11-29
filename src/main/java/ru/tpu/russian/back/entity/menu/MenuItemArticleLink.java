package ru.tpu.russian.back.entity.menu;

import lombok.*;
import ru.tpu.russian.back.entity.Article;

import javax.persistence.*;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "MENU_ITEM_ARTICLE_LINK")
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class MenuItemArticleLink {

    @Id
    @Column(name = "ID")
    private String id;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "MENU_ITEM_ID")
    private Menu menu;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "ARTICLE_ID")
    private Article article;

    public MenuItemArticleLink(Menu menu, Article article) {
        id = UUID.randomUUID().toString();
        this.menu = menu;
        this.article = article;
    }
}
