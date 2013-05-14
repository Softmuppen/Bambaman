package server;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import network.*;

import character.Player;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.minlog.Log;



public class MainServer extends Thread{

	
	private Server server;
	private LinkedList<Player> players;
	private boolean isChanged;
	
	public MainServer()
	{
		Log.set(Log.LEVEL_DEBUG);
		players = new LinkedList<>();
		isChanged = false;
		
		server = new Server();
		Register.register(server);
		server.start();
		
		
		
		try {
			server.bind(54555, 54777);
			Log.info("[SERVER] Starting mainserver");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		server.addListener(new Listener(){
			@Override
			public void received(Connection connection, Object obj)
			{
				if(obj instanceof Startup)
				{
					Startup r = (Startup)obj;
					Log.debug("[SERVER] Recieved startup message: " + r.player.getName());
					players.add(r.player);
					Log.debug("[SERVER] Adding player to serverlist");
					
					NetworkCommand resp = new NetworkCommand();
					resp.cmd = NetworkCommands.ACCEPTED;
					connection.sendTCP(resp);
					Log.debug("[SERVER] Sent response");
				}
				else if(obj instanceof MoveRequest)
				{
					MoveRequest r = (MoveRequest) obj;
					for(Player p : players)
					{
						if(p.getName().equals(r.player.getName()))
						{
							p.setX(p.getX() + r.dx);
							p.setY(p.getY() + r.dy);
						}
					}
				}
				else if(obj instanceof ChatMessage)
				{
					server.sendToAllTCP(obj);
				}
				else if(obj instanceof NetworkCommand)
				{
					NetworkCommand cmd = (NetworkCommand)obj;
					if(cmd.cmd == NetworkCommands.QUIT)
					{
						removePlayer(cmd.name);
					}
				}
				isChanged = true;
			}
		});
		
		
	}
	
	private void removePlayer(String playername)
	{
		Iterator<Player> it = players.iterator();
		while(it.hasNext())
		{
			Player p = it.next();
			if(p.getName().equals(playername))
			{
				
				it.remove();
			}
		}
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			if(!(players.size() == 0) && isChanged)
			{
				PlayerUpdate packet = new PlayerUpdate();
				packet.players = players;
				server.sendToAllUDP(packet);
				isChanged = false;
			}
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args)
	{
		MainServer server = new MainServer();
		server.start();
	}
	

}
