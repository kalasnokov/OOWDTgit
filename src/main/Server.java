package main;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import Map.Map;

@SuppressWarnings("unused")
public class Server extends JFrame implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1960888810093318280L;
	private static final JTextArea Log = new JTextArea();
	private static final JTextPane Serverinf = new JTextPane();
	private boolean found;
	public Vector<Player> players = new Vector<Player>();
	private String msg;
	DatagramSocket s;
	private JTextField input;
	private boolean broken = false;
	boolean ready = false;
	Map map;
	DatagramPacket dgp;

	public Server() throws IOException {

		// UI and shit starts here
		super("OOWDT Server");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception error) {
		}
		setLocation(500, 100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// buttons and textfields
		JScrollPane scrollPane = new JScrollPane((Component) null);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		input = new JTextField();
		input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					String Sinput = input.getText().toLowerCase();
					String fl = Sinput.substring(0, 1);
					if (fl.equals("/")) {
						ap(Sinput);
						if (Sinput.contains("/remove ")) {
							Sinput = Sinput.replace("/remove ", "");
							input.setText("");
							ap("Removed player " + Sinput);
							try {
								removePlayer(Sinput);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						if (Sinput.equals("/flush")) {
							players.removeAllElements();
							input.setText("");
							ap("Flushed server");
						}
						if (Sinput.equals("/reload")) {
							for (Player Player : players) {
								try {
									Player.loadOPT();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
							ap("Reloaded player OPT files");
							input.setText("");
						}
						if (Sinput.equals("/list")) {
							for (Player Player : players) {
								ap(Player.getAddress() + ":" + Player.getPort()
										+ " With name " + Player.getName());
							}
							input.setText("");
						}

					} else {
						ap("unknown command");
					}
				}
			}
		});
		input.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(
																input,
																Alignment.LEADING,
																GroupLayout.DEFAULT_SIZE,
																259,
																Short.MAX_VALUE)
														.addComponent(
																scrollPane,
																Alignment.LEADING,
																GroupLayout.DEFAULT_SIZE,
																259,
																Short.MAX_VALUE))
										.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.TRAILING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE,
								411, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(input, GroupLayout.PREFERRED_SIZE, 28,
								GroupLayout.PREFERRED_SIZE).addContainerGap()));
		Log.setEditable(false);

		Log.setLineWrap(true);
		Log.setWrapStyleWord(true);
		scrollPane.setViewportView(Log);
		getContentPane().setLayout(groupLayout);

		pack();
		setVisible(true);

		// non-UI code begins here

		map = new Map(512, 512);

		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));

			String ip = in.readLine();
			ap("Your IP is " + ip);
		} catch (Exception e) {
			ap("Failed to retrive IP, are you sure you are connected to the internet?");
		}

		// datagram socket and port location
		int port = 25565;// port
		try {
			s = new DatagramSocket(port);// socket
		} catch (Exception e) {
			ap("Another server is already running on this adress");
			broken = true;
		}
		// Serverinf.setText(ip + ":" + port);
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(20);// game update speed, about 60 times
											// per second, but not really
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// loops all server-stored players and updates them
					for (int x = players.size() - 1; x >= 0; x--) {
						players.elementAt(x).update();// updater
					}
				}
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);// Player update speed, sending
											// player
											// location to all clients two times
											// every second
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// the actual send function is here,. it loops all players
					// and sends relevant information about all other clients.
					for (Player P : players) {
						InetAddress a = P.getAddress();
						int p = P.getPort();
						for (Player Player : players) {
							if (P.getMx() == Player.getMx()
									&& P.getMy() == Player.getMy()) {
								msg = "@:" + Player.getName() + ":"
										+ Player.getX() + ":" + Player.getY()
										+ ":" + Player.getMx() + ":"
										+ Player.getMy() + ":";

							} else {
								msg = "MP:" + Player.getName() + ":"
										+ Player.getMx() + ":" + Player.getMy()
										+ ":";
							}
							try {
								send(a, p);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}).start();

		// this is the map
		// actual server-stuff starts here, inside the while() loop
		ready = true;
		ap("ready");
		new Thread(new Runnable() {
			public void run() {
				while (!broken) {
					msg = "";
					// receiver
					byte[] buf = new byte[1024];
					dgp = new DatagramPacket(buf, buf.length);
					try {
						s.receive(dgp);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					String rcvd = new String(dgp.getData());
					rcvd.trim();

					// s(rcvd);

					found = false;

					// look if the package is a join request
					for (Player Player : players) {
						if (Player.getAddress().toString()
								.equals(dgp.getAddress().toString())) {
							if (Player.getPort() == dgp.getPort()) {
								found = true;
							}
						}
					}

					String[] Spart = rcvd.split(":");// splitter, divides
														// the package,
														// IE splitting
														// @:hello to @ and
														// hello
					String FL = Spart[0];// FL stands for First Letter, used
											// to identify
											// package type

					try {
						removeplayer(FL, dgp);

						if (FL.equals("¤")) {
							msg = rcvd;
						}

						addplayer(FL, Spart, dgp);

						move(FL, Spart, dgp);
						mapmovement(FL, Spart, dgp, rcvd);
						sendPlayers(FL, dgp);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
	}

	public void sendPlayers(String FL, DatagramPacket dgp) throws IOException {
		if (FL.equals("!")) {
			for (Player Player : players) {
				if (dgp.getAddress().equals(Player.getAddress())
						&& dgp.getPort() == Player.getPort()) {
					for (Player Player2 : players) {
						msg = "§:" + Player2.getName() + ":" + Player2.getX()
								+ ":" + Player2.getY() + ":" + Player2.getCha()
								+ ":" + Player2.getVariation() + ":";
						send(Player.getAddress(), Player.getPort());
					}
					msg = map.getWorldString();
					send(Player.getAddress(), Player.getPort());
				}
			}
		}
	}

	public void removeplayer(String FL, DatagramPacket dgp) throws IOException {
		if (FL.equals("#")) {// remove package recieved...
			for (int x = players.size() - 1; x >= 0; x--) {

				if (players.elementAt(x).getAddress().equals(dgp.getAddress())
						&& players.elementAt(x).getPort() == dgp.getPort()) {

					ap("Player " + players.elementAt(x).getName()
							+ " was removed: client shutdown");// append
																// server
																// log with
																// relevant
																// information

					msg = "#:" + players.elementAt(x).getName() + ":";// remove
																		// order

					send(players.elementAt(x).getAddress(), players
							.elementAt(x).getPort());// send remove order to
														// all clients

					players.remove(players.elementAt(x));// remove player
				}
			}
		}

	}

	public void addplayer(String FL, String[] Spart, DatagramPacket dgp)
			throws IOException {
		if (FL.equals("§") && !found) {
			// if a datagram starts with § it will be identified as
			// a
			// connection request, and the server will attempt to
			// create
			// a new client instance
			String name;
			name = Spart[1];
			players.add(new Player(name, dgp.getAddress(), dgp.getPort(),
					Integer.parseInt(Spart[2]), Integer.parseInt(Spart[3])));
			ap("New client connected from " + dgp.getAddress() + " "
					+ dgp.getPort() + " with name " + name + " and race "
					+ Spart[2]);

			for (Player P : players) {
				// send new client info to all existing clients as
				// well
				// as providing new client with info about all the
				// other
				InetAddress ad = P.getAddress();
				int p = P.getPort();
				for (Player Player : players) {
					msg = "§:" + Player.getName() + ":" + Player.getX() + ":"
							+ Player.getY() + ":" + Player.getCha() + ":"
							+ Player.getVariation() + ":";
					send(ad, p);
				}
			}

		}
	}

	public void mapmovement(String FL, String[] Spart, DatagramPacket dgp,
			String rcvd) throws IOException {
		if (FL.equals("MM")) {
			for (Player player : players) {
				if (player.getAddress().equals(dgp.getAddress())
						&& player.getPort() == dgp.getPort()) {
					if (Spart[1].equals("^")) {
						player.MM(0, -1, map.getWW(), map.getWH());
						msg = "MM:" + player.getName() + ":^:";
					}
					if (Spart[1].equals("<")) {
						player.MM(-1, 0, map.getWW(), map.getWH());
						msg = "MM:" + player.getName() + ":<:";
					}
					if (Spart[1].equals("v")) {
						player.MM(0, 1, map.getWW(), map.getWH());
						msg = "MM:" + player.getName() + ":v:";
					}
					if (Spart[1].equals(">")) {
						player.MM(1, 0, map.getWW(), map.getWH());
						msg = "MM:" + player.getName() + ":>:";
					}
					player.setX(0);
					player.setY(0);
					player.j();
					for (int x = players.size() - 1; x >= 0; x--) {
						if (getDistance(player.getMx(), player.getMy(), players
								.elementAt(x).getMx(), players.elementAt(x)
								.getMy()) < 25) {
							send(players.elementAt(x).getAddress(), players
									.elementAt(x).getPort());
						}
					}
				}
			}
		}
	}

	public double getDistance(int p1x, int p1y, int p2x, int p2y) {
		double xalt = p1x - p2x;
		double yalt = p1y - p2y;
		return Math.sqrt(Math.pow(xalt, 2) + Math.pow(yalt, 2));
	}

	public void move(String FL, String[] Spart, DatagramPacket dgp)
			throws IOException {
		if (FL.equals("$")) {
			boolean pressing = false;
			if (Spart[2].equals("P")) {
				pressing = true;
			}
			for (Player Player : players) {
				if (Player.getAddress().equals(dgp.getAddress())
						&& Player.getPort() == dgp.getPort()) {
					if (Spart[1].equals("<")) {
						if (!pressing) {
							// moving left
							msg = "$:" + Player.getName() + ":<:R:"
									+ Player.getX() + ":" + Player.getY() + ":";
						} else {
							// stopped moving left
							msg = "$:" + Player.getName() + ":<:P:";
						}
						Player.wl(pressing);
					}
					if (Spart[1].equals(">")) {
						if (!pressing) {
							// moving right
							msg = "$:" + Player.getName() + ":>:R:"
									+ Player.getX() + ":" + Player.getY() + ":";
						} else {
							// stopped moving right
							msg = "$:" + Player.getName() + ":>:P:";
						}
						Player.wr(pressing);
					}
					if (Spart[1].equals("^")) {
						// jump
						Player.j();
						msg = "$:" + Player.getName() + ":^:";
					}
				}
			}
		}
		sendtoall();
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

	public Player getPlayer(DatagramPacket dgp) {
		Object o = null;
		for (Player Player : players) {
			if (Player.getAddress().equals(dgp.getAddress())
					&& Player.getPort() == dgp.getPort()) {
				o = Player;
			}
		}
		return (Player) o;
	}

	public static void main(String[] args) throws IOException {
		new Server();
	}

	public void removePlayer(String name) throws IOException {
		for (int x = players.size() - 1; x >= 0; x--) {
			if (name.equals(players.elementAt(x).getName())) {
				ap("Player " + players.elementAt(x).getName()
						+ " was removed: serverside removal");
				msg = "#:" + players.elementAt(x).getName() + ":";
				sendtoall();
				players.remove(players.elementAt(x));

			}
		}
	}

	public void sendtoall() throws IOException {
		Player outplayer = getPlayer(dgp);
		try {
			for (Player Player : players) {
				// send info about action to all clients using string msg
				if (Player.getMx() == outplayer.getMx()
						&& Player.getMx() == outplayer.getMx())
					send(Player.getAddress(), Player.getPort());
			}
		} catch (Exception e) {

		}
	}

	public void ap(String s) {
		// log append shortcut, just call ap(string);
		Log.append(s + "\n");
		Log.setCaretPosition(Log.getDocument().getLength());
	}

	public void s(String s) {
		// console print shortcut, just call s(string);
		System.out.println(s);
	}

	public boolean getready() {
		return ready;
	}

	public void send(InetAddress address, int port) throws IOException {
		// sender function
		if (!msg.isEmpty()) {
			byte[] buf = new byte[1024];
			buf = msg.getBytes();
			DatagramPacket out = new DatagramPacket(buf, buf.length, address,
					port);
			s.send(out);
		}
	}
}
