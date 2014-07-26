package globals;

import rings.Ring;
import rings.Artifact;
import ships.Ship;

import java.util.ArrayList;
import java.util.Collections;

import de.looksgood.ani.Ani;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.XML;

public class LevelManager {

	Main p5;
	Ani ani;

	Ship ship;
	ArrayList<Ring> rings;
	ArrayList<Artifact> artifacts;
	ArrayList<LevelPack> levels;
	
	int ringCount;
	int artifactCount;
	
	boolean levelCompletedTrigger;

	boolean isShipInsideRings;
	
	Timer timer;

	public LevelManager() {
		p5 = getP5();
		Ani.init(p5);

		rings = new ArrayList<Ring>();
		artifacts = new ArrayList<Artifact>();
		levels = new ArrayList<LevelPack>();
		
		
	}

	public void setup(int _rings, Ship _ship) {

		ship = _ship;
		ringCount = _rings;
		artifactCount = 5;
		
		
		buildLevelPacks();

		loadLevel(0);


		timer = new Timer();
		timer.setDuration(30000);
		timer.start();

	}
	
	private void loadLevel(int levelNumber){
		p5.println("Level Count: " + levels.size());
		
		LevelPack levelToLoad = levels.get(levelNumber);
		
		rings.clear();
		artifacts.clear();
		
		// MAKE A COPY (NOT REFERENCE) OF THE LEVELPACK INFO
		//.clear() will wipe out the obj in the list. These objects are references, thus will wipe them all out of memory.
		// If I tried to access the same level twice, that level would have been wiped out at .clear()
		// This "copy" constructor ensures that I do not pass a reference, but a copy of.
		rings = new ArrayList<Ring>(levelToLoad.getRings());
		artifacts = new ArrayList<Artifact>(levelToLoad.getArtifacts());
		
		levelCompletedTrigger = false;
		isShipInsideRings = false;
		
		
	}
	
	private void buildLevelPacks(){
		
		XML levelData;
		levelData = p5.loadXML("levels/levelData.xml");
		
		// LOAD LEVEL TREE
		XML[] allLevels = levelData.getChildren("level");
		p5.println("--- Levels found: " + allLevels.length);
		
		for (int i = 0; i < allLevels.length; i++) {
			
			LevelPack level = new LevelPack();
			
			PImage levelImage = p5.loadImage(allLevels[i].getString("imageUrl"));
			level.setImage(levelImage);
			level.setAnimationTool(ani);
			level.setName(allLevels[i].getString("name"));
			
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
			XML[] artifacts = allLevels[i].getChild("artifacts").getChildren("artifact");
			p5.println("-- Artifacts found: " + artifacts.length);
			for (int j = 0; j < artifacts.length; j++) {
				int type = artifacts[j].getInt("type");
				float x = artifacts[j].getFloat("x");
				float y = artifacts[j].getFloat("y");
				String description = artifacts[j].getString("description");
				
				p5.println("Type: " + type + " / X: " + x + " / Y: " + y + " / Description; " + description);

				
				level.addArtifact(type, x, y, description);
			}
			
			levels.add(level);
		}
		
		
	}

	public void update() {

				
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
		/*
		for (int i = 0; i < artifacts.size(); i++) {
			
			if (artifacts.get(i).collidedWith(ship.getPosition().x, ship.getPosition().y)) {
				if (artifacts.get(i).getType() == 0) {
					p5.noFill();
					p5.stroke(255,0,0);
					p5.line(0, 0, p5.width, p5.height);
					p5.line(p5.width, 0, 0, p5.height);
					p5.noLoop();
				} else {
					ship.addForce(new PVector(p5.random(-20, 20), p5.random(-20,20)));
				}
				
			}
		}
		*/
		// ARTIFACTS - END

		
		// DRAW FINNISH LINE
		p5.noFill();
		p5.stroke(255);
		p5.pushMatrix();
		p5.translate(0, 0, 20);
		p5.ellipse(rings.get(0).getPosition().x, rings.get(0).getPosition().y, 200,200);
		p5.popMatrix();
		
		// CHECK FINISH --------------
		checkFinish();
		

	}

	public void render() {

		p5.hint(p5.DISABLE_DEPTH_TEST);

		
		for (int i = 0; i < rings.size(); i++) {
			Ring currentRing = rings.get(i);
			//currentRing.renderImageMode();
			currentRing.renderOutlineMode();
		}
		
		
		//rings.get(1).render();
		//rings.get(1).render2();
		
		// DRAW EVERYTHING OVER THE RINGS
		//p5.hint(p5.DISABLE_DEPTH_TEST);

		for (int i = 0; i < artifacts.size(); i++) {
			artifacts.get(i).render();
		}

		ship.render();
		p5.hint(p5.ENABLE_DEPTH_TEST);
		
		// DRAW FINISH ZONE
		p5.noFill();
		p5.stroke(127);
		p5.ellipse(rings.get(0).getPosition().x, rings.get(0).getPosition().y, ship.getSize(), ship.getSize());
	}
	
	public void render2D(){
		timer.update();
		timer.render();
		
		ship.render2D();
	}

	public void setShip(Ship _ship) {
		ship = _ship;
	}
	
	private void checkFinish(){
		
		// CHECK IF SHIP IS IN THE MIDDLE OF THE RINGS
		float shipDist = p5.dist(ship.getPosition().x, ship.getPosition().y, rings.get(0).getPosition().x, rings.get(0).getPosition().x);
		if(shipDist < ship.getSize()){
			
			// LOOP THROUGH RINGS TO CHECK IF THEY ARE ALIGNED
			boolean ringsAligned = true;
			for (int i = 0; i < rings.size(); i++) {
				ringsAligned &= rings.get(i).isInTunnelLock();
			}
			
			// CHECKS TO TRIGGER ACTIONS ON LEVEL COMPLETED
			if(ringsAligned && !levelCompletedTrigger){
				
				System.out.println("Level Completed");
				
				alignRings();
				
				levelCompletedTrigger = true;
			}
		}
		
	}

	private void alignRings() {
		for (int i = 0; i < rings.size(); i++) {
			rings.get(i).correctToFinalRotation();
		}
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

	public void onKeyPressed(char key) {
		if(key == '1'){
			loadLevel(0);
		} else if (key == '2'){
			loadLevel(1);
		}
		
	}
}
