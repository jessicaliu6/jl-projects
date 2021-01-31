import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
public class Music extends Applet implements ActionListener {

	private static final long serialVersionUID = 1L;
	Button play = new Button();
	Button stop = new Button();
	Button track1=new Button();
	Button track2=new Button();
	Button track3=new Button();
	Button track4=new Button();
	Button back =new Button();
	
	boolean backMusic=false;
	boolean clickedMusic=false;
	String trackName="ocean.wav";
	AudioClip audioClip;


	public static final Color lightY=new Color (255,255,153);
	boolean bubbles=false;

	public void init(){
		setLayout(null);
		resize(400,600);
		track1.setLabel("Ocean Sounds");
		track1.setBounds (5,75,390,80);
		track1.setBackground(lightY);
		track1.addActionListener(this);
		add(track1);

		track2.setLabel("Classical Music");
		track2.setBounds(5,205,390,80);
		track2.setBackground(lightY);
		track2.addActionListener(this);
		add(track2);

		track3.setLabel("Lofi Serendipity");
		track3.setBounds(5,335,390,80);
		track3.setBackground(lightY);
		track3.addActionListener(this);
		add(track3);

		track4.setLabel("Nature Sounds");
		track4.setBounds(5,465,390,80);;
		track4.setBackground(lightY);
		track4.addActionListener(this);
		add(track4);


		play.setLabel("PLAY");
		play.setBounds(100,300,100,50);
		play.addActionListener(this);

		stop.setLabel("STOP");
		stop.setBounds(200,300,100,50);
		stop.addActionListener(this);

		back.setLabel("BACK");
		back.setBounds(10,10,50,50);
		back.addActionListener(this);

		add(play);
		add(stop);
		add(back);
		play.setVisible(false);
		stop.setVisible(false);
		back.setVisible(false);
		audioClip = getAudioClip(getCodeBase(), trackName);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Ocean Sounds")|e.getActionCommand().equals("Classical Music")|e.getActionCommand().equals("Nature Sounds")|e.getActionCommand().equals("Chill")) {
			play.setVisible(true);
			stop.setVisible(true);
			back.setVisible(true);
			track1.setVisible(false);
			track2.setVisible(false);
			track3.setVisible(false);
			track4.setVisible(false);
			repaint();
		}
		if(e.getActionCommand().equals("Classical Music")) {
			trackName="classical music.wav";
		}
		if(e.getActionCommand().equals("Chill")) {
			trackName="serendipity lofi.wav";
		}
		if(e.getActionCommand().equals("Ocean Sounds")) {
			trackName="ocean.wav";
		}
		if(e.getActionCommand().equals("Nature Sounds")) {
			trackName="rain.wav";
		}
		if(e.getActionCommand().equals("PLAY")){
			audioClip = getAudioClip(getCodeBase(), trackName);
			audioClip.play();
		}
		else if(e.getActionCommand().equals("STOP")){
			audioClip.stop();
		}
		else if(e.getActionCommand().equals("BACK")) {
			back.setVisible(false);
			play.setVisible(false);
			stop.setVisible(false);
			track1.setVisible(true);
			track2.setVisible(true);
			track3.setVisible(true);
			track4.setVisible(true);
		}
		//else {
		//	audioClip.stop();
		//}
		
	}
	public void paint(Graphics g) {
		if(backMusic) {
			
		}
	}
}
