package main;

import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public abstract class Head implements Runnable {

	protected int width;
	protected int height;
	int frames;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	protected String title = "OOWDT";
	protected double UPDATES_PER_SECOND = 60.0;
	protected String version = "";

	boolean running = false;
	private Thread thread;

	public void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	public void stop() {
		running = false;
	}

	private void glinit(int width, int height) {
		this.width = width;
		this.height = height;
		try {
			Display.setDisplayMode(new DisplayMode(width, height));

			Display.setTitle(title);
			Display.create();
		} catch (LWJGLException e) {
			System.exit(0);
		}
		version = glGetString(GL_VERSION);
		Display.setTitle(title + " | " + 0
				+ " ups, " + 0 + " fps");
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glViewport(0, 0, width, height);

		GL11.glLoadIdentity();
		GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

	}

	protected void init() throws InterruptedException, FileNotFoundException, IOException {
	}

	protected void loadAssets() {
	}

	protected void preGLInit() {
	}

	public double getMillis() {
		return ((double) System.nanoTime()) / 1000000.0;
	}

	double oldUpdateTime, newUpdateTime;

	public double getUpdateDelta() {
		oldUpdateTime = newUpdateTime;
		newUpdateTime = getMillis();
		return (newUpdateTime - oldUpdateTime);
	}

	double oldRenderTime, newRenderTime;

	public double getRenderDelta() {
		oldRenderTime = newRenderTime;
		newRenderTime = System.nanoTime();
		return (newRenderTime - oldRenderTime);
	}

	public void run() {
		preGLInit();
		if (!(width > 0 && height > 0)) {
			width = 1280;
			height = 720;
		}
		glinit(width, height);
		try {
			init();
		} catch (InterruptedException | IOException e1) {
			e1.printStackTrace();
		}
		loadAssets();
		Cursor emptyCursor;
		try {
			emptyCursor = new Cursor(1, 1, 0, 0, 1,
					BufferUtils.createIntBuffer(1), null);
			Mouse.setNativeCursor(emptyCursor);
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long lastTime = System.nanoTime();
		long timer = (long) getMillis();
		final double ms = 1000.0 / UPDATES_PER_SECOND;
		final double ns = 1000000000.0 / UPDATES_PER_SECOND;
		double delta = 0;
		frames = 0;
		int updates = 0;
		double updateDelta = getUpdateDelta();
		double renderDelta = getRenderDelta();
		while (running) {
			if (Display.isCloseRequested()) {
				running = false;
			}

			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1 && updates < UPDATES_PER_SECOND) {
				updateDelta = getUpdateDelta();
				try {
					update(updateDelta);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				updates++;
				delta--;
			}
			renderDelta = getRenderDelta();
			double interpolation = (renderDelta / ns) * ms;
			int objects = render(interpolation);
			frames++;
			Display.update();

			if ((long) getMillis() - timer > 1000) {
				timer += 1000;

				if (objects > 0) {
					Display.setTitle(title
							+ " | " + updates + " TICKS, " + frames + " FPS | "
							+ objects + " Rendered");
				} else {
					Display.setTitle(title
							+ " | " + updates + " TICKS, " + frames + " FPS");
				}

				updates = 0;
				frames = 0;
			}
		}
		try {
			kill();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		quit();
		AL.destroy();
		Display.destroy();

	}

	public void kill() throws IOException {
		Game.killthreads();
	}

	protected void quit() {

	}

	protected void update(double dt) throws IOException {
	}

	protected int render(double interpolation) {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		return 0;
	}

	public double getUPS() {
		return UPDATES_PER_SECOND;
	}

	public void setUPS(double ups) {
		UPDATES_PER_SECOND = ups;
	}
	/*
	 * public static void main(String[] args) { new Head().start(); }
	 */
}
