import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JApplet;
import javax.swing.JButton;

import java.applet.AudioClip;

public class CalmMePane extends AbstractPane implements Runnable, ActionListener {

	private static final long serialVersionUID = 1L;

	Color circle = new Color(0, 0, 153);
	int counter;
	Thread gs;
	boolean breathe = true; // runs while loops that shrink and grow circle. Made false when user is done							// breathing.
	boolean showBreathing = false; // determines whether breathing circle is on screen or not

	JButton breathing = new JButton();
	JButton meds = new JButton();
	JButton outline = new JButton();
	JButton stop = new JButton();
	JButton calm = new JButton();
	AudioClip guidedMed;

	public CalmMePane(JApplet parent, CardLayout cardLayout) {
		super(parent, cardLayout);

		addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentShown( ComponentEvent e ) {
	            requestFocusInWindow();
	    		setBackground(Color.yellow);
	    		parent.setBackground(Color.yellow);
	    		parent.getContentPane().setBackground(Color.yellow);
	        }
	    });
		
		init();
	}

	public void init() { // create thread
		setLayout(null);
		setBackground(Color.yellow);
		parent.setBackground(Color.yellow);
		parent.getContentPane().setBackground(Color.yellow);
		setSize(400, 600);

		breathing.setText("Breathing Exercises");
		breathing.addActionListener(this);
		breathing.setBounds(5, 100, 390, 80);
		breathing.setBackground(Color.cyan);
		add(breathing);

		meds.setText("Guided Meditation");
		meds.addActionListener(this);
		meds.setBounds(5, 250, 390, 80);
		meds.setBackground(Color.pink);
		add(meds);

		outline.setText("Return To Outline");
		outline.setBounds(5, 400, 390, 80);
		outline.setBackground(Color.green);
		outline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(parent.getContentPane(), AbstractPane.OutlinePane);
			}
		});
		add(outline);

		stop.setText("Back");
		stop.addActionListener(this);
		stop.setBounds(70, 200, 240, 40);
		stop.setBackground(Color.green);
		stop.setVisible(false);
		add(stop);

		calm.setText("I'm Calm");
		calm.addActionListener(this);
		calm.setBounds(70, 500, 240, 40);
		calm.setBackground(Color.cyan);
		calm.setVisible(false);
		add(calm);

		guidedMed = parent.getAudioClip(parent.getCodeBase(), "../resources/wentao.wav");
		
		counter = 250;
		gs = new Thread(this);
		gs.start();
	}

	public void stop() {
		gs = null;
	}

	public void run() { // executed by Thread
		while (breathe) {
			while (counter < 300 && counter >= 250 && gs != null) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
				counter = counter + 1;
				repaint(); // update screen
			}
			while (counter > 250 && gs != null) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
				counter = counter - 1;
				repaint(); // update screen
			}
		}
	}

	public void paintComponent(Graphics g) {
		if (showBreathing) {
			g.setColor(circle);
			g.fillOval(50, 150, counter, counter);
			g.drawString("Breathe in as the circle grows. Breathe out as it shrinks", 50, 100);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Breathing Exercises")) {
			showBreathing = true;
			meds.setVisible(false);
			outline.setVisible(false);
			breathing.setVisible(false);
			calm.setVisible(true);
		}
		else if (e.getActionCommand().equals("I'm Calm")) {
			showBreathing = false;
			calm.setVisible(false);
			meds.setVisible(true);
			outline.setVisible(true);
			breathing.setVisible(true);
		}
		else if (e.getActionCommand().equals("Guided Meditation")) {
			setBackground(Color.blue);
			meds.setVisible(false);
			breathing.setVisible(false);
			outline.setVisible(false);
			guidedMed.play();
			stop.setVisible(true);
		}
		else if (e.getActionCommand().equals("Back")) {
			setBackground(Color.yellow);
			guidedMed.stop();
			stop.setVisible(false);
			meds.setVisible(true);
			outline.setVisible(true);
			breathing.setVisible(true);
		}
	}
}
