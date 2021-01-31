
import java.awt.CardLayout;
import javax.swing.JApplet;
import javax.swing.UIManager;

public class Main extends JApplet {

	private static final long serialVersionUID = 1L;

    @Override
	public void init() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {
			
		}
				
    	setSize(400, 600);
    	
		final CardLayout cardLayout = new CardLayout();
		setLayout(cardLayout);
		
		AngryBirdPane angryBirdPane = new AngryBirdPane(this, cardLayout);
		getContentPane().add(angryBirdPane, AbstractPane.AngryBirdPane);
		
		CalmMePane calmMePane = new CalmMePane(this, cardLayout);
		getContentPane().add(calmMePane, AbstractPane.CalmMePane);
		
		HelpMePane helpMePane = new HelpMePane(this, cardLayout);
		getContentPane().add(helpMePane, AbstractPane.HelpMePane);
		
		MusicPane musicPane = new MusicPane(this, cardLayout);
		getContentPane().add(musicPane, AbstractPane.MusicPane);
		
		UserManualPane userManualPane = new UserManualPane(this, cardLayout);
		getContentPane().add(userManualPane, AbstractPane.ManualPane);
		
		OutlinePane outlinePane = new OutlinePane(this, cardLayout);
		getContentPane().add(outlinePane, AbstractPane.OutlinePane);
		
		RestNotificationPane restPane = new RestNotificationPane(this, cardLayout);
		getContentPane().add(restPane, AbstractPane.RestNotificationPane);

		TimerPane timerPane = new TimerPane(this, cardLayout);
		getContentPane().add(timerPane, AbstractPane.TimerPane);
				
		cardLayout.show(getContentPane(), AbstractPane.OutlinePane);
	}
}
