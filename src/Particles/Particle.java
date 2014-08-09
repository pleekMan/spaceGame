package Particles;
import processing.core.PGraphics;
import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class Particle {
	
	Main p5;
	PVector pos;
	PVector vel;
	
	
	boolean active;
	
	public Particle(){
		p5 = getP5();
		
		active = true;
		
		pos = new PVector(p5.width * 0.5f, p5.height * 0.5f);
		vel = new PVector(p5.random(-3,3), p5.random(-3,3));
		
		
	}
	
	public void update(){
		pos.add(vel);
		checkReset();
	}
	
	
	public void render(){
		p5.strokeWeight(2);
		p5.stroke(255,127);
		p5.point(pos.x, pos.y);
	}
	
	public void render(PGraphics renderLayer) {
		renderLayer.strokeWeight(2);
		renderLayer.stroke(255);
		renderLayer.point(pos.x, pos.y);		
	}
	
	private void checkReset(){
		if (pos.x < 0 || pos.x > p5.width || pos.y < 0 || pos.y > p5.height) {
			pos.set(p5.width * 0.5f, p5.height * 0.5f);
		}
	}
	
	
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

	
}
