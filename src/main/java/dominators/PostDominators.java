package dominators;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import controlflow.CFGNode;
import graphing.AbstractGraphNode;
import graphing.Graph;

public class PostDominators 
{
	private static Map<CFGNode, Set<CFGNode>> pdoms = new HashMap<>();
	private static Graph<CFGNode> cfg;
	
	/* 
	 * 1. Initialize exit node to the set of itself
	 * 2. Initialize all other nodes to the set of all nodes
	 * 3. Initialize worklist to the set of all nodes
	 * 4. While the worklist isn't empty, compare old and new pdom
	 * 5. If the old and new pdom changed, add its predecessors to the worklist if they don't exist already
	 * 
	 * dom(exitNode) = { exitNode }
	 * dom(any other node) = { all nodes }
	 * worklist = { all nodes }
	 * while worklist is not empty:
	 * 		node = worklist.pop()
	 * 		oldDom = dom(node)
	 * 		dom(node) = {node}
	 * 		intersectionSet = dom(first successor)
	 * 		for each successor(node) s:
	 * 			intersectionSet = intersection( {intersectionSet}, dom(s) )
	 * 		dom(node) = {node} + {intersectionSet}
	 * 		if dom(node) != oldDom:
	 * 			for each predecessor(node) p:
	 * 				worklist.addUnique(p)
	 */
	public static Graph<DomNode> computeWithWorklist(Graph<CFGNode> graph, CFGNode exitNode)
	{
		// Set up graph
		String name = "Post Dominators of " + graph.getName();
		cfg = graph;
		Graph<CFGNode> dgraph = new Graph<CFGNode>(name, graph.getReferenceNode());
		// Initialize nodes
		for(CFGNode node : graph.getNodes())
		{
			dgraph.addNode(node);
			Set<CFGNode> init = new TreeSet<>();
			if(node.equals(exitNode))
			{
				init.add(exitNode);
				pdoms.put(node, init);
				continue;
			}
			init.addAll(graph.getNodes());
			pdoms.put(node, init);
		}
		// Create worklist
		Queue<CFGNode> wl = new LinkedList<CFGNode>();
		Set<CFGNode> nodes = new TreeSet<>(graph.getNodes());
		nodes.remove(exitNode);
		wl.addAll(nodes);
		// Run until no changes have occurred
		while(!wl.isEmpty())
		{
			CFGNode n = wl.remove();
			System.out.println("Got node " + n.getName());
			Set<CFGNode> oldDom = pdoms.get(n);
			System.out.println("Old post dominators for node " + n.getName() + ": " + oldDom);
			Set<CFGNode> newDom = pdom(n);
			System.out.println("New post dominators for node " + n.getName() + ": " + newDom);
			pdoms.put(n, newDom);
			if(!newDom.equals(oldDom))
			{
				System.out.println("Old doms didn't equal new doms! Adding node " + n.getName() + " back to list...");
				//wl.add(n);
				n.getPredecessors().forEach( s -> wl.add((CFGNode)s) );
			}
		}
		System.out.println("Converged!");
		// No more changed!
		for(CFGNode n : graph.getNodes())
		{
			System.out.println("Post dominators for node " + n.getName() + " (ID: " + n.getID() + "): " + pdoms.get(n));
		}
		for(CFGNode n : graph.getNodes())
		{
			System.out.println("Immediate post dominator for node " + n.getName() + " (ID: " + n.getID() + "): " + ipdom(n));
		}
		Graph<DomNode> domGraph = buildPDomGraph(dgraph);
		return domGraph;
	}
	
	// pdom(node) = {node} U intersection( pdom(every successor) )
	private static Set<CFGNode> pdom(CFGNode node)
	{
		Set<CFGNode> domNodes = new TreeSet<>();
		domNodes.add(node);
		boolean flag = false;
		Set<CFGNode> intersection = new TreeSet<>();
		for(AbstractGraphNode s : node.getSuccessors())
		{
			if(!flag)
			{
				//System.out.println("Node: " + s);
				//System.out.println(pdoms.get(cfg.getNode(s.getID())));
				intersection.addAll(pdoms.get(cfg.getNode(s.getID())));
				flag = true;
				continue;
			}
			intersection.retainAll(pdoms.get(cfg.getNode(s.getID())));
		}
		domNodes.addAll(intersection);
		return domNodes;
	}
	
	// X ipdom Y iff (X spdom Y) and (X not spdom any other node in the set spdom(Y) )
	private static CFGNode ipdom(CFGNode node)
	{
		Set<CFGNode> sdoms = spdom(node);
		CFGNode idomNode = null;
		for(CFGNode X : sdoms)
		{
			boolean flag = false;
			for(CFGNode Y : sdoms)
			{
				Set<CFGNode> sdomY = spdom(Y);
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
	
	public static DomNode ipdom(DomNode node)
	{
		Set<DomNode> sdoms = spdom(node);
		DomNode idomNode = null;
		for(DomNode X : sdoms)
		{
			boolean flag = false;
			for(DomNode Y : sdoms)
			{
				Set<DomNode> sdomY = spdom(Y);
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
	
	// X spdom Y iff (X pdom Y) and (X != Y)
	private static Set<CFGNode> spdom(CFGNode node)
	{
		Set<CFGNode> thisDoms = pdoms.get(node);
		Set<CFGNode> sdoms = new TreeSet<>();
		for(CFGNode n : thisDoms)
		{
			if(!n.equals(node))
				sdoms.add(n);
		}
		return sdoms;
	}
	
	public static Set<DomNode> spdom(DomNode node)
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
		private static Graph<DomNode> buildPDomGraph(Graph<CFGNode> graph)
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
				Set<CFGNode> dom = pdoms.get(n);
				Set<DomNode> doms = new TreeSet<>();
				dom.forEach( n2 -> doms.add(domGraph.getNode(n2.getID())) );
				dn.addDom(doms.toArray(new DomNode[doms.size()]));
				CFGNode domNode = ipdom(n);
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
