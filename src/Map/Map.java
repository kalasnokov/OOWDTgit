package Map;

import java.io.Serializable;
import java.util.Random;

import main.Game;
import main.Sprite;

public class Map implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1301968695579108833L;
	int WH;
	int WW;
	private static int grad3[][] = { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 },
			{ -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 },
			{ -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 },
			{ 0, -1, -1 } };
	private int p[] = new int[256];

	private String worldstring;
	float[][] simplexnoise;
	boolean f = true;
	Sprite[] sprites = new Sprite[12];
	int zoom = 0;
	boolean done = false;;
	private int perm[] = new int[512];

	private static double dot(int g[], double x, double y) {
		return g[0] * x + g[1] * y;
	}

	public Map(int WW, int WH, int[] p) {
		// client constructor
		this.WW = WW;
		this.WH = WH;
		this.p = p;
		generate(6, 0.6f, 0.005f);
		so("Map created with variables " + WW + " X " + WH);
	}

	public Map(int WW, int WH) {
		// server constructor
		this.WW = WW;
		this.WH = WH;
		Random rand = new Random();
		for (int i = 0; i < p.length; i++) {
			p[i] = rand.nextInt(256);
		}
		worldstring = "£:" + WW + ":" + WH + ":";
		for (int i = 0; i < p.length; i++) {
			worldstring += p[i] + ":";
		}
		generate(6, 0.6f, 0.005f);
		so(worldstring);
		System.out.println(worldstring.length());
	}

	public void generate(int octaves, float roughness, float scale) {
		for (int i = 0; i < 512; i++) {
			perm[i] = p[i & 255];
		}

		simplexnoise = new float[WW][WH];

		float layerFrequency = scale;
		float layerWeight = 10;

		for (int octave = 0; octave < octaves; octave++) {
			for (int x = 0; x < WW; x++) {
				for (int y = 0; y < WH; y++) {
					simplexnoise[x][y] += (float) noise(x * layerFrequency, y
							* layerFrequency)
							* layerWeight;
				}
			}
			layerFrequency *= 2;
			layerWeight *= roughness;
		}
		/*
		 * for (int i = 0; i < WW; i++) { for (int u = 0; u < WH; u++) {
		 * System.out.print((int)(simplexnoise[i][u]) + " "); }
		 * System.out.println(" "); }
		 */
	}

	public void render(double dt, Game game, int xoffset, int yoffset) {
		if (f) {
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = new Sprite("res/tiles/" + (i - 2) + ".png");
			}
			f = false;
			done = true;
		}
		int xvar = (int) (sprites[0].width - zoom);
		int yvar = (int) (sprites[0].height - zoom);
		for (int x = 0; x < WH; x++) {
			for (int y = 0; y < WH; y++) {
				if (x * xvar - xoffset < game.width
						&& x * xvar + xvar - xoffset > 0
						&& y * yvar - yoffset < game.height
						&& y * yvar + yvar - yoffset > 0) {
					if ((int) simplexnoise[x][y] >= 9) {
						simplexnoise[x][y] = 9;
					}
					if (simplexnoise[x][y] < -2) {
						simplexnoise[x][y] = -2;
					}
					sprites[(int) simplexnoise[x][y] + 2].render(x * xvar
							- xoffset, y * yvar - yoffset, xvar, yvar);
				}
			}
		}
		Game.s.renderonMap(dt, game, xoffset, yoffset, xvar, yvar);
	}

	public double noise(double xin, double yin) {
		double n0, n1, n2;
		final double F2 = 0.5 * (Math.sqrt(3.0) - 1.0);
		double s = (xin + yin) * F2;
		int i = fastfloor(xin + s);
		int j = fastfloor(yin + s);
		final double G2 = (3.0 - Math.sqrt(3.0)) / 6.0;
		double t = (i + j) * G2;
		double X0 = i - t;
		double Y0 = j - t;
		double x0 = xin - X0;
		double y0 = yin - Y0;
		int i1, j1;
		if (x0 > y0) {
			i1 = 1;
			j1 = 0;
		} else {
			i1 = 0;
			j1 = 1;
		}
		double x1 = x0 - i1 + G2;
		double y1 = y0 - j1 + G2;
		double x2 = x0 - 1.0 + 2.0 * G2;
		double y2 = y0 - 1.0 + 2.0 * G2;
		int ii = i & 255;
		int jj = j & 255;
		int gi0 = perm[ii + perm[jj]] % 12;
		int gi1 = perm[ii + i1 + perm[jj + j1]] % 12;
		int gi2 = perm[ii + 1 + perm[jj + 1]] % 12;
		double t0 = 0.5 - x0 * x0 - y0 * y0;
		if (t0 < 0)
			n0 = 0.0;
		else {
			t0 *= t0;
			n0 = t0 * t0 * dot(grad3[gi0], x0, y0);
		}
		double t1 = 0.5 - x1 * x1 - y1 * y1;
		if (t1 < 0)
			n1 = 0.0;
		else {
			t1 *= t1;
			n1 = t1 * t1 * dot(grad3[gi1], x1, y1);
		}
		double t2 = 0.5 - x2 * x2 - y2 * y2;
		if (t2 < 0)
			n2 = 0.0;
		else {
			t2 *= t2;
			n2 = t2 * t2 * dot(grad3[gi2], x2, y2);
		}
		return 70.0 * (n0 + n1 + n2);
	}

	private static int fastfloor(double x) {
		return x > 0 ? (int) x : (int) x - 1;
	}

	public String getWorldString() {
		return worldstring;
	}

	public int getWW() {
		return WW;
	}

	public int getWH() {
		return WH;
	}

	public void zoom(int zoom) {
		this.zoom += zoom;
	}

	public static void update() {

	}

	public int gettilewidth() {
		if (done) {
			return (int) sprites[0].height - zoom;
		} else {
			return 32;
		}
	}

	public void so(String s) {
		// console print shortcut, just call s(string);
		System.out.println(s);
	}
}
