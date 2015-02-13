package main;

import java.io.Serializable;
import java.net.InetAddress;

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
	boolean fc=true;
	boolean cs=false;

	public Player(String name, InetAddress address, int port, int race, int variation) {
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
		System.out.println(race);
		// senders constructor for self
		this.name = name;
		this.cha = race;
		this.var = variation;
		// tester = new tester(name);
	}

	public void update() {
		if (pressing && left) {
			xacc -= 2;
		}
		if (pressing && right) {
			xacc += 2;
		}
		if (!pressing) {
			xacc = 0;
		}

		if (xacc > 10) {
			xacc = 10;
		}
		if (xacc < -10) {
			xacc = -10;
		}
		yacc--;
		x += xacc;
		y -= yacc;
		if (y > 500) {
			y = 500;
			jumping = false;

		}
		if(x>1480){
			x=-200;
		}
		if(x<-200){
			x=1480;
		}
		// update //tester
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
			cs=true;
			if (two) {
				two = false;
			} else {
				two = true;
			}
		}
		if (pressing && left && !jumping && cs) {
			if (two) {
				sprite = new Sprite("res/char"+cha+"/var"+var+"/w1l.png");
			} else {
				sprite = new Sprite("res/char"+cha+"/var"+var+"/w2l.png");
			}
			fc=true;
			cs=false;
		}
		if (pressing && right && !jumping && cs) {
			if (two) {
				sprite = new Sprite("res/char"+cha+"/var"+var+"/w1.png");
			} else {
				sprite = new Sprite("res/char"+cha+"/var"+var+"/w2.png");
			}
			fc=true;
			cs=false;
		}
		if (jumping) {
			if (yacc < 20 && yacc > 15) {
				if (facing) {
					sprite = new Sprite("res/char"+cha+"/var"+var+"/j1.png");
				} else {
					sprite = new Sprite("res/char"+cha+"/var"+var+"/j1l.png");
				}
			}
			if (yacc == 5) {
				if (facing) {
					sprite = new Sprite("res/char"+cha+"/var"+var+"/j2.png");
				} else {
					sprite = new Sprite("res/char"+cha+"/var"+var+"/j2l.png");
				}
			}
			if (yacc == -5) {
				if (facing) {
					sprite = new Sprite("res/char"+cha+"/var"+var+"/j3.png");
				} else {
					sprite = new Sprite("res/char"+cha+"/var"+var+"/j3l.png");
				}
			}
			fc=true;
		}
		if (!pressing && !jumping && fc) {
			if (facing) {
				sprite = new Sprite("res/char"+cha+"/var"+var+"/char.png");
			} else {
				sprite = new Sprite("res/char"+cha+"/var"+var+"/charl.png");
			}
			fc=false;
		}
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
			yacc = 20;
			jumping = true;
			// tester.jump(jumping);
		}
	}

	public boolean getRight() {
		return right;
	}

	public boolean getLeft() {
		return left;
	}

	public void so(String o) {
		System.out.println(o);
	}

	public void render(double dt, Game game) {
		if (f) {
			sprite = new Sprite("res/char"+cha+"/var"+var+"/char.png");
			f = false;
		}
		sprite.render(x, y);
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
