package practica10;

import org.junit.Test;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GraphTest {

    private static <T, W> Set<T> convertFromIndex(EDGraph<T, W> graph, Set<Integer> indexes) {
        Set<T> ret = new HashSet<>();

        for (int i : indexes)
            ret.add(graph.getNodeValue(i));

        return ret;
    }

    private static <T, W> boolean compareGraphs(EDGraph<T, W> first, EDGraph<T, W> second) {
        Map<T, Integer> map1 = first.getNodes();
        Map<T, Integer> map2 = second.getNodes();

        if (map1.size() != map2.size()) {
            System.out.println("Los grafos tienen distinto tamaño");
            return false;
        }

        if (!map1.keySet().equals(map2.keySet())) {
            System.out.println(" El contenido de los nodos es ditinto");
            System.out.println(" 1 -> " + map1.keySet());
            System.out.println(" 2 -> " + map1.keySet());
            return false;
        }
        for (T item : map1.keySet()) {
            Set<T> val1 = convertFromIndex(first, first.getOutgoing(map1.get(item)));
            Set<T> val2 = convertFromIndex(second, first.getOutgoing(map2.get(item)));
            if (!val1.equals(val2)) {
                System.out.println("Los arcos de nodo " + item + " son distintos");
                System.out.println("1 -> " + val1);
                System.out.println("2 -> " + val2);
                return false;
            }
        }

        return true;
    }

    static String path = "src/practica10/ficheros/";

    private static String[] testFiles = {"laberinto1.txt", "laberinto2.txt", "laberinto3.txt", "laberinto4.txt",
            "laberinto5.txt", "laberinto6.txt", "grafoDir1.txt", "grafoDir2.txt", "grafoDir3.txt", "grafoDir4.txt"};

    @Test
    public void EDListGraphTest() {
        for (String file : testFiles) {
            System.out.println("\nPRUEBA CON FICHERO " + file + "\n");
            System.out.println("GRAFO CON MATRIZ DE ADYACENCIA");
            EDMatrixGraph<String, Object> caso = new EDMatrixGraph<>(path + file);
            caso.printGraphStructure();

            System.out.println("\nGRAFO CON LISTAS DE ADYACENCIA");
            EDListGraph<String, Object> esperado = new EDListGraph<>(caso);
            esperado.printGraphStructure();

            System.out.println("\nCOMPARANDO GRAFOS...");
            assertTrue(compareGraphs(caso, esperado));
            System.out.print("  OK");
        }
    }

    private <T> Set<T> exitDegree(EDGraph<T, ?> graph) {
        Map<T, Integer> map = graph.getNodes();
        Set<Integer> first = new HashSet<>();
        Set<Integer> some = new HashSet<>();

        for (T item : map.keySet()) {
            Set<Integer> out = graph.getOutgoing(map.get(item));
            first.removeAll(out);
            Set<Integer> aux = new HashSet<>(out);
            aux.removeAll(some);
            first.addAll(aux);
            some.addAll(out);
        }
        Set<T> ret = new HashSet<>();
        for (int id : first)
            ret.add(graph.getNodeValue(id));

        return ret;
    }

    @Test
    public void degree1ListTest() {
        for (String file : testFiles) {
            System.out.println("\nPRUEBA CON FICHERO " + file + "\n");
            EDMatrixGraph<String, Object> temp = new EDMatrixGraph<>(path + file);
            EDListGraph<String, Object> caso = new EDListGraph<>(temp);

            caso.printGraphStructure();

            System.out.println("degree1()");
            System.out.println("RESULTADO ESPERADO");
            Set<String> esperado = exitDegree(caso);
            System.out.println(esperado);

            System.out.println("RESULTADO OBTENIDO");
            Set<String> obtenido = caso.degree1();
            System.out.println(obtenido);

            assertEquals(esperado, obtenido);
        }
    }

    @Test
    public void degree1MatrixTest() {
        for (String file : testFiles) {
            System.out.println("\nPRUEBA CON FICHERO " + file + "\n");
            EDMatrixGraph<String, Object> caso = new EDMatrixGraph<>(path + file);
            caso.printGraphStructure();

            System.out.println("degree1()");
            System.out.println("RESULTADO ESPERADO");
            Set<String> esperado = exitDegree(caso);
            System.out.println(esperado);

            System.out.println("RESULTADO OBTENIDO");
            Set<String> obtenido = caso.degree1();
            System.out.println(obtenido);

            assertEquals(esperado, obtenido);
        }
    }

    private <T> boolean checkPath(EDGraph<T, ?> g, List<T> path, T first, T last ) {
        if (path.size() <= 1)
            return false;

        Iterator<T> iter = path.iterator();
        T source = iter.next();

        if (g.getNodeIndex(source) == -1) {
            System.out.println("El nodo " + source + " no esta en el grafo");
            return false;
        }

        while (iter.hasNext()) {
            T target = iter.next();
            if (g.getNodeIndex(target) == -1) {
                System.out.println("El nodo " + target + " no esta en el grafo");
                return false;
            }

            if (g.getEdge(g.getNodeIndex(source), g.getNodeIndex(target)) == null){
                System.out.println("No existe el arco entre "+  source + " y " + target);
                return false;
            }

            source = target;
         }


        if (!first.equals (path.get(0))) {
            System.out.println("El primer nodo del camino no es correcto, es " + first +
                    " y debería ser "+ path.get(0));
            return false;
        }

        if (!last.equals (path.get(path.size()-1))) {
            System.out.println("El ultimo nodo del camino no es correcto, es " + last +
                    " y debería ser "+ path.get(path.size()-1));
            return false;
        }

        return true;
    }

    @Test
    public void findPathTest() {
        for (String file : testFiles) {
            System.out.println("\nPRUEBA CON FICHERO " + file + "\n");
            EDMatrixGraph<String, Object> matrix = new EDMatrixGraph<>(path + file);
            EDListGraph<String, Object> caso = new EDListGraph<>(matrix);
            caso.printGraphStructure();

            System.out.println("findPath(\"A\", \"S\")");
            System.out.println("RESULTADO OBTENIDO");
            int s = caso.getNodeIndex("A");
            int d = caso.getNodeIndex("S");
            System.out.println("(" + s + ", " + d + ")");
            List<String> camino = caso.findPath(s, d);
            System.out.println(camino);

            assertTrue(checkPath(caso, camino, "A", "S"));
        }
    }
}
