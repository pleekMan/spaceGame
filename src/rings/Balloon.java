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
		boxWidth = 300;
		boxHeight = 70;

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

		p5.pushStyle();
		
		p5.noStroke();

		// BODY BOX
		p5.fill(230, 105, 40, alpha - 50);
		p5.rect(posX - 3, posY - 15, boxWidth + 6, boxHeight + 30, 0,0,15,15);

		// TITLE BOX
		p5.fill(170, 0, 0, alpha - 50);
		p5.rect(posX - 3, posY - 15, boxWidth + 6, -30, 15,15,0,0);
		
		// NAME TEXT
		p5.textSize(20);
		p5.fill(255, alpha);
		p5.text(name, posX, posY - 20);

		// DESCRIPTION TEXT
		p5.textSize(15);
		p5.fill(255, alpha);
		p5.text(description, posX, posY, boxWidth, boxHeight);
		
		p5.popStyle();

	}

	void appear() {
		if (!isShowing) {
			// p5.println("APPEARING");
			LevelManager.getAni().to(this, 0.5f, "posY", posY + yOffset);
			LevelManager.getAni().to(this, 0.5f, "alpha", 255);
			isShowing = true;
		}
	}

	void disappear() {
		if (isShowing) {
			LevelManager.getAni().to(this, 0.5f, "posY", posY - yOffset);
			LevelManager.getAni().to(this, 0.5f, "alpha", 0);
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
