package practica9;

public class Heapsort {

	private static void swap(int [] v, int p1, int p2) {
		int aux = v[p1];
		v[p1] = v[p2];
		v[p2] = aux;
	}
	

	private static void sink(int[] v, int p, int size) {
		while((p*2+1) <= size) {
			int izquierda = 2*p + 1;
			int derecha = 2*p + 2;
			if(derecha < size && v[izquierda] > v[derecha]) {
				if(izquierda < size && v[p] < v[izquierda]) {
					swap(p, izquierda, v);
					p = izquierda;
				} else {
					break;
				}
			} else if(derecha < size && v[izquierda] < v[derecha]){
				if(derecha < size && v[p] < v[derecha]) {
					swap(p, derecha, v);
					p = derecha;
				} else {
					break;
				}
			} else if(izquierda < size && v[p] < v[izquierda]) {
				swap(p, izquierda, v);
				p = izquierda;
			} else {
				break;
			}
		}
	}
	private static void swap (int i, int j, int[] v) {
		int elem_i= v[i];
		v[i]=v[j];
		v[j]=elem_i;
	}
	
	
	private static void heapify (int[] v) {
		for (int i=v.length-1; i>=0; i--) {
			sink(v,i,v.length);
		}
	}
	
	static public void heapsort (int [] v) {
		
		heapify(v);
		
		int n=v.length;
		while (n>1) {
			swap (v, 0,n-1);
			n--;
			sink(v, 0,n);
		}
	}
}
