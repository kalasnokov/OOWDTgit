
import java.io.Serializable;

public class Arena implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1301968695579108833L;
	int SW;
	int SH;

	public Arena(int screenwidth, int screenheight) {
		SW = screenwidth;
		SH = screenheight;
		// world generation
	}

	public void render(double dt, Game game, int xoffset, int yoffset) {
	}

	public static void update() {

	}
}
