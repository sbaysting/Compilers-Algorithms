package graphing;

import java.util.Set;

public class GraphUtils 
{
	public static <T> void union(Set<T> outputSet, Set<T>... sets)
	{
		for(Set<T> set : sets)
		{
			outputSet.addAll(set);
		}
	}
	
	public static <T> void intersection(Set<T> outputSet, Set<T>... sets)
	{
		for(Set<T> set : sets)
		{
			outputSet.retainAll(set);
		}
	}
	
	public static <T> void difference(Set<T> outputSet, Set<T>... sets)
	{
		for(Set<T> set : sets)
		{
			outputSet.removeAll(set);
		}
	}
}
