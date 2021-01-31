import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class HelpMePane extends AbstractPane implements ActionListener {

	private static final long serialVersionUID = 1L;

	String[] studyTips = new String[20];
	boolean showTip = false;
	int tip;
	
	JLabel tipLabel = new JLabel();

	JButton tips = new JButton();
	JButton help1 = new JButton();
	JButton help2 = new JButton();
	JButton help3 = new JButton();
	JButton help4 = new JButton();
	JButton back = new JButton();
	JButton outline = new JButton(); // back to outline button

	// Color bg = new Color(100, 70, 230);
	Color bg = new Color(100, 100, 250);
	Color lightB = new Color(51, 153, 255);
	Color b = new Color(0, 153, 0);// change color
	Color niceB = new Color(0, 0, 204);
	Color vLightR = new Color(255, 102, 102);
	Color lightR = new Color(255, 51, 51);

	BufferedImage desmos;
	BufferedImage symbo;
	BufferedImage sn;
	BufferedImage quiz;

	public HelpMePane(JApplet parent, CardLayout cardLayout) {
		super(parent, cardLayout);

		addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentShown( ComponentEvent e ) {
	            requestFocusInWindow();
	    		setBackground(lightB);
	    		parent.setBackground(lightB);
	    		parent.getContentPane().setBackground(lightB);
	        }
	    });

		init();
	}

	public void init() {
		// To set background color, must set both current JPanel and parent
		setBackground(lightB);
		parent.setBackground(lightB);
		parent.getContentPane().setBackground(lightB);
		
		setLayout(null);
		setSize(400, 600);

		try {
			fillTips();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tips.setText("Give Me a Study Tip");
		tips.setBounds(70, 50, 240, 40);
		tips.addActionListener(this);
		tips.setBackground(lightR);

		help1.setText("DESMOS");
		help1.addActionListener(this);
		help1.setBounds(70, 240, 90, 20);
		help1.setBackground(b);

		help2.setText("QUIZLET");
		help2.setBounds(220, 240, 90, 20);
		help2.addActionListener(this);
		help2.setBackground(bg);

		help3.setText("SYMBOLAB");
		help3.addActionListener(this);
		help3.setBounds(70, 430, 90, 20);
		help3.setBackground(lightR);
		help3.setMargin(new Insets(2, 1, 2, 1)); // default is (2, 14, 2, 14), size not enough to show all text

		help4.setText("SPARKNOTES");
		help4.setBounds(220, 430, 90, 20);
		help4.addActionListener(this);
		help4.setBackground(Color.cyan);
		help4.setMargin(new Insets(2, 1, 2, 1)); // default is (2, 14, 2, 14), size not enough to show all text

		outline.setText("Return To Outline");
		outline.setBounds(70, 510, 240, 40);
		outline.setBackground(lightR);
		outline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(parent.getContentPane(), AbstractPane.OutlinePane);
			}
		});

		back.setText("Back");
		back.setBounds(70, 80, 240, 40);
		back.addActionListener(this);
		back.setBackground(Color.cyan);
		back.setVisible(false);

		tipLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tipLabel.setBounds(30, 300, 340, 80);
		tipLabel.setVisible(false);

		add(tips);
		add(help1);
		add(help2);
		add(help3);
		add(help4);
		add(outline);
		add(back);
		add(tipLabel);
	}

	public void fillTips() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(parent.getCodeBase().getPath() + "../resources/tips.txt"));

		int i = 0;
		String line = br.readLine();
		while (line != null) {
			line = line.trim();
			if (line.length() > 0) {
				studyTips[i] = line;
				// System.out.println("studyTips: " + studyTips[i]);
				i = i + 1;
				if (i == 20) {
					break;
				}
			}
			line = br.readLine();
		}
		
		br.close();
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("Give Me a Study Tip")) {
			showTip = true;
			tip = (int) Math.round(Math.random() * 19); // 0 - 19
			
			System.out.println("tip: " + tip);
			
			tipLabel.setText(String.format("<html><font size=5 color=yellow>%s</font></html>", studyTips[tip]));
			
			tips.setVisible(false);
			help1.setVisible(false);
			help2.setVisible(false);
			help3.setVisible(false);
			help4.setVisible(false);
			outline.setVisible(false);
			
			back.setVisible(true);
			tipLabel.setVisible(true);

			// repaint();
		}
		else if (event.getActionCommand().equals("DESMOS")) {
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().browse(new URI("https://www.desmos.com"));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		}
		else if (event.getActionCommand().equals("SYMBOLAB")) {
			try {
				Desktop.getDesktop().browse(new URI("https://www.symbolab.com"));
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
		else if (event.getActionCommand().equals("QUIZLET")) {
			try {
				Desktop.getDesktop().browse(new URI("https://www.quizlet.com"));
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
		else if (event.getActionCommand().equals("SPARKNOTES")) {
			try {
				Desktop.getDesktop().browse(new URI("https://www.sparknotes.com"));
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
		else if (event.getActionCommand().equals("Back")) {
			showTip = false;
			
			tips.setVisible(true);
			help1.setVisible(true);
			help2.setVisible(true);
			help3.setVisible(true);
			help4.setVisible(true);
			outline.setVisible(true);

			back.setVisible(false);
			tipLabel.setVisible(false);
		}
	}

	public void paintComponent(Graphics g) {
		if (! showTip) {
			try {
				URL u = new URL(parent.getCodeBase(), "../resources/desmos.png"); // keep picture in bin folder of project
				desmos = ImageIO.read(u);
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(desmos, 70, 150, 90, 90, null);

			try {
				URL u = new URL(parent.getCodeBase(), "../resources/quiz.png"); // keep picture in bin folder of project
				quiz = ImageIO.read(u);
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(quiz, 220, 150, 90, 90, null);

			try {
				URL u = new URL(parent.getCodeBase(), "../resources/symbolab.png"); // keep picture in bin folder of project
				symbo = ImageIO.read(u);
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(symbo, 70, 341, 90, 90, null);

			try {
				URL u = new URL(parent.getCodeBase(), "../resources/sn.jpg"); // keep picture in bin folder of project
				sn = ImageIO.read(u);
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(sn, 220, 340, 90, 90, null);

		}
	}
}