import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.applet.Applet;
import java.applet.AudioClip;    
public class CalmMe extends Applet implements Runnable, ActionListener{

	private static final long serialVersionUID = 1L;
	
	Color circle=new Color (0,0,153);
	int counter; 
	Thread gs;
	boolean breathe=true; //runs while loops that shrink and grow circle. Made false when user is done breathing.
	boolean yes; //determines whether breathing circle is on screen or not
	boolean hide;
	
	Button breathing = new Button();
	Button meds = new Button();
	Button stop=new Button();
	Button calm =new Button();
	AudioClip guidedMed;

	public void start() { // create thread
		setLayout(null);
		resize(400,600);
		setBackground(Color.yellow);
		breathing.setLabel("Breathing Exercises");
		breathing.addActionListener(this);
		breathing.setBounds(5,205,390,80);
		breathing.setBackground(Color.cyan);
		add(breathing);
		meds.setLabel("Guided Meditation");
		meds.addActionListener(this);
		meds.setBounds(5,335,390,80);
		meds.setBackground(Color.pink);
		add(meds);
		stop.setLabel("Back");
		stop.addActionListener(this);
		stop.setActionCommand("Back1");
		stop.setBounds(70,200,240,40);
		stop.setBackground(Color.green);
		add(stop);
		stop.setVisible(false);
		
		calm.setLabel("I'm Calm");
		calm.addActionListener(this);
		calm.setBounds(70,500,240,40);
		calm.setBackground(Color.cyan);
		add(calm);
		calm.setVisible(false);
		
		guidedMed = getAudioClip(getCodeBase(), "wentao.wav");
		
		counter = 250; 
		gs = new Thread(this); 
		gs.start();
	}
	public void stop() { 
		gs = null;
	}
	public void run() { // executed by Thread
	while (breathe) {
			while (counter<300&&counter>=250  && gs!=null) {
				try{
					Thread.sleep(100);
				} catch (InterruptedException e){}
				counter=counter+1; 
				repaint(); //update screen
			}
			while (counter>250 && gs!=null) {
				try{
					Thread.sleep(100);
				} catch (InterruptedException e){}
				counter=counter-1; 
				repaint(); //update screen
			}	
		}
	}
	public void paint (Graphics g) {	
		if(yes) {
			g.setColor(circle);
			g.fillOval(50, 150, counter, counter);
			g.drawString("Breathe in as the circle grows. Breathe out as it shrinks", 50, 100);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Breathing Exercises")) {
			yes=true;
			meds.setVisible(false);
			breathing.setVisible(false);
			calm.setVisible(true);
		}
		if(e.getActionCommand().equals("Guided Meditation")) {
			setBackground(Color.blue);
			meds.setVisible(false);
			breathing.setVisible(false);
			guidedMed.play();
			stop.setVisible(true);		
		}
		if(e.getActionCommand().equals("Back1")) {
			setBackground(Color.yellow);
			guidedMed.stop();
			stop.setVisible(false);
			meds.setVisible(true);
			breathing.setVisible(true);
		}
		if(e.getActionCommand().equals("I'm Calm")) {
			calm.setVisible(false);
			meds.setVisible(true);
			breathing.setVisible(true);
			yes=false;
		}
	}
}