package practica10;

import java.util.*;

public interface EDGraph<T,W> {
	//returns number of nodes of the graph
	int getSize();
	
	//true if the graph has no nodes; false in other case
	boolean isEmpty();
	
	//inserts a new node with the label item
	int insertNode(T item);

	//inserts a new edge
	boolean insertEdge(EDEdge<W> edge);
	
	//given the label of a node, returns the index of the position
	//in the array where is stored
	int getNodeIndex(T item);
	
	//given the position of a node, returns the node element
	//returns null if an empty or wrong position
	T getNodeValue(int index);
	
	//given the indices of two nodes, returns the Edge that joins the
	//two nodes, if any. If there is no edge, then returns null
	//The direction source --> target is important if the graph is directed
	EDEdge<W> getEdge(int source, int target);
	
	//Returns a map with all the nodes in graph and their assigned indexes
	Map<T, Integer> getNodes();

	// Returns a collection with all the nodes that can be reached from the current
	Set<Integer> getOutgoing(int index);

	//removes a node
	T removeNode(int index);
	
	//removes an edge
	EDEdge<W> removeEdge(int source, int target);

	//prints the structure
	void printGraphStructure();

	// returns the capacity of the graph
	int getNodesCapacity();

	boolean isWeighted();
	boolean isDirected();
}
