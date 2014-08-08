package levels;

import java.awt.Rectangle;
import java.util.ArrayList;

import ddf.minim.AudioPlayer;
import de.looksgood.ani.Ani;
import processing.core.PImage;
import globals.LevelManager;
import globals.Main;
import globals.PAppletSingleton;

public class GameMenu {
	Main p5;

	ArrayList<Rectangle> buttons;
	ArrayList<PImage> infoBoxes;
	PImage backImage;
	PImage wheel;

	float animMultiplier;

	int levelSelected;

	AudioPlayer sonido;

	public GameMenu() {
		p5 = getP5();
	}

	public void setup() {

		animMultiplier = 1f;

		backImage = p5.loadImage("MenuBack.png");
		wheel = p5.loadImage("wheel_blank.png");

		levelSelected = -1;

		buttons = new ArrayList<Rectangle>();
		infoBoxes = new ArrayList<PImage>();

		for (int i = 0; i < 8; i++) {
			Rectangle nuevoBoton = new Rectangle((int) (200 * i), 200, 80, 80);
			buttons.add(nuevoBoton);

		}
		placeButtons();
		loadInfoBoxes();

		// sonido = LevelManager.minim.loadFile(filename);

	}


	public void update() {
		/*
		 * if ( isActive && !fadeOut.isPlaying()) { isActive = false; }
		 */

	}

	public void render() {

		isHovering();

		p5.imageMode(p5.CENTER);
		p5.pushStyle();

		if (animMultiplier > 0.01) {

			p5.tint(255, 255 * animMultiplier);

			p5.pushMatrix();

			p5.translate(p5.width * 0.5f, p5.height * 0.5f);
			p5.scale(1 + (1 - animMultiplier));

			p5.image(backImage, 0, 0);

			p5.popStyle();

			p5.image(wheel, 0, 15);
			
			if(levelSelected != -1){
				p5.image(infoBoxes.get(levelSelected), 0, 15);
			}
			p5.popMatrix();

		}

		for (int i = 0; i < buttons.size(); i++) {
			p5.rect(buttons.get(i).x, buttons.get(i).y, buttons.get(i).width, buttons.get(i).height);
		}

	}

	public void appear() {
		// fadeIn.to(this, 1f, 0, "imageTint", 255);
		LevelManager.getAni().to(this, 1f, 4f, "animMultiplier", 1f);
	}

	public void disappear() {
		LevelManager.getAni().to(this, 1f, 0, "animMultiplier", 0f);
	}

	private void placeButtons() {

		buttons.get(0).x = 472;
		buttons.get(0).y = 120;

		buttons.get(1).x = 645;
		buttons.get(1).y = 185;

		buttons.get(2).x = 717;
		buttons.get(2).y = 360;

		buttons.get(3).x = 645;
		buttons.get(3).y = 530;

		buttons.get(4).x = 472;
		buttons.get(4).y = 605;

		buttons.get(5).x = 300;
		buttons.get(5).y = 530;

		buttons.get(6).x = 230;
		buttons.get(6).y = 360;

		buttons.get(7).x = 300;
		buttons.get(7).y = 185;

	}
	

	private void loadInfoBoxes() {
		infoBoxes.add(p5.loadImage("levels/5/info.png"));
		infoBoxes.add(p5.loadImage("levels/1/info.png"));
		infoBoxes.add(p5.loadImage("levels/5/info.png"));
		infoBoxes.add(p5.loadImage("levels/1/info.png"));
		infoBoxes.add(p5.loadImage("levels/5/info.png"));
		infoBoxes.add(p5.loadImage("levels/1/info.png"));
		infoBoxes.add(p5.loadImage("levels/5/info.png"));
		infoBoxes.add(p5.loadImage("levels/1/info.png"));		
	}

	public void onMousePressed() {
		/*
		 * for (int i = 0; i < buttons.size(); i++) { if
		 * (buttons.get(i).contains(p5.mouseX, p5.mouseY)) { levelSelected = i;
		 * p5.println("LevelSelected: " + levelSelected); break; } }
		 */
		
		//launch(levelSelected);
		
		
	}

	public void isHovering() {
		levelSelected = -1;
		for (int i = 0; i < buttons.size(); i++) {
			if (buttons.get(i).contains(p5.mouseX, p5.mouseY)) {
				levelSelected = i;
				p5.println("Hovering Over: " + levelSelected);
				break;
			}
		}
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

	public int getSelectedLevel() {
		return levelSelected;
	}

}
