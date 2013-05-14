package Engine;

import java.io.IOException;
import java.util.ArrayList;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.minlog.Log;

import Networking.ClientPacket;
//import Networking.ClientPacket;
//import Networking.ServerPacket;
import Networking.Network;
import Networking.ServerPacket;

import World.Map;
import World.World;
import Character.Player;

public class GameClient implements Runnable{
	private Client client;
	private ArrayList<Player> players;
	private Player player;
	private World world;

	public GameClient(String playerName, String localAddress){
		System.setProperty("java.net.preferIPv4Stack" , "true");
		
		players = new ArrayList<Player>();
		world = new World(1);
		player = new Player("RICHARD", 32, 32); // Creates a dummy player, just for the join_request and to fool the GamePanel
		
		initClient(localAddress);
		client.start();
		joinServer(localAddress);
	}
	
	@Override
	public synchronized void run(){
		while(true){	
			updateServer();
			Log.debug("[CLIENT][RUN] " + player.getName());
			Log.debug("[CLIENT][RUN] Connection status to: " + client.getRemoteAddressTCP() + " is " + client.isConnected());
			try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public synchronized void initClient(String localAddress){
		client = new Client(32768,32768);
		Network.register(client);
		
		client.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if (object instanceof String) {
					String response = (String)object;
					// If join request was approved, and a connection is established, ask for a playerCharacter.
					if( response.equals("join_request_approved")){
						Log.debug("[CLIENT] Client join_request approved"); 
						if( connection.isConnected()){
							Log.debug("[CLIENT]	Connection established with " + connection.getRemoteAddressTCP().getHostName()); 
						}
					}else{
						Log.debug("[CLIENT] Client recieved an unknown response: " + response);
					}
				
				}//----------UPDATE CLIENT PLAYER LIST--------------------
				if (object instanceof ServerPacket){
					ServerPacket receivedPacket = (ServerPacket)object;
					Log.debug("[CLIENT] Client received a ServerPacket: " + receivedPacket.message);
					// Receive a new player upon start
					if(receivedPacket.message.equals("join_request_approved")){
						player = receivedPacket.clientPlayer;
						Log.debug("[CLIENT] Join request approved by server");
						Log.debug("[CLIENT] Client recieved player by server: " + receivedPacket.clientPlayer.toString());
						Log.debug("[CLIENT] Client set up a new player: " + player.toString());
					}
					// Receive updated player list
					if(receivedPacket.message.equals("update")){
						Log.debug("[CLIENT] Update packet received");
						player = receivedPacket.clientPlayer;
						players = receivedPacket.players;
						world.setCurrentMap(receivedPacket.world);
						
						
						/*Iterator<PlayerCharacter> it = players.iterator();
						while(it.hasNext()){
							PlayerCharacter listPlayer = it.next();
							if(listPlayer.equals(player)){
								player = listPlayer;
								it.remove();
								Log.debug("[CLIENT] " + listPlayer.getName() + " removed.");
							}
						}
						
						Log.debug("[CLIENT] Playerlist: ");
						for( PlayerCharacter p : players){
							Log.debug("  -" + p.getName());
						}
						Log.debug("[CLIENT] CLientPlayer name :" + player.getName());
						*/
					}
				}//-------------------------------------------------------
			}
		});
	}

	public synchronized void joinServer(String address){
		try {client.connect(5000, address, Network.tcpport);} catch (IOException e) {e.printStackTrace();}
		try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}
		System.out.println("[CLIENT] RTT to Server: " + client.getReturnTripTime() + " ms.");
		ClientPacket joinRequest = new ClientPacket();
		joinRequest.message = "join_request";
		joinRequest.player = player;
		
		
		client.sendTCP(joinRequest);
		Log.debug("[CLIENT] Sending join_request... ");
	}
	public synchronized void  updateServer(){
		Log.debug(":---[CLIENT] Updating Server");
		Log.debug(":------[CLIENT] " + player.getName() + ", " + player.getX() + ", " + player.getY() + ", " + player.getDx() + ", " + player.getDy());
		ClientPacket sendPacket = new ClientPacket();
		sendPacket.message = "client_player_update";
		sendPacket.player = player;
		client.sendTCP(sendPacket);
		Log.debug(":---[CLIENT] Client Update sent. ");
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

	public ArrayList<Player> getPlayers(){
		return players;
	}

	public Player getClientPlayer(){
		return player;
	}
}