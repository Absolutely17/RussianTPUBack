package ru.tpu.russian.back.entity;

import javax.persistence.*;

public class SecondLevelMenu {

	@Id
	private String id;

	private int position;

	private String nameItem;

	private String url;
}
