package globals;

import processing.core.*;
import rings.Ring;
import ships.Ship;

public class Main extends PApplet {

	Ship ship;

	LevelManager levelManager;

	PImage backImage;

	boolean draw3D;

	public void setup() {

		setPAppletSingleton();

		size(1000, 1000, P3D);
		frameRate(30);
		smooth();

		ship = new Ship();

		levelManager = new LevelManager();
		levelManager.setup(2, ship);

		draw3D = false;

		backImage = loadImage("fondo.png");

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
		background(25, 25, 50);

		// DOTTED BACKGROUND
		stroke(200);
		for (int y = 0; y < height; y += 20) {
			for (int x = 0; x < width; x += 20) {
				point(x, y);
			}
		}

		if (draw3D) {
			pushMatrix();
			translate(0, 0, -200);
			rotateX(QUARTER_PI);
		}

		levelManager.update();
		levelManager.render();

		if (draw3D) {
			popMatrix();

		}

		levelManager.render2D();

		hint(DISABLE_DEPTH_TEST);
		fill(255);
		// text("FR: " + frameRate, 20, 20);
		text("X: " + mouseX + " / Y: " + mouseY, mouseX, mouseY);

		text("FR: " + frameRate, 20, 20);
		hint(ENABLE_DEPTH_TEST);

	}

	public void keyPressed() {
		if (key == 'r') {
			ship.resetPosition();
		}

		if (key == 'c') {
			Ship.applyCentrifugeForce = !Ship.applyCentrifugeForce;
		}

		if (key == 'l') {
			loop();
		}

		if (key == 'f') {
			for (int i = 0; i < levelManager.rings.size(); i++) {
				levelManager.rings.get(i).correctToFinalRotation();
			}
		}

		if (key == '3') {
			draw3D = !draw3D;
		}

		if (key == CODED) {
			if (keyCode == UP) {
			}
		}

		levelManager.onKeyPressed(key);

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
