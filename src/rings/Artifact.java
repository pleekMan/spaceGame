package rings;

import processing.core.PVector;
import globals.LevelManager;
import globals.Main;
import globals.PAppletSingleton;

public class Artifact {

	Main p5;

	PVector pos;
	float size;
	int type; // 0 = Kill, 1 = Bounce
	String name;
	String description;
	
	Balloon balloon;

	public Artifact() {
		p5 = getP5();

		pos = new PVector(0, 0);
		size = 30;
	}

	// x,y,img, umbral
	public void setup(int _type, float newX, float newY, String _name, String _description) {
		type = _type;
		pos.x = newX;
		pos.y = newY;
		name = _name;
		description = _description;
		
		balloon = new Balloon(pos.x - 10, pos.y, name, description);
		balloon.setAni(LevelManager.getAni());
		
	}
	
	public void update(){
		
		// BALLOON TRIGGER
		if(p5.dist(p5.mouseX, p5.mouseY, pos.x, pos.y) < size){
			balloon.appear();
		} else {
			if(balloon.isShowing == true){
				balloon.disappear();
			}
		}
		
	}

	public void render() {

		p5.stroke(255);
		if (type == 1) {
			p5.line(pos.x - (size * 0.5f), pos.y - (size * 0.5f), pos.x + (size * 0.5f), pos.y + (size * 0.5f));
			p5.line(pos.x + (size * 0.5f), pos.y - (size * 0.5f), pos.x - (size * 0.5f), pos.y + (size * 0.5f));
		} else {
			p5.ellipse(pos.x, pos.y, size, size);
			// p5.ellipse(0,0, size, size);
		}
		
		//p5.text(description, pos.x, pos.y - size);
		
		balloon.render();
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
	
	public void setDescription(String _description){
		description = _description;
	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
