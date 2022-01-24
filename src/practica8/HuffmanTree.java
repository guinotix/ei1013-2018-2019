package practica8;

import java.io.IOException;
import java.util.*;


public class HuffmanTree {
	
	private static class Node {

		public boolean isLeaf = false;
		public char c = '\0';
		public float f = (float) 0.0;
		public Node left = null;
		public Node right =  null;
		
		public Node(boolean leaf, char chr, float freq) {
			isLeaf = leaf;
			c = chr;
			f = freq;
			left = null;
			right = null;
		}
		
		public String toString() {
			StringBuilder retVal = new StringBuilder();
			retVal.append("[" + isLeaf + ", " + c + ", " + f + "]");
			return retVal.toString();
		}

		public boolean equals(Object obj) {
		    if (obj == null)
		        return false;

		    if (obj.getClass() != getClass())
		        return false;

		    Node root = (Node) obj;

		    if (this.isLeaf && root.isLeaf && (this.c == root.c))
		        return true;

		    if (!this.isLeaf && !root.isLeaf && left.equals(root.left) && right.equals(root.right))
		        return true;

		    return false;
        }
	}

	
	Node root = null; //the Huffman binary tree


    //Builds a binaryTree for Huffman codification given the array of characters and its frequency	
	public HuffmanTree(int freqs[], char chars[]) {
		Comparator<Node> comparator = new NodeComparator();
		
		//codesList = new LinkedList[256];

        //build Huffman binary tree
		PriorityQueue<Node> queue = new PriorityQueue<Node>(256, comparator);

		for (int i=0; i<256; i++)
		{
			if (freqs[i] > 0) {
				Node n = new Node(true, chars[i], freqs[i]);
				queue.add(n);
			}
		}
		
		while (queue.size() > 1)
		{
			Node n1 = queue.remove();
			Node n2 = queue.remove();
			
			Node n3 = new Node(false,'\0',n1.f+n2.f);
			n3.left = n1;
			n3.right = n2;
			queue.add(n3);
		}

		root = queue.remove();

	}

    /** Construye un árbol de Huffman a partir de los carateres y sus codificaciones.
     * @param codes Map en el que por cada caracter se contiene ña lista de 0s y 1s que lo codifica.
     */
	public HuffmanTree (Map<Character, List<Integer>>  codes) {
		if (!codes.isEmpty()) {
			root = new Node(false, '\0', 0);
		}
		Node aux = root;
		for (Map.Entry<Character, List<Integer>> pares : codes.entrySet()) {
			for (Integer i : pares.getValue()) {
				if (i == 0) {
					if (aux.left == null) {
						Node nuevo = new Node(false, '\0', 0);
						aux.left = nuevo;
					} 
					aux = aux.left;
				}
				if (i == 1) {
					if (aux.right == null) {
						Node nuevo = new Node(false, '\0', 0);
						aux.right = nuevo;
					}
					aux = aux.right;
				}
			}
			aux.c = pares.getKey();
			aux.isLeaf = true;
			aux = root;
		}
	}
	

	
	public class NodeComparator implements Comparator<Node>
	{
	    @Override
	    public int compare(Node x, Node y)
	    {
	        // Assume both nodes can be null
	    	
	        if (x == null && y == null)
	        {
	            return 0;
	        }
	        if (x.f < y.f)
	        {
	            return -1;
	        }
	        if (x.f > y.f)
	        {
	            return 1;
	        }
	        
	        return 0;
	    }
	}


    /** Dado un charácter c, devuelve la lista de 0s y 1s que lo codifica dentro del árbol. <cose>null</cose> en el
     *  caso de que el carácter no se encuentre en el árbol.
     * @param c El carácter
     * @return  Lista de 0s y 1s o <code>null</code>.
     */
	public List<Integer> findCode(char c) {
		if (root == null) {
			return null;
		} else {
			List<Integer> resultado = new LinkedList<>();
			resultado = findCode(root, c, resultado);
			if (resultado.isEmpty()) {
				return null;
			}
			return resultado;
		}
	}		
	private List<Integer> findCode(Node n, char c, List<Integer> l) {
		List<Integer> listaDevolver = new LinkedList<>(l) ;
		if(!n.isLeaf) {
			listaDevolver.add(0);
			List<Integer> auxiliar = findCode(n.left, c, listaDevolver);
			listaDevolver.remove(listaDevolver.size()-1);
			
			if(auxiliar.equals(listaDevolver)) {
				listaDevolver.add(1);
				auxiliar = findCode(n.right, c, listaDevolver);
				listaDevolver.remove(listaDevolver.size()-1);
				
				if(auxiliar.equals(listaDevolver)) {
					if(!listaDevolver.isEmpty()) listaDevolver.remove(listaDevolver.size()-1);
					return listaDevolver;
				} else {
					return auxiliar;
				}
			} else {
				return auxiliar;				
			}
		} else {
			if(n.c==c) {
				return listaDevolver;
			}
			if(!listaDevolver.isEmpty()) listaDevolver.remove(listaDevolver.size()-1);
				return listaDevolver;
		}
	}
	

    /** Dada un String compuesto  por 0s y 1s que representan la codificacion de uno o mas carácteres. Decodifica y
     * añade a los carateres a una lista.
     * @param str   0s y 1s
     * @param l     Lista conteniendo los caracteres decodificados.
     */
	public void decode(String str, List<Character> l) {
		
		Node aux = root;
		for (char c : str.toCharArray()) {
			if (aux.isLeaf) {
				l.add(aux.c);
				aux = root;
			}
			if (c == '0') {
				aux = aux.left;
			}
			if (c == '1') {
				aux = aux.right;
			}
		}
		l.add(aux.c);
	}
		
		

	public static HuffmanTree createFromFile(String filename) throws IOException {
         Frequencies table = new Frequencies();
         HuffmanTree tree = null;

        table.loadFile(filename);
        tree = new HuffmanTree(table.frequenciesTable(),table.charsTable());

        return tree;
    }

    @Override
    public boolean equals(Object obj) {
        return (root == obj) || (root != null && (getClass() == obj.getClass()) && root.equals(((HuffmanTree) obj).root));
    }
}
