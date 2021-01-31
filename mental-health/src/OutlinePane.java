
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OutlinePane extends AbstractPane {

	private static final long serialVersionUID = 1L;
	private JButton music = new JButton();
	private JButton manual = new JButton();
	private JButton timeMe = new JButton();
	private JButton helpMe = new JButton();
	private JButton calmMe = new JButton();
	private JButton distractMe = new JButton();

	public OutlinePane(JApplet parent, CardLayout cardLayout) {
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

		music.setText("Music");
		music.setBounds(10, 10, 70, 25);
		music.setBackground(Color.white);
		music.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(parent.getContentPane(), AbstractPane.MusicPane);
			}
		});

		manual.setText("User Manual");
		manual.setBounds(270, 10, 120, 25);
		manual.setBackground(Color.white);
		manual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(parent.getContentPane(), AbstractPane.ManualPane);
			}
		});

		timeMe.setText("TIME ME");
		timeMe.setBounds(10, 100, 380, 80);
		timeMe.setBackground(Colors.lightY);
		timeMe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(parent.getContentPane(), AbstractPane.TimerPane);
			}
		});

		helpMe.setText("HELP ME");
		helpMe.setBounds(10, 200, 380, 80);
		helpMe.setBackground(Colors.y);
		helpMe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(parent.getContentPane(), AbstractPane.HelpMePane);
			}
		});

		calmMe.setText("CALM ME");
		calmMe.setBounds(10, 300, 380, 80);
		calmMe.setBackground(Colors.dY);
		calmMe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(parent.getContentPane(), AbstractPane.CalmMePane);
			}
		});

		distractMe.setText("DISTRACT ME");
		distractMe.setBounds(10, 400, 380, 80);
		distractMe.setBackground(Colors.vdY);
		distractMe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(parent.getContentPane(), AbstractPane.AngryBirdPane);
			}
		});

		add(music);
		add(manual);
		add(timeMe);
		add(helpMe);
		add(calmMe);
		add(distractMe);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, 600);
	}


}
