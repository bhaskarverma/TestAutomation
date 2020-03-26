package Testing.Automation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingInterface {
	
	private Map<String, String> cond_xpath = null;
	private Map<String, List<String>> cond_true_values = null;
	private Map<String, List<String>> cond_false_values = null;
	
	public MappingInterface()
	{
		//init
		this.cond_xpath = new HashMap<String, String>();
		this.cond_true_values = new HashMap<String, List<String>>();
		this.cond_false_values = new HashMap<String, List<String>>();
		
		//Sample Data : XPATH
		this.cond_xpath.put("IF CONDITION 1 THEN", "//letter/a/b/c");
		this.cond_xpath.put("IF CONDITION 2 THEN", "//letter/a/b/d");
		this.cond_xpath.put("IF CONDITION 3 THEN", "//letter/a/b/e");
		this.cond_xpath.put("IF CONDITION 4 THEN", "//letter/a/b/f");
		this.cond_xpath.put("IF CONDITION 5 THEN", "//letter/a/b/g");
		
		//Sample Data : True Values
		List<String> tv = new ArrayList<String>();
		tv.add("true");
		this.cond_true_values.put("IF CONDITION 1 THEN", tv);
		this.cond_true_values.put("IF CONDITION 2 THEN", tv);
		this.cond_true_values.put("IF CONDITION 3 THEN", tv);
		this.cond_true_values.put("IF CONDITION 4 THEN", tv);
		this.cond_true_values.put("IF CONDITION 5 THEN", tv);

		//Sample Data : False Values
		List<String> fv = new ArrayList<String>();
		tv.add("false");
		this.cond_false_values.put("IF CONDITION 1 THEN", fv);
		this.cond_false_values.put("IF CONDITION 2 THEN", fv);
		this.cond_false_values.put("IF CONDITION 3 THEN", fv);
		this.cond_false_values.put("IF CONDITION 4 THEN", fv);
		this.cond_false_values.put("IF CONDITION 5 THEN", fv);
	}
	
	public String getXPath(String cond)
	{
		return this.cond_xpath.get(cond);
	}
	
	public void loadMapSheet(String path)
	{
		
	}
	
	public List<String> getValues(String cond, String ne)
	{
		if(ne.equals("true"))
		{
			return this.cond_true_values.get(cond);
		}
		else
		{
			return this.cond_false_values.get(cond);
		}
	}
	
}
