import java.awt.CardLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JPanel;

public abstract class AbstractPane extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public static final String AngryBirdPane = "angryBird";
	public static final String CalmMePane = "calmMe";
	public static final String HelpMePane = "helpMe";
	public static final String MusicPane = "music";
	public static final String OutlinePane = "outline";
	public static final String RestNotificationPane = "rest";
	public static final String TimerPane = "timer";
	public static final String ManualPane = "manual";

	protected JApplet parent;
	protected CardLayout cardLayout;

	public AbstractPane(JApplet parent, CardLayout cardLayout) {
		this.parent = parent;
		this.cardLayout = cardLayout;
	}
	

    // Returns an ImageIcon, or null if the path was invalid
	public ImageIcon createImageIcon(String path) {
		URL imgURL = AbstractPane.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}

