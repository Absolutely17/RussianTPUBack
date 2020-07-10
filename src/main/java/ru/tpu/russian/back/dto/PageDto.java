package ru.tpu.russian.back.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.*;
import java.util.*;

public class PageDto<T> implements Serializable {

    @ApiModelProperty(value = "Количество статей")
	private Long count;

    @ApiModelProperty(value = "Список статей")
	private List<T> list;

	public PageDto(Long count, List<T> list) {
		this.count = count;
		this.list = list;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}
