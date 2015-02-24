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
	public Arena arena;
	public int xoffset = 0;
	public int yoffset = 0;
	public static Sender s;
	public boolean c = false;
	public boolean t = false;
	private Sprite ground;
	private Sprite back;
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
		if (keys.keyPressed(Keyboard.KEY_A) && !c && !t) {
			msg = "$:<:P:";
			try {
				s.s(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			c = true;
		}
		if (keys.keyPressed(Keyboard.KEY_D) && !c && !t) {
			msg = "$:>:P:";
			try {
				s.s(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			c = true;
		}
		if (keys.keyPressed(Keyboard.KEY_SPACE) && !t) {
			msg = "$:^:";
			try {
				s.s(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (keys.keyReleased(Keyboard.KEY_A) && !t) {
			msg = "$:<:R:";
			try {
				s.s(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			c = false;
		}
		if (keys.keyReleased(Keyboard.KEY_D) && !t) {
			msg = "$:>:R:";
			try {
				s.s(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			c = false;
		}
		if (keys.keyPressed(Keyboard.KEY_LEFT) && !t) {
			msg = "A:1";
			try {
				s.s(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (keys.keyPressed(Keyboard.KEY_UP) && !t) {
			msg = "A:2";
			try {
				s.s(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (keys.keyPressed(Keyboard.KEY_RIGHT) && !t) {
			msg = "A:3";
			try {
				s.s(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (keys.keyPressed(Keyboard.KEY_DOWN) && !t) {
			msg = "A:s";
			try {
				s.s(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
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
		new Thread(new Runnable() {
			public void run() {
				while (running) {
					try {
						new Server();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void giveText(String txt, boolean nuthing) throws IOException {
		if (!nuthing) {
			s.s("¤:" + s.thisplayer.getName() + ": " + txt);
		}
		s.chat.toggleVisible();
		t = false;

	}
}
