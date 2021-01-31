
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RestNotificationPane extends AbstractPane {

	private static final long serialVersionUID = 1L;

	private Timer timer;

	private JLabel msgLabel;
	private JButton okBtn = new JButton();
	
	private Color currentBackground = Colors.vLightY;

	public RestNotificationPane(JApplet parent, CardLayout cardLayout) {
		super(parent, cardLayout);

		addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentShown( ComponentEvent e ) {
	            requestFocusInWindow();
	    		setBackground(Colors.vLightY);
	    		parent.setBackground(Colors.vLightY);
	    		parent.getContentPane().setBackground(Colors.vLightY);
	        }
	    });

		init();
	}
	
	private void toggleBackground() {
		setOpaque(true);
		currentBackground = currentBackground.equals(Colors.vLightY) ? Colors.vLightG : Colors.vLightY;
		setBackground(currentBackground);
	}

	public void init() {
		setLayout(null);
		setBackground(Colors.vLightY);
		parent.setBackground(Colors.vLightY);
		parent.getContentPane().setBackground(Colors.vLightY);
		setSize(400, 600);

		msgLabel = new JLabel();
		msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
		msgLabel.setBounds(10, 150, 380, 80);
		msgLabel.setText("<html><font size='18' color=green>Take a break !</font></html>");

		timer = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleBackground();
			}
		});

		okBtn.setText("OK");
		okBtn.setBounds(10, 400, 380, 80);
		okBtn.setBackground(Colors.dY);

		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				cardLayout.show(parent.getContentPane(), AbstractPane.OutlinePane);
			}
		});

		add(msgLabel);
		add(okBtn);
		
		timer.start();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, 600);
	}

}
