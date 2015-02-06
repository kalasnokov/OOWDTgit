package main;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.soap.Text;

import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;

public class Game extends Head {

	public Keys keys;
	public Cursor cursor;
	public Text text;
	public boolean paused;
	public State gameState;
	public State oldState;
	public boolean buttonClicked;
	public Arena arena;
	public int xoffset = 0;
	public int yoffset = 0;
	public static Sender s;
	public boolean c = false;
	private Sprite ground;
	private Sprite back;
	public enum State {
		MENU, PLAYING, STARTING;
	}

	public void preGLInit() {
	}

	public void init() {
		UPDATES_PER_SECOND = 60;
		gameState = State.MENU;
		String ip = JOptionPane.showInputDialog("Enter IP to connect to");
		s = new Sender(this, ip);
		ground= new Sprite("res/ground.png");
		back= new Sprite("res/back.png");
		keys = new Keys();
		arena = new Arena(height, width); // change later
	}

	public void loadAssets() {
	}

	public void handleInputs(double dt) {
		if (keys.keyPressed(Keyboard.KEY_ESCAPE)) {
			if (gameState != State.MENU)
				paused = !paused;
		}
		String msg;
		if (keys.keyPressed(Keyboard.KEY_A) && !c) {
			msg = "$:<:P:";
			try {
				s.s(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			c = true;
		}
		if (keys.keyPressed(Keyboard.KEY_D) && !c) {
			msg = "$:>:P:";
			try {
				s.s(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			c = true;
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
			c = false;
		}
		if (keys.keyReleased(Keyboard.KEY_D)) {
			msg = "$:>:R:";
			try {
				s.s(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			c = false;
		}
		keys.setKeys();
	}

	public void quit() {
	}

	public void update(double dt) {
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
	}
}
