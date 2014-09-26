package OS.project1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BatchProcessor 
{
	static Batch batch = new Batch();
	public static void main(String[] args) throws ProcessException 
	{
		BatchParser myBatchParser = new BatchParser();
		
		
		//Again, change this to your own folder
		File f = new File("work/batch1.xml");
		batch = myBatchParser.BuildBatch(f);
		executeBatch(batch);
	}
	static void executeBatch(Batch batch) throws ProcessException
	{
		for (int i = 0; i < batch.cmdCmds.size(); i++)
		{
		List<String> command = new ArrayList<String>();
		command.add(batch.cmdCmds.get(i).path);
		for(String argi: batch.cmdCmds.get(i).cmdArgs) 
		{
			command.add(argi);
		}
		String fileIn = batch.cmdCmds.get(i).inID;
		if (!(fileIn == null || fileIn.isEmpty()))
		{
			int intID = Character.getNumericValue(fileIn.charAt(4)) -1;
			command.add(batch.fileCmds.get(intID).path);
		}
		
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(command);
		//builder.directory(new File("work"));
		builder.directory(new File(batch.wdCmd.path));
		builder.redirectError(new File("error.txt"));
		String fileOut = batch.cmdCmds.get(i).outID;
		int outID = Character.getNumericValue(fileOut.charAt(4)) -1;
		builder.redirectOutput(new File(batch.fileCmds.get(outID).path));
		try
		{
		Process process = builder.start();
		process.waitFor();
		}
		catch (Exception e) 
		{
				System.out.println(e.getMessage());
				e.printStackTrace();
		}
		}
		System.out.println("Program terminated!");
	}
}
