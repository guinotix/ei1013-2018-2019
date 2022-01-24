package practica9;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


public class HeapsTest {
    private <T> boolean checkHeap(EDPriorityQueue<T> heap, boolean isMin) {
        for (int i = 0; i < heap.size / 2; i++) {
            int hi = i * 2 + 1;
            int hd = i * 2 + 2;

            if (isMin && hi < heap.size && heap.compare(heap.data[i], heap.data[hi]) > 0)
                return false;

            if (isMin && hd < heap.size && heap.compare(heap.data[i], heap.data[hd]) > 0)
                return false;

            if (!isMin && hi < heap.size && heap.compare(heap.data[i], heap.data[hi]) < 0)
                return false;

            if (!isMin && hd < heap.size && heap.compare(heap.data[i], heap.data[hd]) < 0)
                return false;
        }

        return true;
    }

    static <T> List<List<T>> permutaciones(List<T> list, int limite) {
        List<List<T>> resultado = new ArrayList<>();

        int[] factoriales = new int[list.size() + 1];
        factoriales[0] = 1;
        for (int i = 1; i <= list.size(); i++) {
            factoriales[i] = factoriales[i - 1] * i;
        }

        if (limite < 0 || limite > factoriales[list.size()])
            limite = factoriales[list.size()];

        for (int i = 0; i < limite; i++) {
            List<T> caso = new ArrayList<>();
            List<T> aux = new LinkedList<>(list);

            int positionCode = i;
            for (int position = aux.size(); position > 0; position--) {
                int selected = positionCode / factoriales[position - 1];
                caso.add(aux.get(selected));
                positionCode = positionCode % factoriales[position - 1];
                aux.remove(selected);
            }

            if (i < limite)
                resultado.add(caso);
            else break;

        }

        return resultado;
    }


    static private boolean[] crearMascara(int talla) {
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
            for (int i = 0; i < mascara.length; i++) {
                if (mascara[i])
                    aux.add(semilla.get(i));
            }
            resultado.add(aux);

        } while (incrementaMascara(mascara));

        return resultado;

    }

    static private <T> List<List<T>> permutacionesCompletas(List<T> semilla) {
        List<List<T>> resultado = new LinkedList<>();

        List<List<T>> sublistas = todasSublistas(semilla);
        for (List<T> sub : sublistas)
            resultado.addAll(permutaciones(sub, -1));


        return resultado;
    }

    static private <T> List<T> aplanar(List<List<T>> listas) {
        List<T> resultado = new LinkedList<>();

        for (List<T> l : listas)
            resultado.addAll(l);

        return resultado;
    }

    static private <T> List<List<T>> convertirMatriz(T[][] vec) {
        List<List<T>> resultado = new LinkedList<>();
        for (T[] elem : vec)
            resultado.add(Arrays.asList(elem));

        return resultado;
    }

    static private Integer[][] vSemillas = {{3, 5, 9, 10, 12}, {0, 4, 7}, {11, 15}, {0, 2,}, {6, 14, 20}};

    private static <T> List<List<T>> generarCasos(T[][] semillas) {
        List<List<T>> resultado = new ArrayList<>();
        List<List<T>> aux = convertirMatriz(semillas);

        List<List<List<T>>> permutaciones = permutacionesCompletas(aux);

        for (List<List<T>> perm : permutaciones)
            resultado.add(aplanar(perm));

        return resultado;
    }

    @Test
    public void removeTest() {
        List<List<Integer>> casos = generarCasos(vSemillas);

        int cuenta = 1;
        for (List<Integer> caso : casos) {
            EDPriorityQueue<Integer> heap = new EDPriorityQueue(caso);

            for (Integer i : caso) {
                int s = heap.size();
                System.out.println("\nPRUEBA " + cuenta);
                System.out.println(heap);
                System.out.println("remove(" + i + ")");
                Integer retorno = heap.remove(i);
                System.out.println("RESULTADO OBTENIDO");
                System.out.println(heap);
                s--;
                if (s != heap.size) {
                    System.out.println("El tamaño de la cola no es correto");
                    Assert.fail();
                }

                if (!checkHeap(heap, true)) {
                    System.out.println("La cola no es un montículo a mínimos");
                    Assert.fail();
                }
                cuenta++;
            }
        }
    }

    @Test
    public void maxHeapfyTest() {
        List<List<Integer>> casos = generarCasos(vSemillas);

        int cuenta = 1;
        for (List<Integer> caso : casos) {
            System.out.println("\nPRUEBA " + cuenta);
            EDPriorityQueue<Integer> heap = new EDPriorityQueue(caso);
            int s = heap.size();
            System.out.println(heap);
            System.out.println("RESULTADO OBTENIDO");
            heap.maxHeapify();
            System.out.println(heap);


            if (s != heap.size) {
                System.out.println("El tamaño de la cola no es correto");
                Assert.fail();
            }

            if (!checkHeap(heap, false)) {
                System.out.println("La cola no es un montículo a máximos");
                Assert.fail();
            }
            cuenta++;
        }
    }

    class InverseComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    }

    @Test
    public void typeOfHeapTest() {
        List<List<Integer>> casos = generarCasos(vSemillas);

        int cuenta = 1;
        for (List<Integer> caso : casos) {
            System.out.println("\nPRUEBA " + cuenta);
            EDPriorityQueue<Integer> minHeap = new EDPriorityQueue<>(caso);
            System.out.println(minHeap);
            int esperado = 0;
            if (caso.size() != 0)
                esperado = -1;

            System.out.println("RESULTADO ESPERADO:\n  " + esperado);

            int obtenido = minHeap.typeOfHeap();
            System.out.println("RESULTADO OBTENIDO");
            System.out.println("  " + obtenido);
            assertEquals(esperado, obtenido);

            cuenta++;

            if (esperado == 0)
                continue;

            System.out.println("\nPRUEBA " + cuenta);
            EDPriorityQueue<Integer> maxHeap = new EDPriorityQueue<Integer>(caso, new InverseComparator());
            maxHeap.comparator = null;
            System.out.println(maxHeap);
            esperado = 1;

            System.out.println("RESULTADO ESPERADO:\n  " + esperado);

            obtenido = maxHeap.typeOfHeap();
            System.out.println("RESULTADO OBTENIDO");
            System.out.println("  " + obtenido);
            assertEquals(esperado, obtenido);

            cuenta++;
        }
    }

    private int[] toArrary(List<Integer> l) {
        int resultado[] = new int[l.size()];

        for (int i = 0; i < l.size(); i++)
            resultado[i] = l.get(i);

        return resultado;
    }

    private boolean estaOrdenado(int v[]) {
        for (int i = 0; i < v.length-1; i++)
            if (v[i] > v[i + 1]) return false;

        return true;
    }



    @Test
    public void heapsortTest() {
        List<List<Integer>> casos = generarCasos(vSemillas);

        int cuenta = 1;
        for (List<Integer> caso : casos) {
            System.out.println("\nPRUEBA " + cuenta);
            int v[] = toArrary(caso);
            System.out.println("Vector: " + Arrays.toString(v));

            System.out.println("RESULTADO OBTENIDO");
            Heapsort.heapsort(v);

            System.out.println("Vector: " + Arrays.toString(v));
            if (!estaOrdenado(v)){
                System.out.println("El vector no esta ordenado");
                Assert.fail();
            }

            cuenta++;
        }
    }
}
