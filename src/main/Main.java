package main;

import javax.swing.UIManager;

import gui.ClientGUI;

public class Main {

	public static void main(String[] args)
	{
		try {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	} catch (Exception e) {
		// TODO Auto-generated catch Ablock
		e.printStackTrace();
	}
		
		ClientGUI client = new ClientGUI();
		Thread t = new Thread(client);
		t.start();

	}
	
}
