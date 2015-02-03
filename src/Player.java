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
	Tester tester;

	public Player(String name, InetAddress address, int port) {
		this.name = name;
		this.address = address;
		this.port = port;
		tester = new Tester(name);

	}

	public Player(String name2) {
		this.name = name2;
		tester = new Tester(name);
	}

	public Player(String name2, int x, int y) {
		this.name = name2;
		this.x = x;
		this.y = y;
		tester = new Tester(name);
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
		if (y > 750) {
			y = 750;
			jumping = false;

		}
		tester.jump(jumping);
		tester.ml(left);
		tester.mr(right);
		tester.setx(x);
		tester.sety(y);
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
		pressing = p;
		left = p;
	}

	public void wr(boolean p) {
		pressing = p;
		right = p;
	}

	public void j() {
		if (!jumping) {
			yacc = 20;
			jumping = true;
			tester.jump(jumping);
		}
	}
}
