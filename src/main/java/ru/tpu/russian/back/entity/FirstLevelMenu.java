package ru.tpu.russian.back.entity;

import javax.persistence.*;
import ru.tpu.russian.back.entity.*;

import java.util.*;

public class FirstLevelMenu {

	@Id
	private String id;

	private String nameItem;

	private int position;

	@OneToMany(mappedBy = "firstLevelMenu", cascade = CascadeType.MERGE)
	private List<SecondLevelMenu> sublevels;


}
