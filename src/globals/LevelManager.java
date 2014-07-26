package globals;

import rings.Ring;
import rings.Artifact;
import ships.Ship;

import java.util.ArrayList;

import de.looksgood.ani.Ani;
import processing.core.PImage;
import processing.core.PVector;

public class LevelManager {

	Main p5;
	Ani ani;

	Ship ship;
	ArrayList<Ring> rings;
	ArrayList<Artifact> artifacts;
	
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
		
		
	}

	public void setup(int _rings, Ship _ship) {

		ship = _ship;
		ringCount = _rings;
		artifactCount = 5;
		
		levelCompletedTrigger = false;
		isShipInsideRings = false;

		for (int i = 0; i < ringCount; i++) {
			Ring actualRing;
			actualRing = new Ring(p5.width * 0.5f, p5.height * 0.5f, i, p5.color(0, 200, 200 - (100 * i)), 200f - (100 * i), 300f - (100 * i));
			//actualRing.setImage(p5.loadImage("obraProxy.png"));
			//actualRing.setAngularVelocity(p5.TWO_PI * 0.005f);	
			actualRing.setAniTool(ani);
			rings.add(actualRing);
		}
		
		rings.get(0).setDiameter(700f, 500f);
		rings.get(1).setDiameter(500f, 200f);
		//rings.get(2).setDiameter(400f, 200f);
		
		Ring.setMaxDiameterAllRings(rings.get(0).getDiameter());
		
		for (int i = 0; i < rings.size(); i++) {
			PImage imagenObra = p5.loadImage("lauraPalavecino.png");
			rings.get(i).setImage(imagenObra);

		}

		rings.get(0).setAngularVelocity(p5.TWO_PI * 0.005f);
		rings.get(1).setAngularVelocity(-p5.TWO_PI * 0.005f);
		//rings.get(2).setAngularVelocity(p5.TWO_PI * 0.01f);

		
		
		for (int i = 0; i < artifactCount; i++) {
			Artifact actualArtifact;
			actualArtifact = new Artifact();
			actualArtifact.setup(p5.random(p5.width), p5.random(p5.height));
			artifacts.add(actualArtifact);
		}
		
		artifacts.get(0).setPosition(250,700);
		artifacts.get(1).setPosition(700,500);
		artifacts.get(2).setPosition(370,350);
		artifacts.get(3).setPosition(500,300);
		artifacts.get(4).setPosition(780,350);
		
		artifacts.get(0).setType(1);
		artifacts.get(1).setType(1);
		artifacts.get(2).setType(0);
		artifacts.get(3).setType(1);
		artifacts.get(4).setType(0);

		timer = new Timer();
		timer.setDuration(30000);
		timer.start();

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
}
