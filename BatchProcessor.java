package OS.project1;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.lang.Process;


public class BatchProcessor 
{
	public static void main(String[] args) throws ProcessException 
	{
		Batch batch = new Batch();
		BatchParser myBatchParser = new BatchParser();
		
		String batchName = args[0];
		File f = new File("work/batch4.xml");
		//File f = new File(batchName);

		batch = myBatchParser.BuildBatch(f);
		if (!(batch.pipeCmd == null || batch.pipeCmd.id.isEmpty()))
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
	
	static void executePipeBatch(Batch batch) throws ProcessException
	{
		List<String> command1 = new ArrayList<String>();
		command1.add(batch.pipeCmd.pipeCmds.get(0).path);
		for(String argi: batch.pipeCmd.pipeCmds.get(0).cmdArgs) 
		{
			command1.add(argi);
		}
		ProcessBuilder builder1 = new ProcessBuilder(command1);
		builder1.directory(new File(batch.wdCmd.path));
		File wd = builder1.directory();
		builder1.redirectError(new File(wd, "error.txt"));
		
		List<String> command2 = new ArrayList<String>();
		command2.add(batch.pipeCmd.pipeCmds.get(1).path);
		for(String argi: batch.pipeCmd.pipeCmds.get(1).cmdArgs) 
		{
			command2.add(argi);
		}
		ProcessBuilder builder2 = new ProcessBuilder(command1);
		builder2.directory(new File(batch.wdCmd.path));
		builder2.redirectError(new File(wd, "error.txt"));
		
		try 
		{
			final Process process1 = builder1.start();
			
			
			//Set the input of process 1 as file input and copy it to output stream
			String fileIn = batch.pipeCmd.pipeCmds.get(0).inID;
			String input = batch.cmdMap.get(fileIn).path;

			FileInputStream fis = new FileInputStream(new File(wd, input));
			OutputStream os1 = process1.getOutputStream();
			int achar;
			while ((achar = fis.read()) != -1) {
				os1.write(achar);
			}
			os1.close();
			fis.close();
			
			final Process process2 = builder2.start();
			//Copy from output(inputstream) of process 1 to input(outputstream) of process 2
			InputStream is1 = process1.getInputStream();
			OutputStream os2 = process2.getOutputStream();
			while ((achar = is1.read()) != -1) {
				os2.write(achar);
			}
			is1.close();
			os2.close();
			
			//Set output of process 2 as the file output
			String fileOut = batch.pipeCmd.pipeCmds.get(1).outID;
			fileOut = "work/" + batch.cmdMap.get(fileOut).path;
			FileOutputStream fos = new FileOutputStream(fileOut);
			InputStream is2 = process2.getInputStream();
			int achar2;
			while ((achar2 = is2.read()) != -1) {
				fos.write(achar2);
				System.out.print((char) achar);
			}
			fos.close();
			is2.close();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println("Batch Finished!");
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
		System.out.println("Batch Finished!");
	}
}
