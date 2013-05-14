package network;

import java.util.LinkedList;

import character.Player;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Register {

	public static void register(EndPoint e)
	{
		Kryo kryo = e.getKryo();
		kryo.register(Player.class);
		kryo.register(Startup.class);
		kryo.register(NetworkCommand.class);
		kryo.register(NetworkCommands.class);
		kryo.register(PlayerUpdate.class);
		kryo.register(LinkedList.class);
		kryo.register(MoveRequest.class);
		kryo.register(ChatMessage.class);
	}
}
