
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.*;

public class UserManualPane extends AbstractPane {

	private static final long serialVersionUID = 1L;

	private JScrollPane scrollPane;
	private JTextPane manual;
	private JButton okBtn = new JButton();
	
	public UserManualPane(JApplet parent, CardLayout cardLayout) {
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
	
	public void init() {
		setLayout(null);
		setBackground(Colors.vLightY);
		parent.setBackground(Colors.vLightY);
		parent.getContentPane().setBackground(Colors.vLightY);
		setSize(400, 600);

		manual = new JTextPane();
		manual.setEditable(false);
		manual.setBackground(Color.white);
		manual.setContentType("text/html"); // it support html or text
		String contents = readManual(); // read the manual from a file "../resources/user_manual.html"
		manual.setText(contents);
		
		
		scrollPane = new JScrollPane(manual);
		scrollPane.setBounds(1, 1, 398, 550);
		scrollPane.setBackground(Color.white);

		//scrollPane.setVerticalScrollBarPolicy(
		//                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		okBtn.setText("OK");
		okBtn.setBounds(150, 560, 80, 25);
		okBtn.setBackground(Colors.dY);
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(parent.getContentPane(), AbstractPane.OutlinePane);
			}
		});

		add(scrollPane);
		add(okBtn);
		
	}

	public String readManual() {
		String path = parent.getCodeBase().getPath() + "../resources/user_manual.html";

		try {
			String contents = new String(Files.readAllBytes(Paths.get(path)));
			return contents;
		} catch (IOException e) {
		    System.err.format("Fail to read manual.", e);
		    return "";
		}		
	}
}
