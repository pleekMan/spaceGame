package globals;

import rings.Ring;
import rings.Artifact;
import ships.Ship;

import java.util.ArrayList;

import processing.core.PVector;

public class LevelManager {

	Main p5;

	Ship ship;
	ArrayList<Ring> rings;
	ArrayList<Artifact> artifacts;
	
	int ringCount;
	int artifactCount;
	
	//boolean levelCompleted;

	boolean isShipInsideRings = false;

	public LevelManager() {
		p5 = getP5();

		rings = new ArrayList<Ring>();
		artifacts = new ArrayList<Artifact>();

	}

	public void setup(int _rings, Ship _ship) {

		ship = _ship;
		ringCount = _rings;
		artifactCount = 5;

		for (int i = 0; i < ringCount; i++) {
			Ring actualRing;
			actualRing = new Ring(p5.width * 0.5f, p5.height * 0.5f, p5.color(0, 200, 200 - (100 * i)), 200f - (100 * i), 300f - (100 * i));
			actualRing.setImage(p5.loadImage("image01.png"));
			actualRing.setAngularVelocity(p5.TWO_PI * 0.005f);
			rings.add(actualRing);
		}
		rings.get(1).setAngularVelocity(-p5.TWO_PI * 0.01f);
		
		
		for (int i = 0; i < artifactCount; i++) {
			Artifact actualArtifact;
			actualArtifact = new Artifact();
			actualArtifact.setup(p5.random(p5.width), p5.random(p5.height));
			artifacts.add(actualArtifact);
		}
		


	}

	public void update() {

		// RING MOTION - BEGIN --------------------
		isShipInsideRings = false;

		for (int i = 0; i < rings.size(); i++) {
			Ring currentRing = rings.get(i);
			currentRing.update();

			if (currentRing.isInside(ship.getPosition().x, ship.getPosition().y)) {
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
		
		for (int i = 0; i < artifacts.size(); i++) {
			
			if (artifacts.get(i).collidedWith(ship.getPosition().x, ship.getPosition().y)) {
				p5.noFill();
				p5.stroke(255,0,0);
				p5.line(0, 0, p5.width, p5.height);
				p5.line(p5.width, 0, 0, p5.height);
				p5.noLoop();
			}
		}
		
		checkFinish();
		

	}

	public void render() {

		for (int i = 0; i < rings.size(); i++) {
			Ring currentRing = rings.get(i);
			currentRing.render2();
		}
		
		for (int i = 0; i < artifacts.size(); i++) {
			artifacts.get(i).render();
		}

		ship.render();
		
		// DRAW FINISH ZONE
		p5.noFill();
		p5.stroke(127);
		p5.ellipse(rings.get(0).getPosition().x, rings.get(0).getPosition().y, ship.getSize(), ship.getSize());
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
			
			if(ringsAligned)System.out.println("Level Completed");
		}
		
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
