package ru.tpu.russian.back.entity.menu;

import lombok.*;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.entity.Article;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "MENU_ITEM")
public class Menu {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LEVEL")
    private int level;

    @Column(name = "POSITION")
    private int position;

    @OneToOne
    @JoinColumn(name = "TYPE")
    private MenuType type;

    @Column(name = "LANGUAGE_ID")
    private String language;

    @Column(name = "URL")
    @Nullable
    private String url;

    @OneToOne
    @JoinColumn(name = "ARTICLE_ID")
    private Article article;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Menu parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    @OrderBy("position ASC")
    private List<Menu> children;

    @Column(name = "IMAGE_ID")
    @Nullable
    private String imageId;

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MenuItemArticleLink> linkedArticles = new HashSet<>();
}

