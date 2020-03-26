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
	private Map<String, List<String>> testCases = null;
	private String lastStep = null;
	private Node<String> root = null;
	private Node<String> lastNode = null;
	private Map<Integer, String> conditions = null;
	private int lastConditionIndex;
	private String tempRunValue = "";
	private MappingInterface map = null;

	public void Parse(XWPFDocument xdoc) throws IOException
	{
		//Preparing
		this.loadProperties();
		this.root = new Node<>("root");
		this.lastNode = this.root;
		this.conditions = new HashMap<Integer,String>();
		this.lastConditionIndex = 1;
		this.map = new MappingInterface();
		this.testCases = new HashMap<String, List<String>>();

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

						this.reconstructCondition(r.getText(0).trim());
					}
				}
			}
		}
		
		//Creating Test Cases
		this.createTestCases(root);
		
		//Debugging
		this.printTree(this.root, " ");
//		this.printConditions();
	}

	private void reconstructCondition(String condition)
	{
		if(condition.equalsIgnoreCase("ELSE") || condition.equalsIgnoreCase("ENDIF") || condition.equalsIgnoreCase("END IF"))
		{
			this.updateConditionList(condition);
			return;
		}

		if(condition.toLowerCase().contains("then"))
		{
			this.tempRunValue += " ";
			this.tempRunValue += condition;
			this.updateConditionList(this.tempRunValue);
			this.tempRunValue = "";
		}
		else
		{
			this.tempRunValue += " ";
			this.tempRunValue += condition;
		}

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
		T.setCondition(this.conditions.get(this.lastConditionIndex));
		T.setXPath(this.map.getXPath(this.conditions.get(this.lastConditionIndex)));
		T.setNodeEvaluation("true");
		
		F.setCounterPart(T);
		F.setCondition(this.conditions.get(this.lastConditionIndex));
		F.setXPath(this.map.getXPath(this.conditions.get(this.lastConditionIndex)));
		F.setNodeEvaluation("false");
		
		this.lastNode = T;
	}

	private <T> void createTestCases(Node<T> node)
	{
		if(!node.getData().equals("root"))
		{
			List<String> values = this.map.getValues(node.getCondition().toString(), node.getNodeEvaluation().toString());
			
		}
		else
		{
			node.getChildren().forEach(each ->  createTestCases(each));
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
		return this.properties.getProperty(key);
	}

	//Debugging Part
	private <T> void printTree(Node<T> node, String appender) {
		System.out.println(appender + node.getCondition());
		node.getChildren().forEach(each ->  printTree(each, appender + appender));
	}

	private void printConditions()
	{
		System.out.println(this.conditions.toString());
	}

}
