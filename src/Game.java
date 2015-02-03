import java.awt.event.KeyEvent;
import java.io.IOException;

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
	public Sender s;
	public boolean cr = false;
	public boolean cl = false;

	public enum State {
		MENU, PLAYING, STARTING;
	}

	public void preGLInit() {
	}

	public void init() {
		UPDATES_PER_SECOND = 60;
		gameState = State.MENU;
		s = new Sender(this);
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
		if (keys.keyPressed(Keyboard.KEY_A) && !cl) {
			msg = "$:<:P:";
			try {
				s.s(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			cl = true;
		}
		if (keys.keyPressed(Keyboard.KEY_D) && !cr) {
			msg = "$:>:P:";
			try {
				s.s(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			cr = true;
		}
		if (keys.keyPressed(Keyboard.KEY_W)) {
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
			cl = false;
		}
		if (keys.keyReleased(Keyboard.KEY_D)) {
			msg = "$:>:R:";
			try {
				s.s(msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			cr = false;
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
	}

	public int render(double interpolation) {
		super.render(interpolation);

		if (gameState == State.PLAYING) {
			@SuppressWarnings("unused")
			double interp = interpolation;
			if (gameState != State.PLAYING)
				interp = 0;
		}
		s.render(interpolation, this);
		return 0;
	}

	public static void main(String[] args) {
		new Game().start();
	}
}
