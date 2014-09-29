package OS.project1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.lang.Process;

public class BatchProcessor 
{
	public static void main(String[] args) throws ProcessException 
	{
		Batch batch = new Batch();
		BatchParser myBatchParser = new BatchParser();
		
		//Again, change this to your own folder
		File f = new File("work/batch4.xml");
		batch = myBatchParser.BuildBatch(f);
		if (!(batch.pipeCmd.id == null || batch.pipeCmd.id.isEmpty()))
		{
			System.out.println("Pipe Batch");
			executePipeBatch(batch);
		}
		else
		{
			System.out.println("None Pipe Batch");
			executeBatch(batch);
		}
	}
	
	static void executePipeBatch(Batch batch) 
	{
		for (int i = 0; i < batch.pipeCmd.pipeCmds.size(); i++)
		{
			List<String> command = new ArrayList<String>();
			command.add(batch.pipeCmd.pipeCmds.get(i).path);
			for(String argi: batch.pipeCmd.pipeCmds.get(i).cmdArgs) 
			{
				command.add(argi);
			}
		}
	}

	static void executeBatch(Batch batch) throws ProcessException
	{
		for (int i = 0; i < batch.cmdCmds.size(); i++)
		{
		List<String> command = new ArrayList<String>();
		command.add(batch.cmdCmds.get(i).path);
		
		//Add the arguments
		for(String argi: batch.cmdCmds.get(i).cmdArgs) 
		{
			command.add(argi);
		}
		
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(command);
		builder.directory(new File(batch.wdCmd.path));
		
		//Set the input file
		String fileIn = batch.cmdCmds.get(i).inID;
		String input = null;
		if (!(fileIn == null || fileIn.isEmpty()))
		{
			if( batch.cmdMap.containsKey(fileIn))
			{
				input = "work/" + batch.cmdMap.get(fileIn).path;
				System.out.println("this is test: " + input);
				builder.redirectInput(new File(input));
			}
			else
			{
				throw new ProcessException("IN FileCommand with id: " + fileIn+ " not found");
			}
		}
		
		builder.redirectError(new File("error.txt"));
		
		//Set the output file
		String fileOut = batch.cmdCmds.get(i).outID;
		if (!(fileOut == null || fileOut.isEmpty()))
		{
			if( batch.cmdMap.containsKey(fileOut))
			{
				fileOut = "work/" + batch.cmdMap.get(fileOut).path;
				builder.redirectOutput(new File(fileOut));
			}
			else
			{
				throw new ProcessException("Out FileCommand with id: " + fileOut+ " not found");
			}
		}
		
		//Start the process
		try
		{
		Process process = builder.start();
		System.out.println("Command " + i + " executed");
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
