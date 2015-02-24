package main;

import java.awt.Font;
import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;

import main.Game.State;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;

public class Player implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6508883591128752232L;
	public String name;
	public int health;
	public int x = 0;
	public int y = 0;
	public int xacc = 0;
	public int yacc = 0;
	public InetAddress address;
	public int port;
	public boolean Rpressing = false;
	public boolean Lpressing = false;
	public boolean left;
	public boolean right;
	public boolean jumping = false;
	// tester tester;
	private Sprite sprite;
	public boolean f = true;
	public int cha;
	public int var;
	public TrueTypeFont font;
	public int movespeed = 10;
	public int jumpspeed = 20;
	public String text;
	boolean newText = false;
	public int textrender;
	public Sprite bubble;
	public int jumpani = 0;
	public int walkani = 0;
	public int idleani = 0;
	public int[] animations = new int[3];
	public lookstate LS;
	public String oldsprite;
	public int step;
	public int w;
	public int j;
	public int i;
	public int ws = 1;
	public int js = 1;
	public int is = 1;

	public enum lookstate {
		RIGHT, LEFT;
	}

	public Player(String name, InetAddress address, int port, int race,
			int variation) {
		// serverside constructor
		this.name = name;
		this.address = address;
		this.port = port;
		this.cha = race;
		this.var = variation;
		// tester = new tester(name);
		LS = lookstate.RIGHT;
	}

	public Player(Game game, String name, int x, int y, int race, int variation) {
		// senders constructor for other
		this.name = name;
		this.x = x;
		this.y = y;
		this.cha = race;
		this.var = variation;
		// tester = new tester(name);
		animations(race, variation);
		LS = lookstate.RIGHT;
	}

	public Player(String name, int race, int variation) {
		// senders constructor for self
		this.name = name;
		this.cha = race;
		this.var = variation;
		// tester = new tester(name);
		animations(race, variation);
		LS = lookstate.RIGHT;
	}

	public void animations(int race, int variation) {
		int num = 1;
		int chaint = 0;
		String[] s = { "w", "j", "i" };
		while (true) {
			File f = new File("res/char" + race + "/var" + variation + "/"
					+ s[chaint] + num + ".png");
			if (f.exists()) {
				animations[chaint] = num;
				num++;
			} else {
				if (chaint < 2) {
					chaint++;
				} else {
					break;
				}
			}
		}
		so("walk animations: " + animations[0]);
		so("jump animations: " + animations[1]);
		so("idle animations: " + animations[2]);
		w = movespeed / animations[0];
		j = jumpspeed * 2 / animations[1];
		// i = jumpspeed / animations[2];
	}

	public void update() {
		if (Lpressing && left) {
			xacc -= movespeed / 5;
		}
		if (Rpressing && right) {
			xacc += movespeed / 5;
		}
		if (!Rpressing && !Lpressing) {
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
			yacc = 0;
		}
		if (x > 1480) {
			x = -200;
		}
		if (x < -200) {
			x = 1480;
		}
		textrender++;
		if (xacc < 0) {
			LS = lookstate.LEFT;
		}
		if (xacc > 0) {
			LS = lookstate.RIGHT;
		}
		// update tester
		// tester.jump(jumping);
		// tester.ml(left);
		// tester.mr(right);
		// tester.setx(x);
		// tester.sety(y);
	}

	public void view(Game game) {
		step++;
		String spritestring = "res/char" + cha + "/var" + var + "/";
		if (LS.equals(lookstate.LEFT)) {
			if (jumping) {
				for (int i = 0; i < animations[1]; i++) {
					if (yacc <= jumpspeed - (j * i)) {
						spritestring = "res/char" + cha + "/var" + var + "/j"
								+ (i + 1);
					}
				}
			} else if (xacc == 0 && !jumping && yacc == 0) {
				spritestring += "char";
			}
			spritestring += "l.png";
		}
		if (LS.equals(lookstate.RIGHT)) {
			if (jumping) {
				for (int i = 0; i < animations[1]; i++) {
					if (yacc <= jumpspeed - (j * i)) {
						spritestring = "res/char" + cha + "/var" + var + "/j"
								+ (i + 1);
					}
				}
			} else if (xacc == 0 && !jumping && yacc == 0) {
				spritestring += "char";
			}
			spritestring += ".png";
		}
		if (!spritestring.equals(oldsprite)
				&& !spritestring.equals("res/char" + cha + "/var" + var
						+ "/.png")
				&& !spritestring.equals("res/char" + cha + "/var" + var
						+ "/l.png")) {
			oldsprite = spritestring;
			sprite = new Sprite(spritestring);
		}
	}

	public void wl(boolean p) {
		// walk left
		Lpressing = p;
		left = p;
	}

	public void wr(boolean p) {
		// walk right
		Rpressing = p;
		right = p;
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
			bubble.render(x - 5 + (150 - longestString) / 2, (y - 30)
					- (line * 20), longestString + 5, ((line + 1) * 20));
			for (int i = 0; i < line + 1; i++) {
				font.drawString(x + (150 - font.getWidth(newtext[i])) / 2,
						(y - 30) - ((line - i) * 20), newtext[i], Color.black);
			}
			if (textrender > (line + 1) * 180 && textrender > 180
					|| textrender > 600) {
				newText = false;
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
}
