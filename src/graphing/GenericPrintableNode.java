package graphing;

import graphing.AbstractGraphNode;

public abstract class GenericPrintableNode extends AbstractGraphNode
{
	public GenericPrintableNode()
	{
		super();
	}
	
	public GenericPrintableNode(int ID)
	{
		super(ID);
	}
	
	public GenericPrintableNode(String name, int ID)
	{
		super(name, ID);
	}

	@Override
	public int compareTo(AbstractGraphNode arg0) 
	{
		if(arg0.equals(this)) return 0;
		if(arg0.getID() < this.getID()) return -1;
		return 1;
	}

	@Override
	public String getString() 
	{
		String str = Integer.toString(getID());
		str += ",";
		str += getName();
		for(AbstractGraphNode n : getSuccessors())
		{
			str += ",";
			str += n.getID();
		}
		return str;
	}

	@Override
	public void fromString(String str) 
	{
		String[] split = str.split(",");
		setID(Integer.parseInt(split[0]));
		setName(split[1]);
		for(int i = 2; i < split.length; i++)
		{
			
		}
	}

	@Override
	public int[] getSuccessorIDs(String str) 
	{
		String[] split = str.split(",");
		if(split.length <= 2)
			return new int[0];
		int[] ids = new int[split.length-2];
		for(int i = 2; i < split.length; i++)
		{
			ids[i-2] = Integer.parseInt(split[i]);
		}
		return ids;
	}

}
