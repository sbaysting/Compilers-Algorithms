package dominators;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import graphing.AbstractGraphNode;
import graphing.GenericPrintableNode;

// For dominators and post-dominators
public class DFNode extends GenericPrintableNode
{
	private Set<DFNode> dfnodes = new TreeSet<>();
	
	public DFNode()
	{
		super();
	}
	
	public DFNode(int ID)
	{
		super(ID);
	}
	
	public DFNode(String name, int ID)
	{
		super(name, ID);
	}
	
	public void addDF(DFNode... nodes)
	{
		for(DFNode n : nodes)
		{
			this.dfnodes.add(n);
		}
	}
	
	public Set<DFNode> getDF()
	{
		return dfnodes;
	}

	@Override
	public AbstractGraphNode newInstance() 
	{
		UUID id = UUID.randomUUID();
		return new DFNode(id.toString(), id.hashCode());
	}
}
