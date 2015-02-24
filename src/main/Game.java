package main;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;

import UI.Connector;

public class Game extends Head {

	public Keys keys;
	public Cursor cursor;
	public String text = "";
	public boolean paused;
	public State gameState;
	public State oldState;
	public boolean buttonClicked;
	public int xoffset = 0;
	public int yoffset = 0;
	public static Sender s;
	public boolean cr = false;
	public boolean cl = false;
	public boolean t = false;
	private Sprite ground;
	private Sprite back;
	public Server server;
	@SuppressWarnings("unused")
	private Connector c2;

	public enum State {
		MENU, PLAYING, STARTING;
	}

	public void preGLInit() {
	}

	public void init() throws InterruptedException {

		UPDATES_PER_SECOND = 60;
		gameState = State.MENU;
		// String ip = JOptionPane.showInputDialog("Enter IP to connect to");

		ground = new Sprite("res/ground.png");
		back = new Sprite("res/back.png");
		keys = new Keys();
		// arena = new Arena(height, width); // change later
		try {
			c2 = new Connector(this);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadAssets() {
	}

	public void handleInputs(double dt) throws IOException {
		if (keys.keyPressed(Keyboard.KEY_ESCAPE)) {
			if (gameState != State.MENU)
				paused = !paused;
		}

		if (keys.keyPressed(Keyboard.KEY_RETURN)) {
			if (t) {
				t = false;
				giveText(s.chat.getChatText(), false);
			} else {
				t = true;
				s.chat.toggleVisible();
			}
		}
		String msg;
		if (!t) {
			if (keys.keyPressed(Keyboard.KEY_A)) {
				msg = "$:<:P:";
				try {
					s.s(msg);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				s.thisplayer.wl(true);
			}
			if (keys.keyPressed(Keyboard.KEY_D)) {
				msg = "$:>:P:";
				try {
					s.s(msg);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				s.thisplayer.wr(true);
			}
			if (keys.keyPressed(Keyboard.KEY_SPACE)) {
				msg = "$:^:";
				try {
					s.s(msg);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				s.thisplayer.j();
			}
			if (keys.keyReleased(Keyboard.KEY_A)) {
				msg = "$:<:R:";
				try {
					s.s(msg);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				s.thisplayer.wl(false);
			}
			if (keys.keyReleased(Keyboard.KEY_D)) {
				msg = "$:>:R:";
				try {
					s.s(msg);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				s.thisplayer.wr(false);
			}
		}
		keys.setKeys();
	}

	public void quit() {
	}

	public void update(double dt) throws IOException {
		handleInputs(dt);
		if (gameState == State.STARTING) {
			gameState = State.PLAYING;
			oldState = State.MENU;
		}
		buttonClicked = false;
		s.view(this);
	}

	public int render(double interpolation) {
		super.render(interpolation);
		if (gameState == State.PLAYING) {
			@SuppressWarnings("unused")
			double interp = interpolation;
			if (gameState != State.PLAYING)
				interp = 0;
		}
		back.render(0, 0);
		s.render(interpolation, this);
		ground.render(0, 540);
		return 0;
	}

	public void so(String o) {
		System.out.println(o);
	}

	public static void main(String[] args) {
		new Game().start();
	}

	public static void killthreads() throws IOException {
		s.stop();
		s.chat.Destroy();
	}

	public void setSendervalues(String ip, String name, int variation, int race)
			throws InterruptedException {
		s = new Sender(this, ip, name, variation, race);
	}

	public void Host() {

		try {
			server = new Server();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void giveText(String txt, boolean nuthing) throws IOException {
		if (!nuthing) {
			s.s("¤:" + s.thisplayer.getName() + ": " + txt);
			s.thisplayer.setText(txt);
		}
		s.chat.toggleVisible();
		t = false;
	}
}
