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
	private State renderState = State.WORLD;
	private boolean Uc;
	private boolean Dc;
	private boolean Rc;
	private boolean Lc;
	private int yoffsetacc;
	private int xoffsetacc;

	public enum State {
		MENU, PLAYING, STARTING, MAP, WORLD
	}

	public void preGLInit() {
	}

	public void init() throws InterruptedException, FileNotFoundException,
			IOException {

		UPDATES_PER_SECOND = 60;
		gameState = State.MENU;

		ground = new Sprite("res/ground.png");
		back = new Sprite("res/back.png");
		keys = new Keys();
		c2 = new Connector(this);
	}

	public void loadAssets() {
	}

	public void handleInputs(double dt) throws IOException {
		if (keys.keyPressed(Keyboard.KEY_ESCAPE)) {
			if (gameState != State.MENU)
				paused = !paused;
		}
		if (keys.keyPressed(Keyboard.KEY_RETURN) && renderState != State.MAP) {
			if (t) {
				t = false;
				giveText(s.chat.getChatText(), false);
			} else {
				t = true;
				s.chat.toggleVisible();
			}
		}
		String msg;
		mapkeys();
		if (!t && renderState != State.MAP) {
			if (keys.keyPressed(Keyboard.KEY_A)) {
				msg = "$:<:P:";
				try {
					s.s(msg);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
			if (keys.keyPressed(Keyboard.KEY_D)) {
				msg = "$:>:P:";
				try {
					s.s(msg);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (keys.keyPressed(Keyboard.KEY_SPACE)) {
				msg = "$:^:";
				try {
					s.s(msg);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (keys.keyReleased(Keyboard.KEY_A)) {
				msg = "$:<:R:";
				try {
					s.s(msg);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (keys.keyReleased(Keyboard.KEY_D)) {
				msg = "$:>:R:";
				try {
					s.s(msg);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		keys.setKeys();
	}

	public void mapkeys() throws IOException {
		String msg;
		if (!t) {
			if (keys.keyPressed(Keyboard.KEY_R)) {
				if (renderState == State.MAP) {
					s.map.zoom(-1);
				}
			}
			if (keys.keyPressed(Keyboard.KEY_F)) {
				if (renderState == State.MAP) {
					s.map.zoom(1);
					if (s.map.gettilewidth() * s.map.getWW() < super.getWidth()
							|| s.map.gettilewidth() * s.map.getWH() < super
									.getHeight()) {
						s.map.zoom(-1);
					}
				}
			}
			if (keys.keyPressed(Keyboard.KEY_M)) {
				if (renderState != State.MAP) {
					renderState = State.MAP;
				} else {
					renderState = State.WORLD;
				}
			}
			if (renderState == State.MAP) {
				if (keys.keyPressed(Keyboard.KEY_UP)) {
					if (renderState == State.MAP) {
						Uc = true;
					}
				}
				if (keys.keyPressed(Keyboard.KEY_DOWN)) {
					if (renderState == State.MAP) {
						Dc = true;
					}
				}
				if (keys.keyPressed(Keyboard.KEY_LEFT)) {
					if (renderState == State.MAP) {
						Lc = true;
					}
				}
				if (keys.keyPressed(Keyboard.KEY_RIGHT)) {
					if (renderState == State.MAP) {
						Rc = true;
					}
				}

				if (keys.keyReleased(Keyboard.KEY_UP)) {
					if (renderState == State.MAP) {
						Uc = false;
					}
				}
				if (keys.keyReleased(Keyboard.KEY_DOWN)) {
					if (renderState == State.MAP) {
						Dc = false;
					}
				}
				if (keys.keyReleased(Keyboard.KEY_LEFT)) {
					if (renderState == State.MAP) {
						Lc = false;
					}
				}
				if (keys.keyReleased(Keyboard.KEY_RIGHT)) {
					if (renderState == State.MAP) {
						Rc = false;
					}
				}

				if (keys.keyPressed(Keyboard.KEY_W)) {
					msg = "MM:^:";
					s.s(msg);
				}
				if (keys.keyPressed(Keyboard.KEY_A)) {
					msg = "MM:<:";
					s.s(msg);
				}
				if (keys.keyPressed(Keyboard.KEY_S)) {
					msg = "MM:v:";
					s.s(msg);
				}
				if (keys.keyPressed(Keyboard.KEY_D)) {
					msg = "MM:>:";
					s.s(msg);
				}
			}
		}
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
		if (Uc) {
			yoffsetacc -= 2;
		}
		if (Dc) {
			yoffsetacc += 2;
		}
		if (Lc) {
			xoffsetacc -= 2;

		}
		if (Rc) {
			xoffsetacc += 2;
		}
		if (xoffsetacc < 0) {
			xoffsetacc++;
		}
		if (xoffsetacc > 0) {
			xoffsetacc--;
		}
		if (yoffsetacc < 0) {
			yoffsetacc++;
		}
		if (yoffsetacc > 0) {
			yoffsetacc--;
		}
		xoffset += xoffsetacc;
		yoffset += yoffsetacc;
		if (xoffset < 0) {
			xoffset = 0;
			xoffsetacc = 0;
		}
		if (yoffset < 0) {
			yoffset = 0;
			yoffsetacc = 0;
		}

		if (s.getMapcreated()) {
			int size = s.map.gettilewidth();

			if (xoffset > (s.map.getWW() * size) - width) {
				xoffset = (s.map.getWW() * size) - width;
				xoffsetacc = 0;
			}
			if (yoffset > (s.map.getWH() * size) - height) {
				yoffset = (s.map.getWH() * size) - height;
				yoffsetacc = 0;
			}
		}
	}

	public int render(double interpolation) {
		super.render(interpolation);
		if (gameState == State.PLAYING) {
			@SuppressWarnings("unused")
			double interp = interpolation;
			if (gameState != State.PLAYING)
				interp = 0;
		}
		if (renderState == State.WORLD) {
			back.render(0, 0);
			s.render(interpolation, this);
			ground.render(0, 540);
		}
		if (renderState == State.MAP) {
			s.map.render(interpolation, this, xoffset, yoffset);
		}
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
			s.s("¤:" + s.myName + ": " + txt);
		}
		s.chat.toggleVisible();
		t = false;
	}
}
