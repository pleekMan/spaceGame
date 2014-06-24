package rings;

import globals.Main;
import globals.PAppletSingleton;
import processing.core.PVector;

public class Ring {

	Main p5;
	PVector pos;
	float angularPos;
	float previousAngularPos;
	float angularVelMax;
	float multiplier;

	float limitInner, limitOuter;
	int ringColor;

	float tunnelCenter;
	float tunnelSpread;

	public Ring(float _x, float _y, int _color) {
		p5 = getP5();

		pos = new PVector(_x, _y);
		ringColor = p5.color(_color);

		limitInner = 100;
		limitOuter = 300;

		tunnelCenter = 0f;
		tunnelSpread = p5.TWO_PI * 0.1f;

		angularPos = 0;
		angularVelMax = p5.TWO_PI * 0.01f;
		multiplier = 1f;
	}

	public void update() {

		previousAngularPos = angularPos;
		angularPos += angularVelMax * multiplier;

	}

	public void render() {

		p5.pushMatrix();

		p5.translate(pos.x, pos.y);
		p5.rotate(angularPos);
		angularPos = angularPos > p5.TWO_PI ? 0f : angularPos;

		p5.pushStyle();

		p5.strokeWeight(1);

		p5.stroke(ringColor);
		p5.fill(ringColor, 50);
		p5.ellipse(0, 0, limitOuter * 2, limitOuter * 2);

		p5.fill(0);
		p5.ellipse(0, 0, limitInner * 2, limitInner * 2);

		p5.line(limitInner, 0, limitOuter, 0);

		// DRAW TUNNEL
		p5.strokeWeight(5);
		p5.arc(0, 0, limitInner * 2, limitInner * 2, tunnelCenter - tunnelSpread, tunnelCenter + tunnelSpread);

		p5.popStyle();

		p5.popMatrix();
	}

	public PVector getPosition() {
		return pos;
	}

	public boolean isInside(float x, float y) {

		float distance = p5.dist(pos.x, pos.y, x, y);

		if (distance < limitOuter && distance > limitInner) {
			return true;
		} else {
			return false;
		}

	}

	public void modifyVelocity(float x, float y) {

		float distance = p5.dist(pos.x, pos.y, x, y);
		multiplier = p5.norm(distance, limitInner, limitOuter);

	}

	public float getAngularVelocity() {
		return angularPos - previousAngularPos;
	}

	public PVector getAngularPushVector(PVector inVector) {

		PVector shipToCenter = new PVector(pos.x - inVector.x, pos.y - inVector.y);
		System.out.println(shipToCenter);

		p5.stroke(0, 0, 255);
		p5.line(inVector.x, inVector.y, inVector.x + shipToCenter.x, inVector.y + shipToCenter.y);

		if (getAngularVelocity() < 0) {
			shipToCenter.rotate(p5.HALF_PI);
		} else {
			shipToCenter.rotate(-p5.HALF_PI);

		}
		p5.stroke(0, 255, 0);
		p5.line(inVector.x, inVector.y, inVector.x + shipToCenter.x, inVector.y + shipToCenter.y);

		shipToCenter.normalize();
		float mg = p5.dist(inVector.x, inVector.y, pos.x, pos.y);
		shipToCenter.mult(getAngularVelocity() * mg);
		System.out.println("ShipToCenter: " + p5.dist(inVector.x, inVector.y, pos.x, pos.y) + " / AngVel: " + getAngularVelocity() + " / Result: " + shipToCenter.mag()  *  getAngularVelocity());
		p5.stroke(255, 0, 0);
		p5.line(inVector.x, inVector.y, inVector.x + shipToCenter.x, inVector.y + shipToCenter.y);

		return shipToCenter;
	}
	
	public PVector getAngularPushVector2(PVector inVector) {

		PVector shipToCenter = new PVector(pos.x - inVector.x, pos.y - inVector.y);
		float vectorMagnitude = shipToCenter.mag();
		
		
		
		
		return shipToCenter;
	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
