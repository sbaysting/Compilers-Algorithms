package graphing;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractGraphNode implements Comparable<AbstractGraphNode>
{
	private int ID;
	private String name;
	
	private Set<AbstractGraphNode> successors = new HashSet<>();
	private Set<AbstractGraphNode> predecessors = new HashSet<>();
	
	public AbstractGraphNode()
	{
		UUID id = UUID.randomUUID();
		this.name = id.toString();
		this.ID = id.hashCode();
	}
	
	public AbstractGraphNode(int ID)
	{
		this.ID = ID;
		this.name = Integer.toString(ID);
	}
	
	public AbstractGraphNode(String name, int ID)
	{
		this.name = name;
		this.ID = ID;
	}
	
	public void setID(int ID)
	{
		this.ID = ID;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean successorExists(AbstractGraphNode node)
	{
		return successors.contains(node);
	}
	
	public void addSuccessor(AbstractGraphNode... node)
	{
		for(AbstractGraphNode n : node)
		{
			successors.add(n);
		}
	}
	
	public void removeSuccessor(AbstractGraphNode node)
	{
		successors.remove(node);
	}
	
	public Set<AbstractGraphNode> getSuccessors()
	{
		return successors;
	}
	
	public boolean predecessorExists(AbstractGraphNode node)
	{
		return predecessors.contains(node);
	}
	
	public void addPredecessor(AbstractGraphNode... node)
	{
		for(AbstractGraphNode n : node)
		{
			predecessors.add(n);
		}
	}
	
	public void removePredecessor(AbstractGraphNode node)
	{
		predecessors.remove(node);
	}
	
	public Set<AbstractGraphNode> getPredecessors()
	{
		return predecessors;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof AbstractGraphNode)
		{
			AbstractGraphNode n = (AbstractGraphNode)o;
			if(n.getID() == this.getID())
				return true;
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return Integer.toString(getID());
	}
	
	public abstract AbstractGraphNode newInstance();
	public abstract String getString();
	public abstract void fromString(String str);
	public abstract int[] getSuccessorIDs(String str);
}
