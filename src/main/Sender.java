package main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.Vector;

public class Sender {
	String msg;
	public String rcvd;
	private DatagramSocket s;
	boolean c = false;
	public Vector<Player> players = new Vector<Player>();
	boolean found;
	boolean pressing = false;
	boolean left;
	boolean right;
	boolean jumping = false;
	Tester tester;
	String name;
	int health;
	int x = 0;
	int y = 0;
	int xacc = 0;
	int yacc = 0;
	public Sprite sprite;
	int w = 0;
	boolean two = false;
	boolean running = true;
	boolean facing = false;
	String ip;
	boolean r = true;
	int port = 25565;
	Player thisplayer;

	public Sender(Game game, String ip) {
		sprite = new Sprite("res/char1/char.png");
		this.ip = ip;
		try {
			init(game);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init(Game game) throws IOException {

		s = new DatagramSocket();
		Random rand = new Random();
		int n = rand.nextInt(1000);
		final String ns = Integer.toString(n);
		thisplayer = new Player(ns);
		// so(ns);
		try {
			s("§:" + n + ":");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		new Thread(new Runnable() {
			public void run() {
				while (running) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for (Player Player : players) {
						Player.update();
					}
					thisplayer.update();
				}
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				while (running) {
					byte[] buf = new byte[32];
					DatagramPacket dgp = new DatagramPacket(buf, buf.length);
					try {
						s.setSoTimeout(10000);
						s.receive(dgp);
						r = true;
					} catch (IOException e) {
						r = false;
					}
					
					if (r) {
						rcvd = new String(dgp.getData());
						rcvd = rcvd.trim();
						String[] Spart = rcvd.split(":");
						String FL = Spart[0];
						// so(rcvd);
						try{
						name = Spart[1];
						}catch(Exception e){
							so(rcvd);
						}
						found = false;
						for (Player Player : players) {
							if (Player.getName().equals(name)) {
								// player search
								found = true;
							}
						}
						remover(FL, Spart);
						if (!found && !ns.equals(name)) {

							if (FL.equals("§")) {
								// add new player to player list
								players.add(new Player(name, Integer
										.parseInt(Spart[2]), Integer
										.parseInt(Spart[3])));
							}
						}
						move(FL, Spart, ns);
						positionUpdater(FL, Spart, ns);
					}
				}
			}
		}).start();
	}

	public void positionUpdater(String FL, String[] Spart, String ns) {
		if (FL.equals("@")) {
			int siX = Integer.parseInt(Spart[2]);
			int siY = Integer.parseInt(Spart[3]);
			if (Spart[1].equals(ns)) {
				setXY(thisplayer, siX, siY);
			} else {
				for (Player Player : players) {
					if (Player.getName().equals(Spart[1])) {
						setXY(Player, siX, siY);
					}
				}
			}
		}
	}

	public void remover(String FL, String[] Spart) {
		if (FL.equals("#")) {
			so("attempting to remove");
			players.remove(SafegetPlayer(name));
		}
	}

	public void move(String FL, String[] Spart, String ns) {
		if (FL.equals("$")) {
			int siX = 0;
			int siY = 0;
			// command datagram
			boolean pressing = false;
			if (!Spart[2].equals("^")) {
				if (Spart[3].equals("P")) {
					pressing = true;
				} else {
					siX = Integer.parseInt(Spart[4]);
					siY = Integer.parseInt(Spart[5]);
					if (!Spart[1].equals(ns)) {
						setXY(getPlayer(name), siX, siY);
					} else {
						setXY(thisplayer, siX, siY);
					}
				}
			}
			if (Spart[1].equals(ns)) {
				if (Spart[2].equals("<")) {
					thisplayer.wl(pressing);
				}
				if (Spart[2].equals(">")) {
					thisplayer.wr(pressing);
				}
				if (Spart[2].equals("^")) {
					thisplayer.j();
				}
			} else {
				if (Spart[2].equals("<")) {
					getPlayer(name).wl(pressing);
				}
				if (Spart[2].equals(">")) {
					getPlayer(name).wr(pressing);
				}
				if (Spart[2].equals("^")) {
					getPlayer(name).j();
				}
			}
		}
	}

	public void setXY(Player p, int x, int y) {
		p.setX(x);
		p.setY(y);
	}

	public Player getPlayer(String name) {
		Object o = null;
		for (Player Player : players) {
			if (Player.getName().equals(name)) {
				o = Player;
			}
		}
		return (Player) o;
	}

	public Player SafegetPlayer(String name) {
		Object o = null;
		for (int x = players.size() - 1; x >= 0; x--) {
			if (players.elementAt(x).getName().equals(name)) {
				o = players.elementAt(x);
			}
		}
		return (Player) o;
	}

	public void view(Game game) {
		for (int x = players.size() - 1; x >= 0; x--) {
			players.elementAt(x).view(game);
		}
		thisplayer.view(game);
	}

	public void render(double dt, Game game) {
		for (int x = players.size() - 1; x >= 0; x--) {
			players.elementAt(x).render(dt, game);
		}
		thisplayer.render(dt, game);
	}

	public void so(String o) {
		System.out.println(o);
	}

	public void s(String msg) throws IOException {
		// send function
		byte[] buf = new byte[32];
		InetAddress hostAddress = InetAddress.getByName(ip);
		//so(ip + " " + port);
		buf = msg.getBytes();
		DatagramPacket out = new DatagramPacket(buf, buf.length, hostAddress,
				port);
		s.send(out);
	}

	public void stop() throws IOException {
		running = false;
		s("#:");
	}
}
