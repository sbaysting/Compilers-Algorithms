package dominators;

import java.util.Set;

import controlflow.CFGNode;
import graphing.AbstractGraphNode;
import graphing.Graph;

public class DominanceFrontiers 
{
	/*
	 * for each node n in dominator graph:
	 * 		for each node d dominated by n:
	 * 			for each CFG successor s in d:
	 * 				 if n sdom s:
	 * 					DF(n).add(s)
	 */
	public static Graph<DFNode> compute(Graph<DomNode> domGraph, Graph<CFGNode> cfGraph)
	{
		Graph<DFNode> graph = new Graph<DFNode>("DF for " + domGraph.getName(), new DFNode());
		for(DomNode n : domGraph.getNodes())
		{
			DFNode dfn = new DFNode(n.getName(), n.getID());
			graph.addNode(dfn);
		}
		for(DomNode n : domGraph.getNodes())
		{
			System.out.println("Examining node from dominator graph " + n.getName());
			DFNode dfn = graph.getNode(n.getID());
			// Node n is dominating domNode
			for(AbstractGraphNode domNode : n.getSuccessors())
			{
				// Get successors of domNode from CFG
				System.out.println("Got node being dominated by " + n.getName() + ": " + domNode.getName());
				CFGNode cfgNode = cfGraph.getNode(domNode.getID());
				for(AbstractGraphNode succ : cfgNode.getSuccessors())
				{
					System.out.println("Got successor of node " + domNode.getName() + " from CFG: " + succ.getName());
					// Find nodes in the dominator graph that aren't strictly dominated by n
					DomNode domSucc = domGraph.getNode(succ.getID());
					Set<DomNode> sdoms = Dominators.sdom(domSucc);
					if(sdoms.contains(n))
						continue;
					System.out.println("Adding " + domSucc.getName() + " to dominance frontier of " + dfn.getName());
					dfn.addDF(graph.getNode(domSucc.getID()));
				}
			}
			AbstractGraphNode domNode = n;
			// Get successors of domNode from CFG
			System.out.println("Got node being dominated by " + n.getName() + ": " + domNode.getName());
			CFGNode cfgNode = cfGraph.getNode(domNode.getID());
			for(AbstractGraphNode succ : cfgNode.getSuccessors())
			{
				System.out.println("Got successor of node " + domNode.getName() + " from CFG: " + succ.getName());
				// Find nodes in the dominator graph that aren't strictly dominated by n
				DomNode domSucc = domGraph.getNode(succ.getID());
				Set<DomNode> sdoms = Dominators.sdom(domSucc);
				if(sdoms.contains(n))
					continue;
				System.out.println("Adding " + domSucc.getName() + " to dominance frontier of " + dfn.getName());
				dfn.addDF(graph.getNode(domSucc.getID()));
			}
		}
		for(DFNode n : graph.getNodes())
		{
			System.out.println("Dominance frontier for node " + n.getName() + " (ID: " + n.getID() + "): " + n.getDF());
			graph.addSuccessors(n, n.getDF().toArray(new DFNode[n.getDF().size()]));
		}
		return graph;
	}
}
