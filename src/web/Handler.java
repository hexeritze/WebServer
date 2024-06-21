package web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import threadWorkers.ThreadTask;

public class Handler extends ThreadTask{
	private Socket socket;
	private String directory;
	private BufferedReader input;
	private PrintWriter output;
	private String command;
	
	public Handler(Socket socket, String dir) {
		this.socket = socket;
		directory = dir;
	}
	@Override
	public void doTask() {
	
	}
	private void doCommand() {
		
	}
	

}
