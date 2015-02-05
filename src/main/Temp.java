package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Temp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub

		new Temp();

	}

	public Temp() throws IOException, ClassNotFoundException {
		Player P = new Player("charlie", 0, 0);
		new Thread(new Runnable() {
			public void run() {
				try {
					ExperimentalReceive();
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		ExperimentalSend(null, P);
	}

	public void ExperimentalSend(InetAddress address, Player player)
			throws IOException, ClassNotFoundException {
		Socket s = new Socket("localhost", 25565);
		OutputStream os = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		Positions tos = new Positions(10, 7, player.getName());
		oos.writeObject(tos);
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
		ss.close();;
	}
}
