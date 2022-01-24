package practica10;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public  class EDMatrixGraph<T,W> implements EDGraph<T,W> {

	private boolean isDirected;
	private boolean isWeighted = false;
	private int nVertices; //maximum number of nodes
	private int size; //real number of nodes
	private T[] nodes;
	private boolean[][] adjacencyMat;
	
	@SuppressWarnings("unchecked")
	public EDMatrixGraph(boolean directed) {
		isDirected=directed;
		nVertices = 40;
		this.size =0;
		nodes = (T[]) new Object[this.nVertices];
		adjacencyMat = new boolean[this.nVertices][this.nVertices];
		for (int i=0; i<this.nVertices; i++) {
			nodes[i] = null;
			for (int j=0; j<this.nVertices; j++)
					this.adjacencyMat[i][j]= false;
			}
	}
	
	@SuppressWarnings("unchecked")
	public EDMatrixGraph(int n, boolean directed) {
		isDirected=directed;
		nVertices = n;
		this.size =0;
		this.nodes = (T[]) new Object[nVertices];
		adjacencyMat = new boolean[this.nVertices][this.nVertices];
		for (int i=0; i<this.nVertices; i++) {
			nodes[i] = null;
			for (int j=0; j<this.nVertices; j++)
					this.adjacencyMat[i][j]= false;
			}
	}
	
	@SuppressWarnings("unchecked")
	public EDMatrixGraph(String fileName) {
		isDirected=false;
		Scanner f = null;
		try {
			f = new Scanner (new FileInputStream (fileName));
		} catch (FileNotFoundException e) {
			System.out.println("File "+fileName+" not found");
			System.exit(0);
		}
		nVertices = f.nextInt();
		String s = f.nextLine().trim();
		if (s.equals("directed"))
		    isDirected = true;

		this.size = nVertices;
		nodes = (T[]) new Object[this.nVertices];
		adjacencyMat = new boolean[this.nVertices][this.nVertices];

		for (int i=0; i<this.nVertices; i++)
			nodes[i] = (T) f.next();

		for (int i=0; i<this.nVertices; i++) {
			for (int j=0; j<this.nVertices; j++) {
				int value = f.nextInt();
                this.adjacencyMat[i][j] = (value != 0);
			}
		}
		f.close();
	}
	
	@Override
	public int getSize() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return (size == 0);
	}

	@Override
	public int insertNode (T item) {
		int ret = -1;
		int i=0;
		while (i<this.nVertices && nodes[i]!=null) i++;
		if (i<this.nVertices) {
			ret = i;
			nodes[i]=item;
			size++;
		}
		for (i=0; i<this.nVertices; i++) {
			adjacencyMat[ret][i] =false;
			adjacencyMat[i][ret] = false;
		}
		return ret;
	}

	@Override
	public boolean insertEdge(EDEdge edge) {
		if (edge.getSource()<0 || edge.getTarget() <0 ) return false;
		if (edge.getSource()>=this.nVertices || edge.getTarget() >=this.nVertices ) 
			return false;
		if (nodes[edge.getSource()]==null || nodes[edge.getTarget()]==null)
			return false;
		this.adjacencyMat[edge.getSource()][edge.getTarget()]=true;
		if (!this.isDirected) 
			this.adjacencyMat[edge.getTarget()][edge.getSource()]=true;
		return true;
	}

	@Override
	public int getNodeIndex(T item) {
		int ret=0;
		while (ret<this.nVertices && !nodes[ret].equals(item)) ret++;
		if (ret == this.nVertices) ret = -1;
		return ret;
	}

	@Override
	public T getNodeValue(int index) {
		T ret = null;
		if (index >=0 && index< this.nVertices)
			ret = nodes[index];
		return ret;
	}

	@Override
	public EDEdge<W> getEdge(int source, int target) {

		if (source <0 || target <0 ) return null;
		if (source >=this.nVertices || target >=this.nVertices ) 
			return null;
		if (nodes[source]==null || nodes[target]==null)
			return null;
		EDEdge<W> edge = null;
		if (this.adjacencyMat[source][target])
			edge = new EDEdge<>(source,target);
		return edge;
		
		
	}


	@Override
	public T removeNode(int index) {
	    if (index <0 || index >= this.nVertices || nodes[index]==null) return null;
	    for (int i=0; i<this.nVertices; i++) {
	    	this.adjacencyMat[index][i]=false;
	    	this.adjacencyMat[i][index]=false;
	    }
	    T ret = this.nodes[index];
	    this.nodes[index]=null;
	    size--;
	    return ret;
	}

	@Override
	public EDEdge<W> removeEdge(int source, int target) {
		if (source <0 || target <0 ) return null;
		if (source >=this.nVertices || target >=this.nVertices ) 
			return null;
		if (nodes[source]==null || nodes[target]==null)
			return null;
		EDEdge<W> edge = null;
		if (this.adjacencyMat[source][target]) {
			edge = new EDEdge<>(source,target);
			this.adjacencyMat[source][target]=false;
			if (!this.isDirected) 
				this.adjacencyMat[target][source]=false;
		}
		return edge;
	}

	@Override
	public void printGraphStructure() {
		System.out.println("Vector size "+this.nVertices+" number of nodes "+size);
		System.out.print("Nodes: ");
		for (int i=0; i<this.nVertices; i++) 
			if (this.nodes[i]!=null) 
				System.out.print(nodes[i]+" ");
		System.out.println();
		System.out.println("Adjacency Matrix");
        System.out.print("  ");
		for (int i = 0; i < this.nVertices; i++)
            if (this.nodes[i]!=null)
                System.out.print(nodes[i] + " ");
            else
                System.out.print(". ");
        System.out.println();

		for (int i=0; i<this.nVertices; i++) {
            System.out.print((nodes[i] != null ? nodes[i] + " " : ". " ));
			for (int j=0; j<this.nVertices; j++)
				System.out.print(this.adjacencyMat[i][j] ? "1 " : "  ");
			System.out.println();
		}
		
	}

	@Override
	public int getNodesCapacity() {

		return this.nVertices;
	}
	
	public HashMap<T,Integer> getNodes() {
	
		HashMap<T, Integer> res= new HashMap<>();
		int i=0;
		while (i<nodes.length) {
			if (nodes[i]!=null) 
				res.put(nodes[i], i);
			i++;
		}
		
		return res;
	}

	@Override
	public Set<Integer> getOutgoing(int index) {

	    if (index < 0 || index > this.nVertices)
	        return null;

	    if (nodes[index] == null)
	        return null;

		Set<Integer> ret = new HashSet<>();

		for (int d =0 ; d < nVertices; d++)
		    if (adjacencyMat[index][d])
		        ret.add(d);
		return ret;
	}

	public boolean[][] getAdjacencyMatrix() {
	    return this.adjacencyMat;
	}

	public Set<T> degree1()  {
		Set<T> ret = new HashSet<>();

		for (int i=0; i<this.nVertices; i++) {
			if (nodes[i]!=null) {
			    int count=0;
                for(int j = 0; j < this.nVertices; j++) {
                    if (adjacencyMat[j][i])
                        count++;
                }
				if (count==1) //all false
					ret.add(nodes[i]);
			}
		}
		return ret;
	}

	@Override
	public boolean isWeighted() {
		return this.isWeighted;
	}

	public boolean isDirected() {
		return this.isDirected;
	}
}
