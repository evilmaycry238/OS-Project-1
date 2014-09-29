package OS.project1;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PipeCommand extends Command
{
	ArrayList<CmdCommand> pipeCmds;
	String id;
	public PipeCommand(Element elem) throws ProcessException
	{
		pipeCmds = new ArrayList<CmdCommand>();
		id = elem.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in Pipe Command");
		}
		NodeList nodes = elem.getChildNodes();
		for (int idx = 0; idx < nodes.getLength(); idx++) 
		{
			Node node = nodes.item(idx);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem1 = (Element) node;
				CmdCommand cCmd = new CmdCommand();
				cCmd.parseCmd(elem1);
				pipeCmds.add(cCmd);
				
			}
		}
	}
}
