package practica10;

import java.util.*;


/** Implementation of interface Graph using adjacency lists
 * @param <T> The base type of the nodes
 * @param <W> The base type of the weights of the edges
 */
public class EDListGraph<T,W> implements EDGraph<T,W> {
	@SuppressWarnings("hiding")
	private class Node<U> {
		U data;
		List<EDEdge<W>> lEdges;
		
		Node (U data) {
			this.data = data;
			this.lEdges = new LinkedList<EDEdge<W>>();
		}



        @Override
		public boolean equals (Object other) {
			if (this == other)
			    return true;
			if (!(other instanceof Node))
			    return false;
			Node<T> anotherNode = (Node<T>) other;
			return data.equals(anotherNode.data);
		}
	}
	
	// Private data
	private ArrayList<Node<T>> nodes;
	private int size; //real number of nodes
	private boolean directed;
	private boolean weighted;
	


	public EDListGraph() {
		directed = false; //not directed
		weighted = false;
		nodes =  new ArrayList<>();
		size =0;
	}

    /** Constructor
     * @param dir <code>true</code> for directed edges;
     * <code>false</code> for non directed edges.
     */
	public EDListGraph (boolean dir) {
		directed = dir;
		weighted = false;
		nodes =  new ArrayList<Node<T>>();
		size =0;
	}
	
	public EDListGraph (boolean dir, boolean wei) {
		directed = dir;
		weighted = wei;
		nodes = new ArrayList<Node<T>>();
		size =0;
	}
	
	public EDListGraph (EDMatrixGraph<T,W> g) {
		//EJERCICIO 1
		directed = g.isDirected();
		weighted = g.isWeighted();
		
		boolean[][] matrix = g.getAdjacencyMatrix();
		nodes = new ArrayList<Node<T>>();
		
		//Añadir los nodos a la ArrayList
		for (int i=0; i<g.getSize(); i++) {
			Node<T> nodo = new Node<T>(g.getNodeValue(i));
			nodes.add(nodo);
			size++;
		}
		
		//Vamos a los arcos
		for (int i=0; i<matrix.length; i++) {
			for (int j=0; j<matrix.length; j++) {
				if (matrix[i][j] == true) {
					EDEdge<W> nuevo = new EDEdge<W>(i,j);
					nodes.get(i).lEdges.add(nuevo);
				}
			}
		}
		
	}

    @Override
    public int getSize() {
	    return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

    @Override
    public boolean isWeighted() {
		
		return this.weighted;
	}

	public boolean isDirected() {
		return this.directed;
	}
	

	@Override
	public int getNodesCapacity() {
		return nodes.size();
	}
	
	@Override

	/**
	 * Inserts the item as a new Node of the Graph
	 * The new node is stored in a null position of the array nodes.
	 * If there isn't a null position, then is stored at the end of the array
	 * The method returns the index of the position in the array where the
	 * new node is stored.for (int i=0; i<this.size; i++)
     * 			this.nodes.add(new Node<T>());
	 * Two nodes cannot have the same item. Then, if already exists a node with
	 * item, it returns the position were it is already stored
	 */
	public int insertNode(T item) {

	    int i = 0; int pos=-1; int free=nodes.size();
	    while (i<nodes.size() && pos==-1) {

	    	if (nodes.get(i).data == null) free = i;
	    	else if (nodes.get(i).data.equals(item)) pos = i;
	    	i++;
	    }
	    if (pos == -1) { //No esta
	    	Node<T> newNode = new Node<T>(item);
	    	if (free<nodes.size()) nodes.set(free,newNode);
	    	else {free=size; nodes.add(newNode);}
	    	size++;
	    	return free;
	    }
	    else return pos;
	}
	
	/**
	 * getNodeIndex (T item)  Returns the index of the Node with item in the array of Nodes
	 * It returns -1 if there is no node with that item
	 */
    @Override
    public int getNodeIndex(T item) {
		Node<T> aux = new Node<T>(item);
		return nodes.indexOf(aux);
	}

	@Override
	public T getNodeValue(int index) throws IndexOutOfBoundsException{
		return nodes.get(index).data;
		
	}

	/**
	 * insertEdge (EDEdge edge) Inserts the edge in the graph. If the graph is not directed, the method 
	 * inserts also the reserve edge
	 * Returns true if the edge has been inserted and false otherwise
	 */
    @Override
    public boolean insertEdge(EDEdge<W> edge) {
		int sourceIndex = edge.getSource();
		int targetIndex = edge.getTarget();
		if (sourceIndex >=0 && sourceIndex<nodes.size() && targetIndex >=0 && targetIndex<nodes.size()) {
			Node<T> nodeSr = nodes.get(sourceIndex);
			Node<T> nodeTa = nodes.get(targetIndex);
			if (nodeSr.data!=null && nodeTa.data != null) {
			   if (!nodeSr.lEdges.contains(edge)) {
				   nodeSr.lEdges.add(edge);
				   nodes.set(sourceIndex,nodeSr); 
				   if (!directed) {//no dirigido
					  EDEdge<W> reverse = new EDEdge<W>(targetIndex,sourceIndex,edge.getWeight());
					  nodeTa.lEdges.add(reverse);
					  nodes.set(targetIndex, nodeTa);
				   }
				   return true;
			    }
			}
		}
		return false;
	}
	
	public boolean insertEdge (T fromNode, T toNode) {
		int fromIndex = getNodeIndex(fromNode);
		if (fromIndex <0)
			return false;
		int toIndex = this.getNodeIndex(toNode);
		if (toIndex <0)
			return false;
		W label = null;
		EDEdge<W> e = new EDEdge<W>(fromIndex,toIndex,label);
		this.insertEdge(e);
		return true;
	}

	@Override
	public EDEdge<W> getEdge(int source, int target) {	
		if (source <0 || source >= nodes.size()) return null;
		
		Node<T> node = nodes.get(source);
		if (node.data == null ) return null;
		for (EDEdge<W> edge: node.lEdges)
			
			if (edge.getTarget() == target) return edge;
		
		return null;
	}

    @Override
    public Map<T, Integer> getNodes() {
        Map<T, Integer> ret = new HashMap<>();

        for (int i = 0;i < nodes.size(); i++)
            ret.put(nodes.get(i).data, i);

        return ret;
    }

    @Override
    public Set<Integer> getOutgoing(int index) {

	    if (index<0 || index> nodes.size()) return null;

	    if (nodes.get(index) == null) return null;

        Set<Integer> ret = new HashSet<>();

        for (EDEdge<W> edge: nodes.get(index).lEdges)
            ret.add(edge.getTarget());

        return ret;
    }

	@Override
	public EDEdge<W> removeEdge(int source, int target) {
		if (source <0 || source >= nodes.size() || target<0 || target >= nodes.size()) return null;
		if (nodes.get(source).data!=null && nodes.get(target).data!=null) {
			EDEdge<W> edge = new EDEdge<W>(source, target);
			Node<T> node = nodes.get(source);
			int i = node.lEdges.indexOf(edge);
			if (i != -1) {
				edge = node.lEdges.remove(i);
				if (!directed) {
					EDEdge<W> reverse = new EDEdge<>(target,source);
					nodes.get(target).lEdges.remove(reverse);
				}
				return edge;
			}	
		}
		return null;	
	}

	@Override
	public T removeNode(int index) {
		if (index >=0 && index < nodes.size()){
			if (!directed) {
				Node<T> node = nodes.get(index);
				for (EDEdge<W> edge: node.lEdges ) {
					int target = edge.getTarget();
					W label = edge.getWeight();
					EDEdge<W> other = new EDEdge<>(target,index,label);
					nodes.get(target).lEdges.remove(other);
				}
			}
			else { //directed
				for (int i=0; i<nodes.size(); i++) {
					if (i!=index && nodes.get(i).data !=null) {
						Node<T> node = nodes.get(i);
						for (EDEdge<W> edge: node.lEdges) {
							if (index == edge.getTarget()) //any weight/label
								node.lEdges.remove(edge);
						}
					}
				}
			}
			
			Node<T> node = nodes.get(index);
			node.lEdges.clear();
			T ret = node.data;
			node.data = null; //It is not remove, data is set to null
			nodes.set(index, node);
			size--;
			return ret;
		}
		return null;
	}
	
	
	
		
	public int[] breathFirstSearch (int start) {		
		Queue<Integer> qu = new LinkedList<>();
		if (start<0 || start >= nodes.size()) return new int[0];
		
		//Declare an array 'parent' and initialize its elements to -1
		int [] parent = new int[nodes.size()];
		for (int i=0; i<parent.length; i++)
			parent[i]=-1;
		
		
		parent[start] = start;
		qu.add(start);
		
		while (!qu.isEmpty()) {
			int current = qu.remove(); 

			for (EDEdge<W> edge: nodes.get(current).lEdges) {
				int neighbor = edge.getTarget();
				if (parent[neighbor] == -1) {
					parent[neighbor] = current;
					qu.add(neighbor);
				}
			} 	
		}
		return parent;
	}
	
		
	
	public int[] DepthFirstSearch(int start, int exit) {
		
		int[] entradas = new int[size];
		for (int i=0; i<entradas.length; i++) {
			entradas[i] = -1;
		}
		
		entradas[start] = start;
		entradas =  DepthFirstSearch(start, exit, entradas);
		return entradas;
	}
	private int[] DepthFirstSearch(int start, int exit, int[] vector) {
	
		for (EDEdge<W> flecha : nodes.get(start).lEdges) {
			if (vector[exit] != -1) {
				return vector;
			}
			if (vector[flecha.getTarget()] == -1) {
				vector[flecha.getTarget()] = start;
				vector = DepthFirstSearch(flecha.getTarget(), exit, vector);
			}
		}
		return vector;
	}
	
	
	public void printGraphStructure() {
		//System.out.println("Vector size= " + nodes.length);
		System.out.println("Vector size " + nodes.size());
		System.out.println("Nodes: "+ this.getSize());
		for (int i=0; i<nodes.size(); i++) {
			System.out.print("pos "+i+": ");
	        Node<T> node = nodes.get(i);
			System.out.print(node.data+" -- ");
			Iterator<EDEdge<W>> it = node.lEdges.listIterator();
			while (it.hasNext()) {
					EDEdge<W> e = it.next();
					System.out.print("("+e.getSource()+","+e.getTarget()+", "+e.getWeight()+")->" );
			}
			System.out.println();
		}
	}

	
	public Set<T> degree1() {
		Set<T> solucion = new HashSet<T>();

		int[] degrees = new int[size];


		for (Node<T> elemento: nodes) {
			for (EDEdge<W> vector: elemento.lEdges) {
				degrees[vector.getTarget()]++;
			}
		}

		for (int i=0; i<degrees.length; i++) {
			if (degrees[i] == 1) {
				solucion.add(nodes.get(i).data);
			}
		}

		return solucion;
	}


//	public Set<T> degree1() {
//		Set<T> solution = new HashSet<T>();
//
//		Map<Integer, Integer> degrees = new HashMap<>();
//		for (int i=0; i<size; i++) {
//			degrees.put(i, 0);
//		}
//
//
//		for (Node<T> elem: nodes) {
//			for (EDEdge<W> edge: elem.lEdges) {
//				int pos = edge.getTarget();
//				Integer currentDegree = degrees.get(pos);
//				degrees.replace(pos, currentDegree, ++currentDegree);
//			}
//		}
//
//		for (Map.Entry<Integer, Integer> pairs : degrees.entrySet()) {
//			if (pairs.getValue() == 1) {
//				solution.add(nodes.get(pairs.getKey()).data);
//			}
//		}
//
//		return solution;
//	}

	public List<T> findPath(int entry, int exit) {
		List<T> solucion = new LinkedList<T>();
		
		int[] vector = DepthFirstSearch(entry, exit);

		int posicion = exit;
		while (posicion != entry) {
			solucion.add(0, nodes.get(posicion).data);
			posicion = vector[posicion];
		}
		solucion.add(0, nodes.get(posicion).data);
		return solucion;
	}
	
	
}
