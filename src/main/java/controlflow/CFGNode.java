package controlflow;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import graphing.AbstractGraphNode;
import graphing.GenericPrintableNode;

public class CFGNode extends GenericPrintableNode
{
	private Set<String> defs = new TreeSet<>();
	private Set<String> uses = new TreeSet<>();
	
	public CFGNode()
	{
		super();
	}
	
	public CFGNode(int ID)
	{
		super(ID);
	}
	
	public CFGNode(String name, int ID)
	{
		super(name, ID);
	}
	
	public void addDef(String... defs)
	{
		for(String def : defs)
		{
			this.defs.add(def);
		}
	}
	
	public void addUse(String... uses)
	{
		for(String use : uses)
		{
			this.uses.add(use);
		}
	}
	
	public Set<String> getDefs()
	{
		return defs;
	}
	
	public Set<String> getUses()
	{
		return uses;
	}

	@Override
	public AbstractGraphNode newInstance() 
	{
		UUID id = UUID.randomUUID();
		return new CFGNode(id.toString(), id.hashCode());
	}

}
