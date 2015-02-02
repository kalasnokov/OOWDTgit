import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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

	@SuppressWarnings("resource")
	public Server() throws IOException {

		// stuff
		super("OOWDT Server");
		setAlwaysOnTop(true);
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

		JTextPane Serverinf = new JTextPane();
		Serverinf.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								groupLayout
										.createParallelGroup(Alignment.LEADING)
										.addComponent(scrollPane,
												Alignment.TRAILING,
												GroupLayout.DEFAULT_SIZE, 259,
												Short.MAX_VALUE)
										.addComponent(Serverinf,
												Alignment.TRAILING,
												GroupLayout.DEFAULT_SIZE, 259,
												Short.MAX_VALUE))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.TRAILING).addGroup(
				Alignment.LEADING,
				groupLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(Serverinf, GroupLayout.PREFERRED_SIZE,
								28, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE,
								382, Short.MAX_VALUE).addContainerGap()));
		Log.setEditable(false);

		Log.setLineWrap(true);
		Log.setWrapStyleWord(true);
		scrollPane.setViewportView(Log);
		getContentPane().setLayout(groupLayout);

		pack();
		setVisible(true);

		// non-ui code begins here

		// checks servers ip
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				whatismyip.openStream()));

		String ip = in.readLine();
		ap("your ip is: " + ip);

		// datagram socket and port location
		int port = 25565;
		DatagramSocket sk;
		sk = new DatagramSocket(port);

		Serverinf.setText(ip + ":" + port);

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for (Player Player : players) {

						Player.update();
					}
				}
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				while (true) {

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
			if (!found) {
				// new client added and new player created
				// rcvd="§:testsson";

				if (FL.equals("§")) {
					String name;
					name = Spart[1];
					players.add(new Player(name, dgp.getAddress(), dgp
							.getPort()));
					ap("New client connected from " + dgp.getAddress() + " "
							+ dgp.getPort() + " with name " + name);
				} else {
					// join fail error message, probably due to wrong message
					ap("Client at " + dgp.getAddress() + " " + dgp.getPort()
							+ " sent invalid connection package");
				}

				// move commands from client marked with $
			} else {
				if (FL.equals("$")) {
					boolean pressing = false;
					if (Spart[2].equals("P")) {
						pressing = true;
					}
					if (Spart[1].equals("<")) {
						for (Player Player : players) {
							if (Player.getAddress().equals(dgp.getAddress())
									&& Player.getAddress().equals(
											dgp.getAddress())) {
								if (!pressing) {
									msg = "$:" + Player.getName() + ":<:R:"
											+ Player.getX() + ":"
											+ Player.getY() + ":";
								} else {
									msg = "$:" + Player.getName() + ":<:P:";
								}
								Player.wl(pressing);
							}
						}
					}
					if (Spart[1].equals(">")) {
						for (Player Player : players) {
							if (Player.getAddress().equals(dgp.getAddress())
									&& Player.getAddress().equals(
											dgp.getAddress())) {
								if (!pressing) {
									msg = "$:" + Player.getName() + ":>:R:"
											+ Player.getX() + ":"
											+ Player.getY() + ":";
								} else {
									msg = "$:" + Player.getName() + ":>:P:";
								}
								Player.wr(pressing);
							}
						}
					}
					if (Spart[1].equals("^")) {
						for (Player Player : players) {
							if (Player.getAddress().equals(dgp.getAddress())
									&& Player.getAddress().equals(
											dgp.getAddress())) {
								Player.j();
								msg = "$:" + Player.getName() + ":^:";
							}
						}
					}
					ap(String.valueOf(pressing));
				} else {
					// error due to wrong move etc
					ap("Client at " + dgp.getAddress() + " " + dgp.getPort()
							+ " sent invalid command package:");
					ap(rcvd);
				}
			}
			for (Player Player : players) {
				
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
}
