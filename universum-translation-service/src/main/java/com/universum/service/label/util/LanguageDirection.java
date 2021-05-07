package com.universum.service.label.util;

public enum LanguageDirection {
	LTR("Left-to-Right"),
	RTL("Right-to-Left");
	
	private String label;
	private LanguageDirection(final String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public static LanguageDirection defaultDirection() {
		return LanguageDirection.LTR;
	}
}
