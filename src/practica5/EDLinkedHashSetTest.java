package practica5;

import java.util.*;

import org.junit.Assert;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class EDLinkedHashSetTest {

    static private String vChar = "abcedefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String seleccionaCaracteres(String s, int[] filtro) {
        StringBuilder bf = new StringBuilder();

        for(int i: filtro) {
            i = i % s.length();
            bf.append(s.charAt(i));
        }
        return bf.toString();
    }

    private List<String> generateStrings(int nElem) {
        Set<String> set = new HashSet<>();

        int[] actual = {0,1,2};
        int i = 0;
        while (set.size() < nElem) {
            String s = seleccionaCaracteres(vChar, actual);
            set.add(s);

            if (actual[0] == vChar.length()) {
                actual[0] = 0;
                actual[1] = i;
                actual[2] = 2*i;
            } else
            {
                actual[0]++;
                actual[1]++;
                actual[2]++;
            }
            i++;
        }

        return new LinkedList<>(set);
    }

    private <T> void checkTable(EDLinkedHashSet<T> s)
    {
        if (s.table.length != s.used.length) {
            System.out.println("  table[] y used[] tienen distinto tamaño (" + s.table.length + ", " + s.used.length +
                    ")");
            assertEquals(s.table.length, s.used.length);
        }

        int count=0;
        for(int i=0; i < s.table.length; i++) {
            if (s.table[i]!= null && !s.used[i]) {
                System.out.println("  table[ " + i + "] ocupado pero used[" + i + "] == false");
                assertFalse(s.table[i] != null && !s.used[i]);
            }
            if (s.used[i]) count++;
        }

        if (count != s.dirty) {
            System.out.println("  La cuenta de casillas usadas es incorrecta, debería ser " + count + " y es " + s.dirty);
            assertEquals(count, s.dirty);
        }

        if (s.dirty >= s.rehashThreshold) {
            System.out.println("  La ocpuación de la tabla (" + s.dirty + ") es mayor que el umbral de rehash (" + s.rehashThreshold +")");
        }
        assertTrue(s.dirty < s.rehashThreshold);

    }

    private <T> void checkElementsTable(EDLinkedHashSet<T> s) {
        for (int i = 0; i < s.table.length; i++) {
            if (s.used[i] && s.table[i] != null) {
                int code = s.hash(s.table[i].data);

                while (code != i) {
                    if (!s.used[code]) {
                        System.out.println("  El elemento " + s.table[i].data + " almacenado en table[" + i + "] no " +
                                "esta" + " almacenado en una posición alcanzable desde " + s.hash(s.table[i].data) + "por que " +
                                "table[" + code + "] en false");
                        Assert.fail();
                    }
                    code = (code + 1) % s.table.length;
                }
            }
        }
    }

    private <T> void checkOrderTable(EDLinkedHashSet<T> s, List<T> l) {
        T[] elements = (T[])s.toArray();

        System.out.println(  " Orden de los elementos " + Arrays.toString(elements) + ": " + elements.length);
        if (elements.length != l.size()) {
            System.out.println("  Los nodos no están bien enlazados, faltan elementos");
            Assert.fail();
        }

        for(int i=0; i < elements.length; i++) {
            if (!l.get(i).equals(elements[i])) {
                System.out.println("  El conjunto no mantiene el orden de insercíon, el elemento " + elements[i] + " " +
                        "en la posición " + i + " esta fuera de lugar");
                Assert.fail();
            }
        }
    }

    @org.junit.Test
    public void removeTest() {
        System.out.println("\nPROBANDO EL METODO remove()...");
        EDLinkedHashSet<String> set = new EDLinkedHashSet<>();

        int paquete = set.rehashThreshold - 1;
        List<String> lista = generateStrings(paquete*50);

        int cuenta = 1;
        for (int i = 0; i < 50; i++) {
            List<String> caso = new LinkedList<>(lista.subList(paquete * i, paquete * (i + 1)));
            set = new EDLinkedHashSet<>(caso);

            while (!caso.isEmpty()) {
                System.out.println("\nPRUEBA " + cuenta);
                System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
                System.out.println(set);
                String elem = caso.get((cuenta * 13) % caso.size());
                System.out.println("\nremove(" + elem + ")");
                caso.remove(elem);

                System.out.println("\nORDEN FINAL ESPERADO");
                System.out.println(caso + ": " + caso.size());

                System.out.println("\nESTADO FINAL OBTENIDO");
                set.remove(elem);
                System.out.println(set);

                checkTable(set);
                checkElementsTable(set);
                checkOrderTable(set, caso);

                cuenta++;
            }
        }
    }

    @org.junit.Test
    public void removeRehash() {
        System.out.println("\nPROBANDO EL METODO rehash()...");
        EDLinkedHashSet<String> set = new EDLinkedHashSet<>();

        int paquete = set.rehashThreshold - 1;
        List<String> lista = generateStrings(paquete*90);

        int cuenta = 1;
        for (int i = 0; i < 30; i++) {
            List<String> caso = new LinkedList<>(lista.subList(paquete * i, paquete * (i + 3)));
            set = new EDLinkedHashSet<>();

            System.out.println("\nPRUEBA " + cuenta);
            System.out.println("\nESTADO ACTUAL DEL EDLinkedHashSet");
            System.out.println(set);

            System.out.println("addAll(" + caso + ")");
            set.addAll(caso);

            System.out.println("\nESTADO FINAL OBTENIDO");
            System.out.println(set);

            checkTable(set);
            checkElementsTable(set);
            checkOrderTable(set, caso);

            cuenta++;

        }
    }

}
