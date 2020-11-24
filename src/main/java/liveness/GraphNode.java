package liveness;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GraphNode 
{
	public String name;
	
	public GraphNode(String name)
	{
		this.name = name;
		Graph.addToWorklist(this);
	}
	
	public void addSuccessor(GraphNode node)
	{
		this.successors.add(node);
		node.predecessors.add(this);
	}
	
	public void addDef(String... var)
	{
		for(String def : var)
			this.def.add(def);
	}
	
	public void addUse(String... var)
	{
		for(String use : var)
			this.use.add(use);
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	public Set<String> LVin = new TreeSet<String>();
	public Set<String> LVout = new TreeSet<String>();
	public Set<String> use = new TreeSet<String>();
	public Set<String> def = new TreeSet<String>();
	
	public List<GraphNode> successors = new ArrayList<GraphNode>();
	public List<GraphNode> predecessors = new ArrayList<GraphNode>();
}
