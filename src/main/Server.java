package main;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("unused")
public class Server extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1960888810093318280L;
	private static final JTextArea Log = new JTextArea();
	private static final JTextPane Serverinf = new JTextPane();
	private boolean found;
	public Vector<Player> players = new Vector<Player>();
	private String msg;
	DatagramSocket sk;

	public Server() throws IOException {

		// stuff
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

		JTextPane input = new JTextPane();
		input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				// when pressing the "enter" key.
				if (key == KeyEvent.VK_ENTER) {
				}
			}
		});
		input.setEditable(false);
		input.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
						.addComponent(input, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(input, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		Log.setEditable(false);

		Log.setLineWrap(true);
		Log.setWrapStyleWord(true);
		scrollPane.setViewportView(Log);
		getContentPane().setLayout(groupLayout);

		pack();
		setVisible(true);

		// non-ui code begins here

		// checks servers ip
		/*URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				whatismyip.openStream()));

		String ip = in.readLine();*/

		// datagram socket and port location
		int port = 25565;
		sk = new DatagramSocket(port);

		//Serverinf.setText(ip + ":" + port);

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(20);// game speed
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for (Player Player : players) {
						Player.update();
						// possible location of a setx/sety function to decrease
						// misalignments
					}
				}
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);// update speed
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for (Player P : players) {
						InetAddress a = P.getAddress();
						int p = P.getPort();
						for (Player Player : players) {
							msg = "@:" + Player.getName() + ":" + Player.getX()
									+ ":" + Player.getY() + ":";
							try {
								send(a, p);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}).start();

		while (true) {
			msg = "";
			// receiver
			byte[] buf = new byte[1024];
			DatagramPacket dgp = new DatagramPacket(buf, buf.length);
			sk.receive(dgp);
			String rcvd = new String(dgp.getData());
			rcvd.trim();
			// rcvd="§:testsson";
			found = false;

			// look if the pacage is a join request
			for (Player Player : players) {
				if (Player.getAddress().toString()
						.equals(dgp.getAddress().toString())) {
					if (Player.getPort() == dgp.getPort()) {
						found = true;
					}
				}
			}
			String[] Spart = rcvd.split(":");
			String FL = Spart[0];
			if (FL.equals("#")) {
				for (int x = players.size() - 1; x >= 0; x--) {
					if (players.elementAt(x).getAddress()
							.equals(dgp.getAddress())
							&& players.elementAt(x).getPort() == dgp.getPort()) {
						ap("Player " + players.elementAt(x).getName()
								+ " was removed: client shutdown");
						msg = "#:" + players.elementAt(x).getName() + ":";
						send(players.elementAt(x).getAddress(), players
								.elementAt(x).getPort());
						players.remove(players.elementAt(x));
					}
				}
			} else {
				if (!found) {
					// new client added and new player created
					// rcvd="§:testsson";

					if (FL.equals("§")) {
						// if a datagram starts with § it will be identified as
						// a
						// connection request, and the server will attempt to
						// creata
						// a new client instance
						String name;
						name = Spart[1];
						players.add(new Player(name, dgp.getAddress(), dgp
								.getPort()));
						ap("New client connected from " + dgp.getAddress()
								+ " " + dgp.getPort() + " with name " + name);

						for (Player P : players) {
							// send new client info to all existing clients as
							// well
							// as providing new client with info about all the
							// other
							InetAddress ad = P.getAddress();
							int p = P.getPort();
							for (Player Player : players) {
								msg = "§:" + Player.getName() + ":"
										+ Player.getX() + ":" + Player.getY()
										+ ":";
								send(ad, p);
							}
						}

					} else {
						// join fail error message, probably due to wrong
						// message
						ap("Client at " + dgp.getAddress() + " "
								+ dgp.getPort()
								+ " sent invalid connection package");
					}
					// move commands from client marked with $
				} else {
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
												+ Player.getX() + ":"
												+ Player.getY() + ":";
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
												+ Player.getX() + ":"
												+ Player.getY() + ":";
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
						// ap(String.valueOf(pressing));

					} else {
						// error due to wrong move etc
						ap("Client at " + dgp.getAddress() + " "
								+ dgp.getPort()
								+ " sent invalid command package:");
						ap(rcvd);
					}

				}
			}
			for (Player Player : players) {
				// send info about action to all clients using string msg
				send(Player.getAddress(), Player.getPort());
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new Server();
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

	public void send(InetAddress address, int port) throws IOException {
		// sender function
		byte[] buf = new byte[1024];
		buf = msg.getBytes();
		DatagramPacket out = new DatagramPacket(buf, buf.length, address, port);
		sk.send(out);
	}
}
