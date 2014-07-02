package globals;

public class Timer{

	Main p5;

	int savedTime;
	int totalTime;
	int currentTime;

	public Timer(){
		p5 = getP5();
		totalTime = 0 ;  
	}
	
	public void update(){
		currentTime = p5.millis() - savedTime;
	}
	
	public void render(){
		
		p5.noFill();
		p5.stroke(255);
		p5.strokeWeight(1);
		
		// DRAWING PROGRESS
		p5.noFill();
		for (float i = 0; i < 1.0f ; i+=0.2f) {
			float distance = 70 + ((100 - 70) * i);
			//p5.println(p5.map(currentTime, savedTime, savedTime + totalTime, 0, p5.QUARTER_PI));
			p5.arc(0, 0, distance * 2, distance * 2, 0, p5.map(currentTime, savedTime, savedTime + totalTime, 0, p5.HALF_PI));
		}
		
		//DRAWING PROGRESS BOUNDS
		p5.arc(0, 0, 65 * 2, 65 * 2, 0, p5.HALF_PI);
		p5.arc(0, 0, 100 * 2, 100 * 2, 0, p5.HALF_PI);
		
		p5.fill(255);
		p5.noStroke();
		p5.text((currentTime / 1000) + 1, 30, 30);
	}
	
	public void setDuration(int tempTotalTime){
		totalTime = tempTotalTime; 
	}

	public void start(){
		savedTime = p5.millis(); 
	}

	public boolean isFinished(){
		//currentTime = p5.millis() - savedTime;
		if(currentTime > totalTime){
			return true; 
		} else {
			return false;
		}
	}
	
	public int getTotalTime(){
		return totalTime;
	}
	public int getCurrentTime(){
		return currentTime;
	}
	
	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}



