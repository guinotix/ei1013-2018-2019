package practica1;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class practica1 {

    /**
     *  Método que toma dos conjuntos de enteros y separa los elementos entre aquellos que sólo aparecen una vez
     *  y aquellos que aparecen repetidos. El método modifica los conjuntos que toma como parámetros.
     * @param unicos    A la entrada un conjunto de enteros. A la sálida los elementos que solo aparecen en uno de
     *                  los conjuntos.
     * @param repetidos A la entrada un conjunto de enteros. A la salida los elementos que aparecen en ambos conjuntos.
     */
    static public void separa(Set<String> unicos, Set<String> repetidos) {
    	Iterator<String> iteradorRepetidos = repetidos.iterator();
    	while (iteradorRepetidos.hasNext()) {
    		String elemento = iteradorRepetidos.next();
    		if (unicos.contains(elemento)) {
    			// Si también está en unicos, lo borramos porqué está repetido
    			unicos.remove(elemento);
    		} else {
    			// En caso contrario, en únicos no está el elemento. Lo añadimos ahí
    			unicos.add(elemento);
    			iteradorRepetidos.remove();
    		}
    	}
    }

    /**
     *  Toma un iterador a una colección de enteros positivos y devuelve como resultado un conjunto con aquellos elementos
     *  de la colección que no son múltiplos de algún otro de la colección. Los ceros son descartados
     * @param iter  Iterador a una colección de enteros
     * @return Conjunto de de enteros.
     */
    static public Set<Integer> filtra(Iterator<Integer> iter) {
    	Set<Integer> auxiliar = new HashSet<>(); // Conjunto para pasarle los elementos del iterador
        while (iter.hasNext()) {
        	int numero = iter.next();
        	if (numero == 1) {
        		// Caso especial: número 1
        		Set<Integer> unico = new HashSet<>();
        		unico.add(numero);
        		return unico;
        	}
        	if (numero > 0) {
        		auxiliar.add(numero);
        	}
        }
        if (auxiliar.isEmpty()) {
        	// Si no hay números: fin
        	return auxiliar;
        } else {
        	// Si no está vacío
        	Set<Integer> auxiliar2 = new HashSet<>(auxiliar); // Conjunto copia del primero
        	Set<Integer> auxiliar3 = new HashSet<>(); // Conjunto para el resultado final y eliminar

			for (Integer dividendo: auxiliar) {
        		for (Integer divisor: auxiliar2) {
        			if (!dividendo.equals(divisor) && dividendo % divisor == 0) {
        				// Si son distintos y son múltiplos
        				auxiliar3.add(dividendo);
        			}
        		}
        	}
			// La diferencia saca los NO múltiplos
        	auxiliar.removeAll(auxiliar3);
        	return auxiliar;
        }
    }

        		

    /**
     * Toma una colección de conjuntos de <i>String</i> y devuelve como resultado un conjunto con aquellos <i>String </i>
     * Que aparecen en al menos dos conjuntos de la colección.
     * @param col Coleccion de conjuntos de <i>String</i>
     * @return Conjunto de <i>String</i> repetidos. 
     */
    static public Set<String> repetidos(Collection<Set<String>> col) {
    	Set<String> encontradosUnaVez = new HashSet<>();
        Set<String> solucion = new HashSet<>();

        Iterator<Set<String>> iterador = col.iterator();
        while (iterador.hasNext()) {
            Set<String> subconjunto = iterador.next();

            Iterator<String> iterador2 = subconjunto.iterator();
            while (iterador2.hasNext()) {
            	// Iteramos dentro del subconjunto
                String elemento = iterador2.next();
                if (!encontradosUnaVez.contains(elemento)) {
                    encontradosUnaVez.add(elemento);
                } else {
                    solucion.add(elemento);
                }
            }
        }
        return solucion;
    }
}
