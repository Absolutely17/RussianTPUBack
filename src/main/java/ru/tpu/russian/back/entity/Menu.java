package ru.tpu.russian.back.entity;

import org.springframework.lang.*;

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
    @Column(name = "Подчинённый")
    private String name;

    @Column(name = "Уровень меню")
    private int level;

    @Column(name = "Порядок отображения")
    private int orderNumber;

    @Column(name = "Язык подчинённого")
    private String language;

    @Column(name = "Родитель")
    private String parentName;

    @Nullable
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "Родитель", updatable = false, insertable = false)
    private Menu parent;

    @Column(name = "Язык родителя")
    private String parentLanguage;

    @Transient
    private List<Menu> childrens = new ArrayList<>();

    public Menu() {
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentLanguage() {
        return parentLanguage;
    }

    public void setParentLanguage(String parentLanguage) {
        this.parentLanguage = parentLanguage;
    }

    @Nullable
    public Menu getParent() {
        return parent;
    }

    public void setParent(@Nullable Menu parent) {
        this.parent = parent;
    }

    @NonNull
    public List<Menu> getChildrens() {
        return childrens;
    }

    public void setChildrens(@NonNull List<Menu> childrens) {
        this.childrens = childrens;
    }

    @Override
    public String toString() {
        return "Пункт меню:\n" +
                "name='" + name + '\n' +
                ", level=" + level + '\n' +
                ", language='" + language + '\n' +
                ", parentName='" + parentName + '\n' +
                ", orderNumber=" + orderNumber + '\n' +
                ", parentLanguage='" + parentLanguage + '\n';
    }
}

