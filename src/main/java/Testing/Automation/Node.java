package Testing.Automation;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {

	private T data = null;

	private List<Node<T>> children = new ArrayList<>();

	private Node<T> parent = null;

	private Node<T> counterPart = null;

	private T condition = null;
	
	public Boolean tc_flag = false;
	
	private T evaluation = null;

	private T xpath = null;

	public Node(T data) {
		this.data = data;
	}

	public Node<T> addChild(Node<T> child) {
		child.setParent(this);
		this.children.add(child);
		return child;
	}

	public void addChildren(List<Node<T>> children) {
		children.forEach(each -> each.setParent(this));
		this.children.addAll(children);
	}

	public List<Node<T>> getChildren() {
		return children;
	}

	public void setCounterPart(Node<T> cp)
	{
		this.counterPart = cp;
	}

	public Node<T> getCounterPart()
	{
		return this.counterPart;
	}
	
	public void setNodeEvaluation(T ne)
	{
		this.evaluation = ne;
	}

	public T getNodeEvaluation()
	{
		return this.evaluation;
	}

	public void setCondition(T cond)
	{
		this.condition = cond;
	}

	public T getCondition()
	{
		return this.condition;
	}
	
	public void setXPath(T xp)
	{
		this.xpath = xp;
	}

	public T getXPath()
	{
		return this.xpath;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	private void setParent(Node<T> parent) {
		this.parent = parent;
	}

	public Node<T> getParent() {
		return parent;
	}

}
