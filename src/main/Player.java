package main;
import java.io.Serializable;
import java.net.InetAddress;

public class Player implements Serializable{
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
	////tester //tester;
	private Sprite sprite;
	boolean f=true;
	int w=0;
	boolean two=false;
	boolean facing=false;

	public Player(String name, InetAddress address, int port) {
		//serverside constructor
		this.name = name;
		this.address = address;
		this.port = port;
		//tester = new //tester(name);

	}

	public Player(String name2) {
		//senders own constructor
		this.name = name2;
		//tester = new //tester(name);
	}

	public Player(String name2, int x, int y) {
		//senders constructor for other
		this.name = name2;
		this.x = x;
		this.y = y;
		//tester = new //tester(name);
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

		if (xacc < 0) {
			xacc++;
		}
		if (xacc > 0) {
			xacc--;
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
		//update //tester
		//tester.jump(jumping);
		//tester.ml(left);
		//tester.mr(right);
		//tester.setx(x);
		//tester.sety(y);
	}
	
	public void view(Game game) {
		w++;
		if (w > 6) {
			w = 0;
			if (two) {
				two = false;
			} else {
				two = true;
			}
		}
		if (pressing && left) {
			if (two) {
				sprite = new Sprite("res/char1/w1l.png");
			} else {
				sprite = new Sprite("res/char1/w2l.png");
			}
		}
		if (pressing && right) {
			if (two) {
				sprite = new Sprite("res/char1/w1.png");
			} else {
				sprite = new Sprite("res/char1/w2.png");
			}
		}
		if (!pressing) {
			if(facing){
			sprite = new Sprite("res/char1/char.png");
			}else{
				sprite = new Sprite("res/char1/charl.png");	
			}
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
		//walk left
		pressing = p;
		left = p;
		facing=false;
	}

	public void wr(boolean p) {
		//walk right
		pressing = p;
		right = p;
		facing=true;
	}

	public void j() {
		//jump
		if (!jumping) {
			yacc = 20;
			jumping = true;
			//tester.jump(jumping);
		}
	}

	public void render(double dt, Game game) {
				sprite = new Sprite("res/char1/char.png");
				f=false;

			sprite.render(x, y);

	}
}
