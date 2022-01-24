package practica4;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
* Implementación incompleta de una lista usando una cadena no circular de nodos 
* doblemente enlazados.
*/
public class EDDoubleLinkedList<T> implements List<T> {
    private class Node {
        private T data;
        private Node next;
        private Node prev;

        public Node(T data) {
        	this.data = data;
        }
    }

    private Node first = null;
    private Node last = null;
    private int size;

    public EDDoubleLinkedList(Collection<T> col) {
        for (T elem: col) {
            Node n = new Node(elem);
            if (first == null)
                first = last = n;
            else {
                n.prev = last;
                last.next = n;
                last = n;
            }
        }
        size = col.size();
    }

    
     /**
     * Invierte el orden de los elementos de la lista.
     */
    public void reverse() {
    	
    	if (first != null) {
    		if (first.next != null) {
    		    // Si la lista no está vacía y hay más de un elemento en la lista
                Node aux = last;

                while (aux.prev != null) {
                    // Recorrer hacia atrás
    				aux.next = aux.prev;
    				aux.prev = null;
    				aux = aux.next;
    				if (aux == first) {
                        // Salimos del bucle (lista recorrida)
    					break;
    				}
    			}

                // Esta primera iteración del bucle es necesaria para tener un previous
    			while (aux.next != null) {
    			    // Next al segundo que no se ha borrado
    				aux.prev = aux.next;
    				aux.next = null;
    				aux = aux.prev;
    				break;
    			}

                // Dos nodos para recorrer hacia atrás
    			Node nodoA = last.next;
    			Node nodoB = last;
    			while (nodoA.next != null) {
    			    // Mientras el siguiente de A (el de la izquierda) sea distinto de null
                    // Creo la referencia de previous
                    nodoA.prev = nodoB;
    				if (nodoA == aux) {
    				    //Si el nodo de la izquierda es el auxiliar, anteriormente usado
    					// Hemos puesto todos los previous
                        break;
    				} else {
    				    //Si no es auxiliar, avanzamos los nodos
    					nodoA = nodoA.next;
    					nodoB = nodoB.next;
    				}
    			}

    			// Intercambiar first y last
    			Node reference = first;
    			first = last;
    			last = reference;
    		}
    	}
    }


    /**
     *  Añade los elementos de la lista intercalándolo con la lista actual.
     */
    public void shuffle(List<T> lista) {
    	
    	if (lista != null) {
    		if (this.isEmpty()) {
    		    // Lista vacía
    			Iterator<T> iteradorLista = lista.iterator();
    			while (iteradorLista.hasNext()) {
    				T elemento = iteradorLista.next();
    				if (this.isEmpty()) {
    					Node primero = new Node(elemento);
    					first = primero;
    					last = primero;
    					size++;
    				} else {
    					Node siguiente = new Node(elemento);
    					last.next = siguiente;
    					siguiente.prev = last;
    					last = siguiente;
    					size++;
    				}
    			}
    		} else {
    		    // Caso en que la original no está vacía y lista tampoco
    			if (this.size() >= lista.size()) {
    			    // Caso en el que this.size() >= lista.size()
    				Iterator<T> iteradorLista = lista.iterator();
    				Node aux = first;
    				while (iteradorLista.hasNext()) {
    					if (aux == last) {
    						T elemento = iteradorLista.next();
    						Node añadido = new Node(elemento);
    						aux.next = añadido;
    						añadido.prev = aux;
    						size++;
    						last = añadido;
    					} else {
    						T elemento = iteradorLista.next();
    						Node añadido = new Node(elemento);
    						añadido.next = aux.next;
    						aux.next.prev = añadido;
    						aux.next = añadido;
    						añadido.prev = aux;
    						size++;
    						aux = aux.next.next;
    					}
    				}
    			} else {
    			    // Caso en el que this.size() < lista.size()
    				Iterator<T> iteradorLista = lista.iterator();
    				Node aux = first;
    				while (iteradorLista.hasNext()) {
    					if (aux != last) {
    						T elemento = iteradorLista.next();
    						Node añadido = new Node(elemento);
    						añadido.next = aux.next;
    						añadido.next.prev = añadido;
    						aux.next = añadido;
    						añadido.prev = aux;
    						size++;
    						aux = aux.next.next;
    					} else {
    						T elemento = iteradorLista.next();
    						Node añadido = new Node(elemento);
    						aux.next = añadido;
    						añadido.prev = aux;
    						size++;
    						aux = añadido;
    						last = aux;
    					}
    				}
    			}
    		}
    	}
    }



    @Override
    public int size() {
    	return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() { throw new UnsupportedOperationException(); }

    @Override
    public Object[] toArray() {
        Object[] v = new Object[size];

        Node n = first;
        int i = 0;
        while(n != null) {
            v[i] = n.data;
            n = n.next;
            i++;
        }

        return v;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        first = last = null;
        size = 0;
    }

    @Override
    public T get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");

        if (isEmpty())
            sb.append("[]");
        else {
            sb.append("[");
            Node ref = first;
            while (ref != null) {
                sb.append(ref.data);
                ref = ref.next;
                if (ref == null)
                    sb.append("]");
                else
                    sb.append(", ");
            }
        }

        sb.append(": ");
        sb.append(size);

        return sb.toString();
    }
}
