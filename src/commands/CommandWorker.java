package commands;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandWorker implements Runnable{

	private ConcurrentLinkedQueue<String> sendQueue;
	private OutputStream clientSender;
	
	public CommandWorker(ConcurrentLinkedQueue<String> sendQueue, OutputStream clientSender) {
		this.sendQueue = sendQueue;
		this.clientSender = clientSender;
	}
	@Override
	public void run() {
		try {
			while(true) {
				if(!sendQueue.isEmpty()) 
					sendResponse(sendQueue.poll());
				Thread.sleep(50);
			}
		} catch (IOException | InterruptedException e) {}
	}
	
	private void sendResponse(String message) throws IOException {
		clientSender.write((message+"\n").getBytes());
	}

}
