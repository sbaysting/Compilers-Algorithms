package dominators;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

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
	/*public static Graph<DFNode> compute(Graph<DomNode> pdomGraph, Graph<CFGNode> cfGraph)
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
	}*/
	
	
	/*
	 * Algorithm:
	 * for each x in DFS of pdom tree:
	 * 		PDF(x) = empty set
	 * 		for each y in pred(x):
	 * 			if x is not ipdom(y):
	 * 				PDF(x) = PDF(x) union {y}
	 * for each x in DFS of pdom tree:
	 * 		for each z that x immediate post-dominates:
	 * 			for each y in PDF(z):
	 * 				if x is not ipdom(y):
	 * 					PDF(x) = PDF(x) union {y}
	 */
	public static Graph<DFNode> compute(Graph<DomNode> pdomGraph, Graph<CFGNode> cfGraph)
	{
		Map<DomNode, Set<DomNode>> pdf = new HashMap<>();
		Graph<DFNode> graph = new Graph<DFNode>("PDF for " + pdomGraph.getName(), new DFNode());
		for(DomNode n : pdomGraph.getNodes())
		{
			DFNode dfn = new DFNode(n.getName(), n.getID());
			graph.addNode(dfn);
			pdf.put(pdomGraph.getNode(dfn.getID()), new TreeSet<>());
		}
		
		// Get root node
		DomNode root = null;
		for(DomNode dn : pdomGraph.getNodes())
		{
			if(dn.getPredecessors().isEmpty())
			{
				root = dn;
				break;
			}
		}
		
		// Find node order with DFS
		Stack<DomNode> nodeOrder = new Stack<>();
		getNodeOrder(root, nodeOrder);
		
		// Print node order
		System.out.println("Node traversal order: " + nodeOrder);
		
		// Start step 1 (PDF_local)
		Stack<DomNode> nodes = (Stack<DomNode>)nodeOrder.clone();
		while(!nodes.isEmpty())
		{
			DomNode n = nodes.pop(); // X
			//System.out.println("Got node x = " + n);
			for(AbstractGraphNode pred : cfGraph.getNode(n.getID()).getPredecessors())
			{
				// y = next pred(x)
				//System.out.println("Got next predecessor of x );
				DomNode y = pdomGraph.getNode(pred.getID());
				if(!n.equals(y.getIDom())) // if x != ipdom(y)
					pdf.get(n).add(y);
			}
		}
		
		// Print PDF_local
		nodes = (Stack<DomNode>)nodeOrder.clone();
		while(!nodes.isEmpty())
		{
			DomNode n = nodes.pop();
			System.out.println("PDF_local of node " + n.getName() + ": " + pdf.get(n));
		}
		
		// Start step 2 (PDF)
		nodes = (Stack<DomNode>)nodeOrder.clone();
		while(!nodes.isEmpty())
		{
			DomNode n = nodes.pop(); // X
			// Find nodes z that X immediately post-dominates
			for(AbstractGraphNode succ : n.getSuccessors())
			{
				// y = each PDF(z)
				for(DomNode y : pdf.get(succ))
				{
					if(!n.equals(y.getIDom())) // if x != ipdom(y)
						pdf.get(n).add(y);
				}
			}
		}
		
		// Print PDF
		nodes = (Stack<DomNode>)nodeOrder.clone();
		while(!nodes.isEmpty())
		{
			DomNode n = nodes.pop();
			System.out.println("PDF of node " + n.getName() + ": " + pdf.get(n));
		}
		
		// Add PDFs to the actual PDF nodes
		for(DomNode node : pdf.keySet())
		{
			DFNode thisDFNode = graph.getNode(node.getID());
			for(DomNode pdfNode : pdf.get(node))
			{
				DFNode equivalentDFNode = graph.getNode(pdfNode.getID());
				thisDFNode.addDF(equivalentDFNode);
			}
		}
		
		// Print PDF
		for(DFNode n : graph.getNodes())
		{
			System.out.println("Post-dominance frontier for node " + n.getName() + " (ID: " + n.getID() + "): " + n.getDF());
			graph.addSuccessors(n, n.getDF().toArray(new DFNode[n.getDF().size()]));
		}
		return graph;
	}
	
	private static void getNodeOrder(DomNode currentNode, Stack<DomNode> stack)
	{
		if(!stack.contains(currentNode))
		{
			stack.push(currentNode);
		}
		for(AbstractGraphNode n : currentNode.getSuccessors())
		{
			getNodeOrder((DomNode)n, stack);
		}
	}
}
