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
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						if (Sinput.equals("/flush")) {
							players.removeAllElements();
							input.setText("");
							ap("Flushed server");
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

		// non-ui code begins here

		// checks servers ip

		/*
		 * URL whatismyip = new URL("http://checkip.amazonaws.com");
		 * BufferedReader in = new BufferedReader(new InputStreamReader(
		 * whatismyip.openStream()));
		 * 
		 * String ip = in.readLine(); s(ip);
		 */

		// datagram socket and port location
		int port = 25565;
		s = new DatagramSocket(port);

		// Serverinf.setText(ip + ":" + port);
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
						Thread.sleep(1000);// update speed
					} catch (InterruptedException e) {
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
			byte[] buf = new byte[32];
			DatagramPacket dgp = new DatagramPacket(buf, buf.length);
			s.receive(dgp);
			String rcvd = new String(dgp.getData());
			rcvd.trim();
			// rcvd="�:testsson";
			found = false;
			/*
			 * Object o = null; ByteArrayOutputStream bos = new
			 * ByteArrayOutputStream(); ObjectOutput out = null; out = new
			 * ObjectOutputStream(bos); out.writeObject(o); byte[] yourBytes =
			 * bos.toByteArray();
			 */

			// look if the package is a join request
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
			}
			if (!found) {
				// new client added and new player created
				if (FL.equals("�")) {
					// if a datagram starts with � it will be identified as
					// a
					// connection request, and the server will attempt to
					// create
					// a new client instance
					String name;
					name = Spart[1];
					players.add(new Player(name, dgp.getAddress(), dgp
							.getPort(), Integer.parseInt(Spart[2]), Integer
							.parseInt(Spart[3])));
					ap("New client connected from " + dgp.getAddress() + " "
							+ dgp.getPort() + " with name " + name
							+ " and race " + Spart[2]);

					for (Player P : players) {
						// send new client info to all existing clients as
						// well
						// as providing new client with info about all the
						// other
						InetAddress ad = P.getAddress();
						int p = P.getPort();
						for (Player Player : players) {
							msg = "�:" + Player.getName() + ":" + Player.getX()
									+ ":" + Player.getY() + ":"
									+ Player.getCha() + ":"
									+ Player.getVariation() + ":";
							send(ad, p);
						}
					}

				}
			}
			move(FL, Spart, dgp);
			sendtoall();
		}
	}

	public void move(String FL, String[] Spart, DatagramPacket dgp) {
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
		for (Player Player : players) {
			// send info about action to all clients using string msg
			send(Player.getAddress(), Player.getPort());
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

	public void send(InetAddress address, int port) throws IOException {
		// sender function
		byte[] buf = new byte[32];
		buf = msg.getBytes();
		DatagramPacket out = new DatagramPacket(buf, buf.length, address, port);
		s.send(out);
	}

	public void ExperimentalSend(InetAddress address, Player player)
			throws IOException {
		Socket s = new Socket("localhost", 25565);
		OutputStream os = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		Positions to = new Positions(10, 7, player.getName());
		oos.writeObject(to);
		oos.close();
		os.close();
		s.close();
	}

	public void ExperimentalReceive() throws IOException,
			ClassNotFoundException {
		ServerSocket ss = new ServerSocket(25565);
		Socket s = ss.accept();
		InputStream is = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(is);
		Object o = ois.readObject();
		Positions to = (Positions) o;
		if (to != null) {
			System.out.println(to.x + " " + to.y);
		}
		is.close();
		s.close();
		ss.close();
	}
}
