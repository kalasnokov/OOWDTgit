package Objects;

public class OOWDTObject {
	int x;
	int y;

	int xspd;
	int yspd;

	int spdDeg = 1;
	
	int groundlevel=0;
	boolean Ground=false;

	public OOWDTObject(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void update() {
		x += xspd;
		y += yspd;
		
		if (xspd > 0 && xspd != 0) {
			xspd -= spdDeg;
		}
		if (xspd < 0 && xspd != 0) {
			xspd += spdDeg;
		}
		if (yspd > 0 && yspd != 0) {
			yspd -= spdDeg;
		}
		if (yspd < 0 && yspd != 0) {
			yspd += spdDeg;
		}

	}

	public void render() {

	}

}
