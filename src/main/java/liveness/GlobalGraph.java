package liveness;

public class GlobalGraph 
{
	public static void main(String... args)
	{
		GraphNode a = new GraphNode("a");
		a.addDef("x");
		
		GraphNode b = new GraphNode("b");
		b.addDef("y");
		
		GraphNode c = new GraphNode("c");
		
		GraphNode d = new GraphNode("d");
		d.addUse("x", "y");
		
		GraphNode e = new GraphNode("e");
		e.addDef("c");
		e.addUse("x", "y");
		
		GraphNode f = new GraphNode("f");
		f.addDef("c");
		f.addUse("x", "y");
		
		GraphNode g = new GraphNode("g");
		g.addDef("z");
		g.addUse("x", "y");
		
		GraphNode h = new GraphNode("h");
		h.addUse("z");
		
		GraphNode i = new GraphNode("i");
		i.addUse("a", "b", "c");
		
		// Add edges
		
		a.addSuccessor(b);
		b.addSuccessor(c);
		c.addSuccessor(d);
		d.addSuccessor(e);
		d.addSuccessor(f);
		e.addSuccessor(h);
		f.addSuccessor(g);
		g.addSuccessor(h);
		h.addSuccessor(i);
		
		Graph graph = new Graph();
		graph.computeLiveness();
		System.out.println("\n");
		graph.print();
	}
}
