package mainServer;

import java.io.IOException;

import web.HttpServer;
import web.WebServer;

public class Main {
	public static void main(String[] args) throws IOException {
		String path = "\\test";
		var server = new WebServer();
		server.start();
		
	}

}
