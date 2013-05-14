package controller;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import client.MainClient;

public class KeyboardController extends KeyAdapter{
	
	private MainClient client;
	
	public KeyboardController(MainClient client)
	{
		this.client = client;
	}
	
	
	public void keyPressed(KeyEvent key){
		
		switch (key.getKeyCode()){
		case KeyEvent.VK_UP:
			client.sendMoveRequest(client.getPlayer(), 0, -5);
			break;
		case KeyEvent.VK_LEFT:
			client.sendMoveRequest(client.getPlayer(), -5, 0);
			break;
		case KeyEvent.VK_RIGHT:
			client.sendMoveRequest(client.getPlayer(), 5, 0);
			break;
		case KeyEvent.VK_DOWN:
			client.sendMoveRequest(client.getPlayer(), 0, 5);
			break;
		}
	}
}

