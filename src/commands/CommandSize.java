package commands;

import java.util.concurrent.ConcurrentLinkedQueue;

import executers.DataFile;
import executers.FileWorker;
import executers.SizeExecuter;
import threadWorkers.ThreadTask;

public class CommandSize extends ThreadTask{
	private FileWorker fileworker;
	private ConcurrentLinkedQueue<String> answer;
	private String name;
	public CommandSize(ConcurrentLinkedQueue<String> answer, FileWorker fileworker,String name) {
		this.fileworker = fileworker;
		this.answer = answer;
		this.name = name;
	}
	@Override
	public void doTask() {
		var result = getValue(fileworker.execute(new SizeExecuter()),name);
		if(result!=null) {
			answer.add(String.format("name: %s size: \"%s\" \n", name, result));
		}
		else
			answer.add(String.format("can't find this file: \"%s\" \n", name));
	}
	private String getValue(DataFile file,String name) {
		if(file.dictData.containsKey(name)) {
			return  file.dictData.get(name).toString();
		}
		return null;
	}
	
}
