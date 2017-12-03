package dominators;

import java.util.Set;

import controlflow.CFGNode;
import graphing.AbstractGraphNode;
import graphing.Graph;

public class PostDominanceFrontiers 
{
	/*
	 * for each node n in post-dominator graph:
	 * 		for each node d post-dominated by n:
	 * 			for each CFG predecessor s in d:
	 * 				 if n sdom s:
	 * 					DF(n).add(s)
	 */
	public static Graph<DFNode> compute(Graph<DomNode> pdomGraph, Graph<CFGNode> cfGraph)
	{
		Graph<DFNode> graph = new Graph<DFNode>("PDF for " + pdomGraph.getName(), new DFNode());
		for(DomNode n : pdomGraph.getNodes())
		{
			DFNode dfn = new DFNode(n.getName(), n.getID());
			graph.addNode(dfn);
		}
		for(DomNode n : pdomGraph.getNodes())
		{
			System.out.println("Examining node from dominator graph " + n.getName());
			DFNode dfn = graph.getNode(n.getID());
			// Node n is post-dominating domNode
			for(AbstractGraphNode domNode : n.getSuccessors())
			{
				// Get predecessors of domNode from CFG
				System.out.println("Got node being post-dominated by " + n.getName() + ": " + domNode.getName());
				CFGNode cfgNode = cfGraph.getNode(domNode.getID());
				for(AbstractGraphNode succ : cfgNode.getPredecessors())
				{
					System.out.println("Got predecessor of node " + domNode.getName() + " from CFG: " + succ.getName());
					// Find nodes in the dominator graph that aren't strictly dominated by n
					DomNode domPred = pdomGraph.getNode(succ.getID());
					Set<DomNode> sdoms = PostDominators.spdom(domPred);
					if(sdoms.contains(n))
						continue;
					System.out.println("Adding " + domPred.getName() + " to post-dominance frontier of " + dfn.getName());
					dfn.addDF(graph.getNode(domPred.getID()));
				}
			}
			AbstractGraphNode domNode = n;
			// Get predecessors of domNode from CFG
			System.out.println("Got node being post-dominated by " + n.getName() + ": " + domNode.getName());
			CFGNode cfgNode = cfGraph.getNode(domNode.getID());
			for(AbstractGraphNode succ : cfgNode.getPredecessors())
			{
				System.out.println("Got predecessor of node " + domNode.getName() + " from CFG: " + succ.getName());
				// Find nodes in the post-dominator graph that aren't strictly dominated by n
				DomNode domPred = pdomGraph.getNode(succ.getID());
				Set<DomNode> sdoms = PostDominators.spdom(domPred);
				if(sdoms.contains(n))
					continue;
				System.out.println("Adding " + domPred.getName() + " to post-dominance frontier of " + dfn.getName());
				dfn.addDF(graph.getNode(domPred.getID()));
			}
		}
		for(DFNode n : graph.getNodes())
		{
			System.out.println("Post-dominance frontier for node " + n.getName() + " (ID: " + n.getID() + "): " + n.getDF());
			graph.addSuccessors(n, n.getDF().toArray(new DFNode[n.getDF().size()]));
		}
		return graph;
	}
}
