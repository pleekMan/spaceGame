package rings;

import globals.LevelManager;
import globals.Main;
import globals.PAppletSingleton;
import de.looksgood.ani.Ani;

public class Balloon {
	Main p5;

	// Ani animation;

	float posX, posY;
	float yOffset;
	int boxWidth, boxHeight;
	
	String name;
	String description;
	float alpha;

	boolean isShowing;

	public Balloon(float _x, float _y, String _name, String _description) {
		p5 = getP5();

		posX = _x;
		posY = _y;
		yOffset = -20;
		boxWidth = 400;
		boxHeight = 30;
		
		// RE-POSITION posY
		posY -= boxHeight + 10;
		
		name = _name;
		description = _description;

		alpha = 0;

		isShowing = false;

	}

	void update() {

	}

	void render() {

		p5.noStroke();
		p5.fill(50, 200, 50, alpha);
		p5.rect(posX - 3, posY - 15, boxWidth + 6, boxHeight + 30);

		p5.fill(255, alpha);
		p5.text(name, posX, posY);
		
		p5.fill(255, alpha);
		p5.text(description, posX, posY + 12, boxWidth, boxHeight);

	}

	void appear() {
		if (!isShowing) {
			//p5.println("APPEARING");
			LevelManager.getAni().to(this, 0.5f, "posY", posY + yOffset);
			LevelManager.getAni().to(this, 0.5f, "alpha", 255);
			isShowing = true;
		}
	}

	void disappear() {
		if (isShowing) {
			LevelManager.getAni().to(this, 0.5f, 1, "posY", posY - yOffset);
			LevelManager.getAni().to(this, 0.5f, 1, "alpha", 0);
			isShowing = false;
		}
	}

	void setIsShowing(boolean state) {
		isShowing = state;
	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

	public void setAni(Ani _ani) {

	}
}
