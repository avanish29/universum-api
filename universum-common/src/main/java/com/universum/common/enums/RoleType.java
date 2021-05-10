package com.universum.common.enums;

public enum RoleType {
	ROLE_ADMIN("ROLE_ADMIN"), 
	ROLE_USER("ROLE_USER");

	private final String name;

	public String getName() {
		return name;
	}

	private RoleType(String name) {
		this.name = name;
	}
}
