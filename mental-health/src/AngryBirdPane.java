
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/* ================================================================================================================
Program Name: Assignment3
Author: Jessica Liu
Date: 06/14/2019
Java SE 1.8
==================================================================================================================
Program Definition: This program has three levels and checks to see if a projectile (bird) hits an object, and 
displays the top ten highest scores. The launcher's position and the speed of the bird is controlled using the 
keyboard. 
The Swing is used to implement the game. Mainly, a JPanel is used to:
- paint all the components including the objects, the launcher, the bird, and the game information
- listen to the keyboard and handle the events
- listen to the timer and change the motion accordingly

Methods:
- init
- keyPressed
- keyReleased
- keyTyped
- resetGame
- updateHighestScores
- actionPerformed
- wx
- wy
- paintComponent
- drawGround
- drawObjects
- drawLauncher
- drawBirds
- drawVelocityBar
- drawInfo
- checkHit
- setObjectsForCurrentLevel
- readHighetsScores
- writeHighestScores
- readLevels
- main

====================================================================================================================
*/

public class AngryBirdPane extends AbstractPane implements KeyListener, ActionListener {

	private static final long serialVersionUID = 1L;

	// static String SCORES_FILE = "/homework/workspace/assignment3/AngryBirdsScores.txt"; //file to save scores //(type str)
	// static String LEVELS_FILE = "/homework/workspace/assignment3/AngryBirdsLevels.txt"; //file of level configuration //(type str)

	//set frame size
	static int WINDOW_X = 400; //(type int)
	static int WINDOW_Y = 600; //(type int)

	//set origin of 'physics coordinates' on the screen
	static int ORIGIN_X = 100; //(type int)
	static int ORIGIN_Y = 450; //(type int)

	//set the limitations of the launcher position
	static int LAUNCHER_MIN_X = -70; //(type int)
	static int LAUNCHER_MAX_X = -20; //(type int)
	static int LAUNCHER_MIN_Y = 0; //(type int)
	static int LAUNCHER_MAX_Y = 50; //(type int)

	static int MAX_LIFE = 4; //how many birds can be tried //(type int)
	static int MAX_VELOCITY = 70; //max velocity to current screen //(type int)
	
	static int SCORES_Y = 360;
	static int MESSAGE_Y = 180;

	// predefined all level object's X coordinates, read from file. (type int)
	int[][] allLevelObjectXs = {
			{  50, 100, 150, 200, 250 },
			{  80, 140, 200, 260 },
			{  90, 160, 230 },
			{ 100, 180, 260 },
			{ 110, 200, 290 },
			{ 120, 220 }
	};

	//x-coordinates of objects in current level. All objects are on the ground (y = 0) (type int)
	int currentLevelObjectXs[];
	int hit[]; // use this to remember hit status of each target object. value could be -1: hit right side, 0: not hit, 1: hit left side
			   // (type int)

	double initialVelocity = 0; // initial velocity in metres/second (type double)
	int angle = 45; // initial angle of elevation in degrees (type int)
	int x0 = -50; // initial x-coordinate in metres of the bird, which is the launcher's position (type int)
	int y0 = 20; // initial y-coordinate in metres of the bird, which is the launcher's position (type int)

	int x = 0; // current x-coordinate in metres of the bird (type int)
	int y = 0; // current y-coordinate in metres of the bird (type int)

	int currentLevel = 1; //game level (type int)
	int life = MAX_LIFE; //current life (type int)
	int hits = 0; //counts the amount of objects that have been hit (type int)

	int scoreFromPrevLevel = 0; //counts the scores from the previous levels (not including the current level) (type int)
	int score = 0; //total scores (all levels included) (type int)
	
	//top 10 scores in history (type int)
	int[] highestScores = { 0, 0, 0 };
	
	Timer timer; //bird will be in motion relative to the timer's ticks
	double time = 0; //the current time, in seconds, since the last status change (type int)

	static enum Status { //status of the game (bird's status) (Status is type enum)
		initial, //bird is at the launcher (initial position) --> launcher position can be changed, angle can be increased/decreased
		prepare, //after " " is pressed, initial velocity increases
		flying,  //bird is flying --> timer's 'ticks' will be received to change the bird's position
		landed,  //bird has landed --> after three seconds, the bird's position will reset to it's initial position (at the launcher)
		finished;    //level has been finished --> 2 cases: if any object has been hit, or if no lives are available
	}

	Status status = Status.initial; //initial status when the game begins
	

	public AngryBirdPane(JApplet parent, CardLayout cardLayout) {
		super(parent, cardLayout);
		
		init();
		
		timer = new Timer(100, this); // every 0.1 seconds, the timer will issue an event to the AngryBirds panel

		addKeyListener(this); //listen to the keyboard- so the AngryBirds panel can receive a keyboard event
		setFocusable(true); //make the panel focusable
		setRequestFocusEnabled(true);
				
		addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentShown( ComponentEvent e ) {
	            requestFocusInWindow();
	            resetGame();
	            
	    		//set up timer to drive animation events
	    		timer.start(); 	            
	        }
	    });
	}

	/** init
	 * This method initializes the variables used by the program
	 */
	public void init() { //initial panel
		// allLevelObjectXs = readLevels(); //read object positions of all levels from file
		// highestScores = readHighestScores(); //read highest scores in history from file
		
		resetGame();
	}
	
	private void resetGame() {
		angle = 45; // initial angle of elevation in degrees (type int)
		x0 = -50; // initial x-coordinate in metres of the bird, which is the launcher's position (type int)
		y0 = 20; // initial y-coordinate in metres of the bird, which is the launcher's position (type int)

		currentLevel = 1; //game level (type int)
		life = MAX_LIFE; //current life (type int)
		hits = 0; //counts the amount of objects that have been hit (type int)

		scoreFromPrevLevel = 0; //counts the scores from the previous levels (not including the current level) (type int)
		score = 0; //total scores (all levels included) (type int)

		backToLauncher(true); //resets game parameters
	}

	/* keyPressed
	 * This method checks to see if the space bar has been pressed and changes the status from 'initial' to 'prepare'
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) { //if a key has been pressed
		char c = e.getKeyChar(); //key that has been pressed
		switch (c) {
		case ' ': //if ' ' (space) has been pressed
			if (status == Status.initial) { //if the current status of the bird is 'initial'
				status = Status.prepare; //change/set the status to 'prepare'
			}
			break;
		}
		repaint(); //repaint the panel
		e.consume(); //mark that the event has been used
	}

	/* keyReleased
	 * This method checks to see if the space bar has been released and changes the status from 'prepare' to 'flying'
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) { //if the key has been released
		char c = e.getKeyChar(); //key that has been pressed
		switch (c) {
		case ' ': //if ' ' (space) has been released
			if (status == Status.prepare) { //if the current status of the bird is 'prepare'
				status = Status.flying; //change/set the status to 'flying'
			}
			break;
		}
		repaint(); //repaint the panel
		e.consume(); //mark that the event has been used
	}

	/* keyTyped
	 * This method checks to see if a key has been typed and performs an action
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) { //if a key has been typed
		char c = e.getKeyChar(); //key that has been pressed
		switch (c) {
		//case in which 'w' or 'W' has been typed
		case 'w': 
		case 'W':
			if (y0 < LAUNCHER_MAX_Y) { // if the position of the launcher is smaller than the maximum of the y-value permitted
				y0++; //move the launcher up
			}
			break;
			//case in which 'x' or 'X' has been typed
		case 'x':
		case 'X':
			if (y0 > LAUNCHER_MIN_Y) { // if the position of the launcher is greater than the minimum of the y-value permitted
				y0--; //move the launcher down
			}
			break;
			//case in which 'a' or 'A' has been typed
		case 'a':
		case 'A':
			if (x0 > LAUNCHER_MIN_X) { // if the position of the launcher is greater than the minimum of the x-value permitted
				x0--; //move the launcher left
			}
			break;
			//case in which 'd' or 'D' has been typed
		case 'd':
		case 'D':
			if (x0 < LAUNCHER_MAX_X) { // if the position of the launcher is smaller than the maximum of the x-value permitted
				x0++; //move the launcher right
			}
			break;
			//case in which 'e' or 'E' has been typed
		case 'e':
		case 'E':
			if (angle <= 89) { //if the angle is less than or equal to 89 degrees
				angle++; //increase the angle
			}
			break;
			//case in which 'c' or 'C' has been typed
		case 'c':
		case 'C':
			if (angle > 0) { //if the angle is larger than 0 degrees
				angle--; //decrease the angle
			}
			break;
			//case in which 'r' or 'R' has been typed
		case 'r':
		case 'R':
			if (status == Status.finished) { //if the status is currently 'finished'
				life = MAX_LIFE; //set the current amount of lives to the max amount of lives 
				score = scoreFromPrevLevel; //set the current score to the score from the previous level
				backToLauncher(true); //reset the game parameters
			}
			break;
			//case in which 'n' or 'N' has been typed
		case 'n':
		case 'N':
			//if the status is currently 'finished' and the current level is less than 3
			if (status == Status.finished && hits > 0 && currentLevel < allLevelObjectXs.length) {
				currentLevel++; //go to the next level
				life = MAX_LIFE; //set the current amount of lives to the max amount of lives
				scoreFromPrevLevel = score; //set the score from the previous level to the current score
				backToLauncher(true); //reset the game parameters
			}
			break;
			//case in which 'q' or 'Q' has been typed
		case 'q': // next level
		case 'Q':
			if (status == Status.finished) { //if the status is currently 'finished'
				timer.stop();
				cardLayout.show(parent.getContentPane(), AbstractPane.OutlinePane);
				// System.exit(0); //exit the program
			}
			break;

		}
		repaint(); //repaint the panel
		e.consume(); //mark that the event has been used
	}

	/** resetGame
	 * This method resets the game parameters
	 * @param newObjects
	 */
	private void backToLauncher(boolean newObjects) {
		status = Status.initial; //set the current status to 'initial'

		//set the current position of the bird to the initial position
		x = x0;
		y = y0;

		time = 0; //set the time back to zero
		initialVelocity = 0; //set the initial velocity of the bird back to zero
		if (newObjects) //if the game has gone to the next level and there are new objects
			setObjectsForCurrentLevel(); //set the objects as the objects for the current level
	}

	/** updateHighestScores
	 * This program updates the highest scores when needed (assuming that the highest scores are sorted from highest to lowest)
	 */
	private void updateHighestScores() { //update the current high scores using 'insertion sort'
		for (int i=0; i<highestScores.length; i++) { //loop the top ten scores in history
			if (score > highestScores[i]) { //find the first score in history that is smaller than the current score
				// scores need to be updated
				// insert the new score at position [i].
				// shift all the scores (from i to end) down. the last one will be lost.
				for (int j = highestScores.length-2; j >= i; j--) { //start from end
					highestScores[j+1] = highestScores[j]; //copy from [j] to [j+i]
				}
				highestScores[i] = score; // insert the new score at [i]
				// writeHighestScores(); //write new scores to file
				break; // after all the scores are updated, break the loop
			}
		}
	}

	/* actionPerformedb
	 * This method handles the events caused by the timer (updates the bird's position)
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) { //this method will be triggered in every 'tick' of the timer
		switch (status) { //update based on current status

		case flying: // update to next position
			time = time + 0.3; //every tick (0.1 seconds) counts as 0.3 seconds (displays a 3x faster speed than the real speed)
			x = (int)Math.round(x0 + initialVelocity * Math.cos(Math.toRadians(angle)) * time); //calculate horizontal distance
			y = (int)Math.round(y0 + initialVelocity * Math.sin(Math.toRadians(angle)) * time - 9.81 / 2 * time * time); //calculate vertical distance

			if (y <= 0) { // if the bird has landed on the ground
				y = 0; 
				status = Status.landed; //set the current status to 'landed'
				time = 0; //reset the time
				hits = checkHit(x); //check if any objects have been hit
				life--; //life decreases by one
				if (hits > 0) { //when object has been hit
					score = scoreFromPrevLevel + life * 10 * currentLevel + 50 * currentLevel; //update the current score
					updateHighestScores(); //update the highest score
				}
			}
			break;

		case prepare: // when " " (space) is pressed
			if (initialVelocity < MAX_VELOCITY) // when the maximum velocity has not been reached
				initialVelocity = initialVelocity + 2; // increase the initial velocity by 2
			break;

		case landed: // when the bird lands on the ground, it will stay for one second
			time += 0.1; //increase the time by 0.1 second
			if (time >= 1) { //when one second has passed
				if (hits > 0 || life == 0) //if the current level is finished by hitting an object or having no more available lives
					status = Status.finished; //set the current status to 'finished'
				else
					backToLauncher(hits > 0); //if no objects are hit, and lives are still available, try again
			}
			break;
		}

		repaint(); // repaint the panel
	}

	/** wx
	 * This method converts the physics x-coordinate to the x-position in Window screen of the game
	 * @param x
	 * @return
	 */
	private int wx(int x) {
		return ORIGIN_X + x; //add the physics' x-coordinate origin and the input
	}

	/** wy
	 * This method converts the physics' y-coordinate to the y-position in Window screen of the game
	 * @param y
	 * @return
	 */
	private int wy(int y) {
		return ORIGIN_Y - y; //subtract the input from the physics' y-coordinate origin
	}

	/* paintComponent
	 * This method paints all the panels
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g); //call the super paint component
		
		drawGround(g);
		drawObjects(g);
		drawLauncher(g);
		drawBird(g);
		drawVelocityBar(g);
		drawInfo(g);
	}

	/** drawGround
	 * This method draws the ground
	 * @param g
	 */
	private void drawGround(Graphics g) {
		g.setColor(Color.GREEN); //sets the color to green
		g.fillRect(0, wy(-1), WINDOW_X, 3);
	}

	/** drawObjects
	 * This method draws the objects and their reaction when they are hit
	 * @param g
	 */
	private void drawObjects(Graphics g) { //draw the objects
		g.setColor(Color.BLACK);

		for (int i = 0; i < currentLevelObjectXs.length; i++) {
			if (hit[i] == -1) { //if the object is hit from the right side (object falls and moves left a little)
				//the object tilts 45 degrees to the left
				int d = (int)Math.round(5 * Math.cos(Math.toRadians(45))); //half of the object's length mapping to the x and y-coordinate 
				// four x and y coordinates of the corner of the object
				int[] xPoints = {wx(currentLevelObjectXs[i]-d), wx(currentLevelObjectXs[i]+d), wx(currentLevelObjectXs[i]-d), wx(currentLevelObjectXs[i]-3*d)};
				int[] yPoints = {wy(-d), wy(d), wy(3*d), wy(d)};
				g.fillPolygon(xPoints, yPoints, 4); //draw the object
			} else if (hit[i] == 1) { //if the object is hit from the left side (object falls and moves right a little)
				//the object tilts 45 degrees to the right
				int d = (int)Math.round(5 * Math.cos(Math.toRadians(45)));
				// four x and y coordinates of the corner of the object
				int[] xPoints = {wx(currentLevelObjectXs[i]-d), wx(currentLevelObjectXs[i]+d), wx(currentLevelObjectXs[i]+3*d), wx(currentLevelObjectXs[i]+d)};
				int[] yPoints = {wy(d), wy(-d), wy(d), wy(3*d)};
				g.fillPolygon(xPoints, yPoints, 4); //draw the object
			} else {
				g.fillRect(wx(currentLevelObjectXs[i]-5), wy(9), 10, 10); //draw the object if it has not been hit
			}
		}
	}

	/** drawBird
	 * This method draws the bird
	 * @param g
	 */
	private void drawBird(Graphics g) {
		if (status == Status.initial || status == Status.prepare) { //if the status of the bird is 'initial' or 'prepare'
			//the bird is at the launcher
			x = x0;
			y = y0;
		}
		g.setColor(Color.RED); //bird color
		g.fillRect(wx(x-5), wy(y+9), 10, 10); //the bird
	}

	/** drawLauncher
	 * This method draws the launcher
	 * @param g
	 */
	private void drawLauncher(Graphics g) {
		g.setColor(Color.PINK); //launcher color
		g.fillRect(wx(x0-30),  wy(y0-1), 60, 3); //the launcher

	}

	/** drawVelocityBar
	 * This method draws the velocity bar and the line of the angle of which the launcher will shoot the bird
	 * @param g
	 */
	private void drawVelocityBar(Graphics g) { //draw the velocity bar and the line of the angle
		//draw the line of the angle
		g.setColor(Color.PINK); //angle line color
		int x1 = x0 - (int)Math.round(5.0 / Math.tan(Math.toRadians(angle)));
		int y1 = y0;
		int x2 = x1 + (int)Math.round(30.0 * Math.cos(Math.toRadians(angle)));
		int y2 = y1 + (int)Math.round(30.0 * Math.sin(Math.toRadians(angle)));
		g.drawLine(wx(x1), wy(y1), wx(x2), wy(y2));
		
		if (status != Status.prepare) { //if the status is not 'prepare', do not draw the velocity bar
			return; 
		}
		
		//draw the initial velocity bar
		g.setColor(Color.BLUE); //velocity bar color

		//top centre of the velocity bar
		int x4 = x1 + (int)Math.round(30.0 * initialVelocity / MAX_VELOCITY * Math.cos(Math.toRadians(angle)));
		int y4 = y1 + (int)Math.round(30.0 * initialVelocity / MAX_VELOCITY * Math.sin(Math.toRadians(angle)));
		
		// width of velocity bar is 3.0
		int dx = (int)Math.round(3.0 * Math.sin(Math.toRadians(angle)));
		int dy = (int)Math.round(3.0 * Math.cos(Math.toRadians(angle)));

		//coordinate of four points on the velocity bar are re-mapped
		int xPoints[] = {wx(x1 - dx), wx(x1 + dx), wx(x4 + dx), wx(x4 - dx)};
		int yPoints[] = {wy(y1 + dy), wy(y1 - dy), wy(y4 - dy), wy(y4 + dy)};
		g.fillPolygon(xPoints, yPoints, 4); //draw the velocity bar
	}

	/** drawInfo
	 * This method draws the bird information, the life information, the keyboard guide, and the levels menu on the panel
	 * @param g
	 */
	private void drawInfo(Graphics g) {
		g.setColor(Color.BLUE); //info color is blue

		String birdInfo = "Platform:(" + x0 + "," + y0 + ")"; //create a string for the bird's info, add the platform/launcher's position
		birdInfo += "   Angle:" + angle; //add the angle to the string
		birdInfo += "   Initial Velocity:" + initialVelocity; //add the initial velocity to the string
		birdInfo += "   Bird:(" + x + "," + y + ")"; //add the bird's current position to the string
		g.drawString(birdInfo,  wx(-90),  wy(-25)); //draw the bird position on the panel


		String lifeInfo = "Life:" + life; //create a string for the number of lives the user has
		lifeInfo += "   Score:" + score; //add the current score to the string
		lifeInfo += "   Level:" + currentLevel; //add the current level to the string
		g.drawString(lifeInfo,  wx(100),  wy(410)); //draw the life info on the panel

		if (status == Status.finished) { //if the status is 'finished'
			if (hits == 0) { //if the number of hits is zero
				g.drawString("You did not complete the level !",  wx(15),  wy(MESSAGE_Y));
				g.drawString("You Would like to:",  wx(15),  wy(MESSAGE_Y - 20));
				g.drawString("   * (R)eplay level " + currentLevel,  wx(15),  wy(MESSAGE_Y - 40));
				g.drawString("   * (Q)uit",  wx(15),  wy(MESSAGE_Y - 60));
			} else if (currentLevel < allLevelObjectXs.length) { //if an object was hit and the current level is less than three
				g.drawString("You completed the level !",  wx(15),  wy(MESSAGE_Y));
				g.drawString("You Would like to:",  wx(15),  wy(MESSAGE_Y - 20));
				g.drawString("   * (R)eplay level " + currentLevel,  wx(15),  wy(MESSAGE_Y - 40));
				g.drawString("   * (N)ext level",  wx(15),  wy(MESSAGE_Y - 60));
				g.drawString("   * (Q)uit",  wx(15),  wy(MESSAGE_Y - 80));
			} else { //if an object was hit and the current level is final level
				g.drawString("You completed all levels !",  wx(15),  wy(MESSAGE_Y));
				g.drawString("You Would like to:",  wx(15),  wy(MESSAGE_Y - 20));
				g.drawString("   * (R)eplay level " + currentLevel,  wx(15),  wy(MESSAGE_Y - 40));
				g.drawString("   * (Q)uit",  wx(15),  wy(MESSAGE_Y - 60));
			}
		} else if (status == Status.landed) { //if the status is 'landed'
			if (hits > 0) //if an object was hit, display message
				g.drawString("Good job !",  wx(60),  wy(150));
			else //if an object was missed, display message
				g.drawString("Missed !",  wx(60),  wy(150));
		}

		g.drawString("Space: velocity",  wx(195),  wy(SCORES_Y)); //displays the action keys
		g.drawString("W: up",  wx(195),  wy(SCORES_Y - 20));
		g.drawString("X: down",  wx(195),  wy(SCORES_Y - 40));
		g.drawString("A: left",  wx(195),  wy(SCORES_Y - 60));
		g.drawString("D: right",  wx(195),  wy(SCORES_Y - 80));
		g.drawString("E: angle up",  wx(195),  wy(SCORES_Y - 100));
		g.drawString("C: angle down",  wx(195),  wy(SCORES_Y - 120));

		g.drawString("Highest Scores:",  wx(-90),  wy(SCORES_Y)); //displays the highest scores
		for (int i=0; i<highestScores.length; i++) { //displays the top 10 highest scores
			g.drawString("" + highestScores[i],  wx(-90),  wy(SCORES_Y - 20 - i * 20)); 
		}
	}

	/**
	  checkHit
	  This method checks how many targets have been hit (within -+10m)
	  return:
	     hits - number of objects to be hit
	 */
	//multiple targets at: A(50,0), B(120,0), C(250,0) 
	private int checkHit(double distance) {
		int hits = 0;
		for (int i = 0; i < currentLevelObjectXs.length; i++) { //check if the projectile hits any of the targets 
			//if the horizontal distance is  within -+10 m of the target
			if (distance >= currentLevelObjectXs[i] - 10 && distance <= currentLevelObjectXs[i] + 10) {
				// System.out.println("You hit target #"+(i+1)+"!");
				hit[i] = distance >= currentLevelObjectXs[i] ? -1 : 1;
				hits++;
			}
		}
		return hits;
	}

	/** setObjectsForCurrentLevel
	 * This method takes the objects from the levels file and puts it in the current level
	 */
	private void setObjectsForCurrentLevel() {
		currentLevelObjectXs = allLevelObjectXs[currentLevel-1];
		hit = new int[currentLevelObjectXs.length];
	}

	/** readHighestScores
	 * This method reads the highest score from the file 
	 * List of Identifiers
	 * s - (type string) sets the line read from the file as 's'
	 * count - (type int) counts the amount of times a line has been read
	 * scores - (type int) an array that holds the te highest scores in history
	 * @return
	 */
	/*
	public int[] readHighestScores() { //reads the highest scores
		int[] scores = new int[10]; //create an array to hold ten highest scores in history

		try (BufferedReader br = new BufferedReader(new FileReader(SCORES_FILE))) { //reads highest scores from file
			String s; //create string s
			int count = 0; //initiate counter to zero
			while ((s = br.readLine()) != null && count < 10) { //while a line has been read and count is less than ten
				scores[count] = Integer.parseInt(s); //convert the string (the score) to an integer
				count++; //increase counter by one
			}
		} catch (IOException e) {
			System.out.println("Fail to read highest scores !");
			System.exit(-1); //exit the program with error code -1
		}

		return scores;
	}
	 */

	/** writeHighestScores
	 * This method writes the highest scores into the file
	 * 
	 */
	/*
	private void writeHighestScores() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORES_FILE))) { //opens the file to write
			for (int i = 0; i < highestScores.length-1; i++) { //loop all the highest scores except the last one
				writer.write(highestScores[i] + "\n"); //write scores with new line into the file
			}
			writer.write(highestScores[highestScores.length-1] + ""); // write the last one without new line
		} catch (IOException e) {
			System.out.println("Fail to update highest scores !");
			System.exit(-1); //exit the program with error code -1
		}
	}
	*/
	
	/** readLevels
	 * This method reads the level design from file (each line has the following format: (ex.) "Level 1: 50 100 150")
	 * @return
	 */
	/*
	private int[][] readLevels() { 
		int[][] levels = new int[3][]; //create three levels

		try (BufferedReader br = new BufferedReader(new FileReader(LEVELS_FILE))) { //opens a file to read levels
			String s; //string s
			int level = 0; //initial level
			while ((s = br.readLine()) != null && level < 3) { // if lines are read and the level is lower than 3
				String[] t = s.split(" "); //split the string by spaces (ex.  Level 1: 50 100 150--> "Level", "1:", "50", "100", "150"
				int[] objectsX = new int[t.length - 2]; //create an array called objectsX with two less indexes than the length of t
				for (int i = 0; i < objectsX.length; i++) { //loop from zero to the length of objectsX
					objectsX[i] = Integer.parseInt(t[2+i]); //fill in the array with string t and skip the first two (and convert to integer)
				}
				levels[level] = objectsX; //assign objectsX's objects to levels
				level++; //increase the level by one
			}
		} catch (IOException e) {
			System.out.println("Fail to read object positions for each level !");
			System.exit(-1); //exit the program with error code -1
		}

		return levels;
	}
	*/

	/** main
	 * This method starts the Swing framework and creates the JPanel in the frame
	 * @param args
	 */
	/*
	public static void main(String[] args) {
		//String currentDirectory = System.getProperty("user.dir");
		//System.out.println("The current working directory is " + currentDirectory);


		EventQueue.invokeLater(new Runnable() { //anonymous class based on Runnable
			public void run() {
				JFrame frame = new JFrame("Angry Bird"); //show angry birds in the frame title (caption)
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //when closed, the program will exit
				frame.setLayout(new BorderLayout()); //set the layout as BorderLayout

				AngryBirds panel = new AngryBirds(); //create a panel for AngryBirds program
				panel.init(); //initialize the panel
				// panel.setSize(WINDOW_X + 1, WINDOW_Y + 1);

				frame.add(panel); //add the panel to the frame
				frame.setSize(WINDOW_X + 1, WINDOW_Y + 1); //set the size of the frame
				frame.setLocationByPlatform(true); //lets the local system (ex. macbook) decide location of the frame
				frame.setVisible(true); //make the frame visible
			}
		});
	}
	*/
	
}
