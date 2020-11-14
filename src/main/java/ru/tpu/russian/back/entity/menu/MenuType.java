package ru.tpu.russian.back.entity.menu;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "MENU_TYPE")
public class MenuType {

    @Column(name = "NAME")
    private String name;

    @Id
    @Column(name = "TYPE")
    private String type;
}
