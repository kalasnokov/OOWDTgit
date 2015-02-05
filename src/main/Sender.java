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
		String ns = Integer.toString(n);
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
					byte[] buf = new byte[1024];
					DatagramPacket dgp = new DatagramPacket(buf, buf.length);
					try {
						s.setSoTimeout(1000);
						s.receive(dgp);
						r = true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						r = false;
					}
					if (r) {
						rcvd = new String(dgp.getData());
						rcvd = rcvd.trim();
						String[] Spart = rcvd.split(":");
						String FL = Spart[0];
						// so(rcvd);
						String name;
						name = Spart[1];
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
								// so("New client with name " + name +
								// " created");
							} else {
								// join fail error message, probably due to
								// wrong
								// message
								// so("received invalid connection package");
								// so(rcvd);
							}

							// move commands from client marked with $
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
			if (Spart[1].equals(ns)) {
				x = Integer.parseInt(Spart[2]);
				y = Integer.parseInt(Spart[3]);
			} else {
				for (Player Player : players) {
					if (Player.getName().equals(Spart[1])) {
						Player.setX(Integer.parseInt(Spart[2]));
						Player.setY(Integer.parseInt(Spart[3]));
					}
				}
			}
		}
	}

	public void remover(String FL, String[] Spart) {
		if (FL.equals("#")) {
			for (int x = players.size() - 1; x >= 0; x--) {
				if (players.elementAt(x).getName().equals(Spart[1])) {
					players.remove(players.elementAt(x));
				}
			}
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
				}
			}
			if (Spart[2].equals("<")) {
				if (pressing) {
					if (Spart[1].equals(ns)) {
						thisplayer.wl(pressing);
					} else {
						for (Player Player : players) {
							if (Player.getName().equals(Spart[1])) {
								Player.wl(pressing);
							}
						}
					}
				} else {
					if (Spart[1].equals(ns)) {
						thisplayer.wl(pressing);
						setXY(thisplayer, siX, siY);
					} else {
						for (Player Player : players) {
							if (Player.getName().equals(Spart[1])) {
								Player.wl(pressing);
								setXY(Player, siX, siY);
							}
						}
					}
				}
			}
			if (Spart[2].equals(">")) {
				if (pressing) {
					if (Spart[1].equals(ns)) {
						thisplayer.wr(pressing);
					} else {
						for (Player Player : players) {
							if (Player.getName().equals(Spart[1])) {
								Player.wr(pressing);
							}
						}
					}
				} else {
					if (Spart[1].equals(ns)) {
						thisplayer.wr(pressing);
						setXY(thisplayer, siX, siY);
					} else {
						for (Player Player : players) {
							if (Player.getName().equals(Spart[1])) {
								Player.wr(pressing);
								setXY(Player, siX, siY);
							}
						}
					}
				}
			}
			if (Spart[2].equals("^")) {
				if (Spart[1].equals(ns)) {
					thisplayer.j();
				} else {
					for (Player Player : players) {
						if (Player.getName().equals(Spart[1])) {
							Player.j();
						}
					}
				}
			}
		}
	}

	public void setXY(Player p, int x, int y) {
		p.setX(x);
		p.setY(y);
	}

	public void view(Game game) {
		for (Player Player : players) {
			Player.view(game);
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
		byte[] buf = new byte[1024];
		InetAddress hostAddress = InetAddress.getByName(ip);
		buf = msg.getBytes();
		DatagramPacket out = new DatagramPacket(buf, buf.length, hostAddress,
				25565);
		s.send(out);
	}

	public void stop() throws IOException {
		running = false;
		s("#:");
	}
}
