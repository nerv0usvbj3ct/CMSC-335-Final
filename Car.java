//class to handle the movement of the cars across the x axis

//imports
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class Car implements Runnable{
	//variables
	private int xPos;
	private int speed = 0;
	private String threadName;
	private Thread carThread;
	
	private final AtomicBoolean isRunning = new AtomicBoolean(false); //will be updated when program is ran
	//separate booleans to track if the car is at the light and one for if the pause button is used
	public final AtomicBoolean atStopLight = new AtomicBoolean(false);
	public final AtomicBoolean isPaused = new AtomicBoolean(false);
	
	//constructor
	//the max and min will be used to find the range of the cars x position
	public Car(String threadName, int min, int max) {
		this.threadName = threadName;
		this.xPos = ThreadLocalRandom.current().nextInt(min, max);
		System.out.println("Creating " + threadName);
	}
	
	//using synchronize to get the x position
	public synchronized int getXPos() {
		return xPos;
	}
	
	//method to get the cars speed
	public int getSpeed() {
		//if..else to check if thread is running + nested if..else to check
		//if the car is at a light
		if (isRunning.get()) {
			if (atStopLight.get()) {
				//if true the speed should be 0
				speed = 0;
			}else {
				//calculations for the speed
				speed = 3 * 60;
			}
			
		}else {
			speed = 0;
		}
		
		//return statement
		return speed;
	}
	
	//start method to begin thread
	public void start() {
		System.out.println ("Starting " + threadName);
		
		if (carThread == null) {
			//new thread
			carThread = new Thread(this, threadName);
			carThread.start();
		}
	}
	
	//stop method to stop the thread
	public void stop() {
		carThread.interrupt();
		
		//set running to false
		isRunning.set(false);
		
		System.out.println("Stopping " + threadName);
	}
	
	//suspend method to pause the thread
	public void suspend() {
		//set paused to true
		isPaused.set(true);
		
		System.out.println("Pausing " + threadName);
	}
	
	//using synchronize to resume all cars
	public synchronized void resume() {
		//if the car is paused, setting the paused state to false and using notify
		//to alert the thread
		if (isPaused.get() || atStopLight.get()) {
			isPaused.set(false);
			atStopLight.set(false);
			notify();
			
			System.out.println ("Resuming " + threadName);
		}
	}
	
	//overridden run method
	@Override
	public void run() {
		System.out.println ("Running " + threadName);
		//setting running to true
		isRunning.set(true);
		
		//while loop to track if thread is running
		while (isRunning.get()) {
			//try..catch
			try {
				//space between traffic lights should be 1000 meters(3000 total meters)
				//while loop
				while (xPos < 3000) {
					synchronized(this) {
						while (isPaused.get() || atStopLight.get()) {
							System.out.println (threadName + " waiting");
							wait();
						}
					}
					//checking run status
					if (isRunning.get()) {
						//1 sec interval
						Thread.sleep(100);
						
						//increasing car position
						xPos += 5;
					}
				}
				
				//setting x position
				xPos = 0;
			}catch (InterruptedException ex) {
				return;
			}
		}
	}

}
