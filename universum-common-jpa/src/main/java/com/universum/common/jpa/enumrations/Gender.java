package com.universum.common.jpa.enumrations;

public enum Gender {
	/**
	 * Male.
	 */
	M("Male"),
	
	/**
	 * Female.
	 */
	F("Female"),
	
	/**
	 * Unknown
	 */
	U("Unknown");
	
	private String value;
	private Gender(final String name) {
		this.value = name;
	}
	
	/**
	 * Returns the male gender.
	 * @return a gender.
	 */
	public Gender getMale() {
		return M;
	}
	
	/**
	 * Returns the female gender.
	 * @return a gender.
	 */
	public Gender getFemale() {
		return F;
	}
	
	/**
	 * Returns the unknown gender.
	 * @return a gender.
	 */
	public Gender getUnknown() {
		return U;
	}
	
	public String getValue() {
		return value;
	}
}
