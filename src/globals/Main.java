package globals;

import processing.core.*;
import rings.Ring;
import ships.Ship;

public class Main extends PApplet {

	Ship ship;
	Ring ring;
	Ring ring2;


	public void setup() {

		setPAppletSingleton();

		size(700, 700, P3D);
		frameRate(30);
		smooth();

		ship = new Ship();
		ring = new Ring(width * 0.5f, height * 0.5f, color(0, 200, 200), 200f, 300f);
		ring.setImage(loadImage("image01.png"));
		ring.setAngularVelocity(TWO_PI * 0.01f);
		
		ring2 = new Ring(width * 0.5f, height * 0.5f, color(200, 0, 200), 100f, 200f);
		ring2.setImage(loadImage("image01.png"));
		ring.setAngularVelocity(-(TWO_PI * 0.005f));

	}

	public static void main(String args[]) {
		PApplet.main(new String[] { Main.class.getName() });
		// PApplet.main(new String[] { "--present", Main.class.getName() }); //
		// PRESENT MODE
	}

	private void setPAppletSingleton() {
		PAppletSingleton.getInstance().setP5Applet(this);
	}

	public void draw() {
		background(0);
		
		text("FR: " + frameRate, 20,20);

		strokeWeight(1);
		stroke(127,50);
		for (int i = 0; i < width; i += 20) {
			line(i, 0, i, height);
		}

		ring.update();
		ring2.update();
		ship.update();
		// ship.maneuver();

		if (ring.isInside(ship.getPosition().x, ship.getPosition().y)) {
			ship.setColor(color(255, 255, 0));
			ring.modifyVelocity(ship.getPosition().x, ship.getPosition().y);

			// PVector shipToCenter = new PVector(ring.getPosition().x -
			// ship.getPosition().x, ring.getPosition().y -
			// ship.getPosition().y);
			// shipToCenter.normalize();
			// shipToCenter.rotate(-PI);
			// shipToCenter.mult(ring.getAngularVelocity() * 10);
			// println(shipToCenter);
			// ship.addForce(shipToCenter);
			// line(ship.getPosition().x,
			// ship.getPosition().y,ship.getPosition().x + shipToCenter.x,
			// ship.getPosition().y + shipToCenter.y);
			ship.addForce(ring.getAngularPushVector(ship.getPosition()));

		} else if (ring2.isInside(ship.getPosition().x, ship.getPosition().y)) {
			ship.setColor(color(255, 255, 0));
			ring2.modifyVelocity(ship.getPosition().x, ship.getPosition().y);

			// PVector shipToCenter = new PVector(ring.getPosition().x -
			// ship.getPosition().x, ring.getPosition().y -
			// ship.getPosition().y);
			// shipToCenter.normalize();
			// shipToCenter.rotate(-PI);
			// shipToCenter.mult(ring.getAngularVelocity() * 10);
			// println(shipToCenter);
			// ship.addForce(shipToCenter);
			// line(ship.getPosition().x,
			// ship.getPosition().y,ship.getPosition().x + shipToCenter.x,
			// ship.getPosition().y + shipToCenter.y);
			ship.addForce(ring2.getAngularPushVector(ship.getPosition()));

		}else {
			ship.setColor(color(0, 255, 255));
			ship.addForce(new PVector(0, 0));
		}

		ring.render();
		ring2.render();
		ship.render();

	}

	public void keyPressed() {
		if (key == 'r') {
			ship.resetPosition();
		}

		if (key == 'l') {
		}

		if (key == CODED) {
			if (keyCode == UP) {
			}
		}
	}

	public void mousePressed() {
		ship.onMousePressed();
	}

	public void mouseReleased() {
		ship.onMouseReleased();
	}

	public void mouseClicked() {
	}

	public void mouseDragged() {
		// ship.onMouseDragged();

		line(width * 0.5f, height * 0.5f, mouseX, mouseY);
	}

	public void mouseMoved() {
		// ship.onMouseMoved();
	}
}
