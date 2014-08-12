package globals;

import levels.GameMenu;
import processing.core.*;
import rings.Ring;
import ships.Ship;

// NEEDED TO CREATE System/Library/Frameworks/gluegen-rt.Framework/
// AND COPY P5s gluegen-rt and gluegen-rt-universal

// TO RUN: FatJar
// RUN FROM TERMINAL (AT FOLDER): java -Xmx512m -jar SpaceGame_fat.jar
// -Xmx###m : Max heap (memory) to use is ###. "m" means Mbytes
// Also: java -Xms128m -Xmx1024m -jar SpaceGame_fat.jar (where Xms###m: Minimum Heap Size)

//TO RUN: ProClipsing Export
//RUN FROM TERMINAL (AT FOLDER): open Main.app --args -Xmx512m

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
		//PApplet.main(new String[] { "--present", Main.class.getName() }); //
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
		
		//fill(255);
		// text("FR: " + frameRate, 20, 20);
		//text("X: " + mouseX + " / Y: " + mouseY, mouseX, mouseY);

		//text("FR: " + frameRate, 20, 20);
		
		
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
