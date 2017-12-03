package test;

import controlflow.CFGNode;
import dominators.DomNode;
import dominators.Dominators;
import dominators.PostDominators;
import graphing.Graph;

public class ComputeDominators 
{
	public static void main(String... args)
	{
		CFGNode n0 = new CFGNode("Entry", 0);
		CFGNode n1 = new CFGNode(1);
		CFGNode n2 = new CFGNode(2);
		CFGNode n3 = new CFGNode(3);
		CFGNode n4 = new CFGNode(4);
		CFGNode n5 = new CFGNode(5);
		CFGNode n6 = new CFGNode(6);
		CFGNode n7 = new CFGNode(7);
		CFGNode n8 = new CFGNode(8);
		CFGNode n9 = new CFGNode(9);
		CFGNode n10 = new CFGNode(10);
		CFGNode n11 = new CFGNode("Exit", 11);
		
		CFGNode ref = new CFGNode();
		
		// Ref has to be there to be able to create new nodes
		Graph<CFGNode> cfg = new Graph<CFGNode>("CFG", ref);
		
		cfg.addSuccessors(n0, n1);
		cfg.addSuccessors(n1, n2, n3);
		cfg.addSuccessors(n2, n3);
		cfg.addSuccessors(n3, n4);
		cfg.addSuccessors(n4, n3, n5, n6);
		cfg.addSuccessors(n5, n7);
		cfg.addSuccessors(n6, n7);
		cfg.addSuccessors(n7, n4, n8);
		cfg.addSuccessors(n8, n3, n9, n10);
		cfg.addSuccessors(n9, n1, n11);
		cfg.addSuccessors(n10, n7);
		
		cfg.visualize();
		
		Graph<DomNode> dom = Dominators.computeWithWorklist(cfg, n0);
		Graph<DomNode> pdom = PostDominators.computeWithWorklist(cfg, n11);
		
		dom.visualize();
		pdom.visualize();
	}
}
