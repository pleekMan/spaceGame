package rings;

import java.awt.Image;

import globals.Main;
import globals.PAppletSingleton;
import processing.core.PGraphics;
import processing.core.PImage;
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
	boolean shipWentThrough;

	PGraphics ringBuffer;
	PImage ringImage;
	PImage ringImageMask;

	public Ring(float _x, float _y, int _color, float _inner, float _outer) {
		p5 = getP5();

		pos = new PVector(_x, _y);
		ringColor = p5.color(_color);

		limitInner = _inner;
		limitOuter = _outer;

		tunnelCenter = p5.PI;
		tunnelSpread = p5.TWO_PI * 0.05f;

		shipWentThrough = false;

		angularPos = 0;
		angularVelMax = p5.TWO_PI * 0.005f;
		multiplier = 1f;

		ringBuffer = p5.createGraphics((int) limitOuter * 2, (int) limitOuter * 2);
		// ringBuffer.mask(createRingMask());

	}

	public void update() {

		previousAngularPos = angularPos;

		if(!(p5.abs(angularVelMax * multiplier) < 0.002f)){
			angularPos += angularVelMax * multiplier;
		}

	}

	public void render() {

		// RING BUFFER DRAW - BEGIN
		ringBuffer.beginDraw();
		// ringBuffer.background(0,0);

		ringBuffer.pushMatrix();
		ringBuffer.translate(ringBuffer.width * 0.5f, ringBuffer.height * 0.5f);
		ringBuffer.rotate(angularPos);
		// angularPos = angularPos > p5.TWO_PI ? 0f : angularPos;

		float alpha = p5.map(getAngularVelocity(), 0, angularVelMax, 255, 10);
		ringBuffer.tint(255, 255, 255, alpha);
		ringBuffer.imageMode(p5.CENTER);
		ringBuffer.image(ringImage, 0, 0, ringBuffer.width, ringBuffer.height);

		ringBuffer.popMatrix();

		ringBuffer.endDraw();

		// RING BUFFER DRAW - END

		p5.pushMatrix();

		p5.translate(pos.x, pos.y);
		// p5.rotate(angularPos);
		// angularPos = angularPos > p5.TWO_PI ? 0f : angularPos;

		/*
		 * ringBuffer.beginDraw(); ringBuffer.background(0,0);
		 * 
		 * ringBuffer.pushStyle();
		 * 
		 * ringBuffer.image(ringImage, 0, 0, ringBuffer.width,
		 * ringBuffer.height);
		 * 
		 * ringBuffer.strokeWeight(1);
		 * 
		 * ringBuffer.stroke(ringColor); ringBuffer.fill(ringColor, 50);
		 * ringBuffer.ellipse(0, 0, limitOuter * 2, limitOuter * 2);
		 * 
		 * ringBuffer.fill(0); ringBuffer.ellipse(0, 0, limitInner * 2,
		 * limitInner * 2);
		 * 
		 * ringBuffer.line(limitInner, 0, limitOuter, 0);
		 * 
		 * // DRAW TUNNEL ringBuffer.strokeWeight(5); ringBuffer.arc(0, 0,
		 * limitInner * 2, limitInner * 2, tunnelCenter - tunnelSpread,
		 * tunnelCenter + tunnelSpread);
		 * 
		 * ringBuffer.popStyle();
		 * 
		 * ringBuffer.endDraw();
		 */

		p5.imageMode(p5.CENTER);
		p5.image(ringBuffer, 0, 0);

		p5.popMatrix();

	}

	public void render2() {
		/*
		 * ringBuffer.beginDraw();
		 * 
		 * ringBuffer.pushMatrix();
		 * 
		 * ringBuffer.translate(pos.x, pos.y); ringBuffer.rotate(angularPos); //
		 * angularPos = angularPos > p5.TWO_PI ? 0f : angularPos;
		 * 
		 * ringBuffer.background(0,0);
		 * 
		 * ringBuffer.pushStyle();
		 * 
		 * ringBuffer.strokeWeight(1);
		 * 
		 * ringBuffer.stroke(ringColor); ringBuffer.fill(ringColor, 50);
		 * ringBuffer.ellipse(0, 0, limitOuter * 2, limitOuter * 2);
		 * 
		 * ringBuffer.fill(0); ringBuffer.ellipse(0, 0, limitInner * 2,
		 * limitInner * 2);
		 * 
		 * ringBuffer.line(limitInner, 0, limitOuter, 0);
		 * 
		 * // DRAW TUNNEL ringBuffer.strokeWeight(5); ringBuffer.arc(0, 0,
		 * limitInner * 2, limitInner * 2, tunnelCenter - tunnelSpread,
		 * tunnelCenter + tunnelSpread);
		 * 
		 * ringBuffer.popStyle();
		 * 
		 * ringBuffer.popMatrix();
		 * 
		 * ringBuffer.endDraw();
		 * 
		 * 
		 * p5.imageMode(p5.CENTER); p5.image(ringBuffer, 0, 0);
		 */
		// //-------------

		p5.pushMatrix();

		p5.translate(pos.x, pos.y);
		//p5.rotateX(p5.HALF_PI - 0.1f);
		p5.rotate(angularPos);
		angularPos = angularPos > p5.TWO_PI ? 0f : angularPos;
		angularPos = angularPos < 0f ? p5.TWO_PI : angularPos;

		p5.pushStyle();

		p5.strokeWeight(1);

		p5.stroke(ringColor);
		p5.fill(ringColor, 50);
		p5.ellipse(0, 0, limitOuter * 2, limitOuter * 2);

		// p5.fill(0);
		p5.ellipse(0, 0, limitInner * 2, limitInner * 2);

		p5.line(limitInner, 0, limitOuter, 0);

		// DRAW TUNNEL
		if (isInTunnelLock()) {
			p5.strokeWeight(7);
		}
		p5.strokeWeight(2);
		p5.arc(0, 0, limitOuter * 2, limitOuter * 2, -tunnelSpread, tunnelSpread);

		p5.popStyle();

		p5.popMatrix();

		p5.text(angularPos, pos.x, pos.y - limitOuter);

		showTunnelCenter();

	}

	public PVector getPosition() {
		return pos;
	}

	private void showTunnelCenter() {

		p5.pushMatrix();
		p5.translate(pos.x, pos.y);

		p5.rotate(tunnelCenter);
		p5.strokeWeight(3);
		p5.line(limitInner, 0, limitOuter, 0);

		p5.rotate(tunnelSpread);
		p5.line(limitInner, 0, limitOuter, 0);

		p5.rotate(-tunnelSpread * 2);
		p5.line(limitInner, 0, limitOuter, 0);

		p5.popMatrix();
	}

	public boolean isInside(float x, float y) {

		float distance = p5.dist(pos.x, pos.y, x, y);

		if (distance < limitOuter && distance > limitInner) {
			return true;
		} else {
			return false;
		}

	}

	public boolean isInTunnelLock() {
		if (angularPos > (tunnelCenter - tunnelSpread) && angularPos < (tunnelCenter + tunnelSpread)) {
			p5.fill(255, 100);
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

		// System.out.println(shipToCenter);

		p5.stroke(0, 0, 255);
		p5.line(inVector.x, inVector.y, inVector.x + shipToCenter.x, inVector.y + shipToCenter.y);

		/*
		 * if (getAngularVelocity() < 0) { shipToCenter.rotate(p5.HALF_PI); }
		 * else { shipToCenter.rotate(-p5.HALF_PI);
		 * 
		 * }
		 */
		shipToCenter.rotate(-p5.HALF_PI);

		p5.stroke(0, 255, 0);
		p5.line(inVector.x, inVector.y, inVector.x + shipToCenter.x, inVector.y + shipToCenter.y);

		shipToCenter.normalize();
		float mg = p5.dist(inVector.x, inVector.y, pos.x, pos.y);
		shipToCenter.mult(getAngularVelocity() * mg);
		// System.out.println("ShipToCenter: " + mg + " / AngVel: " +
		// getAngularVelocity() + " / Result: " + getAngularVelocity() * mg);
		p5.stroke(255, 0, 0);
		p5.line(inVector.x, inVector.y, inVector.x + shipToCenter.x, inVector.y + shipToCenter.y);

		return shipToCenter;
	}

	public void setAngularVelocity(float vel) {
		angularVelMax = vel;
	}

	public void setImage(PImage _image) {
		ringImage = _image;

		createRingMask(ringImage);
	}

	private void createRingMask(PImage _image) {
		PGraphics mask = p5.createGraphics(_image.width, _image.height);
		mask.beginDraw();
		mask.background(0);
		mask.fill(255);
		mask.ellipse(mask.width * 0.5f, mask.height * 0.5f, mask.width, mask.height);
		mask.endDraw();

		_image.mask(mask);

	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
