package test;

import controldependence.CDNode;
import controldependence.ControlDependence;
import controlflow.CFGNode;
import dominators.DFNode;
import dominators.DomNode;
import dominators.PostDominanceFrontiers;
import dominators.PostDominators;
import graphing.Graph;

public class ComputeCD 
{
	public static void main(String[] args) 
	{
		CFGNode B0 = new CFGNode("Entry", 0);
		CFGNode B1 = new CFGNode("1", 1);
		CFGNode B2 = new CFGNode("2", 2);
		CFGNode B3 = new CFGNode("3", 3);
		CFGNode B4 = new CFGNode("4", 4);
		CFGNode B5 = new CFGNode("5", 5);
		CFGNode B6 = new CFGNode("6", 6);
		CFGNode B7 = new CFGNode("7", 7);
		CFGNode B8 = new CFGNode("8", 8);
		CFGNode B9 = new CFGNode("9", 9);
		CFGNode B10 = new CFGNode("10", 10);
		CFGNode B11 = new CFGNode("11", 11);
		CFGNode B12 = new CFGNode("12", 12);
		CFGNode B13 = new CFGNode("Exit", 13);
		
		CFGNode ref = new CFGNode();
		
		// Ref has to be there to be able to create new nodes
		Graph<CFGNode> cfg = new Graph<CFGNode>("CFG", ref);
		
		cfg.addSuccessors(B0, B1);
		cfg.addSuccessors(B1, B2);
		cfg.addSuccessors(B2, B3, B7);
		cfg.addSuccessors(B3, B4, B5);
		cfg.addSuccessors(B4, B6);
		cfg.addSuccessors(B5, B6);
		cfg.addSuccessors(B6, B8);
		cfg.addSuccessors(B7, B8);
		cfg.addSuccessors(B8, B9);
		cfg.addSuccessors(B9, B10, B11);
		cfg.addSuccessors(B10, B11);
		cfg.addSuccessors(B11, B9, B12);
		cfg.addSuccessors(B12, B2, B13);
		
		cfg.visualize();
		
		Graph<DomNode> pdom = PostDominators.computeWithWorklist(cfg, B13);
		Graph<DFNode> pdf = PostDominanceFrontiers.compute(pdom, cfg);
		
		pdom.visualize();
		pdf.visualize();
		
		Graph<CDNode> cd = ControlDependence.compute(pdom, cfg);
		Graph<CDNode> cdpdf = ControlDependence.computeWithPDF(pdf, cfg);
		
		cd.visualize();
		cdpdf.visualize();
	}
}
