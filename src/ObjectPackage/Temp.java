package ObjectPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import main.Player;

public class Temp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {

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
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				ServerSocket ss = new ServerSocket(25566);
		Socket so= ss.accept();
		InputStream is = so.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(is);
		Object o = ois.readObject();
		Positions to = (Positions) o;
		if (to != null) {
			System.out.println(to.x + " " + to.y +" "+to.name);
		}
		is.close();
		so.close();
		ss.close();
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
			System.out.println(to.x + " " + to.y +" "+to.name);
		}
		is.close();
		s.close();
		ss.close();
		
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Socket so = new Socket("localhost", 25566);
		OutputStream os = so.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		Positions tos = new Positions(10, 7, "Gaben");
		oos.writeObject(tos);
		oos.close();
		os.close();
		so.close();
	}
}
