
public class GlobalGraph 
{
	public static void main(String... args)
	{
		GraphNode A = new GraphNode("A");
		A.addDef("i");
		
		GraphNode B = new GraphNode("B");
		B.addDef("j");
		
		GraphNode C = new GraphNode("C");
		C.addDef("T1", "T2", "T3", "T4", "T21", "T22", "j");
		C.addUse("i", "j", "n");
		
		GraphNode D = new GraphNode("D");
		D.addDef("i");
		D.addUse("i", "n");
		
		GraphNode E = new GraphNode("E");
		E.addDef("i");
		
		GraphNode F = new GraphNode("F");
		F.addDef("j");
		
		GraphNode G = new GraphNode("G");
		G.addDef("k");
		
		GraphNode H = new GraphNode("H");
		H.addDef("T4", "T5", "T6", "T7", "T8", "T9", "T24", "T10", "T11", "T12", "T13", "T25", "T14", "T15", "T16", "T17", "T18", "T19", "T26", "T20", "k");
		H.addUse("i", "j", "n", "k");
		
		GraphNode I = new GraphNode("I");
		I.addDef("j");
		I.addUse("j", "n");
		
		GraphNode J = new GraphNode("J");
		J.addDef("i");
		J.addUse("i", "n");
		
		GraphNode K = new GraphNode("K");
		
		C.addSuccessor(C);
		D.addSuccessor(B);
		H.addSuccessor(H);
		I.addSuccessor(G);
		J.addSuccessor(F);
		
		Graph graph = new Graph();
		graph.computeLiveness();
		System.out.println("\n");
		graph.print();
	}
}
