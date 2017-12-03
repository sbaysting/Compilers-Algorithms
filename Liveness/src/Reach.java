import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class Reach 
{
	private static Queue<ReachNode> worklist = new LinkedList<ReachNode>();
	
	public static void addToWorklist(ReachNode node)
	{
		//if(worklist.size() > 0)
			//worklist.peek().addSuccessor(node);
		worklist.add(node);
	}
	
	public Reach()
	{
		
	}
	
	public void computeRDA()
	{
		Queue<ReachNode> wl = new LinkedList<ReachNode>(worklist);
		System.out.println("Starting worklist: " + wl);
		while(wl.size() > 0)
		{
			ReachNode node = wl.remove();
			System.out.println("Removing node " + node.name + " from worklist");
			Set<String> RoutOld = node.Rout;
			Rin(node);
			Rout(node);
			if(!RoutOld.equals(node.Rout))
			{
				for(ReachNode n : node.successors)
				{
					if(!wl.contains(n))
						wl.add(n);
				}
			}
			System.out.println("Current worklist: " + wl);
		}
	}
	
	// Rout(S) = ( Rin(S) - Kill(S) ) | Gen(S)
	public void Rout(ReachNode node)
	{
		//System.out.println("LVin(" + node.name + ") = ( LVout(" + node.name + ") - Def(" + node.name + ") ) || Use(" + node.name + ")");
		System.out.println("Rout(" + node.name + ") = ( " + node.Rin + " - " + node.kill + " ) || " + node.gen);
		Set<String> result = (TreeSet<String>)((TreeSet<String>)node.Rin).clone();
		node.kill.forEach( def -> result.remove(def) );
		node.gen.forEach( use -> result.add(use) );
		node.Rout = result;
		System.out.println("Rout(" + node.name + ") = " + node.Rout);
		return;
	}
	
	// Rin(S) = union(Rout(predecessors(S)))
	public void Rin(ReachNode node)
	{
		//System.out.println("LVout(" + node.name + ") = LVin( successors(" + node.name + ") )");
		String eq = "Rin(" + node.name + ") = ";
		int i = 0;
		if(node.predecessors.size() == 0)
			eq += "[]";
		for(ReachNode n : node.predecessors)
		{
			eq += "Rout(" + n.name + ")";
			if(i != node.predecessors.size()-1)
				eq += " || ";
			i++;
		}
		//System.out.println(eq);
		eq = "Rin(" + node.name + ") = ";
		i = 0;
		if(node.predecessors.size() == 0)
			eq += "[]";
		for(ReachNode n : node.predecessors)
		{
			eq += n.Rout;
			if(i != node.predecessors.size()-1)
				eq += " || ";
			i++;
		}
		//System.out.println(eq);
		Set<String> result = new TreeSet<String>();
		node.predecessors.forEach( succ -> result.addAll(succ.Rout) );
		node.Rin = result;
		System.out.println("Rin(" + node.name + ") = " + node.Rin);
		return;
	}
	
	public void print()
	{
		final Object[][] table = new String[5][];
		List<String> names = new ArrayList<String>();
		List<String> uses = new ArrayList<String>();
		List<String> defs = new ArrayList<String>();
		List<String> Rins = new ArrayList<String>();
		List<String> Routs = new ArrayList<String>();
		names.add("Node: ");
		uses.add("Gens: ");
		defs.add("Kills: ");
		Rins.add("Rin: ");
		Routs.add("Rout: ");
		worklist.forEach( node -> {
			names.add(node.name);
			uses.add(node.gen.toString());
			defs.add(node.kill.toString());
			Rins.add(node.Rin.toString());
			Routs.add(node.Rout.toString());
		});
		table[0] = names.toArray(new String[names.size()]);
		table[1] = defs.toArray(new String[defs.size()]);
		table[2] = uses.toArray(new String[uses.size()]);
		table[3] = Rins.toArray(new String[Rins.size()]);
		table[4] = Routs.toArray(new String[Routs.size()]);
		
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
		
		ReachNode B1 = new ReachNode("1");
		B1.addGen("d1");
		B1.addKill("d4", "d7");
		
		ReachNode B2 = new ReachNode("2");
		B2.addGen("d2");
		B2.addKill("d6", "d8");
		
		ReachNode B3 = new ReachNode("3");
		B3.addGen("d3");
		
		ReachNode B4 = new ReachNode("4");
		
		ReachNode B5 = new ReachNode("5");
		B5.addGen("d4");
		B5.addKill("d1", "d7");
		
		ReachNode B6 = new ReachNode("6");
		B6.addGen("d5");
		
		ReachNode B7 = new ReachNode("7");
		
		ReachNode B8 = new ReachNode("8");
		B8.addGen("d6");
		B8.addKill("d2", "d8");
		
		ReachNode B9 = new ReachNode("9");
		
		ReachNode B10 = new ReachNode("10");
		B10.addGen("d7");
		B10.addKill("d1", "d4");
		
		ReachNode B11 = new ReachNode("11");
		B11.addGen("d8");
		B11.addKill("d2", "d4");
		
		ReachNode B12 = new ReachNode("12");
		
		ReachNode B13 = new ReachNode("13");
		
		// Set up successors
		
		B1.addSuccessor(B2);
		B2.addSuccessor(B3);
		B3.addSuccessor(B4);
		B4.addSuccessor(B5);
		B4.addSuccessor(B13);
		B5.addSuccessor(B6);
		B6.addSuccessor(B7);
		B7.addSuccessor(B8);
		B7.addSuccessor(B10);
		B8.addSuccessor(B9);
		B9.addSuccessor(B3);
		B10.addSuccessor(B11);
		B11.addSuccessor(B12);
		B12.addSuccessor(B3);
		
		Reach graph = new Reach();
		graph.computeRDA();
		System.out.println("\n");
		graph.print();
	}
}
