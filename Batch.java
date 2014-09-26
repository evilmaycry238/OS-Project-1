package OS.project1;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Batch 
{
	String workingDir;
	Map <String, FileCommand> cmdMap;
	WDCommand wdCmd;
	CmdCommand cmdCmd1, cmdCmd2;
	ArrayList<FileCommand> fileCmds; 
	ArrayList<CmdCommand> cmdCmds;
	PipeCommand pipeCmd;
	public Batch()
	{
		cmdMap = new HashMap<String, FileCommand>();
		wdCmd = new WDCommand();
		cmdCmd1 = new CmdCommand();
		cmdCmd2 = new CmdCommand();
		fileCmds = new ArrayList<FileCommand>(3); 
		cmdCmds = new ArrayList<CmdCommand>();
	}
}
