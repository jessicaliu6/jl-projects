import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.*;
import java.net.*;
import java.io.*;
import java.awt.image.*;
import java.applet.*;

public class HelpMe extends Applet implements ActionListener{

	
	private static final long serialVersionUID = 1L;
	
	String[] studyTips= new String[20];
	boolean showTip;
	int tip;

	Button tips=new Button();
	Button help1=new Button();
	Button help2=new Button();
	Button help3=new Button();
	Button help4=new Button();
	Button back=new Button();

	
	BufferedImage desmos;
	BufferedImage symbo;
	BufferedImage sn;
	BufferedImage quiz;
	

	public void init() {
		setBackground(Colors.lightB);
		setLayout(null);
		resize(400,600);

		try {
			fillTips();
		} catch (IOException e) {
			e.printStackTrace();
		}

		tips.setLabel("Give Me a Study Tip");
		tips.addActionListener(this);
		tips.setBounds(80,80,240,40);
		tips.setBackground(Colors.lightY);

		help1.setLabel("DESMOS");
		help1.addActionListener(this);
		help1.setBounds(80,290,90,20); 
		help1.setBackground(Colors.desmos);

		help2.setLabel("QUIZLET");
		help2.setBounds(230,290,90,20);
		help2.addActionListener(this);
		help2.setBackground(Colors.quizlet);

		help3.setLabel("SYMBOLAB");
		help3.addActionListener(this);
		help3.setBounds(80,490,90,20);
		help3.setBackground(Colors.symbolab);

		help4.setLabel("SPARKNOTES");
		help4.setBounds(230,490,90,20);
		help4.addActionListener(this);
		help4.setBackground(Color.blue);
		
		back.setLabel("Back");
		back.setBounds(80,80,240,40);
		back.setActionCommand("back");
		back.addActionListener(this);
		back.setBackground(Colors.lightY);

		add(tips);
		add(help1);
		add(help2);
		add(help3);
		add(help4);
		add(back);
	}
	public void fillTips() throws IOException {
		BufferedReader br=new BufferedReader(new FileReader(getCodeBase().getPath() + "../resources/tips.txt"));//change to c: drive REMEMBER
		for (int i=0;i<20;i++)
			studyTips[i]=br.readLine();
		br.close();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Give Me a Study Tip")) {
			showTip=true;
			tip=(int) Math.round(Math.random()*20);
			repaint();
		}
		if (e.getActionCommand().equals("DESMOS")) {
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
		if (e.getActionCommand().equals("SYMBOLAB")) {
			try {
				Desktop.getDesktop().browse(new URI("https://www.symbolab.com"));
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getActionCommand().equals("QUIZLET")) {
			try {
				Desktop.getDesktop().browse(new URI("https://www.quizlet.com"));
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getActionCommand().equals("SPARKNOTES")) {
			try {
				Desktop.getDesktop().browse(new URI("https://www.sparknotes.com"));
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getActionCommand().equals("back")) {
			showTip=false;
			repaint();
		}
	}
	public void paint(Graphics g) {
		if(showTip) {
			tips.setVisible(false);
			help1.setVisible(false);
			help2.setVisible(false);
			help3.setVisible(false);
			help4.setVisible(false);
			back.setVisible(true);
			g.drawString(studyTips[tip], 0, 300);
		}
		else if (!showTip){   
			tips.setVisible(true);
			help1.setVisible(true);
			help2.setVisible(true);
			help3.setVisible(true);
			help4.setVisible(true);
			back.setVisible(false);
			try 
			{
				URL u = new URL(getCodeBase(),"../resources/desmos.png"); //keep picture in bin folder of project
				desmos = ImageIO.read(u);
			}   
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			g.drawImage(desmos,80,200,90,90,null);
			
			try 
			{
				URL u = new URL(getCodeBase(),"../resources/quiz.png"); //keep picture in bin folder of project
				quiz = ImageIO.read(u);
			}   
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			g.drawImage(quiz,230,200,90,90,null);
			
			try 
			{
				URL u = new URL(getCodeBase(),"../resources/symbolab.png"); //keep picture in bin folder of project
				symbo = ImageIO.read(u);
			}   
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			g.drawImage(symbo,80,400,90,90,null);
			
			try 
			{
				URL u = new URL(getCodeBase(),"../resources/sn.jpg"); //keep picture in bin folder of project
				sn = ImageIO.read(u);
			}   
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			g.drawImage(sn,230,400,90,90,null);

		}
	}
}