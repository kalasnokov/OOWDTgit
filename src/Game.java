
import javax.xml.soap.Text;

import org.lwjgl.input.Mouse;
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
	public int xoffsetacc = 0;
	public int yoffsetacc = 0;
	boolean rl = false;
	boolean rr = false;

	public enum State {
		MENU, PLAYING, STARTING;
	}

	public void preGLInit() {
	}

	public void init() {
		UPDATES_PER_SECOND = 60;
		gameState = State.MENU;
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
		if (keys.keyDown(Keyboard.KEY_LEFT)) {
			if (xoffsetacc < 15) {
				xoffsetacc++;
			}
		} else {
			if (xoffsetacc > 0) {
				xoffsetacc--;
			}
		}
		if (keys.keyDown(Keyboard.KEY_RIGHT)) {
			if (xoffsetacc > -15) {
				xoffsetacc--;
			}
		} else {
			if (xoffsetacc < 0) {
				xoffsetacc++;
			}
		}
		if (keys.keyDown(Keyboard.KEY_UP)) {
			if (yoffsetacc < 15) {
				yoffsetacc++;
			}
		} else {
			if (yoffsetacc > 0) {
				yoffsetacc--;
			}
		}
		if (keys.keyDown(Keyboard.KEY_DOWN)) {
			if (yoffsetacc > -15) {
				yoffsetacc--;
			}
		} else {
			if (yoffsetacc < 0) {
				yoffsetacc++;
			}
		}

		if (keys.keyDown(Keyboard.KEY_D)) {

		}
		if (keys.keyDown(Keyboard.KEY_A)) {

		}
		if (keys.keyDown(Keyboard.KEY_SPACE)) {

		} else {

		}

		int dWheel = Mouse.getDWheel();
		if (dWheel < 0) {
			super.outZoom();
		} else if (dWheel > 0) {
			super.inZoom();

		}
		dWheel = 0;
		keys.setKeys();

	}

	public void quit() {
	}

	public void update(double dt) {
		Arena.update();
		xoffset += xoffsetacc;
		yoffset += yoffsetacc;
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
		arena.render(interpolation, this, xoffset, yoffset);
		return 0;
	}

	public static void main(String[] args) {
		new Game().start();
	}
}
