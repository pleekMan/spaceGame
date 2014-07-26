package rings;

import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class Artifact {

	Main p5;

	PVector pos;
	float size;
	int type;
	String description;

	public Artifact() {
		p5 = getP5();

		pos = new PVector(0, 0);
		size = 30;
	}

	// x,y,img, umbral
	public void setup(int _type, float newX, float newY, String _description) {
		type = _type;
		pos.x = newX;
		pos.y = newY;
		description = _description;
		
	}

	public void render() {
		if (type == 0) {
			p5.line(pos.x - (size * 0.5f), pos.y - (size * 0.5f), pos.x + (size * 0.5f), pos.y + (size * 0.5f));
			p5.line(pos.x + (size * 0.5f), pos.y - (size * 0.5f), pos.x - (size * 0.5f), pos.y + (size * 0.5f));
		} else {
			p5.ellipse(pos.x, pos.y, size, size);
			// p5.ellipse(0,0, size, size);
		}
	}

	public boolean collidedWith(float _x, float _y) {
		float distancia = p5.dist(pos.x, pos.y, _x, _y);
		if (distancia < (size)) {
			return true;
		} else {
			return false;
		}
	}

	public void setPosition(int _x, int _y) {
		pos.x = _x;
		pos.y = _y;
	}

	public void setType(int _type) {
		type = _type;
	}

	public int getType() {
		return type;
	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
