package commands;

import java.util.concurrent.ConcurrentLinkedQueue;

import executers.DataFile;
import executers.FileWorker;
import executers.treeExecuter;
import threadWorkers.ThreadTask;

public class CommandList extends ThreadTask{

	private FileWorker fileworker;
	private ConcurrentLinkedQueue<String> answer;
	
	public CommandList(ConcurrentLinkedQueue<String> answer, FileWorker fileworker) {
		this.fileworker = fileworker;
		this.answer = answer;
	}
	@Override
	public void doTask() {
		answer.add("\n"+getList(fileworker.execute(new treeExecuter())));
		
	}
	private String getList(DataFile file) {
		StringBuilder sb = new StringBuilder();
		for (var e : file.dictData.keySet()) {
			sb.append(e+"\n");
		}
		return sb.toString();
	}

}
