package practica1;

import java.util.*;

import static junit.framework.TestCase.assertEquals;

@SuppressWarnings("all")
public class practica1Test {

    private String[] vSeparaTestRepes= {"cero", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};
    private String[] vSeparaTestUnicos = {"CERO", "UNO", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE"};
    private int[][] vSeparaTestPares= {{0,0}, {0, 5}, {1, 4}, {2, 3}, {3, 2}, {4, 1}, {5, 0}, {5, 5}};

    private void preparaSeparaCaso(int repes, int par, List<Set<String>> conjuntos) {
        for (Set<String> s: conjuntos)
            s.clear();

        for (int r = 0; r < repes; r++) {
            String s = vSeparaTestRepes[r];
            conjuntos.get(0).add(s);
            conjuntos.get(1).add(s);
            conjuntos.get(3).add(s);
        }

        for (int u = 0; u < vSeparaTestPares[par][0] ; u++) {
            String s = vSeparaTestUnicos[u];
            conjuntos.get(0).add(s);
            conjuntos.get(2).add(s);

        }

        for(int u = 0; u < vSeparaTestPares[par][1]; u++) {
            String s = vSeparaTestUnicos[vSeparaTestUnicos.length-u-1];
            conjuntos.get(1).add(s);
            conjuntos.get(2).add(s);
        }
    }

    @org.junit.Test
    public void separaTest() {
        List<Set<String>> conjuntos = new ArrayList<Set<String>>();
        for (int i = 0; i < 4; i++)
            conjuntos.add(new HashSet<String>());

        for (int repes = 0; repes < vSeparaTestRepes.length; repes++) {
            for (int pares = 0; pares < vSeparaTestPares.length; pares++) {
                preparaSeparaCaso(repes, pares, conjuntos);

                //System.out.println("\nCaso - repetidos: " + repes + " unicos: "
                //        + vSeparaTestPares[pares][0] + ", " + vSeparaTestPares[pares][1]);
                System.out.println("Entrada");
                System.out.println("  unicos   : " + conjuntos.get(0));
                System.out.println("  repetidos: " + conjuntos.get(1));
                System.out.println("Salida esperada");
                System.out.println("  unicos   : " + conjuntos.get(2));
                System.out.println("  repetidos: " + conjuntos.get(3));

                practica1.separa(conjuntos.get(0), conjuntos.get(1));

                System.out.println("Salida");
                System.out.println("  unicos   : " + conjuntos.get(0));
                System.out.println("  repeticos: " + conjuntos.get(1));

                assertEquals(conjuntos.get(0), conjuntos.get(2));
                assertEquals(conjuntos.get(1), conjuntos.get(3));
                System.out.println("-----------------------");

            }
        }

    }

    private int[][] vFiltraSemillas = {{}, {0}, {1}, {2, 3},  {3, 2, 5}, {11, 5, 3, 7}};
    private int[][] vFiltraMultiplicador = {{0}, {1}, {2, 3}, {1, -2, 5}, {5, 3, 6, 4, -5, 9, 2, -9, 7}};

    private void preparaFiltraCaso(int[] semilla, int[] multiplicador, Collection<Integer> col, Set<Integer> resultado) {
        col.clear();
        resultado.clear();

        for (int s: semilla) {
            for (int m: multiplicador)
                col.add(s*m);
            col.add(s);
            if (s != 0)
                resultado.add(s);
        }
    }
    @org.junit.Test
    public void filtraTest() {
        Collection<Integer> caso = new LinkedList<Integer>();
        Set<Integer> resultado;
        Set<Integer> esperado = new HashSet<Integer>();

        for (int[] semilla: vFiltraSemillas)
            for(int[] multiplicador: vFiltraMultiplicador) {
                preparaFiltraCaso(semilla, multiplicador, caso, esperado);

                //System.out.println("\n Caso - semilla: " + Arrays.toString(semilla) + " multiplicador: " + Arrays.toString(multiplicador));
                System.out.println("Entrada: ");
                System.out.println("  Iterador de : " + caso);
                System.out.println("Salida esperada:");
                System.out.println("  Set: " + esperado);

                resultado = practica1.filtra(caso.iterator());

                System.out.println("Salida");
                System.out.println("  Set: " + resultado);

                assertEquals("filtra: RESPUESTA INCORRECTA", esperado, resultado);
                System.out.println("--------------------");
            }

    }

    private Collection<Set<String>> preparaRepetidosCaso(int conjuntos, int repetidos, int nelementos, Set<String> esperado) {
        List<Set<String>> caso = new ArrayList<>();
        esperado.clear();

        for (int i = 0; i < conjuntos; i++)
            caso.add(new HashSet<String>());

        if (conjuntos > 2) {
            int c = 0;
            for (int i = 0; i < repetidos; i++) {
                String rep = String.valueOf(i+2);
                int j = i+2;
                while (j > 0) {
                    caso.get(c).add(rep);
                    j--;
                    c = (c + 1) % caso.size();
                }
                esperado.add(rep);
            }
        }

        int aux = repetidos + 10;
        for(Set<String> s: caso) {
            for (int i = 0; i < nelementos; i++, aux++)
                s.add(String.valueOf(aux));
        }

        return caso;
    }

    private int[][] cRepetidosCasos =
            { {0, 0, 0}, {1, 0, 1}, {3, 0, 0}, {3, 1, 0}, {3, 1, 3},
                    {3, 2, 0}, {3, 2, 3}, {3, 3, 3}, {5, 1, 0}, {5, 2, 2},
                    {5, 3, 1}, {5, 5, 4}};

    @org.junit.Test
    public void repetidosTest() {
        Collection<Set<String>> caso;
        Set<String> esperado = new HashSet<>();

        for (int[] par: cRepetidosCasos) {
            caso = preparaRepetidosCaso(par[0], par[1], par[2], esperado);

            System.out.println("\nCaso - conjuntos " + par[0] + " repetidos " + par[1] + " elementos " + par[2]);
            System.out.println("Entrada");
            System.out.println("  col: " + caso);
            System.out.println("Salida esperada:");
            System.out.println("  set: " + esperado);

            Set<String> resultado = practica1.repetidos(caso);

            System.out.println("Salida:");
            System.out.println("  set: " + resultado);
            assertEquals("repetidos: RESPUESTAS INCORRECTA", esperado, resultado);
            System.out.println("----------------");
        }
    }
}