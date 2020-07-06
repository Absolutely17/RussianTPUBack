package ru.tpu.russian.back.entity;

import org.springframework.lang.*;
import ru.tpu.russian.back.dto.enums.MenuType;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.FetchType.EAGER;

@NamedStoredProcedureQuery(
        name = "GetMenuByLanguage",
        procedureName = "GetMenuByLanguage",
        resultClasses = {Menu.class},
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "Language", type = String.class)
        })
@Entity
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
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "ID родителя", updatable = false, insertable = false)
    private Menu parent;

    @Transient
    private List<Menu> children = new ArrayList<>();

    public Menu() {
    }

    public String getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public String getParentId() {
        return parentId;
    }

    @Nullable
    public Menu getParent() {
        return parent;
    }

    public MenuType getType() {
        return type;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    public List<Menu> getChildren() {
        return children;
    }
}

