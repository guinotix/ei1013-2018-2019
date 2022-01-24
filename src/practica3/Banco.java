package practica3;

import java.util.*;


public class Banco {
    public HashMap<String, Integer> cuentas = new HashMap<>();                // Mapa con las cuentas y su saldos
    public HashMap<String, List<Transferencia>> desglose = new HashMap<>();  // Mapa con las trasnferencias por cada cuenta

    public Banco(List<String> codigos, List<Integer> saldos) {
    	if (codigos == null || saldos == null || codigos.size() != saldos.size()) {
    		throw new IllegalArgumentException();
    	}
    	ListIterator<String> iteradorCodigos = codigos.listIterator();
    	ListIterator<Integer> iteradorSaldos = saldos.listIterator();
    	while (iteradorCodigos.hasNext()) {
    		try {
    			String key = iteradorCodigos.next();
        		int value = iteradorSaldos.next();
        		if (key.equals(null)) {
        			throw new IllegalArgumentException();
        		}
        		cuentas.put(key, value);
    		} catch (Exception e) {
    			throw new IllegalArgumentException();
    		}
    	}
    }

    /**
     * Realiza una una trasnferencia entres dos cuentas modificando su saldo. Guarda la transferencia en un histórico.
     *
     *
     * @param tr La transferencia. Los códigos de cuenta deben existir y ser distintos de <i>null</i>. La cuenta origen debe
     *           tener saldo positivo suficiente para realizar la transferencia.
     * @return <i>True</i> si la transferencia fue posible. ç
     * @throws <i>IllegalArgumentExcpetion</i> si alguna de las cuentas no existe, o el código es <i>null</i>.
     */
    public boolean asiento(Transferencia tr) {
    	if (!cuentas.containsKey(tr.origen) || !cuentas.containsKey(tr.destino)) {
    		throw new IllegalArgumentException();
    	}
    	// Primer caso, la cantidad positiva
    	if (tr.cantidad > 0) {
    		if (tr.cantidad > cuentas.get(tr.origen)) {
    			return false;
    		} else {
    			// Intercambio de la cantidad
    			int saldoOrigen = cuentas.get(tr.origen);
    			cuentas.put(tr.origen, saldoOrigen - tr.cantidad);
    			int saldoDestino = cuentas.get(tr.destino);
    			cuentas.put(tr.destino, saldoDestino + tr.cantidad);

    			// Actualizar el desglose de cuentas
    			// (Origen)
    			if (desglose.containsKey(tr.origen)) {
    				desglose.get(tr.origen).add(tr);
    			} else {
    				LinkedList<Transferencia> listaOrigen = new LinkedList<>();
    				listaOrigen.add(tr);
    				desglose.put(tr.origen, listaOrigen);
    			}
    			//(Destino)
    			if (desglose.containsKey(tr.destino)) {
    				desglose.get(tr.destino).add(tr);
    			} else {
    				LinkedList<Transferencia> listaDestino = new LinkedList<>();
    				listaDestino.add(tr);
    				desglose.put(tr.destino, listaDestino);
    			}
    			return true;
    		}
    	} else {
    		// Segundo caso, la cantidad es negativa
    		if (tr.cantidad * -1 > cuentas.get(tr.destino)) {
    			return false;
    		} else {
    			// Intercambio de la cantidad
    			int saldoOrigen = cuentas.get(tr.origen);
    			cuentas.put(tr.origen, saldoOrigen + tr.cantidad*-1);
    			int saldoDestino = cuentas.get(tr.destino);
    			cuentas.put(tr.destino, saldoDestino - tr.cantidad*-1);

    			// Actualizar el desglose de cuentas
    			// Parte de Origen (recibe dinero)
    			if (desglose.containsKey(tr.origen)) {
    				desglose.get(tr.origen).add(tr);
    			} else {
    				LinkedList<Transferencia> listaOrigenB = new LinkedList<>();
    				listaOrigenB.add(tr);
    				desglose.put(tr.origen, listaOrigenB);
    			}
    			//Parte de Destino (da el dinero)
    			if (desglose.containsKey(tr.destino)) {
    				desglose.get(tr.destino).add(tr);
    			} else {
    				LinkedList<Transferencia> listaDestinoB = new LinkedList<>();
    				listaDestinoB.add(tr);
    				desglose.put(tr.destino, listaDestinoB);
    			}
    			return true;
    		}
    	}
    }

    /**
     * Devuelve el saldo de una cuenta
     * @param codigo Código de la cuenta. Debe ser distinto <i>null</i> y existir.
     * @return El saldo de la cuenta.
     * @throws <i>IllegalArgumentException</i> si el código de cuenta no es válido.
     */
    public Integer consulta(String codigo) {
    	if (!cuentas.containsKey(codigo)) {
    		throw new IllegalArgumentException();
    	}
    	int value = cuentas.get(codigo);
    	return value;
    }

    /**
     *  Devuelve el histórico de transferencias entre dos cuentas.
     * @param primera Código válido de cuenta
     * @param segunda Código válido de cuenta
     * @return Lista de transferencias. El código <i> primera</i> siempre aparecerá como cuenta de origen de cada
     *          transferencia. En caso de el código de cuenta sea el mismo la lista estará vacía.
     * @throws <i>IllegalArgumentExcpetion</i> si alguno de los códigos de cuenta no son válidos.
     */
    public List<Transferencia> historico(String primera, String segunda) {
    	List<Transferencia> solucion = new LinkedList<>();
        if (primera == null || segunda == null || !desglose.containsKey(primera) || !desglose.containsKey(segunda)) {
        	throw new IllegalArgumentException();
        }
        if (primera.equals(segunda)) {
        	return solucion;
        }
        Iterator<Transferencia> iterador = desglose.get(primera).iterator();
        while (iterador.hasNext()) {
        	Transferencia elemento = iterador.next();
        	if (segunda.equals(elemento.origen) && primera.equals(elemento.destino)) {
        		elemento.invertir();
        		solucion.add(elemento);
        	} else if (primera.equals(elemento.origen) && segunda.equals(elemento.destino)) {
        		solucion.add(elemento);
        	}
        }
        return solucion;
    }

    //EXAMEN PRACTICO 20-06-2019
	public Map<String, Integer> balance(String origen) {
    	if (origen == null || !cuentas.containsKey(origen)) {
    		throw new IllegalArgumentException();
		}
    	Map<String, Integer> mapaBalances = new HashMap<>();
    	Iterator<Transferencia> iterador = desglose.get(origen).iterator();
    	while (iterador.hasNext()) {
    		Transferencia elemento = iterador.next();
    		if (elemento.origen.equals(origen) && elemento.cantidad != 0) {
    			mapaBalances.put(elemento.destino, elemento.cantidad);
			} else if (elemento.destino.equals(origen) && elemento.cantidad != 0) {
    			mapaBalances.put(elemento.origen, elemento.cantidad);
			}
    	}
		return mapaBalances;
	}

    public String toString() {
        StringBuilder bf = new StringBuilder("Banco - cuentas {\n") ;

        for (String cod: cuentas.keySet())
            bf.append("  " + cod + ": " +cuentas.get(cod)+ "\n");


        bf.append("} - size:" + cuentas.size());

        return bf.toString();
    }

    public String toStringDesglose() {
        StringBuilder bf = new StringBuilder("Banco - desglose {\n");

        for (String cod: desglose.keySet())
            bf.append("  " + cod + ": " + desglose.get(cod));


        bf.append("}");
        return bf.toString();
    }

}
