package levels;

import java.awt.Rectangle;
import java.util.ArrayList;

import Particles.VortexParticle;
import ddf.minim.AudioPlayer;
import de.looksgood.ani.Ani;
import processing.core.PGraphics;
import processing.core.PImage;
import globals.LevelManager;
import globals.Main;
import globals.PAppletSingleton;

public class GameMenu {
	Main p5;

	PGraphics menuLayer;

	ArrayList<Rectangle> buttons;
	ArrayList<PImage> infoBoxes;
	PImage backImage;
	PImage wheel;

	float animMultiplier;

	int levelSelected;
	int lastLevelSelected;

	VortexParticle[] particles;

	AudioPlayer[] introSounds;

	public GameMenu() {
		p5 = getP5();
	}

	public void setup() {

		menuLayer = p5.createGraphics(p5.width, p5.height);

		animMultiplier = 1f;

		backImage = p5.loadImage("MenuBack.png");
		wheel = p5.loadImage("wheel_blank.png");

		levelSelected = -1;
		lastLevelSelected = -2;

		buttons = new ArrayList<Rectangle>();
		infoBoxes = new ArrayList<PImage>();

		for (int i = 0; i < 8; i++) {
			Rectangle nuevoBoton = new Rectangle((int) (200 * i), 200, 80, 80);
			buttons.add(nuevoBoton);

		}
		placeButtons();
		loadInfoBoxes();

		loadIntroSounds();

		particles = new VortexParticle[400];
		for (int i = 0; i < particles.length; i++) {
			particles[i] = new VortexParticle();
		}

	}

	public void update() {
		/*
		 * if ( isActive && !fadeOut.isPlaying()) { isActive = false; }
		 */

	}

	public void render() {

		isHovering();

		menuLayer.beginDraw();
		menuLayer.background(0);

		menuLayer.imageMode(p5.CENTER);
		menuLayer.pushStyle();

		if (animMultiplier > 0.01) {

			// menuLayer.tint(255, 255 * animMultiplier);

			menuLayer.pushMatrix();

			menuLayer.translate(p5.width * 0.5f, p5.height * 0.5f);
			menuLayer.scale(1 + (1 - animMultiplier));

			menuLayer.image(backImage, 0, 0);

			// RENDER vortex PARTICLES
			for (int i = 0; i < particles.length; i++) {
				particles[i].update();
				particles[i].render(menuLayer);
			}

			menuLayer.popStyle();

			// menuLayer.image(wheel, 0, 15);

			if (levelSelected != -1) {
				menuLayer.image(infoBoxes.get(levelSelected), 0, 15);
			}
			menuLayer.popMatrix();

			/*
			 * menuLayer.noFill(); for (int i = 0; i < buttons.size(); i++) {
			 * menuLayer.rect(buttons.get(i).x, buttons.get(i).y,
			 * buttons.get(i).width, buttons.get(i).height); }
			 */

			menuLayer.endDraw();

			p5.pushStyle();

			p5.tint(255, 255 * animMultiplier);
			p5.imageMode(p5.CORNER);
			p5.image(menuLayer, 0, 0);

			p5.popStyle();

		}

	}

	public void appear() {
		// fadeIn.to(this, 1f, 0, "imageTint", 255);
		LevelManager.getAni().to(this, 2f, 4f, "animMultiplier", 1f);
	}

	public void disappear() {
		LevelManager.getAni().to(this, 1f, 0, "animMultiplier", 0f, Ani.EXPO_IN);
		//LevelManager.getAni().to
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
		infoBoxes.add(p5.loadImage("levels/0/info.png"));
		infoBoxes.add(p5.loadImage("levels/4/info.png"));
		infoBoxes.add(p5.loadImage("levels/3/info.png"));
		infoBoxes.add(p5.loadImage("levels/2/info.png"));
		infoBoxes.add(p5.loadImage("levels/6/info.png"));
		infoBoxes.add(p5.loadImage("levels/7/info.png"));
	}

	private void loadIntroSounds() {

		introSounds = new AudioPlayer[8];
		introSounds[0] = LevelManager.minim.loadFile("levels/5/intro.mp3");
		introSounds[1] = LevelManager.minim.loadFile("levels/1/intro.mp3");
		introSounds[2] = LevelManager.minim.loadFile("levels/0/intro.mp3");
		introSounds[3] = LevelManager.minim.loadFile("levels/5/intro.mp3");
		introSounds[4] = LevelManager.minim.loadFile("levels/3/intro.mp3");
		introSounds[5] = LevelManager.minim.loadFile("levels/2/intro.mp3");
		introSounds[6] = LevelManager.minim.loadFile("levels/6/intro.mp3");
		introSounds[7] = LevelManager.minim.loadFile("levels/5/intro.mp3");

	}

	public void onMousePressed() {
		/*
		 * for (int i = 0; i < buttons.size(); i++) { if
		 * (buttons.get(i).contains(p5.mouseX, p5.mouseY)) { levelSelected = i;
		 * p5.println("LevelSelected: " + levelSelected); break; } }
		 */

		// launch(levelSelected);

	}

	public void isHovering() {

		levelSelected = -1;
		// lastLevelSelected = -1;

		if (isActive()) {

			for (int i = 0; i < buttons.size(); i++) {
				if (buttons.get(i).contains(p5.mouseX, p5.mouseY)) {
					levelSelected = i;
					break;
				}
			}

			if (levelSelected != lastLevelSelected && levelSelected != -1) {
				lastLevelSelected = levelSelected;
				if (!introSounds[levelSelected].isPlaying()) {
					introSounds[levelSelected].rewind();
					introSounds[levelSelected].play();
				}
			}
		}
	}

	public int getSelectedLevel() {
		return levelSelected;
	}

	public boolean isActive() {
		if (animMultiplier > 0.99) {
			return true;
		} else {
			return false;
		}
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
