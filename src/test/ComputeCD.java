package test;

import controldependence.CDNode;
import controldependence.ControlDependence;
import controlflow.CFGNode;
import dominators.DomNode;
import dominators.PostDominators;
import graphing.Graph;

public class ComputeCD 
{
	public static void main(String[] args) 
	{
		CFGNode A = new CFGNode("A", 0);
		CFGNode B = new CFGNode("B", 1);
		CFGNode C = new CFGNode("C", 2);
		CFGNode D = new CFGNode("D", 3);
		CFGNode E = new CFGNode("E", 4);
		CFGNode F = new CFGNode("F", 5);
		CFGNode G = new CFGNode("G", 6);
		CFGNode H = new CFGNode("H", 7);
		
		CFGNode ref = new CFGNode();
		
		// Ref has to be there to be able to create new nodes
		Graph<CFGNode> cfg = new Graph<CFGNode>("CFG", ref);
		
		cfg.addSuccessors(A, B, H);
		cfg.addSuccessors(B, C, G);
		cfg.addSuccessors(C, D, E);
		cfg.addSuccessors(D, F);
		cfg.addSuccessors(E, F);
		cfg.addSuccessors(F, B);
		cfg.addSuccessors(G, H);
		
		cfg.visualize();
		
		Graph<DomNode> pdom = PostDominators.computeWithWorklist(cfg, H);
		
		pdom.visualize();
		
		Graph<CDNode> cd = ControlDependence.compute(pdom, cfg);
		
		cd.visualize();
	}
}
