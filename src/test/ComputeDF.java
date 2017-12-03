package test;

import controlflow.CFGNode;
import dominators.DFNode;
import dominators.DomNode;
import dominators.DominanceFrontiers;
import dominators.Dominators;
import dominators.PostDominanceFrontiers;
import dominators.PostDominators;
import graphing.Graph;

public class ComputeDF {

	public static void main(String[] args) 
	{
		CFGNode A = new CFGNode("A", 0);
		CFGNode B = new CFGNode("B", 1);
		CFGNode C = new CFGNode("C", 2);
		CFGNode D = new CFGNode("D", 3);
		CFGNode E = new CFGNode("E", 4);
		CFGNode F = new CFGNode("F", 5);
		
		CFGNode ref = new CFGNode();
		
		// Ref has to be there to be able to create new nodes
		Graph<CFGNode> cfg = new Graph<CFGNode>("CFG", ref);
		
		cfg.addSuccessors(A, B, F);
		cfg.addSuccessors(B, C, D);
		cfg.addSuccessors(C, E);
		cfg.addSuccessors(D, E);
		cfg.addSuccessors(E, F);
		
		cfg.visualize();
		
		Graph<DomNode> dom = Dominators.computeWithWorklist(cfg, A);
		Graph<DomNode> pdom = PostDominators.computeWithWorklist(cfg, A);
		
		dom.visualize();
		pdom.visualize();

		Graph<DFNode> df = DominanceFrontiers.compute(dom, cfg);
		Graph<DFNode> pdf = PostDominanceFrontiers.compute(pdom, cfg);
		
		df.visualize();
		pdf.visualize();
	}

}
