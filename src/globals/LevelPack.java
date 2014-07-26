package globals;

import java.util.ArrayList;

import processing.core.PImage;
import rings.Artifact;
import rings.Ring;

public class LevelPack {
	
	Main p5;
	
	PImage image;
	ArrayList<Artifact> artifacts;
	ArrayList<Ring> rings;
	
	public void LevelPack(String levelUrl){
		p5 = getP5();
		
		
		
	}
	
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
