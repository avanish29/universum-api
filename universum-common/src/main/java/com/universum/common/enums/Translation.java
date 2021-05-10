package com.universum.common.enums;

public enum Translation {
	/**
	 * Original.
	 */
	O("Original"),
	
	/**
	 * Human.
	 */
	H("Human"),
	
	/**
	 * Machine.
	 */
	M("Machine");
	
	private String value;
	private Translation(final String name) {
		this.value = name;
	}
	
	/**
	 * Returns the original translation.
	 * @return a translation.
	 */
	public Translation getOriginal() {
		return O;
	}
	
	/**
	 * Returns the human translation.
	 * @return a translation.
	 */
	public Translation getHuman() {
		return H;
	}
	
	/**
	 * Returns the machine translation.
	 * @return a translation.
	 */
	public Translation getMachine() {
		return M;
	}
	
	public String getValue() {
		return value;
	}
}
