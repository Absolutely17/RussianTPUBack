package ru.tpu.russian.back.entity.menu;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "MENU_VIEW")
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

    @Column(name = "ARTICLE_ID")
    @Nullable
    private String idArticle;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "PARENT_ID", updatable = false, insertable = false)
    private Menu parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("position ASC")
    private List<Menu> children;

    @Column(name = "IMAGE_ID")
    @Nullable
    private String image;
}

