package main;

import java.io.Serializable;

class Positions implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int x;
	int y;
	String name;

	public Positions(int x, int y, String name) {
		this.x = x;
		this.y = y;
	}
}
