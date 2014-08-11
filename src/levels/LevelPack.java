package levels;

import globals.Main;
import globals.PAppletSingleton;

import java.util.ArrayList;

import de.looksgood.ani.Ani;
import processing.core.PImage;
import rings.Artifact;
import rings.Ring;

public class LevelPack {
	
	Main p5;
	
	String name;
	PImage image;
	PImage splashImage;
	ArrayList<Artifact> artifacts;
	ArrayList<Ring> rings;
	
	Ani ani;
	
	public LevelPack(){
		p5 = getP5();
		
		name = "";
		artifacts = new ArrayList<Artifact>();
		rings = new ArrayList<Ring>();
		
		
	}
	
	public void addRing(float outerLimit, float innerLimit, float velocity){
		
		Ring newRing = new Ring(p5.width * 0.5f, p5.height * 0.5f, 0f, p5.color(p5.random(127,255)), outerLimit, innerLimit);
		
		newRing.setDiameter(outerLimit, innerLimit);
		newRing.setAngularVelocity(velocity);
		newRing.setAniTool(ani);
		
		//if(rings.isEmpty()){
			//Ring.setMaxDiameterAllRings(newRing.getDiameter());
		//}
		
		
		PImage newImageToRing = image.get();
		newRing.setImage(newImageToRing);
		
		rings.add(newRing);
		
		
	}
	
	public void addArtifact(int type, float _x, float _y, String _name, String description, PImage _artifactImage, String soundPath){
		
		Artifact newArtifact = new Artifact();
		newArtifact.setup(type, _x, _y, _name, description);
		newArtifact.setImage(_artifactImage);
		newArtifact.setSound(soundPath);
		
		artifacts.add(newArtifact);
	}
	
	public void setImage(PImage _image){
		image = _image;
	}
	
	public void setSplashImage(PImage _splashImage){
		splashImage = _splashImage;
	}
	
	public PImage getSplashImage(){
		return splashImage;
	}
	
	public void setName(String _name){
		name = _name;
	}
	public String getName(){
		return name;
	}
	
	public void setAnimationTool(Ani _ani){
		ani = _ani;
	}
	
	public ArrayList<Ring> getRings(){
		return rings;
	}
	
	public ArrayList<Artifact> getArtifacts(){
		return artifacts;
	}
	
	
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
