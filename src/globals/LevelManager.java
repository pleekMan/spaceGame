package globals;

import rings.Ring;
import rings.Artifact;
import ships.Ship;

import java.util.ArrayList;
import java.util.Collections;

import levels.LevelPack;
import levels.GameMenu;
import ddf.minim.Minim;
import de.looksgood.ani.Ani;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.XML;

public class LevelManager {

	Main p5;
	GameMenu gameMenuInstance;
	static Ani ani;
	static public Minim minim;

	Ship ship;
	ArrayList<Ring> rings;
	ArrayList<Artifact> artifacts;
	ArrayList<LevelPack> levels;

	//GameMenu levelSplash;

	int ringCount;
	int artifactCount;

	boolean levelCompletedTrigger;
	boolean isShipInsideRings;

	Timer timer;

	int atLevel = 0;

	public enum LevelState {
		PLAY, SPLASH
	}

	LevelState levelState;

	public LevelManager() {

		p5 = getP5();
		Ani.init(p5);

		rings = new ArrayList<Ring>();
		artifacts = new ArrayList<Artifact>();
		levels = new ArrayList<LevelPack>();

		//levelSplash = new GameMenu();

		minim = new Minim(p5);

	}

	public void setup(int _rings, Ship _ship, GameMenu gm) {

		ship = _ship;
		ringCount = _rings;
		artifactCount = 0;
		
		gameMenuInstance = gm;

		buildLevelPacks();

		loadLevel(atLevel);
		// levelSplash.disappear();
		//levelSplash.setup();

		timer = new Timer();
		timer.setDuration(30000);
		timer.start();

		levelState = LevelState.PLAY;

	}

	public void loadLevel(int levelNumber) {

		if (levelNumber < levels.size()) {
			p5.println("Level Count: " + levels.size());

			LevelPack levelToLoad = levels.get(levelNumber);

			rings.clear();
			artifacts.clear();

			// MAKE A COPY (NOT REFERENCE) OF THE LEVELPACK INFO
			// .clear() will wipe out the obj in the list. These objects are
			// references, thus will wipe them all out of memory.
			// If I tried to access the same level twice, that level would have
			// been wiped out at .clear()
			// This "copy" constructor ensures that I do not pass a reference,
			// but a copy of.
			rings = new ArrayList<Ring>(levelToLoad.getRings());
			artifacts = new ArrayList<Artifact>(levelToLoad.getArtifacts());

			levelCompletedTrigger = false;
			isShipInsideRings = false;

			//levelSplash.setImage(levelToLoad.getSplashImage());
			//levelSplash.appear();
		}

	}

	private void buildLevelPacks() {

		XML levelData;
		levelData = p5.loadXML("levels/levelData_Single.xml");
		
		// LOAD ARTIFACT IMAGE
		PImage bounceArtifactImage = p5.loadImage("artifactBounce.png");
		PImage killArtifactImage = p5.loadImage("artifactKill.png");

		// LOAD LEVEL TREE
		XML[] allLevels = levelData.getChildren("level");
		p5.println("--- Levels found: " + allLevels.length);

		for (int i = 0; i < allLevels.length; i++) {

			LevelPack level = new LevelPack();

			PImage levelImage = p5.loadImage(allLevels[i].getString("imageUrl"));
			PImage splashImage = p5.loadImage(allLevels[i].getString("splashUrl"));
			
			level.setImage(levelImage);
			level.setSplashImage(splashImage);
			level.setAnimationTool(ani);
			level.setName(allLevels[i].getString("name"));

			p5.println("------------------------------------");
			p5.println("Level Name: " + allLevels[i].getString("name"));

			// LOAD RINGS TREE and ADD RINGS
			XML[] rings = allLevels[i].getChild("rings").getChildren("ring");
			p5.println("-- Rings found: " + rings.length);

			for (int j = 0; j < rings.length; j++) {
				float outLimit = rings[j].getFloat("outerLimit");
				float innLimit = rings[j].getFloat("innerLimit");
				float vel = rings[j].getFloat("velocity");

				p5.println("Out: " + outLimit + " / In: " + innLimit + " / Vel: " + vel);

				level.addRing(outLimit, innLimit, vel);

			}

			// LOAD ARTIFACTS TREE and ADD ARTIFACTS
			if (allLevels[i].getChild("artifacts").hasChildren()) {
				XML[] artifacts = allLevels[i].getChild("artifacts").getChildren("artifact");
				p5.println("-- Artifacts found: " + artifacts.length);
				for (int j = 0; j < artifacts.length; j++) {
					int type = artifacts[j].getInt("type");
					float x = artifacts[j].getFloat("x");
					float y = artifacts[j].getFloat("y");
					String name = artifacts[j].getString("name");
					String description = artifacts[j].getString("description");
					
					PImage actualArtifactImage;
					if (type == 0) {
						actualArtifactImage = bounceArtifactImage;
					} else {
						actualArtifactImage = killArtifactImage;
					}
					
					p5.println("Type: " + type + " / X: " + x + " / Y: " + y + " / Description: " + description);
					

					level.addArtifact(type, x, y, name, description, actualArtifactImage);
				}
			}

			levels.add(level);
		}

	}

	public void update() {

		//if (levelState == LevelState.PLAY) {

			// RING MOTION - BEGIN --------------------
			isShipInsideRings = false;

			for (int i = 0; i < rings.size(); i++) {
				Ring currentRing = rings.get(i);
				currentRing.update();

				if (currentRing.isInside(ship.getPosition())) {
					ship.setColor(p5.color(255, 255, 0));
					currentRing.modifyVelocity(ship.getPosition().x, ship.getPosition().y);
					ship.addForce(currentRing.getAngularPushVector(ship.getPosition()));

					isShipInsideRings = true;

				} else {
					// ship.setColor(p5.color(0, 255, 255));
					// ship.addForce(new PVector(0, 0));
				}

			}

			if (!isShipInsideRings) {
				ship.setColor(p5.color(0, 255, 255));
				ship.addForce(new PVector(0, 0));
			}

			// RING MOTION - END ---------------------

			ship.update();

			// ARTIFACTS - BEGIN

			for (int i = 0; i < artifacts.size(); i++) {

				artifacts.get(i).update();

				if (artifacts.get(i).collidedWith(ship.getPosition().x, ship.getPosition().y)) {
					if (artifacts.get(i).getType() == 1) {
						// GAME OVER
						p5.noFill();
						p5.stroke(255, 0, 0);
						p5.line(0, 0, p5.width, p5.height);
						p5.line(p5.width, 0, 0, p5.height);
						p5.noLoop();
					} else {
						ship.addForce(new PVector(p5.random(-200, 200), p5.random(-200, 200)));
					}

				}
			}
			// ARTIFACTS - END

			// DRAW FINNISH LINE
			p5.noFill();
			p5.stroke(255);
			p5.pushMatrix();
			p5.translate(0, 0, 20);
			p5.ellipse(rings.get(0).getPosition().x, rings.get(0).getPosition().y, 200, 200);
			p5.popMatrix();

			// CHECK FINISH --------------
			checkFinish();
		/*
		} else {
			if (levelSplash.fadeOutFinished()) {
				atLevel++;
				loadLevel(atLevel++);

			}
		}
		*/

	}

	public void render() {

		p5.hint(p5.DISABLE_DEPTH_TEST);

		for (int i = 0; i < rings.size(); i++) {
			Ring currentRing = rings.get(i);
			currentRing.renderOutlineMode();
			currentRing.renderImageMode();
		}

		// rings.get(1).render();
		// rings.get(1).render2();

		// DRAW EVERYTHING OVER THE RINGS
		// p5.hint(p5.DISABLE_DEPTH_TEST);

		for (int i = 0; i < artifacts.size(); i++) {
			artifacts.get(i).render();
		}

		ship.render();
		
		//levelSplash.render();
		
		p5.hint(p5.ENABLE_DEPTH_TEST);

		// DRAW FINISH ZONE
		p5.noFill();
		p5.stroke(127);
		p5.ellipse(rings.get(0).getPosition().x, rings.get(0).getPosition().y, ship.getSize(), ship.getSize());
		


	}

	public void render2D() {
		timer.update();
		timer.render();

		ship.render2D();
	}

	public void setShip(Ship _ship) {
		ship = _ship;
	}

	private void checkFinish() {

		// p5.println("CHECKING FINISH");

		// CHECK IF SHIP IS IN THE MIDDLE OF THE RINGS
		float shipDist = p5.dist(ship.getPosition().x, ship.getPosition().y, rings.get(0).getPosition().x, rings.get(0).getPosition().y);
		if (shipDist < ship.getSize()) {

			// p5.println("SHIP INSIDE FINSIH LINE");

			// LOOP THROUGH RINGS TO CHECK IF THEY ARE ALIGNED
			boolean ringsAligned = true;
			for (int i = 0; i < rings.size(); i++) {
				ringsAligned &= rings.get(i).isInTunnelLock();
				// p5.println("Ring " + i +" Locked");
			}

			// CHECKS TO TRIGGER ACTIONS ON LEVEL COMPLETED
			if (ringsAligned && !levelCompletedTrigger) {

				System.out.println("Level Completed");

				alignRings();
				//levelSplash.appear();
				//levelState = LevelState.SPLASH;
				
				gameMenuInstance.appear();

				levelCompletedTrigger = true;
			}
		}

	}

	private void alignRings() {
		for (int i = 0; i < rings.size(); i++) {
			rings.get(i).correctToFinalRotation();
		}
	}

	static public Ani getAni() {
		return ani;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

	public void onKeyPressed(char key) {
		if (key == '1') {
			atLevel = 0;
			loadLevel(atLevel);

		} else if (key == '2') {
			atLevel = 1;
			loadLevel(atLevel);
		}

		if (p5.keyCode == p5.RIGHT) {
			atLevel++;
			atLevel = p5.constrain(atLevel, 0, levels.size());
			loadLevel(atLevel);
		}
		if (p5.keyCode == p5.LEFT) {
			atLevel--;
			atLevel = p5.constrain(atLevel, 0, levels.size());
			loadLevel(atLevel);
		}

		if (p5.keyCode == p5.ENTER) {
			atLevel++;
			atLevel = p5.constrain(atLevel, 0, levels.size());
			loadLevel(atLevel);
			//levelSplash.disappear();
		}

	}
}
