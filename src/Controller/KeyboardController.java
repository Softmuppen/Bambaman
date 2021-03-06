package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.io.Serializable;
import Character.Player;
import Engine.Collision;

/**
 * Keeps track of which keys that the user is pressing and takes
 * action according to which keys that are pressed.
 * @author kristoffer
 */
public class KeyboardController extends KeyAdapter  implements Serializable{

	// fields:
	private static final long serialVersionUID = 32L;
	private Player player;

	/**
	 * Constructor
	 * @param player The playercharacter
	 * @param collision The collision manager
	 */
	public KeyboardController(Player player){	
		this.player = player;
	}

	/**
	 * Gets called when a key is pressed
	 * @param key The key you have pressed
	 */
	public void keyPressed(KeyEvent key){
		switch (key.getKeyCode()){
		case KeyEvent.VK_UP:
			player.setDy(-1);
			break;
		case KeyEvent.VK_LEFT:
			player.setDx(-1);
			break;
		case KeyEvent.VK_RIGHT:
			player.setDx(1);
			break;
		case KeyEvent.VK_DOWN:
			player.setDy(1);
			break;
		}
	}

	/**
	 * Gets called when a key is released
	 * @param key The key you have released
	 */
	public void keyReleased(KeyEvent key){

		switch (key.getKeyCode()){
		case KeyEvent.VK_UP:
			player.setDy(0);
			break;
		case KeyEvent.VK_LEFT:
			player.setDx(0);
			break;
		case KeyEvent.VK_RIGHT:
			player.setDx(0);
			break;
		case KeyEvent.VK_DOWN:
			player.setDy(0);
			break;
		case KeyEvent.VK_SPACE:
			System.out.println(player.getX() + ", " + player.getY());
			break;
		}
	}
}
