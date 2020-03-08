package Testing.Automation;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class Parser {

	private Properties properties = null;
	
	public void Parse(XWPFDocument xdoc) throws IOException
	{
		//Preparing
		this.loadProperties();
		
		
		for (XWPFParagraph p : xdoc.getParagraphs()) {
	        List<XWPFRun> runs = p.getRuns();
	        if (runs != null) {
	            for (XWPFRun r : runs) {
	                if(r.getColor().equals(this.getValue("ConditionColor")))
	                {
//	                	if(r.getText(0).trim().equalsIgnoreCase("ELSE"))
	                	System.out.println(r.getText(0));
	                }
	            }
	        }
	    }
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
		String value = this.properties.getProperty(key);
		return this.properties.getProperty(value);
	}
	
}
