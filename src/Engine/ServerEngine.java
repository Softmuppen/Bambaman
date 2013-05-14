package Engine;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import Character.Player;
import World.World;
import Engine.Collision;

import Networking.ClientPacket;
import Networking.Network;
import Networking.ServerPacket;
//import Networking.ClientPacket;
//import Networking.ServerPacket;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.minlog.Log;


/**
 * GameEngine
 */
public class ServerEngine implements Runnable, Serializable{

	// networking
	Server server;

	// fields:
	private static final long serialVersionUID = 12L;
	private World world;
	private ArrayList<Player> players;
	private Collision collision;	

	// constants:
	private static final int PLAYER_WIDTH = 22;
	private static final int PLAYER_HEIGHT = 28;
	private static final int PLAYER_LIFE = 100;
	private static final int PLAYER_MONEY = 100;
	private static final int PLAYER_INVENTORY_SIZE = 6;
	private static final int PLAYER_MAXHEALTH = 100;

	/**
	 * Constructor
	 */
	public ServerEngine(){

		world = new World(1);
		players = new ArrayList<Player>();
		collision = new Collision(players, world.getCurrentMap().getBlockTiles());

		initServer();
		server.start();
	}

	public synchronized void initServer(){
		server = new Server(32768,32768);
		Network.register(server);

		server.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				Log.trace("[SERVER] Server received an object.." + object.getClass());
				
				if(object.equals(null)){
					Log.error("[SERVER] Server received an null object");
				}

				if(object instanceof String){
					Log.trace("[SERVER] Received string: " + (String)object);
				}

				if (object instanceof ClientPacket) {
					
					ClientPacket cp = (ClientPacket) object;
					
					if( cp.message.equals(null)){
						Log.error("[SERVER] ClientPacket contained a null message");
					}else{
						Log.debug("[SERVER] Server recieved a ClientPacket: " + cp.message + ", from: " + connection.getRemoteAddressTCP());
					
						if( cp.message.equals("join_request")){
							ServerPacket joinResponse = new ServerPacket();
							joinResponse.message = "join_request_approved";
							Player createdPlayer = createPlayer(cp.player.getName());
							joinResponse.message = "join_request_approved";
							joinResponse.clientPlayer = createdPlayer;
							Log.debug("OMGOMGOMG" + createdPlayer.getName());
							connection.sendTCP(joinResponse);
							Log.debug("[SERVER] A player joined the server: " + cp.player.getName());
						}else if( cp.message.equals("client_player_update")){
							Player player = cp.player;
							updatePlayer(player);
						}
					}
					
				}
			}
		});
		Log.info("[SERVER] Binding ports, TCP = " + Network.tcpport);
		try {server.bind(Network.tcpport);} catch (IOException e) {e.printStackTrace();}
		try {Log.info("[SERVER] Server initiated at address: " + InetAddress.getLocalHost().getHostAddress());} catch (UnknownHostException e) {e.printStackTrace();}
	}



	/**
	 * Here goes all things that should constantly get updated
	 */
	@Override
	public void run() {
		while(true){	
			if( !players.isEmpty()){
				Log.trace("[SERVER][RUN] Updating players...");
				Iterator<Player> it = players.iterator();
				while(it.hasNext()){
					Player player = it.next();
					Log.trace(":---[SERVER][RUN] Before update: " + player.getName() + ", " + player.getX() + ", " + player.getY() + ", " + player.getDx() + ", " + player.getDy());
					player.update();
					Log.trace(":------[SERVER][RUN] Updating " + player.getName());
					Log.trace(":---[SERVER][RUN] After update:" + player.getName() + ", " + player.getX() + ", " + player.getY() + ", " + player.getDx() + ", " + player.getDy());

				}
				Log.trace("[SERVER][RUN] Done updating players.");

				collision.update();
				updateClients();
			}else{
				Log.trace("[SERVER] No players..");
			}
			try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}

	public synchronized void updatePlayer(Player player){
		Log.debug("[SERVER][CLIENTUPDATE] Server received a client player to update...");
		Log.debug("[SERVER][CLIENTUPDATE]  " + player.getName() + "," + player.getX() + "," + player.getY() + "," + player.getDx() + "," + player.getDy());
		Iterator<Player> it = players.iterator();
		while(it.hasNext()){
			Player p = it.next();
			if(p.equals(player)){
				Log.debug("[SERVER][CLIENTUPDATE] Player: " + p.getName() + " was replaced with " + player.getName());
				p = player;
			}
		}
	}

	public synchronized Player createPlayer(String playerName){
		Player player = new Player(playerName, PLAYER_WIDTH, PLAYER_HEIGHT);	
		players.add(player);
		Log.debug("[SERVER][CREATEPLAYER] " + playerName + " created");
		Log.debug("[SERVER][CREATEPLAYER] Players on the server: " + players.size());
		Iterator<Player> it = players.iterator();
		while(it.hasNext()){
			Player p = it.next();
			Log.debug(":---[SERVER] " + p.getName());
		}
		return player;
	}		

	public synchronized void updateClients(){
		if( !server.getConnections().equals(null)){
			Log.debug("[SERVER][UPDATECLIENTS] Sending server state to all clients...");
			ServerPacket sendPacket = new ServerPacket();
			sendPacket.message = "update";
			sendPacket.players = players;
			sendPacket.world = world.getCurrentMap();
			Log.debug("[SERVER][UPDATECLIENTS] sendPacket contains:  MSG = " + sendPacket.message + ", Players = " + sendPacket.players + ", World = " + world.getID());
			server.sendToAllTCP(sendPacket);

		}else{
			Log.debug("[SERVER][UPDATECLIENTS] No connections to the server...");
		}
	}

	/**
	 * Returns the world
	 * @return world The world
	 */
	public World getWorld(){
		return world;
	}

	/**
	 * Sets the World when you have loaded a previous game
	 * @param world The world
	 */
	public void setWorld(World world){
		this.world = world;
	}

	/**
	 * Returns the Collision object
	 * @return collision The collision manager
	 */
	public Collision getCollision(){
		return collision;
	}

	/**
	 * Sets the Collision when you have loaded a previous game
	 * @param collision The collision manager
	 */
	public void setCollision(Collision collision){
		this.collision = collision;
	}

	/**
	 * Returns a List of characters, thats currently in the map
	 * @return characters List of Characters
	 */
	public ArrayList<Player> getPlayers(){
		return players;
	}
}
