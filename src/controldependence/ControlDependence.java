package controldependence;

import controlflow.CFGNode;
import dominators.DomNode;
import graphing.AbstractGraphNode;
import graphing.Graph;

public class ControlDependence 
{
	/*
	 * Y is control dependent on X iff:
	 * 1. Y post-dominates a successor of X
	 * 2. Y does not post-dominate ALL successors of X
	 */
	public static Graph<CDNode> compute(Graph<DomNode> pdomGraph, Graph<CFGNode> cfGraph)
	{
		Graph<CDNode> graph = new Graph<CDNode>("Control Dependence Graph of " + cfGraph.getName(), new CDNode());
		for(CFGNode n : cfGraph.getNodes())
		{
			CDNode cdn = new CDNode(n.getName(), n.getID());
			graph.addNode(cdn);
		}
		
		for(CDNode X : graph.getNodes())
		{
			for(CDNode Y : graph.getNodes())
			{
				boolean dominatesASuccessor = false;
				boolean dominatesAllSuccessors = true;
				for(AbstractGraphNode n : cfGraph.getNode(X.getID()).getSuccessors())
				{
					// Does Y post-dominate this successor of X?
					boolean res = pdomGraph.getNode(n.getID()).getDoms().contains(pdomGraph.getNode(Y.getID()));
					// Does Y post-dominate a successor of X?
					dominatesASuccessor = dominatesASuccessor || res;
					// Does Y post-dominate all successors of X?
					dominatesAllSuccessors = dominatesAllSuccessors && res;
				}
				if(dominatesASuccessor && !dominatesAllSuccessors)
				{
					System.out.println(Y.getName() + " is control dependent on " + X.getName());
					graph.addSuccessors(X, Y);
				}
			}
		}
		
		for(CDNode n : graph.getNodes())
		{
			System.out.println("Nodes control dependent on " + n.getName() + " (ID: " + n.getID() + "): " + n.getSuccessors());
		}
		
		return graph;
	}
}
