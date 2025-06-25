//class to handle intersection and traffic lights

//imports
import java.awt.Color;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JLabel;

public class TrafficLights implements Runnable{
	//variables
	//array for the light colors
	private final String[] lightColors = {"Green", "Yellow", "Red"};
	private int i = 0;
	private String currColor = lightColors[i];//current light color
	private final AtomicBoolean isRunning = new AtomicBoolean(false);
	private final AtomicBoolean isPaused = new AtomicBoolean(false);
	private Thread lightThread;
	private String threadName;
	private JLabel label;
	
	//constructor
	public TrafficLights(String threadName, JLabel label) {
		this.threadName = threadName;
		this.label = label;
		System.out.println ("Creating " + threadName);
	}
	
	//using synchronize to get the traffic light colors
	public synchronized String getLightColor() {
		this.currColor = lightColors[i];
		
		//return
		return this.currColor;
	}
	
	public void suspend() {
		isPaused.set(true);
		System.out.println ("Pausing " + threadName);
	}
	
	public synchronized void resume() {
		isPaused.set(false);
		notify();
		System.out.println ("Resuming " + threadName);
	}
	
	public void start() {
		System.out.println ("Starting " + threadName);
		
		if (lightThread == null) {
			lightThread = new Thread (this, threadName);
			lightThread.start();
		}
	}
	
	public void stop() {
		lightThread.interrupt();
		isRunning.set(false);
		System.out.println ("Stopping " + threadName);
	}
	
	public void interrupt() {
		//if the light is in a sleep state, we can call the interrupt function
		//to put it in a wake state when the pause button is used
		lightThread.interrupt();
	}
	
	//overridden run method
	@Override
	public void run() {
		System.out.println ("Running " + threadName);
		isRunning.set(true);
		
		while (isRunning.get()) {
			try {
				synchronized(this) {
					while (isPaused.get()) {
						System.out.println (threadName + " waiting");
						wait();
					}
				}
				
				//switch block to change the light colors
				switch (getLightColor()) {
				//green light
				case "Green":
					//setting the label to green
					label.setForeground(Color.GREEN);
					label.setText(getLightColor());
					
					//light is green for 10 secs
					Thread.sleep(10000);
					//increment i for color change
					i++;
					
					break;
					
				//yellow light
				case "Yellow":
					label.setForeground(Color.YELLOW);
					label.setText(getLightColor());
					
					//light yellow for 5 secs
					Thread.sleep(5000);
					i++;
					
					break;
					
				//red light
				case "Red":
					label.setForeground(Color.RED);
					label.setText(getLightColor());
					
					//light red for 10 secs
					Thread.sleep(10000);
					//set i back to 0 to reset the light
					i = 0;
					
					break;
				}
			}catch (InterruptedException ex) {
				//if an interruption happens, isPaused should be set to true
				isPaused.set(true);
			}
		}
	}

}
