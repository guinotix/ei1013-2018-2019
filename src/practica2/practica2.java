package practica2;

import java.util.*;

public class practica2 {

    /** Comprueba si dos listas de String son equivalentes.
     *
     * Dos listas son equivalentes si contienen los mismos elementos y la misma cantidad de ellos.
     * @param l1    Primera lista
     * @param l2    Segunda lista
     * @return <code>true</code> si las listas son equivalentes. <code>false</code> en caso contrario.
     */
    static public boolean equivalentes(List<String> l1, List<String> l2) {
    	if (l1.size() != l2.size()) {
    		return false;
    	}
    	// Lista auxiliar
    	LinkedList<String> auxiliar = new LinkedList<>(l2);

    	Iterator<String> iterador = l1.iterator();
    	while (iterador.hasNext()) {
    		String elemento = iterador.next();
    		if (auxiliar.contains(elemento)) {
    			// Si ese elemento está en el auxiliar, lo borro
    			auxiliar.remove(elemento);
    		}
    	}
    	if (auxiliar.isEmpty()) {
			// Las dos listas son iguales
    		return true;
    	} else return false;
    }
    		

    /** Invierte el orden de los elmentos de una lista.
     *
     * @param iter Un iterador de la lista. Puede estar en cualqueir posición de la lista.
     */
    static public void invierte(ListIterator<String> iter) {
    	// Vamos al inicio
    	while (iter.hasPrevious()) {
    		iter.previous();
        }

    	int numeroElementos = 0;
    	while (iter.hasNext()) {
    		iter.next();
    		numeroElementos++;
    	}

    	int desplazamientos = numeroElementos - 1;
    	while (desplazamientos != 0 && iter.hasPrevious()) {
    		String auxiliar = iter.previous();
    		if (iter.hasPrevious()) {
    			iter.remove();
    			iter.previous();
    			iter.add(auxiliar);
    		}
    		desplazamientos--;
    		if (desplazamientos == 0) {
    			while (iter.hasNext()) {
    				auxiliar = iter.next();
    			}
    			numeroElementos--;
    			desplazamientos = numeroElementos - 1;
    		}
    		if (numeroElementos == 0) {
    			break;
    		}
    	}
    }


    /** Ordena los elementos de una lista de menor a mayor
     * @param l     La lista
     * @return      Una nueva lista con los mismo elementos, pero ordenados.
     */
    static public List<Integer> ordenar(List<Integer> l) {
    	if (l.isEmpty()) {
    		return l;
    	} else {
    		LinkedList<Integer> solucion = new LinkedList<>();
    		for (int i=0; i<l.size(); i++) {
    			int candidato = l.get(i);
    			if (solucion.isEmpty()) {
    				solucion.add(candidato);
    			} else {
    				for (Integer elemento: solucion) {
    					if (candidato > elemento) {
    						solucion.add(solucion.indexOf(elemento) + 1, candidato);
    						break;
    					}
    					else {
    						solucion.add(solucion.indexOf(elemento), candidato);
    						break;
    					}
    				}
    			}
    		}
    		return solucion;
    	}
    }

}
