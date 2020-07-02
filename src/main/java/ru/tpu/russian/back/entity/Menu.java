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

    @Column(name = "Порядок отображения")
    private int position;

    @Column(name = "Тип")
    @Enumerated(EnumType.STRING)
    private MenuType type;

    @Column(name = "Язык подчинённого")
    private String language;

    @Column(name = "Родитель")
    private String parentName;

    @Column(name = "URL")
    @Nullable
    private String url;

    @Nullable
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "Родитель", updatable = false, insertable = false)
    private Menu parent;

    @Transient
    private List<Menu> childrens = new ArrayList<>();

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

    public String getLanguage() {
        return language;
    }

    public String getParentName() {
        return parentName;
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

    public List<Menu> getChildrens() {
        return childrens;
    }
}

