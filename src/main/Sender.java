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

	public Sender(Game game) {
		sprite = new Sprite("res/char1/char.png");
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
		// so(ns);
		try {
			s("§:" + n + ":");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for (Player Player : players) {
						Player.update();
					}
					update(game);
				}
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					byte[] buf = new byte[1024];
					DatagramPacket dgp = new DatagramPacket(buf, buf.length);
					try {
						s.receive(dgp);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
					if (!found && !ns.equals(name)) {
						if (FL.equals("§")) {
							// add new player to player list
							players.add(new Player(name, Integer
									.parseInt(Spart[2]), Integer
									.parseInt(Spart[3])));
							// so("New client with name " + name + " created");
						} else {
							// join fail error message, probably due to wrong
							// message
							// so("received invalid connection package");
							// so(rcvd);
						}

						// move commands from client marked with $
					} else {
						if (FL.equals("$")) {
							// command datagram
							boolean pressing = false;
							if (!Spart[2].equals("^")) {
								if (Spart[3].equals("P")) {
									pressing = true;
								}
							}
							if (Spart[2].equals("<")) {
								if (pressing) {
									if (Spart[1].equals(ns)) {
										wl(pressing);
									} else {
										for (Player Player : players) {
											if (Player.getName().equals(
													Spart[1])) {
												Player.wl(pressing);
											}
										}
									}
								} else {
									if (Spart[1].equals(ns)) {
										wl(pressing);
										x = Integer.parseInt(Spart[4]);
										y = Integer.parseInt(Spart[5]);
									} else {
										for (Player Player : players) {
											if (Player.getName().equals(
													Spart[1])) {
												Player.wl(pressing);
												Player.setX(Integer
														.parseInt(Spart[4]));
												Player.setY(Integer
														.parseInt(Spart[5]));
											}
										}
									}
								}
							}
							if (Spart[2].equals(">")) {
								if (pressing) {
									if (Spart[1].equals(ns)) {
										wr(pressing);
									} else {
										for (Player Player : players) {
											if (Player.getName().equals(
													Spart[1])) {
												Player.wr(pressing);
											}
										}
									}
								} else {
									if (Spart[1].equals(ns)) {
										wr(pressing);
										x = Integer.parseInt(Spart[4]);
										y = Integer.parseInt(Spart[5]);
									} else {
										for (Player Player : players) {
											if (Player.getName().equals(
													Spart[1])) {
												Player.wr(pressing);
												Player.setX(Integer
														.parseInt(Spart[4]));
												Player.setY(Integer
														.parseInt(Spart[5]));
											}
										}
									}
								}
							}
							if (Spart[2].equals("^")) {
								if (Spart[1].equals(ns)) {
									j();
								} else {
									for (Player Player : players) {
										if (Player.getName().equals(Spart[1])) {
											Player.j();
										}
									}
								}
							}
						} else {
							// so("received invalid command package:");
							// so(rcvd);
						}
					}
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
			}
		}).start();
	}

	public void update(Game game) {
		// movement updater
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
	}

	public void view(Game game) {
		w++;
		if (w > 10) {
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
			sprite = new Sprite("res/char1/char.png");
		}
	}

	public void render(double dt, Game game) {
		for (Player Player : players) {
			Player.render(dt, game);
		}
		sprite.render(x, y);
	}

	public void so(String o) {
		System.out.println(o);
	}

	public void s(String msg) throws IOException {
		// send function
		String ip = "25.5.72.222";
		byte[] buf = new byte[1024];
		InetAddress hostAddress = InetAddress.getByName(ip);
		buf = msg.getBytes();
		DatagramPacket out = new DatagramPacket(buf, buf.length, hostAddress,
				25565);
		s.send(out);
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
			yacc = 20;
			jumping = true;
		}
	}
}
