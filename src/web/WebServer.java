package web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import commands.CommandHash;
import commands.CommandList;
import commands.CommandSize;
import commands.CommandWorker;
import executers.FileWorker;
import threadWorkers.ThreadDispatcher;

public class WebServer {
	public static int port = 8081;
	
	private ServerSocket server;
	private ThreadDispatcher dispatcher;
	private FileWorker fileworker;
	private String path = "C:/Users/Public/ооп/Server/test/";
	private HttpServer httpServer;
	private BufferedReader input;
	private PrintWriter output;
	private String command;
	private ConcurrentLinkedQueue<String> answer = new ConcurrentLinkedQueue<String>();
	private Thread worker;
	public WebServer() {
			dispatcher = ThreadDispatcher.create(4);
			fileworker = new FileWorker(path);
			fileworker.setRecursive(false);
			
			
	}
	public void start() {
		try {
			server = new ServerSocket(port);
			System.out.println("Server started! 8081");
			Socket socket = server.accept();
			System.out.println("Client joined!");
			worker = new Thread(new CommandWorker(answer,socket.getOutputStream()));
			worker.setDaemon(true);
			worker.start();
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			while(true) {
				command = input.readLine();
				if(command!=null && command.equals("exit")) {
					socket.close();
					server.close();
					break;
				}
				doCommand(command);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				System.out.println("сервер закрыт!");
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void doCommand(String c) {
		switch(c) {
		case ("start"):
			if(httpServer==null) {
				httpServer = new HttpServer(path);
				Thread thread = new Thread(httpServer);
				thread.setDaemon(true);
				thread.start();
			}
			answer.add("httpServer started");
			break;
		case ("stop"):
			if(httpServer!=null) {
				httpServer.stopServer();
				httpServer=null;
			}
			answer.add("httpServer stopped");
			break;
		case ("status"):
			if(httpServer==null) {
				answer.add("httpServer stopped");
			}
			else
			{
				answer.add("httpServer active");
			}
			break;
		case("list"):
			dispatcher.addInQueue(new CommandList(answer,fileworker));
			break;
		default:
			if(command.length()>5) {
        		var com=command.substring(0, 4);
        		var name=command.substring(5);
        		if(com.equals("hash")) {
        			dispatcher.addInQueue(new CommandHash(answer, fileworker, name));
        		}
        		else if(com.equals("size")) {
        			dispatcher.addInQueue(new CommandSize(answer, fileworker, name));     			
        		}
        		else answer.add(String.format("Unknown command: %s \n", command)); 
    		}
    		else answer.add(String.format("Unknown command: %s \n", command)); 
    		break;
        }
	}
}
