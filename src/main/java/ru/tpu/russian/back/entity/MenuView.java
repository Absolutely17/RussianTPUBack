package ru.tpu.russian.back.entity;

import javax.persistence.*;

import java.util.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "Menu")
public class MenuView {

    @Column("Подчинённый")
	private String name;

    @Column("Уровень меню")
    private int level;

    @Column("Язык подчинённого")
    private String language;

    @Column("Родитель")
    private String parentName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "Родитель", updatable = false, insertable = false)
    private MenuView parent;

    @Column("Позиция")
    private int position;

    @Column("Язык родителя")
    private String parentLanguage;

    @OneToMany(mappedBy = "parent", fetch = LAZY)
//    @JoinTable(name = "Menu",
//    joinColumns = @JoinColumn(name = "Родитель", referencedColumnName = "Подчинённый"))
    private List<MenuView> childs;

    public MenuView() {
    }

    public MenuView(
            String name,
            int level,
            String language,
            String parentName,
            int position
    ) {
        this.name = name;
        this.level = level;
        this.language = language;
        this.parentName = parentName;
        this.position = position;
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

    public MenuView getParent() {
        return parent;
    }

    public int getPosition() {
        return position;
    }

    public String getParentLanguage() {
        return parentLanguage;
    }

    public List<MenuView> getChilds() {
        return childs;
    }
}