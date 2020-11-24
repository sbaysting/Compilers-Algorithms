package graphing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.view.Viewer;

public class Graph<T extends AbstractGraphNode>
{
	// GraphStream objects
	private org.graphstream.graph.Graph graph;
	private Viewer view;
		
	// Graph nodes
	private String name;
	private TreeSet<T> nodes = new TreeSet<>();
	private T ref = null;
	
	public Graph(String name, T refNode)
	{
		ref = refNode;
		this.name = name;
		graph = new SingleGraph(name);
	}
	
	public T getReferenceNode()
	{
		return ref;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void addNode(T... nodes)
	{
		for(T node : nodes)
		{
			if(!this.nodes.contains(node))
				createNode(node.getName(), node.getID());
			this.nodes.add(node);
		}
	}
	
	public void removeNode(T node)
	{
		nodes.remove(node);
		graph.removeNode(node.getID());
	}
	
	public Set<T> getNodes()
	{
		return nodes;
	}
	
	public void clear()
	{
		nodes.clear();
		graph.clear();
	}
	
	public int size()
	{
		return nodes.size();
	}
	
	public T getNode(int ID)
	{
		for(T node : nodes)
		{
			if(node.getID() == ID)
				return node;
		}
		return null;
	}
	
	public void addSuccessors(T rootNode, T... successors)
	{
		addNode(rootNode);
		for(T node : successors)
		{
			addNode(node);
			rootNode.addSuccessor(node);
			node.addPredecessor(rootNode);
			createEdge(rootNode.getID(), node.getID());
		}
	}
	
	public void addPredecessors(T rootNode, T... predecessors)
	{
		addNode(rootNode);
		for(T node : predecessors)
		{
			addNode(node);
			rootNode.addPredecessor(node);
			node.addSuccessor(rootNode);
			createEdge(node.getID(), rootNode.getID());
		}
	}
	
	public void toFile(String filename) throws IOException
	{
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
		for(T node : nodes)
		{
			writer.write(node.toString());
			writer.write("\n");
		}
		writer.close();
	}
	
	public void fromFile(String filename) throws IOException
	{
		File file = new File(filename);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;
		nodes.clear();
		graph.clear();
		T node = null;
		while ((line = bufferedReader.readLine()) != null) 
		{
			node = (T) ref.newInstance();
			node.fromString(line);
			nodes.add(node);
		}
		T defNode = (T) ref.newInstance();
		bufferedReader.reset();
		while ((line = bufferedReader.readLine()) != null) 
		{
			defNode.fromString(line);
			int thisID = defNode.getID();
			int[] successorIDs = defNode.getSuccessorIDs(line);
			for(int s : successorIDs)
			{
				// Find this node in set
				T thisNode = getNode(thisID);
				T succNode = getNode(s);
				if(thisNode == null)
					throw new RuntimeException("Definition for node ID " + thisID + " was not found in file!");
				if(succNode == null)
					throw new RuntimeException("Definition for node ID " + s + " was not found in file!");
				addSuccessors(thisNode, succNode);
			}
		}
		bufferedReader.close();
		fileReader.close();
	}
	
	private void createNode(String name, int ID)
	{
		Node n = graph.addNode(Integer.toString(ID));
		n.addAttribute("ui.label", name + " (ID: " + Integer.toString(ID) + ")");
		//n.setAttribute("x", 0);
		//n.setAttribute("y", ID);
	}
	
	private void createEdge(int srcID, int dstID)
	{
		graph.addEdge(UUID.randomUUID().toString(), Integer.toString(srcID), Integer.toString(dstID), true);
	}
	
	public void visualize()
	{
		view = graph.display();
		JFrame frame = (JFrame) SwingUtilities.getRoot(view.getDefaultView());
		frame.setTitle(getName());
		view.enableAutoLayout(new HierarchicalLayout());
		//view.disableAutoLayout();
        view.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
	}
}
