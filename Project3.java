//main project class that handles the GUI
//GUI should include: time stamp, traffic light display, and cars moving along the x-axis
//each component should be handled in separate threads (placed in different classes)

//imports
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Project3 extends JFrame implements Runnable, ChangeListener{
	//variables
	private static boolean isRunning;
	private static final AtomicBoolean programRunning = new AtomicBoolean(false);
	static Thread guiThread;
	
	//light labels
	static JLabel lightALabel = new JLabel(); 
	static JLabel lightBLabel = new JLabel(); 
	static JLabel lightCLabel = new JLabel();
	
	//control buttons
	static JButton startButton = new JButton("Start");
	static JButton pauseButton = new JButton("Pause");
	static JButton stopButton = new JButton("Stop");
	
	//jsliders to display the cars and track the progress
	//0-3000 range for the distance between each light
	static JSlider carASlider  = new JSlider(0, 3000);
	static JSlider carBSlider  = new JSlider(0, 3000); 
	static JSlider carCSlider  = new JSlider(0, 3000);
	
	//creating three traffic light objects
	//each light should get their own thread
	TrafficLights A = new TrafficLights("threadA", lightALabel);
	TrafficLights B = new TrafficLights("threadB", lightBLabel);
	TrafficLights C = new TrafficLights("threadC", lightCLabel);
	
	//creating three car objects, each car has their own thread
	//(constructor) -> thread, min, max
	Car carA = new Car ("carAThread", 0, 1000);
	Car carB = new Car ("carBThread", 0, 1000);
	Car carC = new Car ("carCThread", 0, 1000);
	
	//creating arrays to handle the cars and the lights
	Car[] carArr = {carA, carB, carC};
	TrafficLights[] lightsArr = {A, B, C};
	
	//array to get the traffic info for each car
	Object[][] trafficInfo = {
		{"Car A", carA.getXPos(), 0, 0},
		{"Car B", carB.getXPos(), 0, 0},
		{"Car C", carC.getXPos(), 0, 0}
	};
	
	//creating a data table to display the data
	String[] columnNames = {"Car", "X-Pos", "y-Pos", "Speed"};
	JTable carData = new JTable (trafficInfo, columnNames);
	
	//constructor
	public Project3() {
		isRunning = Thread.currentThread().isAlive();
		GUIContent();
		buttonInteractions();
	}
	
	//method to create the content of the frame
	private void GUIContent() {
		//creating the frame
		JFrame frame = new JFrame("Traffic Tracker");
		frame.setSize (620, 400);
		
		//layout
		frame.setLayout(new FlowLayout());
			
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//
		
		//panel for the welcome messages
		JPanel welcomePanel = new JPanel();
		//adding to the frame
		frame.add(welcomePanel);
		
		//creating headings
		JLabel welcome1 = new JLabel ("Welcome to the traffic tracking program!");
		JLabel welcome2 = new JLabel ("To start the program, hit the start button below.");
		//adding to the panel
		welcomePanel.add(welcome1);
		welcomePanel.add(welcome2);
		
		//panel for the buttons
		JPanel buttonsPanel = new JPanel();
		//adding to frame
		frame.add(buttonsPanel);
		
		//adding the buttons to the panel
		buttonsPanel.add(startButton);
		buttonsPanel.add(pauseButton);
		buttonsPanel.add(stopButton);
		
		//panel for the sliders
		JPanel slidersPanel = new JPanel();
		//adding to the frame
		frame.add(slidersPanel);
		
		//adding the sliders to the panel
		slidersPanel.add(carASlider);
		slidersPanel.add(carBSlider);
		slidersPanel.add(carCSlider);
		
		
		//adding change listeners to the sliders
		carASlider.addChangeListener(this);
		carBSlider.addChangeListener(this);
		carCSlider.addChangeListener(this);
		
		carASlider.setMajorTickSpacing(1000);
		carASlider.setPaintTicks(true);
		
		carBSlider.setMajorTickSpacing(1000);
		carBSlider.setPaintTicks(true);
		
		carCSlider.setMajorTickSpacing(1000);
		carCSlider.setPaintTicks(true);
		
		//setting the slider values based on their position
		carASlider.setValue(carA.getXPos());
		carBSlider.setValue(carB.getXPos());
		carCSlider.setValue(carC.getXPos());
		
		//light labels panel
		JPanel lightPanel = new JPanel();
		frame.add(lightPanel);
		//adding labels for the traffic lights
		JLabel trafficLightA = new JLabel ("Traffic Light A: ");
		lightPanel.add(trafficLightA);
		lightPanel.add(lightALabel);
		JLabel trafficLightB = new JLabel ("\tTraffic Light B: ");
		lightPanel.add(trafficLightB);
		lightPanel.add(lightBLabel);
		JLabel trafficLightC = new JLabel ("Traffic Light C: ");
		lightPanel.add(trafficLightC);
		lightPanel.add(lightCLabel);
		
		
		//data panel to hold the data table
		JPanel dataPanel = new JPanel();
		//adding it to the frame
		frame.add(dataPanel);
		
		carData.setPreferredScrollableViewportSize(carData.getPreferredSize());
		carData.setFillsViewportHeight(true);
		
		//creating a scroll pane for the data table
		JScrollPane scrollPane = new JScrollPane(carData);
		//adding to the panel
		dataPanel.add(scrollPane);
		
		
		//frame visibility
		frame.setVisible(true);
	}
	
	//method to handle the action events for the buttons
	private void buttonInteractions() {
		//start button
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				if (!programRunning.get()) {
					System.out.println (Thread.currentThread().getName() + 
					" calling start");
					
					//starting the threads
					A.start();
					B.start();
					C.start();
					carA.start();
					carB.start();
					carC.start();
					
					guiThread.start();
				}
				
				//setting the running value to true
				programRunning.set(true);
			}
		});
		
		//pause button
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				if (programRunning.get()) {
					//looping through the cars and the car array 
					//to pause all the cars
					for (Car i:carArr) {
						i.suspend();
						System.out.println (Thread.currentThread().getName()
						+ " calling suspend");
					}
					//looping through the lights and light array to pause the 
					//traffic lights
					for (TrafficLights i:lightsArr) {
						i.interrupt();
						i.suspend();
					}
					
					pauseButton.setText("Resume");
					programRunning.set(false);
				}else {
					//for loops to resume
					for (Car i: carArr) {
						if (i.isPaused.get()) {
							i.resume();
							System.out.println (Thread.currentThread().getName()
							+ " calling resume");
						}
					}
					for (TrafficLights i:lightsArr) {
						i.resume();
					}
					pauseButton.setText("Pause");
					programRunning.set(true);
				}
			}
		});
		
		//stop button
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				if (programRunning.get()) {
					System.out.println (Thread.currentThread().getName()
					+ " calling stop");
					
					for (Car i: carArr) {
						i.stop();
					}
					
					for (TrafficLights i: lightsArr) {
						i.stop();
					}
					
					programRunning.set(false);
				}
			}
		});
	}
	
	//method to handle the state change event for the cars
	//and handle updating the traffic information
	public void stateChanged (ChangeEvent e) {
		//when the cars move and change values the data information
		//should be updated
		trafficInfo[0][1] = carASlider.getValue();
		trafficInfo[1][1] = carBSlider.getValue();
		trafficInfo[2][1] = carCSlider.getValue();
		
		//updating the speed information
		trafficInfo[0][3] = carA.getSpeed() + "km/h";
		trafficInfo[1][3] = carB.getSpeed() + "km/h";
		trafficInfo[2][3] = carC.getSpeed() + "km/h";
		
		//using the repaint function to update the table
		carData.repaint();
	}
	
	//method to get the traffic light colors and handle the interactions
	//between the cars and the traffic lights
	private void getInfo() {
		if (programRunning.get()) {
			//switch for the first light
			switch (A.getLightColor()) {
			case "Red":
				for (Car i:carArr) {
					//if the car is at least half way to the light, flag the car
					//to stop at the upcoming light
					if (i.getXPos() >= 500 && i.getXPos() < 1000) {
						i.atStopLight.set(true);
					}
				}
				break;
				
			case "Green":
				for (Car i:carArr) {
					if (i.atStopLight.get()) {
						i.resume();
					}
				}
				break;
			}//end of switch
			//switch for the second light
			switch (B.getLightColor()) {
			case "Red":
				for (Car i:carArr) {
					if (i.getXPos() >= 1500 && i.getXPos() < 2000) {
						i.atStopLight.set(true);
					}
				}
				break;
				
			case "Green":
				for (Car i:carArr) {
					if (i.atStopLight.get() ) {
						i.resume();
					}
				}
				break;
			}//end of switch
			//switch for the third light
			switch (C.getLightColor()) {
			case "Red":
				for (Car i:carArr) {
					if (i.getXPos() >= 2500 && i.getXPos() < 3000) {
						i.atStopLight.set(true);
					}
				}
				break;
				
			case "Green":
				for (Car i:carArr) {
					if (i.atStopLight.get()) {
						i.resume();
					}
				}
				break;
			}
		}
	}
	
	//overridden run method
	@Override
	public void run() {
		while (isRunning) {
			if (programRunning.get()) {
				//while the program is running, set the car slider values as the cars
				//xPos value
				carASlider.setValue(carA.getXPos());
				carBSlider.setValue(carB.getXPos());
				carCSlider.setValue(carC.getXPos());
				
				getInfo();
			}
		}
	}


	public static void main(String[] args) {
		Project3 P3 = new Project3();
		P3.GUIContent();
		
		guiThread = new Thread(P3); 

	}

}
