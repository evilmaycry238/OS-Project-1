package OS.project1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BatchProcessor 
{
	public static void main(String[] args) throws ProcessException 
	{
		Batch batch = new Batch();
		BatchParser myBatchParser = new BatchParser();
		
		//Again, change this to your own folder
		File f = new File("work/batch2.xml");
		batch = myBatchParser.BuildBatch(f);
		executeBatch(batch);
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
		
		//Set the input file
		String fileIn = batch.cmdCmds.get(i).inID;
		if (!(fileIn == null || fileIn.isEmpty()))
		{
			/* Old methods
			int intID = Character.getNumericValue(fileIn.charAt(4)) -1;
			command.add(batch.fileCmds.get(intID).path);
			*/
			//New mapping method
			if( batch.cmdMap.containsKey(fileIn))
			{
				command.add(batch.cmdMap.get(fileIn).path);
			}
			else
			{
				throw new ProcessException("IN FileCommand with id: " + fileIn+"not found");
			}
		}
		
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(command);
		builder.directory(new File(batch.wdCmd.path));
		builder.redirectError(new File("error.txt"));
		
		//Set the output file
		String fileOut = batch.cmdCmds.get(i).outID;
		if (!(fileOut == null || fileOut.isEmpty()))
		{
			/* Old methods
			int outID = Character.getNumericValue(fileOut.charAt(4)) -1;
			fileOut = "work/" + batch.fileCmds.get(outID).path;
			builder.redirectOutput(new File(fileOut));
			*/
			//New mapping method
			if( batch.cmdMap.containsKey(fileOut))
			{
				fileOut = "work/" + batch.cmdMap.get(fileOut).path;
				builder.redirectOutput(new File(fileOut));
			}
			else
			{
				throw new ProcessException("Out FileCommand with id: " + fileOut+"not found");
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
