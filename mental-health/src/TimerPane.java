
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TimerPane extends AbstractPane {

	private static final long serialVersionUID = 1L;

	private int counter;

	private Timer timer;

	private JLabel label;
	private JSlider sb;
	private JButton b1 = new JButton();
	private JButton b2 = new JButton();
	private JButton b3 = new JButton();
	private JButton b4 = new JButton();

	public TimerPane(JApplet parent, CardLayout cardLayout) {
		super(parent, cardLayout);

		init();
	}

	private void updateTime() {
		label.setText(String.format("<html><font size='18' color=green>%02d : %02d</font></html>", 
				counter / 60,
				counter % 60));
	}

	public void init() {

		counter = 30 * 60;

		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				--counter;
				if (counter <= 0) {
					timer.stop();
					cardLayout.show(parent.getContentPane(), AbstractPane.RestNotificationPane);
				} else {
					updateTime();
					repaint();
				}
			}
		});

		setLayout(null);
		setBackground(Colors.vLightY);
		setSize(400, 600);

		label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(10, 30, 380, 80);
		// label.setOpaque(true);
		// label.setBackground(Color.RED);
		updateTime();

		sb = new JSlider(JSlider.HORIZONTAL, 0, 60, 30);
		sb.setValue(30);
		sb.setValue(0);
		sb.setPaintTicks(true);
		sb.setMajorTickSpacing(10);
		sb.setMinorTickSpacing(5);
		// sb.setBorder(new TitledBorder("Change timer"));
		sb.setBounds(10, 100, 380, 80);
		sb.setLocation(5, 100);
		// sb.setSize(160, 20);
		sb.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				counter = 60 * sb.getValue();
				updateTime();
			}
		});

		b2.setText("Start");
		b2.setBounds(10, 200, 380, 80);
		b2.setBackground(Colors.y);
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer.start();
			}
		});

		b3.setText("Pause");
		b3.setBounds(10, 300, 380, 80);
		b3.setBackground(Colors.dY);
		b3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer.stop();
			}
		});

		b4.setText("Back");
		b4.setBounds(10, 400, 380, 80);
		b4.setBackground(Colors.lightO);
		b4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				cardLayout.show(parent.getContentPane(), AbstractPane.OutlinePane);
			}
		});

		add(label);
		add(sb);
		add(b2);
		add(b3);
		add(b4);

	}

}
