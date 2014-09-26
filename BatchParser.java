package OS.project1;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BatchParser 
{
	Batch BuildBatch(File batchFile)
	{
		Batch batch = new Batch();
		try
		{
			FileInputStream fis = new FileInputStream(batchFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fis);
	
			Element pnode = doc.getDocumentElement();
			NodeList nodes = pnode.getChildNodes();
			for (int idx = 0; idx < nodes.getLength(); idx++) 
			{
				Node node = nodes.item(idx);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element elem = (Element) node;
					parseCommand(elem, batch);
				}
			}
		}
		catch (Exception e) 
		{
				System.out.println(e.getMessage());
				e.printStackTrace();
		}
		return batch;
	}
	private void parseCommand(Element elem, Batch batch) throws ProcessException
	{
		String cmdName = elem.getNodeName();
		
		if (cmdName == null) {
			throw new ProcessException("unable to parse command from " + elem.getTextContent());
		}
		else if ("wd".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing wd");
			batch.wdCmd.parseWd(elem);
		}
		else if ("file".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing file");
			FileCommand fCmd = new FileCommand();
			fCmd.parseFile(elem);
			batch.fileCmds.add(fCmd);
		}
		else if ("cmd".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing cmd");
			CmdCommand cCmd = new CmdCommand();
			cCmd.parseCmd(elem);
			batch.cmdCmds.add(cCmd);
			
		}
		else if ("pipe".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing pipe");
			//Command cmd = PipeCommand.parse(elem);
		}
		else {
			throw new ProcessException("Unknown command " + cmdName + " from: " + elem.getBaseURI());
		}
	}
}
