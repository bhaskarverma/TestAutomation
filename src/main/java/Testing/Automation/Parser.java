package Testing.Automation;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class Parser {

	private Properties properties = null;
	private Map<String, String> testCases = null;
	private String lastStep = null;
	
	public void Parse(XWPFDocument xdoc) throws IOException
	{
		//Preparing
		this.loadProperties();
		
		for (XWPFParagraph p : xdoc.getParagraphs()) {
	        List<XWPFRun> runs = p.getRuns();
	        if (runs != null) {
	            for (XWPFRun r : runs) {
	                if(this.getValue("ConditionColor").equalsIgnoreCase(r.getColor()))
	                {
	                	if(r.getText(0).trim().isEmpty())
	                	{
	                		continue;
	                	}
	                	
	                	this.updateTestCases(r.getText(0).trim());
	                }
	            }
	        }
	    }
	}
	
	private void updateTestCases(String condition)
	{
		
	}
	
	private void loadProperties() throws IOException
	{
		 FileReader reader=new FileReader("values.properties");  
	      
		 Properties p=new Properties();  
		 p.load(reader);
		 
		 this.properties = p;
	}
	
	private String getValue(String key)
	{ 
		return this.properties.getProperty(key);
	}
	
}
