package web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import threadWorkers.ThreadDispatcher;


public class HttpServer implements Runnable{
	private static int port = 8080;
	private ServerSocket server;
	private String directory;
	private ThreadDispatcher dispatcher;
	private boolean stop = false;
	
	public HttpServer(String dir) {
		directory = dir;
		dispatcher = ThreadDispatcher.create();
	}

	
	public void stopServer() {
		stop = true;
		try {
			if(server!=null)
				server.close();
		} catch (IOException e) {
		}
	}
	@Override
	public void run() {
		try {
			server = new ServerSocket(port);
			while(!stop) {
				dispatcher.addInQueue(new HttpHundler(server.accept(),directory));
			}
		} catch (IOException e) {
		}
		
	}
}
