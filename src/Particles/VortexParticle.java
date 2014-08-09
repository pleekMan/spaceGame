package Particles;

import processing.core.PGraphics;
import globals.Main;
import globals.PAppletSingleton;

public class VortexParticle {

	Main p5;
	
	float radius, radiusIncrement;;
	float angle, angleIncrement;
	
	float prevX, prevY;
	
	int colorcito;
	
	
	public VortexParticle(){
		p5 = getP5();
		
		float multiplierLink = p5.random(1);
		
		radius = p5.random(p5.width * 0.5f);
		radiusIncrement = 1.8f;
		angle = p5.random(0,p5.TWO_PI);
		angleIncrement = p5.random(-0.015f,-0.05f);
		
		prevX = prevY = 0;
		
		colorcito = p5.color(p5.random(200,255),p5.random(127,255),0);

	}
	
	public void update(){
		angle += angleIncrement;
		radius += radiusIncrement;
		
		if(radius > p5.width * 0.6f){
			radius = 0;
			prevX = prevY = 0;
		}
		
		//p5.println("Angle: " + angle);
	}
	
	public void render(){
		p5.stroke(255);
		p5.point(radius + p5.cos(angle), radius + p5.sin(angle));
	}
	
	public void render(PGraphics renderLayer){
		
		float x = radius * p5.cos(angle);
		float y = radius * p5.sin(angle);
		
		renderLayer.stroke(colorcito);
		
		renderLayer.line(x,y, prevX, prevY);
		
		prevX = x;
		prevY = y;
	}
	
	
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
