package web;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import threadWorkers.ThreadTask;

public class HttpHundler extends ThreadTask{
	private Socket socket;
	private InputStream input;
	private OutputStream output;
	private String directory;
	public HttpHundler(Socket socket, String dir) throws IOException {
		this.socket = socket;
		directory = dir;
		input = socket.getInputStream();
		output = socket.getOutputStream();
	}
	
	private String writeHTML(String name) throws Throwable{
		String relativePath=name;
    	String backPath = "";
    	if(name.contains("/")) 
    		backPath=name.substring(0, name.lastIndexOf("/"));
    	if(!name.equals("")) {
    		relativePath=relativePath+"/";
    	}
    		
    	
    	if(!name.equals("false")) {
    		File file = new File(directory+relativePath);
    		File temp;
    		var result=new StringBuilder();
    		result.append("<html><body>");
    		if(file.isDirectory()) {
    			result.append("<h1>"+"root/"+relativePath+"</h1>");
    			if(!name.equals("")) {
    				result.append(String.format("<h4><a href=\"http://localhost:8080/%s\"> %s</a></h4>", backPath, "../")); 
    			}
    				
    			for(var dir : file.list()) {
    				temp=new File(directory+relativePath+dir);
    				if(temp.isDirectory()) {
    					result.append(String.format("<h4><a href=\"http://localhost:8080/%s\"> %s</a></h4>", relativePath+temp.getName(), temp.getName()));
    					
    				}
    				else if(temp.isFile()) {
    					result.append(String.format("<h4><a href=\"%s\" download> %s</a></h4>", directory+relativePath+temp.getName(), temp.getName()));
    				}
    			}    			
    		}
    		result.append("</body></html>");
    		return result.toString();
    	}
    	return "";
	}
	private void writeResponse(String s) throws IOException {
		String HEADERS =
				"HTTP/1.1 200 OK\r\n" +
	             "Server: server\r\n" +
	             "Content-Type: text/html\r\n" +
	             "Content-Length: " + s.length() + "\r\n" +
	             "Connection: close\r\n\r\n";
		String result = HEADERS + s;
		output.write(result.getBytes());
		output.flush();
	}
	private String getPathFromRequest(String req) {
    	String path=req.substring(5, req.indexOf("Host")-9);
    	if(path.equals("favicon.ico")) 
    		return "false";
    	return path;
    }
	private String readInput(BufferedReader reader) throws IOException{
		
		String text = reader.readLine();
		
		StringBuilder sb = new StringBuilder();
		while(!(text==null || text.trim().length() == 0)) {
			sb.append(text);
			text = reader.readLine();
		}

		return sb.toString();
	}
	@Override
	public void doTask() {
		try {
			var request = readInput(new BufferedReader(new InputStreamReader(input)));
			writeResponse(writeHTML(getPathFromRequest(request)));
		}catch(Throwable e) {
			System.out.println("Server already used");
		}
		try {
			socket.close();
		} catch (IOException e) {}
	}
}
