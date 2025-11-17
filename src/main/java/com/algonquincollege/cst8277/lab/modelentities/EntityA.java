package com.algonquincollege.cst8277.lab.modelentities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@SuppressWarnings("unused")

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityA extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public EntityA() {
		super();
	}

}