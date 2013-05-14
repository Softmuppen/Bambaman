package Main;

import Engine.ServerEngine;

import com.esotericsoftware.minlog.Log;

public class Server
{
	
	public static void main(String[] args) 
	{
		Log.set(Log.LEVEL_DEBUG);
		ServerEngine serverEngine = new ServerEngine();
		Thread serverEngineThread = new Thread(serverEngine);
		Log.debug("[MAIN][THREAD] Starting serverEngineThread...");
		serverEngineThread.start();

	}
}
