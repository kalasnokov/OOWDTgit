


public class Keys {
	
	private boolean[] keys;
	
	public Keys() {
		keys = new boolean[org.lwjgl.input.Keyboard.KEYBOARD_SIZE];
	}
	
	public boolean lastKeyDown(int keyCode) {
		return keys[keyCode];
	}
	
	public boolean keyDown(int keyCode) {
		return org.lwjgl.input.Keyboard.isKeyDown(keyCode);
	}
	
	public boolean keyPressed(int keyCode) {
		return !keys[keyCode] && org.lwjgl.input.Keyboard.isKeyDown(keyCode);
	}
	
	public boolean keyReleased(int keyCode) {
		return keys[keyCode] && !org.lwjgl.input.Keyboard.isKeyDown(keyCode);
	}
	
	public void setKeys() {
		for (int i = 0; i < keys.length; i++) {
			keys[i] = org.lwjgl.input.Keyboard.isKeyDown(i);
		}
	}
	
}
