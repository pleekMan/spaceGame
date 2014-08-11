package globals;

import levels.GameMenu;
import processing.core.*;
import rings.Ring;
import ships.Ship;

public class Main extends PApplet {

	Ship ship;

	LevelManager levelManager;
	GameMenu gameMenu;
	
	boolean draw3D;

	public void setup() {

		setPAppletSingleton();

		size(1024, 768, P3D);
		frameRate(30);
		smooth();

		ship = new Ship();
		
		levelManager = new LevelManager();
		gameMenu = new GameMenu();
		
		levelManager.setup(2, ship, gameMenu);
		gameMenu.setup();

		

		draw3D = false;

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


		

		if (draw3D) {
			pushMatrix();
			translate(0, 0, -200);
			rotateX(QUARTER_PI);
		}

		if (!gameMenu.isActive()) {
			levelManager.update();
			levelManager.render();
		}
		

		if (draw3D) {
			popMatrix();

		}


		hint(DISABLE_DEPTH_TEST);
		
		levelManager.render2D();
		
		gameMenu.render();
		
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
		
		
		if (key == 'o') {
			gameMenu.appear();
		}
		if (key == 'l') {
			gameMenu.disappear();
		}
		
		levelManager.onKeyPressed(key);

	}

	public void mousePressed() {
		
		if (!gameMenu.isActive()) {	
			ship.onMousePressed();
		}
		
		//gameMenu.onMousePressed();
		
		if(gameMenu.getSelectedLevel() != -1){
			levelManager.loadLevel(gameMenu.getSelectedLevel());
			gameMenu.disappear();
		}
		
	}

	public void mouseReleased() {
		ship.onMouseReleased();
	}

	public void mouseClicked() {
	}

	public void mouseDragged() {
		// ship.onMouseDragged();
	}

	public void mouseMoved() {
		// ship.onMouseMoved();
	}
}
