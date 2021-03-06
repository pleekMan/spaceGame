package ships;

import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class Ship {

	Main p5;

	PVector vel;
	PVector pos;

	float size;
	float rotation;
	int shipColor;
	
	boolean controlled = false;
	//PVector externalForce;

	public Ship() {

		p5 = getP5();

		pos = new PVector(p5.width * 0.5f, p5.height - 10f);
		vel = new PVector(0, 0);
		
		size = 20f;
		rotation = 0f;
		
		shipColor = p5.color(255,255,0);

	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

	public void update() {

		pos.add(vel);

	}

	public void render() {

		p5.pushStyle();

		p5.noFill();
		p5.stroke(shipColor);

		p5.ellipse(pos.x, pos.y, size, size);

		p5.popStyle();

	}
	
	public PVector getPosition(){
		return pos;
	}
	
	public void setColor(int _color){
		shipColor = _color;
		
	}
	
	public void addForce(PVector force){
		//force.mult(-1);
		vel.add(force);	
	}
	
	public void resetPosition(){
		pos.set(p5.width * 0.5f, p5.height - 10f);
		vel.set(0,0);
	}
	
	public void maneuver(){
		if(controlled){
			//p5.println("Vectoring Ship");
			vel.set(p5.mouseX - (p5.width * 0.5f), p5.mouseY - (p5.height * 0.5f));
			//vel.mult(0.1f);
			p5.stroke(255, 255, 0);
			p5.line(p5.width * 0.5f, p5.height * 0.5f, p5.mouseX, p5.mouseY);
		}
	}

	public void onMouseDragged() {

		vel.set(p5.mouseX - (p5.width * 0.5f), p5.mouseY - (p5.height * 0.5f));
		vel.mult(0.1f);
		p5.stroke(255, 255, 0);
		p5.line(p5.width * 0.5f, p5.height * 0.5f, p5.mouseX, p5.mouseY);
	}
	
	public void onMousePressed(){
		controlled = true;
		p5.println("onMP");

	}
	
	public void onMouseReleased(){
		controlled = false;
		p5.println("onMR");

	}
	
	public void onMouseMoved(){
		p5.println("onMM");
	}
	
	
}
