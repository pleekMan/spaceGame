package rings;

import java.awt.Image;

import de.looksgood.ani.Ani;
import globals.Main;
import globals.PAppletSingleton;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class Ring {

	Main p5;
	Ani ani;

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
	public static float maxDiameterAllRings;

	boolean enableMotion;

	public Ring(float _x, float _y, float zDisplace, int _color, float _outer, float _inner) {
		p5 = getP5();

		pos = new PVector(_x, _y, zDisplace);
		ringColor = p5.color(_color);

		limitInner = _inner;
		limitOuter = _outer;

		maxDiameterAllRings = 700; // PONELE

		tunnelCenter = p5.PI;
		tunnelSpread = p5.TWO_PI * 0.03f;

		shipWentThrough = false;

		angularPos = 0;
		previousAngularPos = angularPos;
		angularVelMax = p5.TWO_PI * 0.01f;
		multiplier = 1f;

		enableMotion = true;

		// createRingBuffer();
		// ringBuffer = p5.createGraphics((int) limitOuter * 2, (int) limitOuter
		// * 2);
		// ringBuffer.mask(createRingMask());

	}

	public void update() {

		if (enableMotion) {
			previousAngularPos = angularPos;

			// COMPLETELY STOPPING THE ROTATION
			if (!(p5.abs(angularVelMax * multiplier) < 0.002f)) {
				angularPos += angularVelMax * multiplier;
			}
		}

	}

	public void renderImageMode() {

		// RING BUFFER DRAW - BEGIN
		ringBuffer.beginDraw();
		// ringBuffer.background(0,0);

		// ringBuffer.fill(0,10);;
		// ringBuffer.rect(0,0,ringBuffer.width, ringBuffer.height);

		ringBuffer.pushMatrix();	

		/*
		 * ringBuffer.textureMode(p5.NORMAL); ringBuffer.beginShape();
		 * ringBuffer.texture(ringImage); ringBuffer.vertex(0, 0, 0,0);
		 * ringBuffer.vertex(ringBuffer.width, 0, 1,0);
		 * ringBuffer.vertex(ringBuffer.width, ringBuffer.height, 1,1);
		 * ringBuffer.vertex(0, ringBuffer.height, 0,1);
		 * ringBuffer.endShape(p5.CLOSE);
		 */

		ringBuffer.translate(ringBuffer.width * 0.5f, ringBuffer.height * 0.5f);

		// p5.fill(0,255,0);
		// p5.text("Ring", 0, -10);

		ringBuffer.rotate(angularPos);
		// angularPos = angularPos > p5.TWO_PI ? 0f : angularPos;

		if (enableMotion) {
			float alpha = p5.map(getAngularVelocity(), 0, angularVelMax, 255, 10);
			ringBuffer.tint(255, 255, 255, alpha);
		} else {
			// WHEN LEVEL IS FINISHED AND WE ARE CORRECTING ROTATION
			ringBuffer.tint(255);
		}

		ringBuffer.imageMode(p5.CENTER);
		ringBuffer.rotate(p5.PI);

		ringBuffer.image(ringImage, 0, 0, ringBuffer.width, ringBuffer.height);

		ringBuffer.fill(255);
		ringBuffer.noStroke();
		ringBuffer.ellipse(100, 0, 20, 20);
		;

		ringBuffer.popMatrix();

		ringBuffer.endDraw();

		// RING BUFFER DRAW - END

		p5.pushMatrix();

		p5.translate(pos.x, pos.y);
		// p5.rotate(angularPos);
		// angularPos = angularPos > p5.TWO_PI ? 0f : angularPos;

		p5.imageMode(p5.CENTER);
		p5.image(ringBuffer, 0, 0);

		// p5.rectMode(p5.CENTER);
		// p5.rect(0, 0, ringBuffer.width, ringBuffer.height);

		p5.popMatrix();

	}

	public void renderOutlineMode() {

		p5.pushMatrix();

		p5.translate(pos.x, pos.y, pos.z);
		// p5.rotateX(p5.HALF_PI - 0.1f);
		p5.rotate(angularPos);
		angularPos = angularPos > p5.TWO_PI ? 0f : angularPos;
		angularPos = angularPos < 0f ? p5.TWO_PI : angularPos;

		p5.pushStyle();

		// RING
		p5.strokeWeight(1);
		p5.stroke(ringColor);
		// p5.fill(ringColor, 50);
		p5.noFill();
		p5.ellipse(0, 0, limitOuter, limitOuter);

		// p5.fill(0);
		// p5.ellipse(0, 0, limitInner * 2, limitInner * 2);

		// p5.line(limitInner, 0, limitOuter, 0);

		// DRAW TUNNEL
		if (isInTunnelLock()) {
			p5.strokeWeight(3);
		}
		// p5.strokeWeight(1);
		p5.noFill();
		for (float i = 0; i < 1.0f; i += 0.1f) {
			float distance = (limitInner + ((limitOuter - limitInner)) * i);
			p5.arc(0, 0, distance, distance, -tunnelSpread, tunnelSpread);
		}

		p5.popStyle();

		p5.popMatrix();

		// p5.text(angularPos, pos.x, pos.y - limitOuter);

		showTunnelCenter();

	}

	public PVector getPosition() {
		return pos;
	}

	private void showTunnelCenter() {

		p5.stroke(255, 0, 0);

		p5.pushMatrix();
		p5.translate(pos.x, pos.y);

		p5.rotate(tunnelCenter);
		p5.strokeWeight(3);
		p5.line(limitInner * 0.5f, 0, limitOuter * 0.5f, 0);

		p5.rotate(tunnelSpread);
		p5.line(limitInner * 0.5f, 0, limitOuter * 0.5f, 0);

		p5.rotate(-tunnelSpread * 2);
		p5.line(limitInner * 0.5f, 0, limitOuter * 0.5f, 0);

		p5.popMatrix();
	}

	public boolean isInside(float x, float y) {

		float distance = p5.dist(pos.x, pos.y, x, y);

		if (distance < (limitOuter * 0.5f) && distance > (limitInner * 0.5f)) {
			return true;
		} else {
			return false;
		}

	}

	public boolean isInside(PVector inPos) {

		float distance = p5.dist(pos.x, pos.y, inPos.x, inPos.y);

		if (distance < (limitOuter * 0.5f) && distance > (limitInner * 0.5f)) {
			return true;
		} else {
			return false;
		}

	}

	public float getDiameter() {
		return limitOuter;
	}

	public void setDiameter(float outer, float inner) {
		limitOuter = outer;
		limitInner = inner;
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
		multiplier = p5.norm(distance, limitInner * 0.5f, limitOuter * 0.5f);

	}

	public float getAngularVelocity() {

		// CORRECTION FOR: WHEN RESETTING angularPos to 0 after a whole turn
		// previousAngularPos would become bigger than angularPos, thus giving
		// out strange velocity
		if (angularVelMax > 0) {
			if (angularPos < previousAngularPos) {
				previousAngularPos = -previousAngularPos;
			}
		} else {
			if (angularPos > previousAngularPos) {
				//previousAngularPos = p5.TWO_PI + (previousAngularPos * 2);
				//previousAngularPos = p5.TWO_PI;
			}
		}
 
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

	public static void setMaxDiameterAllRings(float _maxDiameter) {
		maxDiameterAllRings = _maxDiameter;
	}

	public void setAngularVelocity(float vel) {
		angularVelMax = vel;
	}

	public void setImage(PImage _image) {
		ringImage = _image;

		ringBuffer = p5.createGraphics((int) maxDiameterAllRings, (int) maxDiameterAllRings);

		// SET BUFFER BACKGROUND TO TRANSPARENT
		ringBuffer.beginDraw();
		ringBuffer.background(0, 0);
		ringBuffer.endDraw();

		createRingMask(ringImage);
	}

	private void createRingMask(PImage _image) {
		PGraphics mask = p5.createGraphics(_image.width, _image.height);
		mask.beginDraw();
		mask.background(0);
		mask.fill(255);
		mask.ellipse(mask.width * 0.5f, mask.height * 0.5f, limitOuter, limitOuter);
		mask.fill(0);
		mask.ellipse(mask.width * 0.5f, mask.height * 0.5f, limitInner, limitInner);
		mask.endDraw();

		_image.mask(mask);
		// ringBuffer.mask(mask);
	}

	public void correctToFinalRotation() {
		enableMotion = false;
		// previousAngularPos = angularPos;
		ani.to(this, 1.0f, "angularPos", tunnelCenter);
	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

	public void setAniTool(Ani _ani) {
		ani = _ani;
	}

}
