package ships;

import processing.core.PImage;
import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class Ship {

	Main p5;

	PImage shipImage;
	
	PVector accel;
	PVector vel;
	PVector pos;

	float size;
	float rotation;
	int shipColor;

	boolean controlled = false;
	PVector externalForce;
	public static boolean applyCentrifugeForce;

	ShipController shipControl;


	public Ship() {

		p5 = getP5();
		
		shipImage = p5.loadImage("ship.png");

		pos = new PVector(p5.width * 0.5f, p5.height - 10f);
		vel = new PVector(0, 0);
		accel = new PVector(0, 0);

		size = 40f;
		rotation = 0f;

		shipColor = p5.color(150, 100, 100);
		externalForce = new PVector(0, 0);
		applyCentrifugeForce = false;

		shipControl = new ShipController();
		shipControl.setPosition((int)(p5.width * 0.5f), (int)(p5.height * 0.5f));

	}

	public void update() {

		if (!applyCentrifugeForce) {
			//vel.set(0, 0);
			vel.mult(0.5f);
		}
		
		//accel.mult(0.9f);
		maneuver();
		vel.add(accel);
		pos.add(vel);

		accel.mult(0.9f);
		
		checkBorders();
	}

	public void render() {

		shipControl.render();

	}

	private float orientShip() {
		
		//EITHER REPRESENT ANGLE FROM:
		// SHIP'S ACTUAL VELOCITY
		//float angle = p5.atan2(vel.y, vel.x);
		
		//OR CONTROLLER VELOCITY VECTOR (FEELING OF DRAG)
		float angle = p5.atan2(shipControl.getForce().y, shipControl.getForce().x);

		return angle + p5.HALF_PI;
	}

	public void render2D() {
		
		p5.pushStyle();

		p5.noFill();
		p5.stroke(shipColor);

		p5.pushMatrix();
		
		p5.translate(pos.x, pos.y);
		
		p5.rotate(orientShip());
		
		p5.image(shipImage, 0, 0);
		
		//p5.ellipse(pos.x, pos.y, size, size);
		//p5.ellipse(pos.x, pos.y, size * 0.5f, size * 0.5f);

		
		p5.popMatrix();
		p5.popStyle();
	}

	public PVector getPosition() {
		return pos;
	}
	public PVector getVelocity() {
		return vel;
	}

	public float getSize() {
		return size;
	}

	public void setColor(int _color) {
		shipColor = _color;

	}

	public void addForce(PVector force) {
		// force.mult(-1);
		externalForce = force;

		externalForce.mult(0.1f);
		accel.add(externalForce);
	}

	public void resetPosition() {
		pos.set(p5.width * 0.5f, p5.height - 10f);
		vel.set(0, 0);
		accel.set(0,0);
	}
	
	public void checkBorders(){
		if (pos.x < 0) {
			pos.x = 0;
		} else if (pos.x > p5.width){
			pos.x = p5.width;
		}
		
		if (pos.y < 0){
			pos.y = 0;
		} else if (pos.y > p5.height){
			pos.y = p5.height;
		}
	}

	private void maneuver() {
		if (controlled) {
			// p5.println("Vectoring Ship");
			
			vel.set(shipControl.getForce());
			vel.mult(0.1f);
			
			//accel.set(shipControl.getForce());
			//accel.mult(0.1f);
			
		}
	}

	public void onMouseDragged() {
		/*
		vel.set(p5.mouseX - (p5.width * 0.5f), p5.mouseY - (p5.height * 0.5f));
		vel.mult(0.1f);
		p5.stroke(255, 255, 0);
		p5.line(p5.width * 0.5f, p5.height * 0.5f, p5.mouseX, p5.mouseY);
		*/
	}

	public void onMousePressed() {
		
		controlled = true;
		//p5.println("onMP");

	}

	public void onMouseReleased() {
		controlled = false;
		//p5.println("onMR");

	}

	public void onMouseMoved() {
		//p5.println("onMM");
	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
