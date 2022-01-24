package practica9;

import java.util.*;

//IMPLEMENTS A PRIORITY QUEUE USING A MINHEAP

public class EDPriorityQueue<E>  {

	private static final int DEFAULT_INITIAL_CAPACITY = 11;

	E[] data;
	int size;
	//optional reference to comparator object
	Comparator<E> comparator;

	//Methods
	//Constructor
	public EDPriorityQueue() {
		this.data = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
		this.size = 0;
	}

	public EDPriorityQueue(Comparator<E> comp) {
		this.data = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
		this.size = 0;
		this.comparator = comp;
	}

	public EDPriorityQueue(Collection<E> c, Comparator<E> comp) {
		this.data = (E[]) new Object[c.size()];
		this.size = 0;
		this.comparator = comp;
		for (E elem: c)
			add(elem);
	}

	public EDPriorityQueue(Collection<E> c) {
		this.data = (E[]) new Object[c.size()];
		this.size= 0;
		int i=0;
		for (E elem: c)
			add(elem);
	}


	//private methods
	/** compares two objects and returns a negative number if object
	 * left is less than object right, zero if are equal, and a positive number if
	 * object left is greater than object right
	 */
	int compare(E left, E right) {
		if (comparator != null) { //A comparator is defined
			return comparator.compare(left,right);
		}
		else {
			return (((Comparable<E>) left).compareTo(right));
		}
	}

	/** Exchanges the object references in the data field at indexes i and j
	 * 
	 * @param i  index of firt object in data
	 * @param j  index of second objet in data
	 */
	private void swap(int i, int j) {
		E elem_i= this.data[i];
		this.data[i]=this.data[j];
		this.data[j]=elem_i;
	}
	//Izquierdo = 2*parent + 1
	//Derecho = 2*parent + 2
	private void sink(int parent) {
		while (2*parent + 1 < this.size()) {
			int izquierda = 2*parent + 1;
			int derecha = 2*parent +2;
			if (compare(data[izquierda], data[derecha]) <= 0) {
				if (compare(data[parent], data[izquierda]) > 0) {
					swap(parent, izquierda);
					parent = 2*parent + 1;
				} else {
					break;
				}
			} else if (compare(data[izquierda], data[derecha]) > 0) {
				if (compare(data[parent], data[derecha]) > 0) {
					swap(parent, derecha);
					parent = 2*parent + 2;
				} else {
					break;
				}
			}
		}
	}

	private void floating (int child) {
		while (child > 0) {
			int parent = (int) (child - 1)/2;
			if (compare(data[parent], data[child]) > 0) {
				swap(parent, child);
				child = parent;
			} else {
				break;
			}
		}
	}

	private void reallocate() {
		if (size == data.length) {
			E[] aux = data;
			data = (E[]) new Object[data.length*2];
			for (int i=0; i<size; i++)
				data[i] = aux[i];
		}
	}

	/**Public methods*/
	public boolean isEmpty() {
		return (this.size == 0);
	}

	public int size() {
		return this.size;
	}

	public Object[] toArray() {
		Object[] array = new Object[this.size];
		for (int i=0; i<this.size; i++)
			array[i] = this.data[i];
		return array;
	}

	String toStringHeap() {
		StringBuilder s = new StringBuilder();
		int enNivel = 1;
		int finNivel = 1;
		for (int i = 0; i < size; i++) {
			s.append(data[i]);

			if (i != size -1)
				if (i == finNivel-1) {
					s.append("] [");
					enNivel *= 2;
					finNivel += enNivel;
				} else
					s.append(", ");
		}
		s.append("]");
		return s.toString();
	}

	public String toString() {
		return "EDPriorityQueue: [" + this.toStringHeap() + " - size: " + size;
	}


	/** Inserts an item into the priority_queue. Returns true if successful;
	 * returns false if the item is not inserted
	 * @param item Item to be inserted in the priority queue
	 * @return boolean
	 */
	public boolean add(E item) {
		//Add the item to the end of the heap

		this.data[size]=item;
		size++;

		reallocate();

		floating(size-1);

		return true;
	}


	/** Removes the smallest entry and returns it if the priority queue is not empty.
	 * Otherwise, returns NoSuchElementException
	 * @return E smallest element in the queue
	 */
	public E remove() throws NoSuchElementException {
		if (isEmpty()) throw new NoSuchElementException();
		E result = data[0]; //the root of the heap


		//remove the last item in the array and put it in the root position
		swap(0,size-1);
		size--;
		if (size > 1) 
			sink(0);
		return result;

	}



	/** Returns the smallest entry, WITHOUT REMOVING IT.
	 * If the queue is empty, returns NoSuchElementException
	 * @return E smallest entry
	 */
	public E element() throws NoSuchElementException {
		if (isEmpty()) throw new NoSuchElementException();
		return data[0];
	}



	public int indexOf(E item) {
		int i=0;
		while (i<this.size && compare(data[i],item)!=0) i++;
		if (i==this.size) return -1;
		return i;
	}

	/** Removes a single instance of the element item
	 * 
	 * @param item Element to be removed (a single instance)
	 * @return the value of the element removed
	 * @throws NoSuchElementException If there no such item in the collection
	 */
	public E remove(E item) throws NoSuchElementException {
		if (this.isEmpty()) {
			return null;
		}
		E elemento = null;
		for (int i=0; i<data.length; i++) {
			if (data[i].equals((E) item)) {
				swap(0, i); // Intercambio de ese elemento con la raíz
				swap(0, size-1); // Intercambio de la raíz con el último
				elemento = element(); // Guardamos el elemento para devolverlo
				remove(); // Se quita el último elemento

				//Reordenar todos los elementos para que sea un montículo minHeap
				for (int j=0; j<size; j++) {
					floating(j);
				}
				return elemento;
			}
		}
		return elemento;
	}


	/**Converts a minHeap into a maxHeap.**/
	public void maxHeapify() {
		for (int i=0; i<size; i++) {
			flota_maximos(i);
		}
	}
	private void flota_maximos(int child) {
		while (child > 0) {
			int parent = (int) (child - 1)/2;
			if (compare(data[parent], data[child]) < 0) {
				swap(parent, child);
				child = parent;
			} else {
				break;
			}
		}
	}


	/**
	 * @return -1 if the queue contains a MinHeap, +1 if it is a maxHeap, or = if its empty.
	 */
	public int typeOfHeap() {
		if (this.isEmpty()) {
			return 0;
		}
		if (compare(data[0], data[1]) < 0) {
			return -1;
		} else if (compare(data[0], data[1]) > 0){
			return 1;
		} 
		if (compare(data[0], data[2]) < 0) {
			return -1;
		} else {
			return 1;
		}
	}
}
