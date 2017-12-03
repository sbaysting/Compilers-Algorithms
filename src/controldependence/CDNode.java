package controldependence;

import java.util.UUID;

import graphing.AbstractGraphNode;
import graphing.GenericPrintableNode;

public class CDNode extends GenericPrintableNode
{
	public CDNode()
	{
		super();
	}
	
	public CDNode(int ID)
	{
		super(ID);
	}
	
	public CDNode(String name, int ID)
	{
		super(name, ID);
	}

	@Override
	public AbstractGraphNode newInstance() 
	{
		UUID id = UUID.randomUUID();
		return new CDNode(id.toString(), id.hashCode());
	}

}
