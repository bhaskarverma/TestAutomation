package Testing.Automation;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
	private Node<String> root = null;
	private Node<String> lastNode = null;
	private Map<Integer, String> conditions = null;
	private int lastConditionIndex;
	
	public void Parse(XWPFDocument xdoc) throws IOException
	{
		//Preparing
		this.loadProperties();
		this.root = new Node<>("root");
		this.lastNode = this.root;
		this.conditions = new HashMap<Integer,String>();
		this.lastConditionIndex = 1;
		
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
	                	
	                	this.updateConditionList(r.getText(0).trim());
	                }
	            }
	        }
	    }
		
		//this.printTree(this.root, " ");
		this.printConditions();
	}
	
	private void updateConditionList(String condition)
	{
		if(condition.equalsIgnoreCase("ELSE"))
		{
			this.lastNode = this.lastNode.getCounterPart();
			return;
		}
		
		if(condition.equalsIgnoreCase("END IF") || condition.equalsIgnoreCase("ENDIF"))
		{
			this.lastNode = this.lastNode.getParent();
			return;
		}
		
		this.conditions.put(this.lastConditionIndex, condition);
		updateTree();
		this.lastConditionIndex++;
	}
	
	private void updateTree()
	{
		String tiv = "T" + Integer.toString(this.lastConditionIndex);
		String fiv = "F" + Integer.toString(this.lastConditionIndex);
		Node<String> T = this.lastNode.addChild(new Node<String>(tiv));
		Node<String> F = this.lastNode.addChild(new Node<String>(fiv));
		T.setCounterPart(F);
		F.setCounterPart(T);
		this.lastNode = T;
	}
	
	private void updateTestCases(String condition)
	{
		System.out.println(condition);
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
	
	//Debugging Part
	private <T> void printTree(Node<T> node, String appender) {
	     System.out.println(appender + node.getData());
	     node.getChildren().forEach(each ->  printTree(each, appender + appender));
	}
	
	private void printConditions()
	{
		System.out.println(this.conditions.toString());
	}
	
}
