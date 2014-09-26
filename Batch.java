package OS.project1;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Batch 
{
	String workingDir;
	Map <String, Command> commands = new HashMap<String, Command>();
	WDCommand wdCmd;
	CmdCommand cmdCmd1, cmdCmd2;
	FileCommand fileCmd1, fileCmd2, fileCmd3;
	ArrayList<FileCommand> fileCmds; 
	ArrayList<CmdCommand> cmdCmds;
	PipeCommand pipeCmd;

	public Batch()
	{
		wdCmd = new WDCommand();
		cmdCmd1 = new CmdCommand();
		cmdCmd2 = new CmdCommand();
		fileCmds = new ArrayList<FileCommand>(3); 
		cmdCmds = new ArrayList<CmdCommand>();
	}
}
