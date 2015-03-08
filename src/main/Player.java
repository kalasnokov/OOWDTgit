package main;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;

import javax.imageio.ImageIO;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

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
	public boolean pressing = false;
	public boolean left;
	public boolean right;
	public boolean jumping = false;
	// Tester tester;
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
	public int idlestage = 1;
	public int idletick;

	public enum lookstate {
		RIGHT, LEFT;
	}

	public Player(String name, InetAddress address, int port, int race,
			int variation) throws IOException {
		// serverside constructor
		this.name = name;
		this.address = address;
		this.port = port;
		this.cha = race;
		this.var = variation;
		// tester = new Tester(name);
		LS = lookstate.RIGHT;
		loadOPT();
	}

	public Player(Game game, String name, int x, int y, int race, int variation)
			throws IOException {
		// senders constructor for other
		readandsend("res/i1.png");
		this.name = name;
		this.x = x;
		this.y = y;
		this.cha = race;
		this.var = variation;
		// tester = new Tester(name);
		animations(race, variation);
		LS = lookstate.RIGHT;
		loadOPT();
	}

	public Player(String name, int race, int variation) throws IOException {
		// senders constructor for self
		this.name = name;
		this.cha = race;
		this.var = variation;
		// tester = new Tester(name);
		animations(race, variation);
		LS = lookstate.RIGHT;
		loadOPT();
	}

	public void readandsend(String s) throws FileNotFoundException, IOException {
		File f = new File(s);
		if (f.exists() && !f.isDirectory()) {

			BufferedImage image = ImageIO.read(new File("res/i1.png"));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			String encodedImage = Base64.encode(baos.toByteArray());

			f = new File("res/i2.png");
			f.createNewFile();

			FileOutputStream osf = new FileOutputStream(f);
			byte[] btDataFile = new sun.misc.BASE64Decoder()
					.decodeBuffer(encodedImage);
			osf.write(btDataFile);
			osf.flush();
			osf.close();

		} else {
			// error
		}
	}

	public void loadOPT() throws IOException {
		File f = new File("res/char" + cha + "/var" + var + "/opt.cvcf");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		int i = 1;
		while ((line = br.readLine()) != null) {
			String[] split = line.split(":");
			System.out.println(line);
			if (i == 1) {
				movespeed = Integer.parseInt(split[1]);
			}
			if (i == 2) {
				jumpspeed = Integer.parseInt(split[1]);
			}
			if (i == 3) {
				idletick = Integer.parseInt(split[1]);
			}
			i++;
		}
		br.close();
	}

	public void animations(int race, int variation) {
		int num = 1;
		int chaint = 0;
		String[] s = { "w", "j", "i" };
		while (true) {
			File f = new File("res/char" + race + "/var" + variation + "/"
					+ s[chaint] + num + ".png");
			so(f.getPath().toString());
			if (f.exists()) {
				animations[chaint] = num;
				num++;
			} else {
				if (chaint < 2) {
					chaint++;
					num = 1;
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
		if (left) {
			xacc -= movespeed / 5;
		}
		if (right) {
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

		if (jumping) {
			for (int i = 0; i < animations[1]; i++) {
				if (yacc <= jumpspeed - (j * i)) {
					spritestring = "res/char" + cha + "/var" + var + "/j"
							+ (i + 1);
				}
			}
		} else if (pressing) {
			spritestring += "/w" + ws;
			if (step >= 7) {
				ws++;
				step = 0;
				if (ws > animations[0]) {
					ws = 1;
				}
			}
		} else {
			if (step >= idletick) {
				spritestring += "i" + idlestage;
				idlestage++;
				step = 0;
				if (idlestage > animations[2]) {
					idlestage = 1;
				}
			}
		}
		spritestring += ".png";

		if (!spritestring.equals(oldsprite)
				&& !spritestring.equals("res/char" + cha + "/var" + var
						+ "/.png")
				&& !spritestring.equals("res/char" + cha + "/var" + var
						+ "/l.png")) {

			try {
				sprite = new Sprite(spritestring);
			} catch (Exception e) {
				sprite = new Sprite(oldsprite);
				so("Sprite error catched");
			}
			oldsprite = spritestring;
		}
	}

	public void wl(boolean p) {
		// walk left
		pressing = p;
		left = p;
	}

	public void wr(boolean p) {
		// walk right
		pressing = p;
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
			sprite = new Sprite("res/char" + cha + "/var" + var + "/i1.png");
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
		boolean l;
		if (LS.equals(lookstate.RIGHT)) {
			l = false;
		} else {
			l = true;
		}
		sprite.render(x, y, l);
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
