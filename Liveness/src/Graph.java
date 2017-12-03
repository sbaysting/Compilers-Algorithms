import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class Graph 
{
	private static Stack<GraphNode> worklist = new Stack<GraphNode>();
	
	public static void addToWorklist(GraphNode node)
	{
		if(worklist.size() > 0)
			worklist.peek().addSuccessor(node);
		worklist.push(node);
	}
	
	public Graph()
	{
		
	}
	
	public void computeLiveness()
	{
		Stack<GraphNode> wl = (Stack<GraphNode>) worklist.clone();
		System.out.println("Starting worklist: " + wl);
		while(wl.size() > 0)
		{
			GraphNode node = wl.pop();
			System.out.println("Removing node " + node.name + " from worklist");
			Set<String> LVoutOld = node.LVout;
			Set<String> LVinOld = node.LVin;
			LVout(node);
			LVin(node);
			if(!node.LVout.equals(LVoutOld) || !node.LVin.equals(LVinOld))
			{
				//System.out.println("New LVin or LVout is different than previous result for node " + node.name +"!");
				for(GraphNode pred : node.predecessors)
				{
					if(!wl.contains(pred))
					{
						//System.out.println("Pushing node " + pred + " into worklist");
						wl.push(pred);
					}
				}
			}
			System.out.println("Current worklist: " + wl);
		}
	}
	
	// LVout(S) = union(LVin(successors(S)))
	public void LVout(GraphNode node)
	{
		//System.out.println("LVout(" + node.name + ") = LVin( successors(" + node.name + ") )");
		String eq = "LVout(" + node.name + ") = ";
		int i = 0;
		if(node.successors.size() == 0)
			eq += "[]";
		for(GraphNode n : node.successors)
		{
			eq += "LVin(" + n.name + ")";
			if(i != node.successors.size()-1)
				eq += " || ";
			i++;
		}
		//System.out.println(eq);
		eq = "LVout(" + node.name + ") = ";
		i = 0;
		if(node.successors.size() == 0)
			eq += "[]";
		for(GraphNode n : node.successors)
		{
			eq += n.LVin;
			if(i != node.successors.size()-1)
				eq += " || ";
			i++;
		}
		//System.out.println(eq);
		Set<String> result = new TreeSet<String>();
		node.successors.forEach( succ -> result.addAll(succ.LVin) );
		node.LVout = result;
		System.out.println("LVout(" + node.name + ") = " + node.LVout);
		return;
	}
	
	// LVin(S) = ( LVout(S) - Def(S) ) | Use(S)
	public void LVin(GraphNode node)
	{
		//System.out.println("LVin(" + node.name + ") = ( LVout(" + node.name + ") - Def(" + node.name + ") ) || Use(" + node.name + ")");
		System.out.println("LVin(" + node.name + ") = ( " + node.LVout + " - " + node.def + " ) || " + node.use);
		Set<String> result = (TreeSet<String>)((TreeSet<String>)node.LVout).clone();
		node.def.forEach( def -> result.remove(def) );
		node.use.forEach( use -> result.add(use) );
		node.LVin = result;
		System.out.println("LVin(" + node.name + ") = " + node.LVin);
		return;
	}
	
	public void print()
	{
		final Object[][] table = new String[5][];
		List<String> names = new ArrayList<String>();
		List<String> uses = new ArrayList<String>();
		List<String> defs = new ArrayList<String>();
		List<String> lvins = new ArrayList<String>();
		List<String> lvouts = new ArrayList<String>();
		names.add("Node: ");
		uses.add("Uses: ");
		defs.add("Defs: ");
		lvins.add("LVin: ");
		lvouts.add("LVout: ");
		worklist.forEach( node -> {
			names.add(node.name);
			uses.add(node.use.toString());
			defs.add(node.def.toString());
			lvins.add(node.LVin.toString());
			lvouts.add(node.LVout.toString());
		});
		table[0] = names.toArray(new String[names.size()]);
		table[1] = defs.toArray(new String[defs.size()]);
		table[2] = uses.toArray(new String[uses.size()]);
		table[3] = lvins.toArray(new String[lvins.size()]);
		table[4] = lvouts.toArray(new String[lvouts.size()]);
		
		String format = "%30s";
		String finalFormat = "";
		for(String node : names)
		{
			finalFormat += format;
		}
		finalFormat += "\n";

		for (final Object[] row : table) {
		    System.out.format(finalFormat, row);
		}
	}
	
	public static void main(String... args)
	{
		// Set up nodes with defs and uses
		
		GraphNode B1 = new GraphNode("1");
		B1.addDef("i");
		
		GraphNode B2 = new GraphNode("2");
		B2.addDef("j");
		
		GraphNode B3 = new GraphNode("3");
		B3.addUse("n","i");
		B3.addDef("T1");
		
		GraphNode B4 = new GraphNode("4");
		B4.addUse("T1", "j");
		B4.addDef("T2");
		
		GraphNode B5 = new GraphNode("5");
		B5.addUse("T2");
		B5.addDef("T3");
		
		GraphNode B6 = new GraphNode("5'");
		B6.addUse("T3");
		B6.addDef("T21");
		
		GraphNode B7 = new GraphNode("6");
		B7.addUse("T21");
		
		GraphNode B8 = new GraphNode("7");
		B8.addUse("j");
		B8.addDef("j");
		
		GraphNode B9 = new GraphNode("7'");
		B9.addUse("j", "n");
		B9.addDef("T22");
		
		GraphNode B10 = new GraphNode("8");
		B10.addUse("T22");
		
		GraphNode B11 = new GraphNode("9");
		B11.addUse("i");
		B11.addDef("i");
		
		GraphNode B12 = new GraphNode("10");
		B12.addUse("i", "n");
		
		GraphNode B13 = new GraphNode("11");
		B13.addDef("i");
		
		GraphNode B14 = new GraphNode("12");
		B14.addDef("j");
		
		GraphNode B15 = new GraphNode("13");
		B15.addDef("k");
		
		GraphNode B16 = new GraphNode("14");
		B16.addDef("T4");
		B16.addUse("n", "i");
		
		GraphNode B17 = new GraphNode("15");
		B17.addDef("T5");
		B17.addUse("T4", "j");
		
		GraphNode B18 = new GraphNode("16");
		B18.addDef("T6");
		B18.addUse("T5");
		
		GraphNode B19 = new GraphNode("16'");
		B19.addDef("T23");
		B19.addUse("T6");
		
		GraphNode B20 = new GraphNode("17");
		B20.addDef("T7");
		B20.addUse("T23");
		
		GraphNode B21 = new GraphNode("18");
		B21.addDef("T8");
		B21.addUse("n", "i");
		
		GraphNode B22 = new GraphNode("19");
		B22.addDef("T9");
		B22.addUse("k", "T8");
		
		GraphNode B23 = new GraphNode("19'");
		B23.addDef("T24");
		B23.addUse("T9");
		
		GraphNode B24 = new GraphNode("20");
		B24.addDef("T10");
		B24.addUse("T24");
		
		GraphNode B25 = new GraphNode("21");
		B25.addDef("T11");
		B25.addUse("T10");
		
		GraphNode B26 = new GraphNode("22");
		B26.addDef("T12");
		B26.addUse("n", "k");
		
		GraphNode B27 = new GraphNode("23");
		B27.addDef("T13");
		B27.addUse("T12", "j");
		
		GraphNode B28 = new GraphNode("24");
		B28.addDef("T25");
		B28.addUse("T13");
		
		GraphNode B29 = new GraphNode("24'");
		B29.addDef("T14");
		B29.addUse("T25");
		
		GraphNode B30 = new GraphNode("25");
		B30.addDef("T15");
		B30.addUse("T14");
		
		GraphNode B31 = new GraphNode("26");
		B31.addDef("T16");
		B31.addUse("T11", "T15");
		
		GraphNode B32 = new GraphNode("27");
		B32.addDef("T17");
		B32.addUse("T7", "T16");
		
		GraphNode B33 = new GraphNode("28");
		B33.addDef("T18");
		B33.addUse("n", "i");
		
		GraphNode B34 = new GraphNode("29");
		B34.addDef("T19");
		B34.addUse("T18", "j");
		
		GraphNode B35 = new GraphNode("29'");
		B35.addDef("T26");
		B35.addUse("T19");
		
		GraphNode B36 = new GraphNode("30");
		B36.addDef("T20");
		B36.addUse("T26");
		
		GraphNode B37 = new GraphNode("31");
		B37.addUse("T20", "T17");
		
		GraphNode B38 = new GraphNode("32");
		B38.addDef("k");
		B38.addUse("k");
		
		GraphNode B39 = new GraphNode("33");
		B39.addUse("k", "n");
		
		GraphNode B40 = new GraphNode("34");
		B40.addDef("j");
		B40.addUse("j");
		
		GraphNode B41 = new GraphNode("35");
		B41.addUse("j", "n");
		
		GraphNode B42 = new GraphNode("36");
		B42.addDef("i");
		B42.addUse("i");
		
		GraphNode B43 = new GraphNode("37");
		B43.addUse("i", "n");
		
		GraphNode B44 = new GraphNode("38");
		
		// Set up additional successors
		B10.addSuccessor(B3);
		B12.addSuccessor(B2);
		B39.addSuccessor(B16);
		B41.addSuccessor(B15);
		B43.addSuccessor(B14);
		
		Graph graph = new Graph();
		graph.computeLiveness();
		System.out.println("\n");
		graph.print();
	}
}
