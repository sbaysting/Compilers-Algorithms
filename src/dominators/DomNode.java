package dominators;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import graphing.AbstractGraphNode;
import graphing.GenericPrintableNode;

// For dominators and post-dominators
public class DomNode extends GenericPrintableNode
{
	private Set<DomNode> doms = new TreeSet<>();
	private DomNode idom = null;
	
	public DomNode()
	{
		super();
	}
	
	public DomNode(int ID)
	{
		super(ID);
	}
	
	public DomNode(String name, int ID)
	{
		super(name, ID);
	}
	
	public void addDom(DomNode... nodes)
	{
		for(DomNode n : nodes)
		{
			this.doms.add(n);
		}
	}
	
	public void setIDom(DomNode idom)
	{
		this.idom = idom;
	}
	
	public Set<DomNode> getDoms()
	{
		return doms;
	}
	
	public DomNode getIDom()
	{
		return idom;
	}

	@Override
	public AbstractGraphNode newInstance() 
	{
		UUID id = UUID.randomUUID();
		return new DomNode(id.toString(), id.hashCode());
	}
}
