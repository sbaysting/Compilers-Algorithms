package dominators;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import controlflow.CFGNode;
import graphing.AbstractGraphNode;
import graphing.Graph;

public class Dominators 
{
	private static Map<CFGNode, Set<CFGNode>> doms = new HashMap<>();
	
	/* 
	 * 1. Initialize entry node to the set of itself
	 * 2. Initialize all other nodes to the set of all nodes
	 * 3. Initialize worklist to the set of all nodes
	 * 4. While the worklist isn't empty, compare old and new dom
	 * 5. If the old and new dom changed, add its successors to the worklist if they don't exist already
	 * 
	 * dom(entryNode) = { entryNode }
	 * dom(any other node) = { all nodes }
	 * worklist = { all nodes }
	 * while worklist is not empty:
	 * 		node = worklist.pop()
	 * 		oldDom = dom(node)
	 * 		dom(node) = {node}
	 * 		intersectionSet = dom(first predecessor)
	 * 		for each predecessor(node) p:
	 * 			intersectionSet = intersection( {intersectionSet}, dom(p) )
	 * 		dom(node) = {node} + {intersectionSet}
	 * 		if dom(node) != oldDom:
	 * 			for each successor(node) s:
	 * 				worklist.addUnique(s)
	 */
	public static Graph<DomNode> computeWithWorklist(Graph<CFGNode> graph, CFGNode entryNode)
	{
		// Set up graph
		String name = "Dominators of " + graph.getName();
		Graph<CFGNode> dgraph = new Graph<CFGNode>(name, graph.getReferenceNode());
		// Initialize nodes
		for(CFGNode node : graph.getNodes())
		{
			dgraph.addNode(node);
			Set<CFGNode> init = new TreeSet<>();
			if(node.equals(entryNode))
			{
				init.add(entryNode);
				doms.put(node, init);
				System.out.println("Initialize Dom(" + node.getName() + ") = " + init);
				continue;
			}
			init.addAll(graph.getNodes());
			doms.put(node, init);
			System.out.println("Initialize Dom(" + node.getName() + ") = " + init);
		}
		// Create worklist
		Queue<CFGNode> wl = new LinkedList<CFGNode>();
		Set<CFGNode> nodes = new TreeSet<>(graph.getNodes());
		nodes.remove(entryNode);
		wl.addAll(nodes);
		// Run until no changes have occurred
		while(!wl.isEmpty())
		{
			System.out.println("W = " + wl);
			CFGNode n = wl.remove();
			//System.out.println("Got node " + n.getName());
			Set<CFGNode> oldDom = doms.get(n);
			System.out.println("Dom_old(" + n.getName() + ") = " + oldDom);
			Set<CFGNode> newDom = dom(n);
			String str = "";
			for(AbstractGraphNode gn : n.getPredecessors())
			{
				str += "Dom(" + gn.getName() + ") ";
			}
			System.out.println("Dom(" + n.getName() + ") = [" + n.getName() + "] union( intersection( " + str + ") )");
			System.out.println("Dom(" + n.getName() + ") = " + newDom);
			doms.put(n, newDom);
			if(!newDom.equals(oldDom))
			{
				System.out.println("Dom(" + n.getName() + ") changed! Adding successors to list...");
				//wl.add(n);
				n.getSuccessors().forEach( s -> {
					if(!wl.contains((CFGNode)s))
						wl.add((CFGNode)s);
				});
			}
		}
		System.out.println("Converged!");
		// No more changed!
		for(CFGNode n : graph.getNodes())
		{
			System.out.println("Dominators for node " + n.getName() + ": " + doms.get(n));
		}
		for(CFGNode n : graph.getNodes())
		{
			System.out.println("Immediate dominator for node " + n.getName() + ": " + idom(n));
		}
		Graph<DomNode> domgraph = buildDomGraph(dgraph);
		return domgraph;
	}
	
	// dom(node) = {node} U intersection( dom(every predecessor) )
	private static Set<CFGNode> dom(CFGNode node)
	{
		Set<CFGNode> domNodes = new TreeSet<>();
		domNodes.add(node);
		boolean flag = false;
		Set<CFGNode> intersection = new TreeSet<>();
		for(AbstractGraphNode pred : node.getPredecessors())
		{
			if(!flag)
			{
				intersection.addAll(doms.get(pred));
				flag = true;
				continue;
			}
			intersection.retainAll(doms.get(pred));
		}
		domNodes.addAll(intersection);
		return domNodes;
	}
	
	// X idom Y iff (X sdom Y) and (X not sdom any other node in the set sdom(Y) )
	private static CFGNode idom(CFGNode node)
	{
		Set<CFGNode> sdoms = sdom(node);
		CFGNode idomNode = null;
		for(CFGNode X : sdoms)
		{
			boolean flag = false;
			for(CFGNode Y : sdoms)
			{
				Set<CFGNode> sdomY = sdom(Y);
				if(sdomY.contains(X))
				{
					flag = true;
					break;
				}
			}
			if(!flag)
			{
				idomNode = X;
				break;
			}
		}
		return idomNode;
	}
	
	public static DomNode idom(DomNode node)
	{
		Set<DomNode> sdoms = sdom(node);
		DomNode idomNode = null;
		for(DomNode X : sdoms)
		{
			boolean flag = false;
			for(DomNode Y : sdoms)
			{
				Set<DomNode> sdomY = sdom(Y);
				if(sdomY.contains(X))
				{
					flag = true;
					break;
				}
			}
			if(!flag)
			{
				idomNode = X;
				break;
			}
		}
		return idomNode;
	}
	
	// X sdom Y iff (X dom Y) and (X != Y)
	private static Set<CFGNode> sdom(CFGNode node)
	{
		Set<CFGNode> thisDoms = doms.get(node);
		Set<CFGNode> sdoms = new TreeSet<>();
		for(CFGNode n : thisDoms)
		{
			if(!n.equals(node))
				sdoms.add(n);
		}
		return sdoms;
	}
	
	public static Set<DomNode> sdom(DomNode node)
	{
		Set<DomNode> sdoms = new TreeSet<>();
		for(DomNode n : node.getDoms())
		{
			if(!n.equals(node))
				sdoms.add(n);
		}
		return sdoms;
	}
	
	// Link nodes in the graph
	private static Graph<DomNode> buildDomGraph(Graph<CFGNode> graph)
	{
		Graph<DomNode> domGraph = new Graph<DomNode>(graph.getName(), new DomNode());
		for(CFGNode n : graph.getNodes())
		{
			DomNode dn = new DomNode(n.getName(), n.getID());
			domGraph.addNode(dn);
		}
		for(CFGNode n : graph.getNodes())
		{
			DomNode dn = domGraph.getNode(n.getID());
			Set<CFGNode> dom = doms.get(n);
			Set<DomNode> doms = new TreeSet<>();
			dom.forEach( n2 -> doms.add(domGraph.getNode(n2.getID())) );
			dn.addDom(doms.toArray(new DomNode[doms.size()]));
			CFGNode domNode = idom(n);
			if(domNode != null)
			{
				DomNode realDomNode = domGraph.getNode(domNode.getID());
				dn.setIDom(realDomNode);
				domGraph.addSuccessors(realDomNode, dn);
			}
		}
		return domGraph;
	}
}
