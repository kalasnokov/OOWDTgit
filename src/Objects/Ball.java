package Objects;

import main.Sprite;

public class Ball extends OOWDTObject{
	Sprite sprite;
	public Ball(int x, int y, boolean S) {
		super(x, y);
		if(!S){
			sprite = new Sprite("res/objects/interactable/ball.png");
		}
	}
	
}
