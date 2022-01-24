package practica4;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class EDDoubleLinkedListTest {

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
        } else {
            List<T> aux = new LinkedList<>();
            resultado.add(aux);
        }
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

    private String[][] vSemillasResverseTest = {{"A"}, {"B", "C"}, {"X, Y"}, {"D", "E", "F"}};

    static private List<String> invierteLista(List<String> l) {
        List<String> resultado = new LinkedList<>();
        for (String elem: l)
            resultado.add(0,elem);

        return resultado;
    }

    @org.junit.Test
    public void reverseTest () {
        List<List<String>> aux = convertirMatriz(vSemillasResverseTest);

        List<List<List<String>>> permutaciones = permutacionesCompletas(aux);

        int cuenta = 0;
        for (List<List<String>> perm: permutaciones) {
            List<String> caso = aplanar(perm);
            List<String> esperado = invierteLista(caso);

            for(int i = 0; i <= caso.size(); i++) {
                EDDoubleLinkedList<String> actual = new EDDoubleLinkedList<>(caso);

                System.out.println("\nPRUEBA " + cuenta);
                System.out.println("ESTADO INCIAL DE LA LISTA");
                System.out.println("  " + actual);
                System.out.println("ESTADO FINAL ESPERADO");
                System.out.println("  " + esperado + ": " + esperado.size());

                actual.reverse();
                System.out.println("ESTADO FINAL OBTENIDO:");
                System.out.println("  " + actual);

                //boolean iguales = Arrays.equals(esperado.toArray(), actual.toArray());
                assertTrue(Arrays.equals(esperado.toArray(), actual.toArray()));
                cuenta++;
            }
        }

        System.out.println("Se han probado " + cuenta + " casos\nFIN");
    }

    static private String[][] v1SemillasShuffleTest = {{"A"}, {"B", "C"}, {"D", "E", "F"}};
    static private String[]   v2SemillasShuffleTest = {"a", "b", "c", "d"};

    static private <T> List<T> calcularRessultadShuffle(List<T> l1, List<T> l2) {
        List<T> resultado = new ArrayList<>();

        int i1 = 0, i2 = 0;

        for (int j = 0; j < (l1.size() + l2.size()); j++)
            if (j%2 == 0)
                if (i1 < l1.size()) {
                    resultado.add(l1.get(i1));
                    i1++;
                } else {
                    resultado.add(l2.get(i2));
                    i2++;
                }
            else
                if (i2 < l2.size()) {
                    resultado.add(l2.get(i2));
                    i2++;
                } else {
                    resultado.add(l1.get(i1));
                    i1++;
                }


        return resultado;
    }

    @org.junit.Test
    public void shuffleTest() {
        List<List<String>> aux1 = convertirMatriz(v1SemillasShuffleTest);
        List<List<List<String>>> permutaciones1 = permutacionesCompletas(aux1);

        List<List<String>> permutaciones2 = permutacionesCompletas(Arrays.asList(v2SemillasShuffleTest));

        int cuenta = 1;
        for (List<List<String>> perm1: permutaciones1)
            for (List<String> caso1: permutaciones2 ) {
                List<String> caso2 = aplanar(perm1);

                EDDoubleLinkedList<String> actual = new EDDoubleLinkedList<>(caso1);

                System.out.println("\nPRUEBA " + cuenta);
                System.out.println("ESTADO INCIAL");
                System.out.println("  this:" + actual);
                System.out.println("  otra:" + caso2);
                System.out.println("  this.shuffle(otra)");

                List<String> esperado = calcularRessultadShuffle(caso1, caso2);
                System.out.println("ESTADO FINAL ESPERADO");
                System.out.println("  " + esperado + ": " + esperado.size());

                actual.shuffle(caso2);
                System.out.println("ESTADO FINAL OBTENIDO:");
                System.out.println("  " + actual);

                assertTrue(Arrays.equals(esperado.toArray(), actual.toArray()));

                cuenta++;
            }


    }

}
