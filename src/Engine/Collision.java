package Engine;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.io.Serializable;

import Character.Player;
import World.Tile;

/**
 * This handles all sort of collision in the game, Character->Tile, Character->Character and more
 * It contains methods that is constantly called and methods
 * that is only invoked when with certain events occur (such as attacks).
 * 
 * @author Richard Norling
 * @version 2013-02-28
 */
public class Collision implements Serializable{

	// fields:
	private static final long serialVersionUID = 8L;
	private ArrayList<Player> players;
	private ArrayList<Tile> blockTiles;
	

	/**
	 * Constructor
	 * @param player
	 * @param blockTiles
	 * @param characters
	 */
	public Collision(ArrayList<Player> players, ArrayList<Tile> blockTiles){
		this.players = players;
		this.blockTiles = blockTiles;
	}

	/**
	 * Updates active BlockTiles
	 * @param blockTiles
	 */
	public void setCurrentTiles(ArrayList<Tile> blockTiles){this.blockTiles = blockTiles;}

	/**
	 * Updates active characters
	 * @param characters
	 */
	public void setCurrentPlayerCharacters(ArrayList<Player> players){this.players = players;}

	

	/**
	 * Checks for new collisions, updates constantly
	 */
	public void update(){
		// Player
		checkPlayerTileCollision();		// Checks if <PlayerCharacter> collides with <BlockTile>.
		checkPlayerPlayerCollision();

		//checkItemCollision();			// Checks if <PlayerCharacter> enters <Item> bounds.
		//checkProjectileCollision();	// Checks if <Projectile> hits <Characters> or <Blocktiles>
		//checkInteractCollision();    	// Checks if <PlayerCharacter> enters a <Character> area.
	}

	/**
	 * Checks for Player to Character collision
	 */
	/*public void checkPlayerCharacterCollision(){
		for(Character c : characters){
			if(player.getBounds().intersects(c.getBounds())){
				moveBack(player);
			}
		}
	}*/

	/**
	 * Checks for Player to BlockTile collision
	 */
	public void checkPlayerTileCollision(){
		for(Player player : players){
			player.setY(player.getY()-player.getDy());		// move from collision
			player.setX(player.getX()-player.getDx());		// move from collision

			player.setY(player.getY()+player.getDy());
			for(Tile t1 : blockTiles){
				if(player.getBounds().intersects(t1.getBounds())){
					player.setY(player.getY()-player.getDy());
				}
			}

			player.setX(player.getX()+player.getDx());
			for(Tile t2 : blockTiles){
				if(player.getBounds().intersects(t2.getBounds())){
					player.setX(player.getX()-player.getDx());
				}
			}
		}
	}

	/**
	 * Checks for all Character to Character collision ( NOT PLAYER )
	 * Uses a overridden equals() in Character.
	 */
	public void checkPlayerPlayerCollision(){
		for(Player p1 : players){
			for(Player p2 : players){
				if( !p1.equals(p2) ){
					if(p1.getBounds().intersects(p2.getBounds())){
						moveBack(p1);
					}
				}
			}
		}
	}


	/**
	 * Checks for a single Character to Tile collision
	 * shouldn't be updated constantly.
	 * @param character
	 */
	public void checkSingleCharacterTileCollision(Player players){
		for(Tile blockTile : blockTiles){
			Rectangle block = blockTile.getBounds();
			if(players.getBounds().intersects(block)){
				moveBack(players);
			}
		}
	}


	/**
	 * Checks for all character tile collision ( NON PLAYER )
	 */
	public void checkPlayerCharacterTileCollision(){
		for(Tile blockTile : blockTiles){
			Rectangle block = blockTile.getBounds();
			for(Player p : players){
				if(p.getBounds().intersects(block)){
					moveBack(p);
				}
			}
		}
	}

	/**
	 * Checks if a Player is colliding with a Item
	 */
	/*
	public void checkItemCollision(){
		for(Player player : players){
			for(Item item : items){
				if(item.getBounds().intersects(player.getBounds()) ){
					// Play a pickup sound
					player.pickUpItem(item);
					System.out.println("Item picked up.");
				}
			}
		}
	}*/

	/**
	 * Pushes a character a certain amount of pixels in a certain direction
	 * depending on the attackers direction.
	 * @param pusher
	 * @param target
	 * @param pixels
	 */
	/*public void pushCharacter(Character pusher, Character target, int pixels){
		
		String pushDirection = pusher.getDirection();
		
		for(int i = 0; i < pixels ; i++){
			
			String beforeDirection = target.getDirection();
			
			if(pushDirection == "up"){
				target.setY(target.getY()-1);
				target.setDirection("up");
				
				for(Tile blockTile : blockTiles){
					Rectangle block = blockTile.getBounds();
					if(target.getBounds().intersects(block)){
						target.setY(target.getY()+1);
					}
				}
			}
			if(pushDirection == "down"){ 
				target.setY(target.getY()+1);
				target.setDirection("down");

				for(Tile blockTile : blockTiles){
					Rectangle block = blockTile.getBounds();
					if(target.getBounds().intersects(block)){
						target.setY(target.getY()-1);
					}
				}
			}
			if(pushDirection == "left"){
				target.setX(target.getX()-1);
				target.setDirection("left");
				
				for(Tile blockTile : blockTiles){
					Rectangle block = blockTile.getBounds();
					if(target.getBounds().intersects(block)){
						target.setX(target.getX()+1);
					}
				}
			}
			if(pushDirection == "right"){
				target.setX(target.getX()+1);
				target.setDirection("right");

				for(Tile blockTile : blockTiles){
					Rectangle block = blockTile.getBounds();
					if(target.getBounds().intersects(block)){
						target.setX(target.getX()-1);
					}
				}
			}
			target.setDirection(beforeDirection);
		}
	}*/

	/**
	 * Moves the character one pixel back from the way he is moving
	 * @param character
	 */
	public void moveBack(Player character){
		if(character.getDy() < 0){
			character.setY(character.getY()+1);
		}
		if(character.getDx() < 0){
			character.setX(character.getX()+1);
		}
		if(character.getDx() > 0){
			character.setX(character.getX()-1);
		}
		if(character.getDy() > 0){
			character.setY(character.getY()-1);
		}
	}
}
