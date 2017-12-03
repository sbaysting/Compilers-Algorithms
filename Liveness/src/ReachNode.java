import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ReachNode 
{
	public String name;
	
	public ReachNode(String name)
	{
		this.name = name;
		Reach.addToWorklist(this);
	}
	
	public void addSuccessor(ReachNode node)
	{
		this.successors.add(node);
		node.predecessors.add(this);
	}
	
	public void addGen(String... var)
	{
		for(String def : var)
			this.gen.add(def);
	}
	
	public void addKill(String... var)
	{
		for(String use : var)
			this.kill.add(use);
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	public Set<String> Rin = new TreeSet<String>();
	public Set<String> Rout = new TreeSet<String>();
	public Set<String> gen = new TreeSet<String>();
	public Set<String> kill = new TreeSet<String>();
	
	public List<ReachNode> successors = new ArrayList<ReachNode>();
	public List<ReachNode> predecessors = new ArrayList<ReachNode>();
}
