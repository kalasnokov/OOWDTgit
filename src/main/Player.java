package main;

import java.awt.Font;
import java.io.Serializable;
import java.net.InetAddress;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;

public class Player implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6508883591128752232L;
	String name;
	int health;
	int x = 0;
	int y = 0;
	int xacc = 0;
	int yacc = 0;
	InetAddress address;
	int port;
	boolean pressing = false;
	boolean left;
	boolean right;
	boolean jumping = false;
	// tester tester;
	private Sprite sprite;
	boolean f = true;
	int w = 0;
	boolean two = false;
	boolean facing = false;
	int cha;
	int var;
	boolean fc = true;
	boolean cs = false;
	TrueTypeFont font;
	int movespeed = 10;
	int jumpspeed = 20;
	private String text;
	boolean newText = false;
	int textrender;
	int FPS;
	TextField t;
	private Sprite bubble;

	public Player(String name, InetAddress address, int port, int race,
			int variation) {
		// serverside constructor
		this.name = name;
		this.address = address;
		this.port = port;
		this.cha = race;
		this.var = variation;
		// tester = new tester(name);
	}

	public Player(String name, int x, int y, int race, int variation) {
		// senders constructor for other
		this.name = name;
		this.x = x;
		this.y = y;
		this.cha = race;
		this.var = variation;
		// tester = new tester(name);
	}

	public Player(String name, int race, int variation) {
		// senders constructor for self
		this.name = name;
		this.cha = race;
		this.var = variation;
		// tester = new tester(name);
	}

	public void update() {
		if (pressing && left) {
			xacc -= movespeed / 5;
		}
		if (pressing && right) {
			xacc += movespeed / 5;
		}
		if (!pressing) {
			xacc = 0;
		}

		if (xacc > movespeed) {
			xacc = movespeed;
		}
		if (xacc < -movespeed) {
			xacc = -movespeed;
		}
		yacc--;
		x += xacc;
		y -= yacc;
		if (y > 500) {
			y = 500;
			jumping = false;

		}
		if (x > 1480) {
			x = -200;
		}
		if (x < -200) {
			x = 1480;
		}
		textrender++;
		// update tester
		// tester.jump(jumping);
		// tester.ml(left);
		// tester.mr(right);
		// tester.setx(x);
		// tester.sety(y);
	}

	public void view(Game game) {
		w++;
		if (w > 6) {
			w = 0;
			cs = true;
			if (two) {
				two = false;
			} else {
				two = true;
			}
		}
		if (pressing && left && !jumping && cs) {
			if (two) {
				sprite = new Sprite("res/char" + cha + "/var" + var
						+ "/w1l.png");
			} else {
				sprite = new Sprite("res/char" + cha + "/var" + var
						+ "/w2l.png");
			}
			fc = true;
			cs = false;
		}
		if (pressing && right && !jumping && cs) {
			if (two) {
				sprite = new Sprite("res/char" + cha + "/var" + var + "/w1.png");
			} else {
				sprite = new Sprite("res/char" + cha + "/var" + var + "/w2.png");
			}
			fc = true;
			cs = false;
		}
		if (jumping) {
			if (yacc < jumpspeed && yacc > 3 * (jumpspeed / 4)) {
				if (facing) {
					sprite = new Sprite("res/char" + cha + "/var" + var
							+ "/j1.png");
				} else {
					sprite = new Sprite("res/char" + cha + "/var" + var
							+ "/j1l.png");
				}
			}
			if (yacc == (jumpspeed / 4)) {
				if (facing) {
					sprite = new Sprite("res/char" + cha + "/var" + var
							+ "/j2.png");
				} else {
					sprite = new Sprite("res/char" + cha + "/var" + var
							+ "/j2l.png");
				}
			}
			if (yacc == -(jumpspeed / 4)) {
				if (facing) {
					sprite = new Sprite("res/char" + cha + "/var" + var
							+ "/j3.png");
				} else {
					sprite = new Sprite("res/char" + cha + "/var" + var
							+ "/j3l.png");
				}
			}
			fc = true;
		}
		if (!pressing && !jumping && fc) {
			if (facing) {
				sprite = new Sprite("res/char" + cha + "/var" + var
						+ "/char.png");
			} else {
				sprite = new Sprite("res/char" + cha + "/var" + var
						+ "/charl.png");
			}
			fc = false;
		}
	}

	public void wl(boolean p) {
		// walk left
		pressing = p;
		left = p;
		facing = false;
	}

	public void wr(boolean p) {
		// walk right
		pressing = p;
		right = p;
		facing = true;
	}

	public void j() {
		// jump
		if (!jumping) {
			yacc = jumpspeed;
			jumping = true;
			// tester.jump(jumping);
		}
	}

	public void TR(String txt, Game game) {
		font.drawString(x, y, txt, Color.black);
	}

	public void setFont(String Sfont, int size) {
		Font awtFont = new Font(Sfont, Font.CENTER_BASELINE, size);
		font = new TrueTypeFont(awtFont, false);
	}

	public void so(String o) {
		System.out.println(o);
	}

	public void render(double dt, Game game) {
		if (f) {
			setFont("Verdana", 16);
			sprite = new Sprite("res/char" + cha + "/var" + var + "/char.png");
			bubble = new Sprite("res/Bubble.png");
			f = false;
		}
		if (newText) {
			text.trim();
			String[] st = text.split(" ");
			String[] newtext = new String[1000];
			int line = 0;
			int longestString = 0;
			newtext[0] = "";
			for (int i = 0; i < st.length; i++) {
				newtext[line] += st[i] + " ";
				if (font.getWidth(newtext[line]) > longestString) {
					longestString = font.getWidth(newtext[line]);
				}
				if (font.getWidth(newtext[line]) > 150 && i != st.length - 1) {
					line++;
					newtext[line] = "";
				}
			}
			bubble.render(x-5 + (150 - longestString) / 2, (y-30) - (line * 20),
					longestString+5, ((line + 1) * 20));
			for (int i = 0; i < line + 1; i++) {
				font.drawString(x + (150 - font.getWidth(newtext[i])) / 2,
						(y - 30) - ((line - i) * 20), newtext[i], Color.black);
			}
			if(textrender>(line+1)*180 && textrender>180 || textrender>600){
				newText=false;
			}
		}

		sprite.render(x, y);
	}

	public void setText(String text) {
		this.text = text;
		newText = true;
		textrender = 0;
		text.length();
	}

	public boolean getRight() {
		return right;
	}

	public boolean getLeft() {
		return left;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean getJumping() {
		return jumping;
	}

	public int getCha() {
		return cha;
	}

	public void setCha(int cha) {
		this.cha = cha;
	}

	public int getVariation() {
		return var;
	}

	public void setVariation(int variation) {
		this.var = variation;
	}

	public void setFPS(int FPS) {
		this.FPS = FPS;

	}
}
