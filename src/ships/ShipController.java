package ships;

import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class ShipController {

	Main p5;
	PVector center;
	PVector force;
	int maxRadius;
	float attenuator;

	public ShipController() {
		p5 = getP5();

		center = new PVector();
		force = new PVector();
		maxRadius = 150;
		attenuator = 0.5f;
	}

	public void render() {

		// RENDER CONTROLLER GUI

		p5.noFill();
		p5.strokeWeight(2);
		for (float i = 1; i > 0; i -= 0.1f) {
			p5.stroke(200, 200 - (200 * i));
			p5.ellipse(center.x, center.y, maxRadius * (i), maxRadius * (i));
		}

	}

	public PVector getForce() {
		force.set(p5.mouseX - center.x, p5.mouseY - center.y);
		force.mult(attenuator);
		return force;
	}
	
	public void setAttenuation(float _attenuation){
		attenuator = _attenuation;
	}
	
	public void setPosition(int _x, int _y){
		center.set(_x, _y, 0);
	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
