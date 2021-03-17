package com.universum.common.util;

/**
 * Application profiles
 * 
 * @author @vanish
 */
public enum AppProfiles {
	SPRING_PROFILE_DEVELOPMENT("dev"), SPRING_PROFILE_TEST("test"), SPRING_PROFILE_PRODUCTION("prod");

	private String name;

	private AppProfiles(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
