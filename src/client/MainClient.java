package client;

import gui.ClientGUI;

import java.io.IOException;
import java.util.LinkedList;

import network.*;


import character.Player;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.minlog.Log;

public class MainClient {

	private Client client;
	private Player player;
	private LinkedList<Player> players;
	private boolean isChanged;
	private ClientGUI gui;
	
	public MainClient(String username, String IPAdress, ClientGUI gui)
	{
		players = new LinkedList<>();
		isChanged = false;
		this.gui = gui;
		
		Log.set(Log.LEVEL_DEBUG);
		client = new Client();
		Register.register(client);
		client.start();
		
		
		
		try {
			client.connect(5000, IPAdress, 54555, 54777);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Startup req = new Startup();
		player = new Player(username);
		req.player = player;
		client.sendTCP(req);
		
		client.addListener(new Listener(){
			@Override
			public void received(Connection connection, Object object)
			{
				if(object instanceof NetworkCommand)
				{
					NetworkCommand resp = (NetworkCommand)object;
					Log.debug("[CLIENT] recieved response: " + resp.cmd);
				}
				else if(object instanceof PlayerUpdate)
				{
					PlayerUpdate players = (PlayerUpdate)object;
					Log.debug("[CLIENT] recieved playerupdate: " + players.players + " with x coord: " + players.players.getFirst().getX());
					MainClient.this.players = players.players;
				}
				else if(object instanceof ChatMessage)
				{
					ChatMessage msg = (ChatMessage) object;
					MainClient.this.gui.update(msg.message);
				}
				isChanged = true;
			}
		});
	}
	
	public void sendNetworkCommand(NetworkCommands cmd, String name)
	{
		NetworkCommand packet = new NetworkCommand();
		packet.cmd = cmd;
		packet.name = name;
		client.sendTCP(packet);
	}
	
	public LinkedList<Player> getPlayers()
	{
		return players;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public void sendMoveRequest(Player player, int dx, int dy)
	{
		MoveRequest request = new MoveRequest(player, dx, dy);
		System.out.println(request.dx + " " + request.dy);
		client.sendUDP(request);
	}
	
	public boolean isChanged()
	{
		return isChanged;
	}
	
	public void setChanged(boolean b)
	{
		isChanged = b;
	}
	
	public void sendMessage(String msg)
	{
		ChatMessage chat = new ChatMessage();
		chat.message = player.getName() + ": " + msg;
		client.sendTCP(chat);
	}
	
	
}
