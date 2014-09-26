package OS.project1;

import org.w3c.dom.Element;

public class WDCommand extends Command
{
	String id, path;
	void parseWd(Element elem) throws ProcessException
	{
		id = elem.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in CMD Command");
		}
		System.out.println("ID: " + id);
		
		path = elem.getAttribute("path");
		if (path == null || path.isEmpty()) {
			throw new ProcessException("Missing PATH in CMD Command");
		}
		System.out.println("Path: " + path);
	}
}
