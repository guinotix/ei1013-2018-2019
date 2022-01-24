package practica2;

import org.junit.Assert;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static practica2.practica2.ordenar;

public class practica2Test {

    static private String [][] vEquivalentesCasos = {{}, {"A"}, {"A", "B"}, {"A", "A", "B"}, {"A", "B", "B"}, {"A", "A", "B", "B"}, {"A", "B", "C", "D"}};


    static private <T> List<List<T>> permutaciones(List<T> vec) {
        List<List<T>> resultado = new LinkedList<>();

        if (vec.size() > 0) {
            List<T> aux = new LinkedList<>();
            aux.add(vec.get(0));
            resultado.add(aux);

            for (int i = 1; i < vec.size(); i++) {
                while (resultado.get(0).size() == i) {
                    for (int k = 0; k <= resultado.get(0).size(); k++) {
                        aux = new LinkedList<>(resultado.get(0));
                        aux.add(k, vec.get(i));
                        resultado.add(aux);
                    }
                    resultado.remove(0);
                }
            }
        } else
            resultado.add(new LinkedList<>());
        return resultado;
    }

    static private boolean[] crearMascara(int talla){
        return new boolean[talla];
    }

    static private boolean incrementaMascara(boolean mascara[]) {
        boolean propagar = false;
        int pos = 0;
        do {
            if (mascara[pos] == true) {
                mascara[pos] = false;
                propagar = true;
            } else {
                mascara[pos] = true;
                propagar = false;
            }
            pos++;
        } while (propagar && (pos < mascara.length));

        return (!propagar || pos != mascara.length);
    }

    static private <T> List<List<T>> todasSublistas(List<T> semilla) {
        List<List<T>> resultado = new LinkedList<>();

        boolean mascara[] = crearMascara(semilla.size());

        do {
            List<T> aux = new LinkedList<>();
            for(int i= 0; i < mascara.length; i++) {
                if (mascara[i])
                    aux.add(semilla.get(i));
            }
            resultado.add(aux);

        } while(incrementaMascara(mascara));

        return resultado;

    }

    static private <T> List<List<T>> permutacionesCompletas(List<T> semilla){
        List<List<T>> resultado = new LinkedList<>();

        List<List<T>> sublistas = todasSublistas(semilla);
        for(List<T> sub: sublistas)
            resultado.addAll(permutaciones(sub));


        return resultado;
    }

    static private <T> List<T> aplanar(List<List<T>> listas) {
        List<T> resultado = new LinkedList<>();

        for(List<T> l: listas)
            resultado.addAll(l);

        return resultado;
    }

    static private <T> List<List<T>> convertirMatriz(T[][] vec)
    {
        List<List<T>> resultado = new LinkedList<>();
        for (T[] elem: vec)
            resultado.add(Arrays.asList(elem));

        return resultado;
    }


    @org.junit.Test
    public void equivalentesTest() {
        int cuenta = 0;
        for (int i = 0; i < vEquivalentesCasos.length; i++)
            for (int j = i; j < vEquivalentesCasos.length; j++) {
                List<List<String>> casoA;
                List<List<String>> casoB;

                casoA = permutaciones(Arrays.asList(vEquivalentesCasos[i]));
                if (i == j)
                    casoB = casoA;
                else
                    casoB = permutaciones(Arrays.asList(vEquivalentesCasos[j]));

                for (List<String> ca : casoA)
                    for (List<String> cb : casoB) {
                        System.out.println("\nPrueba " + cuenta);
                        System.out.println("Entrada");
                        System.out.println("  l1 : " + ca);
                        System.out.println("  l2 : " + cb);
                        System.out.println("Salida esperada");
                        if (i == j)
                            System.out.println("  true");
                        else
                            System.out.println("  false");

                        boolean resultado = practica2.equivalentes(ca, cb);
                        System.out.println("Salida");
                        System.out.println("  " + resultado);
                        if (i == j)
                            Assert.assertTrue(resultado);
                        else
                            Assert.assertFalse(resultado);
                        cuenta++;
                    }
            }

        System.out.println("Se han probado " + cuenta + " casos\nFIN");
    }

    private String[][] vSemillasInvierteTest = {{"A"}, {"B", "C"}, {"D", "E", "F"}, {"A", "B", "D"}};

    static private List<String> invertirTest(List<String> l) {
        List<String> resultado = new LinkedList<>();
        for (String elem: l)
            resultado.add(0,elem);

        return resultado;
    }

    @org.junit.Test
    public void invierteTest () {
        List<List<String>> aux = convertirMatriz(vSemillasInvierteTest);

        List<List<List<String>>> permutaciones = permutacionesCompletas(aux);

        int cuenta = 0;
        for (List<List<String>> perm: permutaciones) {
            List<String> caso = aplanar(perm);
            List<String> esperado = invertirTest(caso);

            for(int i = 0; i <= caso.size(); i++) {
                List<String> actual = new LinkedList<>(caso);
                ListIterator<String> iter = actual.listIterator(i);

                System.out.println("\nPrueba " + cuenta);
                System.out.println("Entrada");
                System.out.println("  Iterador de  " + actual + " en posicion " + iter.nextIndex());
                System.out.println("Salida esperada");
                System.out.println("  " + esperado);

                practica2.invierte(iter);
                System.out.println("Salida:");
                System.out.println("  " + actual);

                assertEquals(esperado, actual);
                cuenta++;
            }
        }

        System.out.println("Se han probado " + cuenta + " casos\nFIN");
    }


    static private <T extends Comparable<T>> boolean estaOrdenado(List<T> l,  List<T> caso) {
        if (l.size() != caso.size())
            return false;

        if (l.size() < 2)
            return true;


        Iterator<T> iter = l.iterator();
        T previo = iter.next();
        while (iter.hasNext()) {
            T actual = iter.next();
            if (previo.compareTo(actual) > 0)
                return false;
            actual = previo;
        }

        List<T> ref = new LinkedList<>(caso);

        for (T e: l) {
            if (!ref.contains(e))
                return false;
            ref.remove(e);
        }

        return true;
    }

    static Integer[][] vSemillasOrdenarTest = {{0}, {1, 2, 3}, {4, 4, 5, 6}, {7, 8, 3} , {2, 10}};

    @org.junit.Test
    public void ordenarTest() {
        List<List<Integer>> aux = convertirMatriz(vSemillasOrdenarTest);
        List<List<List<Integer>>> permutaciones = permutacionesCompletas(aux);

        int cuenta = 0;
        for (List<List<Integer>> perm: permutaciones) {
            List<Integer> caso = aplanar(perm);

            System.out.println("\nPrueba " + cuenta);
            System.out.println("Entrada");
            System.out.println("  " + caso);

            List<Integer> resultado = ordenar(caso);
            System.out.println("Salida:");
            System.out.println("  " + resultado);

            boolean ok = estaOrdenado(resultado, caso);
            if (!ok)
                System.out.println("  El resultado no está ordenado");
            assertTrue(ok);
            cuenta++;
        }
        System.out.println("Se han probado " + cuenta + " casos\nFIN");

    }
}
