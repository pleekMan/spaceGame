package globals;

import rings.Ring;
import java.util.ArrayList;

public class LevelManager {

	Main p5;
	
	int ringCount;
	ArrayList<Ring> rings;
	
	public LevelManager(){
		p5 = getP5();
		
	}
	
	public void setup(int _rings){
		ringCount = _rings;
		
		
	}
	
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
