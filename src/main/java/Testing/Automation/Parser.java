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
	private Map<String, List<List<String>>> testCases = null;
	private String lastStep = null;
	private Node<String> root = null;
	private Node<String> lastNode = null;
	private Map<Integer, String> conditions = null;
	private int lastConditionIndex;
	private String tempRunValue = "";
	private MappingInterface map = null;

	//tree build test for multiple values
	private String currentEvaluation = "END";
	private String lastEvaluation = "END";
	
	public void Parse(XWPFDocument xdoc) throws IOException
	{
		//Preparing
		this.loadProperties();
		this.root = new Node<>("root");
		this.lastNode = this.root;
		this.conditions = new HashMap<Integer,String>();
		this.lastConditionIndex = 1;
		this.map = new MappingInterface();
		this.testCases = new HashMap<String, List<List<String>>>();

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
//		System.out.println(this.testCases);
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
	
//	private void updateConditionList(String condition)
//	{
//		if(condition.equalsIgnoreCase("ELSE"))
//		{
//			this.lastNode = this.lastNode.getCounterPart();
//			return;
//		}
//
//		if(condition.equalsIgnoreCase("END IF") || condition.equalsIgnoreCase("ENDIF"))
//		{
//			this.lastNode = this.lastNode.getParent();
//			return;
//		}
//
//		this.conditions.put(this.lastConditionIndex, condition);
//		updateTree();
//		this.lastConditionIndex++;
//	}
	
	private void updateConditionList(String condition)
	{
		this.conditions.put(this.lastConditionIndex, condition);
		this.lastConditionIndex++;
		
		this.lastEvaluation = this.lastNode.getNodeEvaluation();
		if(condition.equalsIgnoreCase("ELSE"))
		{
			this.currentEvaluation = "ELSE";
		}
		else if(condition.equalsIgnoreCase("ENDIF") || condition.equalsIgnoreCase("END IF"))
		{
			this.currentEvaluation = "END";
		}
		else if(condition.equalsIgnoreCase("ELSEIF") || condition.equalsIgnoreCase("ELSE IF"))
		{
			this.currentEvaluation = "ELIF";
		}
		else
		{
			this.currentEvaluation = "IF";
		}
		
		updateTree(condition);
	}
	
	private void updateTree(String condition)
	{
		Node<String> currentNode = null;
		Node<String> parentNode = null;
		
		if(this.currentEvaluation.equalsIgnoreCase("END"))
		{
			this.lastNode = this.lastNode.getParent();
			return;
		}
		
		if(this.lastEvaluation == null)
		{
			return;
		}
		
		switch(this.lastEvaluation)
		{
			case "IF":
				parentNode = this.lastNode;
				break;
			case "ELSE":
				parentNode = this.lastNode;
				break;
			case "ELIF":
				parentNode = this.lastNode;
				break;
			case "END":
				if(this.lastNode.equals(this.root))
				{
					parentNode = this.lastNode;
				}
				else {
					parentNode = this.lastNode.getParent();
					return;
				}
				break;
			default:
		}
		
		List<String> true_values = this.map.getValues(condition, "true");
		List<String> false_values = this.map.getValues(condition, "false");
		String xpath = this.map.getXPath(condition);
		
		for(String value : true_values)
		{
			currentNode = new Node<String>("");
			currentNode.setValue(value);
			currentNode.setCondition(condition);
			currentNode.setXPath(xpath);
			currentNode.setNodeEvaluation(this.currentEvaluation);
			parentNode.addChild(currentNode);
			this.lastNode = currentNode;
		}
		
		for(String value : false_values)
		{
			currentNode = new Node<String>("");
			currentNode.setValue(value);
			currentNode.setCondition(condition);
			currentNode.setXPath(xpath);
			currentNode.setNodeEvaluation(this.currentEvaluation);
			parentNode.addChild(currentNode);
		}
		
		
	}

//	private void updateTree()
//	{
//		
//		
//		String tiv = "T" + Integer.toString(this.lastConditionIndex);
//		String fiv = "F" + Integer.toString(this.lastConditionIndex);
//		Node<String> T = this.lastNode.addChild(new Node<String>(tiv));
//		Node<String> F = this.lastNode.addChild(new Node<String>(fiv));
//		
//		T.setCounterPart(F);
//		T.setCondition(this.conditions.get(this.lastConditionIndex));
//		T.setXPath(this.map.getXPath(this.conditions.get(this.lastConditionIndex)));
//		T.setNodeEvaluation("true");
//		
//		F.setCounterPart(T);
//		F.setCondition(this.conditions.get(this.lastConditionIndex));
//		F.setXPath(this.map.getXPath(this.conditions.get(this.lastConditionIndex)));
//		F.setNodeEvaluation("false");
//		
//		this.lastNode = T;
//	}

	private <T> void createTestCases(Node<T> node)
	{
		if(!node.getData().equals("root"))
		{
			List<String> values = this.map.getValues(node.getCondition().toString(), node.getNodeEvaluation().toString());
			
			int cnt = 1;
			for(String value : values)
			{
				List<String> a = new ArrayList();
				a.add(node.getXPath().toString());
				a.add(value);
				a.add(node.getNodeEvaluation().toString());
				
				if(this.testCases.containsKey("TC"+cnt))
				{
					List<List<String>> tmp = this.testCases.get("TC"+cnt);
					tmp.add(a);
					this.testCases.replace("TC"+cnt, tmp);
				}
				else
				{
					List<List<String>> b = new ArrayList();
					b.add(a);
					this.testCases.put("TC"+cnt,b);
				}
				
				cnt++;
			}
			
			node.getChildren().forEach(each ->  createTestCases(each));
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
		System.out.println(appender + node.getData());
		node.getChildren().forEach(each ->  printTree(each, appender + appender));
	}

	private void printConditions()
	{
		System.out.println(this.conditions.toString());
	}

}
