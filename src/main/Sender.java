package main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Vector;

import Map.Map;
import UI.Chat;

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
	// Tester tester;
	String name;
	String myName;
	int health;
	int x = 0;
	int y = 0;
	int xacc = 0;
	int yacc = 0;
	public Sprite sprite;
	int w = 0;
	boolean two = false;
	boolean running = true;;
	boolean facing = false;
	String ip;
	boolean r = true;
	int port = 25565;
	int race;
	int variation;
	Chat chat;
	boolean f = true;
	Map map;
	Boolean mapcreated = false;

	public Sender(Game game, String ip, String name, int var, int race)
			throws InterruptedException {
		this.ip = ip;
		this.myName = name;
		this.variation = var;
		this.race = race;
		sprite = new Sprite("res/char1/var1/i1.png");
		try {
			init(game);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void init(final Game game) throws IOException, InterruptedException {
		chat = new Chat(game);
		s = new DatagramSocket();
		try {
			s("§:" + myName + ":" + race + ":" + variation + ":");
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
					for (int x = players.size() - 1; x >= 0; x--) {
						players.elementAt(x).update();
					}
				}
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				while (running) {
					byte[] buf = new byte[1024];
					DatagramPacket dgp = new DatagramPacket(buf, buf.length);
					try {
						s.setSoTimeout(10000);
						s.receive(dgp);
						r = true;
					} catch (IOException e) {
						r = false;
					}
					if (r) {
						if (f) {
							f = false;
							try {
								s("!:");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						rcvd = new String(dgp.getData());
						rcvd = rcvd.trim();
						String[] Spart = rcvd.split(":");
						String FL = Spart[0];

						so(rcvd);

						generateMap(FL, Spart);
						try {
							name = Spart[1];
						} catch (Exception e) {
							so("Catched " + rcvd);
						}
						found = false;
						for (Player Player : players) {
							if (Player.getName().equals(name)) {
								found = true;
							}
						}
						playeradder(FL, Spart, game);

						if (found) {
							action(FL, Spart);
							mapmovement(FL, Spart);
							chatspeak(FL);
							remover(FL, Spart);
							move(FL, Spart, myName);
							positionUpdater(FL, Spart, myName);
							MapPosition(FL, Spart);
						}
					}
				}
			}
		}).start();
	}

	public void action(String FL, String[] Spart) {
		if (FL.equals("A")) {
			if(Spart[2].equals("^")){
				if(Spart[3].equals("P")){
				getPlayer(name).AU(true);
				}else{
					getPlayer(name).AU(false);
				}
			}
		}
	}

	public void generateMap(String FL, String[] Spart) {
		if (FL.equals("£")) {
			int[] parts = new int[Spart.length - 3];
			for (int i = 0; i < Spart.length - 3; i++) {
				parts[i] = Integer.parseInt(Spart[3 + i]);
			}
			map = new Map(Integer.parseInt(Spart[1]),
					Integer.parseInt(Spart[2]), parts);
			mapcreated = true;
		}
	}

	public void appendChat(String txt) {
		txt = txt.replace("¤:", "");
		chat.ap(txt);
	}

	public void chatspeak(String FL) {
		if (FL.equals("¤")) {
			rcvd.replace("¤:", "");
			appendChat(rcvd);
			rcvd = rcvd.replace("¤:" + name + ": ", "");
			getPlayer(name).setText(rcvd);
		}
	}

	public void playeradder(String FL, String[] Spart, Game game) {
		if (!found) {

			if (FL.equals("§")) {
				try {
					players.add(new Player(game, name, Integer
							.parseInt(Spart[2]), Integer.parseInt(Spart[3]),
							Integer.parseInt(Spart[4]), Integer
									.parseInt(Spart[5])));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void MapPosition(String FL, String[] Spart) {
		if (FL.equals("MP")) {
			for (Player player : players) {
				if (name.equals(player.getName())) {
					player.setMx(Integer.parseInt(Spart[2]));
					player.setMy(Integer.parseInt(Spart[3]));
				}
			}
		}
	}

	public void positionUpdater(String FL, String[] Spart, String ns) {
		if (FL.equals("@")) {
			int siX = Integer.parseInt(Spart[2]);
			int siY = Integer.parseInt(Spart[3]);
			for (Player Player : players) {
				if (Player.getName().equals(Spart[1])) {
					setXY(Player, siX, siY);
					setMXMY(Player, Integer.parseInt(Spart[4]),
							Integer.parseInt(Spart[5]));
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

	public void mapmovement(String FL, String[] Spart) {
		if (FL.equals("MM")) {
			for (Player player : players) {
				if (player.getName().equals(name)) {
					if (Spart[2].equals("^")) {
						player.MM(0, -1, map.getWW(), map.getWH());
					}
					if (Spart[2].equals("<")) {
						player.MM(-1, 0, map.getWW(), map.getWH());
					}
					if (Spart[2].equals("v")) {
						player.MM(0, 1, map.getWW(), map.getWH());
					}
					if (Spart[2].equals(">")) {
						player.MM(1, 0, map.getWW(), map.getWH());
					}
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
					setXY(getPlayer(name), siX, siY);
				}
			}
			try {
				if (Spart[2].equals("<")) {
					getPlayer(name).wl(pressing);
				}
				if (Spart[2].equals(">")) {
					getPlayer(name).wr(pressing);
				}
				if (Spart[2].equals("^")) {
					getPlayer(name).j();
				}
			} catch (Exception e) {
				so("Tried to apply illegal operation to a unknown player");
			}
		}
	}

	public void setXY(Player p, int x, int y) {
		p.setX(x);
		p.setY(y);
	}

	public void setMXMY(Player p, int mx, int my) {
		p.setMx(mx);
		p.setMy(my);
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
	}

	public void renderonMap(double dt, Game game, int xoffset, int yoffset) {
		for (int x = players.size() - 1; x >= 0; x--) {
			players.elementAt(x).render(dt, game, xoffset, yoffset,
					map.getWW(), map.getWH(), map.getXvar(), map.getYvar());
		}
	}

	public void render(double dt, Game game) {
		try {
			for (int x = players.size() - 1; x >= 0; x--) {
				if (SafegetPlayer(myName).getMx() == players.elementAt(x)
						.getMx()
						&& SafegetPlayer(myName).getMy() == players
								.elementAt(x).getMy()) {
					players.elementAt(x).render(dt, game);
				}
			}
		} catch (Exception e) {

		}
	}

	public void so(String o) {
		System.out.println(o);
	}

	public void s(String msg) throws IOException {
		// send function
		byte[] buf = new byte[1024];
		InetAddress hostAddress = InetAddress.getByName(ip);
		// so(ip + " " + port);
		buf = msg.getBytes();
		DatagramPacket out = new DatagramPacket(buf, buf.length, hostAddress,
				port);
		s.send(out);
	}

	public void stop() throws IOException {
		running = false;
		s("#:");
	}

	public boolean getMapcreated() {
		return mapcreated;
	}
}
