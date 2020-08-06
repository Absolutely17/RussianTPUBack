package ru.tpu.russian.back.entity;

import lombok.*;
import org.springframework.lang.Nullable;
import ru.tpu.russian.back.dto.enums.MenuType;

import javax.persistence.*;
import java.util.List;

@NamedStoredProcedureQuery(
        name = "GetMenuByLanguage",
        procedureName = "GetMenuByLanguage",
        resultClasses = {Menu.class},
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "Language", type = String.class)
        })
@Entity
@Getter
@NoArgsConstructor
@Table(name = "Menu")
public class Menu {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "Подчинённый")
    private String name;

    @Column(name = "Уровень меню")
    private int level;

    @Column(name = "Тип")
    @Enumerated(EnumType.STRING)
    private MenuType type;

    @Column(name = "Ссылка")
    @Nullable
    private String url;

    @Column(name = "Порядок отображения")
    private int position;

    @Column(name = "ID родителя")
    private String parentId;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "ID родителя", updatable = false, insertable = false)
    private Menu parent;

    @Column(name = "ID статьи")
    @Nullable
    private String idArticle;

    @OneToMany(mappedBy = "parent")
    @OrderBy("position ASC")
    private List<Menu> children;

    @Column(name = "ID картинки")
    @Nullable
    private String image;
}

