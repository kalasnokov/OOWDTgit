
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
		keys.setKeys();

	}

	public void quit() {
	}

	public void update(double dt) {
		Arena.update();
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
