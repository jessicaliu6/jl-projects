import java.applet.AudioClip;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JApplet;
import javax.swing.JButton;


public class MusicPane extends AbstractPane implements ActionListener {

	private static final long serialVersionUID = 1L;
	JButton playBtn = new JButton();
	JButton stopBtn = new JButton();
	JButton oceanSoundsBtn = new JButton();
	JButton classicalMusicBtn = new JButton();
	JButton lofiSerendipityBtn = new JButton();
	JButton natureSoundsBtn = new JButton();
	JButton backBtn = new JButton();
	JButton outlineBtn = new JButton(); // back to outline button

	boolean backMusic = false;
	boolean clickedMusic = false;
	String trackName = "ocean.wav";
	AudioClip audioClip;

	public static final Color lightY = new Color(255, 255, 153);
	boolean bubbles = false;

	public MusicPane(JApplet parent, CardLayout cardLayout) {
		super(parent, cardLayout);

		addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentShown( ComponentEvent e ) {
	            requestFocusInWindow();
	    		setBackground(Colors.paneBackground);
	    		parent.setBackground(Colors.paneBackground);
	    		parent.getContentPane().setBackground(Colors.paneBackground);
	        }
	    });
		
		init();
	}

	public void init() {
		setLayout(null);
		setBackground(Colors.paneBackground);
		parent.setBackground(Colors.paneBackground);
		parent.getContentPane().setBackground(Colors.paneBackground);
		setSize(400, 600);
		
		oceanSoundsBtn.setText("Ocean Sounds");
		oceanSoundsBtn.setBounds(10, 50, 380, 70);
		oceanSoundsBtn.setBackground(lightY);
		oceanSoundsBtn.addActionListener(this);
		add(oceanSoundsBtn);

		classicalMusicBtn.setText("Classical Music");
		classicalMusicBtn.setBounds(10, 160, 380, 70);
		classicalMusicBtn.setBackground(lightY);
		classicalMusicBtn.addActionListener(this);
		add(classicalMusicBtn);

		lofiSerendipityBtn.setText("Lofi Serendipity");
		lofiSerendipityBtn.setBounds(10, 270, 380, 70);
		lofiSerendipityBtn.setBackground(lightY);
		lofiSerendipityBtn.addActionListener(this);
		add(lofiSerendipityBtn);

		natureSoundsBtn.setText("Nature Sounds");
		natureSoundsBtn.setBounds(10, 380, 380, 70);
		natureSoundsBtn.setBackground(lightY);
		natureSoundsBtn.addActionListener(this);
		add(natureSoundsBtn);
		
		outlineBtn.setText("Return To Outline");
		outlineBtn.setBounds(10, 490, 380, 70);
		outlineBtn.setBackground(lightY);
		outlineBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(parent.getContentPane(), AbstractPane.OutlinePane);
			}
		});
		add(outlineBtn);

		playBtn.setText("PLAY");
		playBtn.setBounds(100, 300, 100, 50);
		playBtn.addActionListener(this);
		playBtn.setVisible(false);
		add(playBtn);

		stopBtn.setText("STOP");
		stopBtn.setBounds(200, 300, 100, 50);
		stopBtn.addActionListener(this);
		add(stopBtn);
		stopBtn.setVisible(false);

		backBtn.setText("BACK");
		backBtn.setBounds(10, 10, 50, 50);
		backBtn.addActionListener(this);
		add(backBtn);
		backBtn.setVisible(false);		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Ocean Sounds") 
				| e.getActionCommand().equals("Classical Music")
				| e.getActionCommand().equals("Nature Sounds") 
				| e.getActionCommand().equals("Lofi Serendipity")) {
			playBtn.setVisible(true);
			stopBtn.setVisible(true);
			backBtn.setVisible(true);
			oceanSoundsBtn.setVisible(false);
			classicalMusicBtn.setVisible(false);
			lofiSerendipityBtn.setVisible(false);
			natureSoundsBtn.setVisible(false);
			outlineBtn.setVisible(false);
		}
		
		if (e.getActionCommand().equals("Classical Music")) {
			trackName = "classical music.wav";
		} else if (e.getActionCommand().equals("Lofi Serendipity")) {
			trackName = "serendipity lofi.wav";
		} else if (e.getActionCommand().equals("Ocean Sounds")) {
			trackName = "ocean.wav";
		} else if (e.getActionCommand().equals("Nature Sounds")) {
			trackName = "rain.wav";
		} else if (e.getActionCommand().equals("PLAY")) {
			if (audioClip != null) {
				audioClip.stop();
			}
			audioClip = parent.getAudioClip(parent.getCodeBase(), "../resources/" + trackName);
			audioClip.play();
		} else if (e.getActionCommand().equals("STOP")) {
			if (audioClip != null) {
				audioClip.stop();
			}
		} else if (e.getActionCommand().equals("BACK")) {
			backBtn.setVisible(false);
			playBtn.setVisible(false);
			stopBtn.setVisible(false);
			oceanSoundsBtn.setVisible(true);
			classicalMusicBtn.setVisible(true);
			lofiSerendipityBtn.setVisible(true);
			natureSoundsBtn.setVisible(true);
			outlineBtn.setVisible(true);
		}
		// else {
		// audioClip.stop();
		// }

	}
}
