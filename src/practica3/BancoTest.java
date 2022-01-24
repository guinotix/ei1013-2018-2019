package practica3;


import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BancoTest {


    static private void crearCuentas(int num, int modo, List<String> codigos, List<Integer> saldos) {
        // modos: 0x00 -> l; 0x01 -> l + l; 0x10 l + (-l); 0x11 -> l + (-l) + l

        codigos.clear();
        saldos.clear();

        for (int i = 0; i < num ; i++) {
            codigos.add(String.format("C%d", i));
            saldos.add(i * 10 + 1);
        }

        int s;
        switch (modo) {
            case 0x01:
                replicarLista(codigos, 1);
                replicarLista(saldos, 1 );
                break;
            case 0x10:
                s = saldos.size();
                replicarLista(codigos, 1);
                replicarLista(codigos, 1);
                negativizarSaldos(saldos, s, saldos.size());
                break;
            case 0x11:
                s = saldos.size();
                replicarLista(codigos, 2);
                replicarLista(saldos, 2);
                negativizarSaldos(saldos, s, s*2);

        }


    }

    static private <T> void replicarLista(List<T> l, int rep) {
        int s = l.size();
        for (int r = 0; r < rep; r++)
            for(int i = 0; i < s; i++)
                l.add(l.get(i));
    }
    static private void negativizarSaldos(List<Integer> saldos, int pos0, int pos1) {
        for (int i = pos0; i < pos1;i ++)
            saldos.set(i, saldos.get(i)*-1);
    }

    static private<T> void anullarLista(List<T> cuentas) {
        for (int i = 1; i < cuentas.size(); i += 3)
            cuentas.set(i, null);
    }

    static private void comprimirCuentas(List<String> codigos, List<Integer> saldos) {
        Map<String, Integer> map = new HashMap<>();

        ListIterator<String> iter = codigos.listIterator();

        while (iter.hasNext()) {
            String cod = iter.next();
            if (!map.containsKey(cod))
                map.put(cod, iter.previousIndex());
            else {
                int sal = saldos.get(map.get(cod)) + saldos.get(iter.previousIndex());
                saldos.set(map.get(cod), sal);
                saldos.remove(iter.previousIndex());
                iter.remove();
            }
        }
    }

    static private <T> String toStringList(List<T> l) {
        final StringBuilder sb = new StringBuilder();

        sb.append("[");
        Iterator<T> iter = l.iterator();
        while (iter.hasNext()) {
            T elem = iter.next();
            sb.append(elem);
            if (iter.hasNext())
                sb.append(", ");
        }

        sb.append("] - size: ");
        sb.append(l.size());

        return sb.toString();
    }


    static private void comprobarConstruccion(Banco banco, List<String> codigos, List<Integer> saldos) {
        comprimirCuentas(codigos, saldos);

        assertEquals(banco.cuentas.size(), codigos.size());

        for (int i = 0; i < codigos.size(); i++) {
            assertTrue(banco.cuentas.containsKey(codigos.get(i)));
            assertEquals(banco.cuentas.get(codigos.get(i)),saldos.get(i));
        }
    }


    static private int NUMERO_CUENTAS = 10;
    @org.junit.Test
    public void BancoTest() {
        System.out.println("PROBANDO EL CONSTRUCTOR");

        List<String> codigos = new LinkedList<>();
        List<Integer> saldos = new LinkedList<>();

        int cuenta = 0;

        for (int i = 2; i <= NUMERO_CUENTAS; i++) {
            System.out.println("\nPRUEBA " + cuenta);
            crearCuentas(i, 0x00, codigos, saldos);

            System.out.println("ENTRADA");
            System.out.println("  codigos " + toStringList(codigos));
            System.out.println("  saldos  " + toStringList(saldos));

            Banco banco = new Banco(codigos, saldos);

            System.out.println("RESULTADO ");
            System.out.println(banco);

            comprobarConstruccion(banco, codigos, saldos);
            cuenta++;
        }

        for (int i = 2; i <= NUMERO_CUENTAS; i++) {
            System.out.println("\nPRUEBA " + cuenta);
            crearCuentas(i, 0x11, codigos, saldos);

            System.out.println("ENTRADA");
            System.out.println("  codigos " + toStringList(codigos));
            System.out.println("  saldos  " + toStringList(saldos));

            Banco banco = new Banco(codigos, saldos);

            System.out.println("RESULTADO ");
            System.out.println(banco);

            comprobarConstruccion(banco, codigos, saldos);
            cuenta++;
        }

        System.out.println(cuenta + " PRUEBAS REALIZADAS CON EXITO");
    }

    @org.junit.Test
    public void BancoExcepccionesTest() {
        System.out.println("PROBANDO LAS EXCEPCIONES DEL CONSTRUCTOR");

        List<String> codigos = new LinkedList<>();
        List<Integer> saldos = new LinkedList<>();

        int cuenta = 1;
        try {
            System.out.println("\nPRUEBA " +  cuenta);
            System.out.println("ENTRADA");
            System.out.println("  codigos null") ;
            System.out.println("  saldos  " + toStringList(saldos));

            Banco banco = new Banco(null, saldos);

            System.out.println("Deberia haberse producido una excepcion IllegalArgumentExcpetion");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.toString() + " ha sido lanzada correctamente");
        }
        cuenta++;

        try {
            System.out.println("\nPRUEBA " +  cuenta);
            System.out.println("ENTRADA");
            System.out.println("  codigos " + toStringList(codigos)) ;
            System.out.println("  saldos  null" );

            Banco banco = new Banco(null, saldos);

            System.out.println("Deberia haberse producido una excepcion IllegalArgumentExcpetion");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.toString() + " ha sido lanzada correctamente");
        }
        cuenta++;

        try {
            System.out.println("\nPRUEBA " +  cuenta);
            System.out.println("ENTRADA");
            crearCuentas(5, 0x00, codigos, saldos);
            saldos.remove(0);
            System.out.println("  codigos " + toStringList(codigos)) ;
            System.out.println("  saldos  " + toStringList(saldos));

            Banco banco = new Banco(codigos, saldos);

            System.out.println("Deberia haberse producido una excepcion IllegalArgumentExcpetion");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.toString() + " ha sido lanzada correctamente");
        }
        cuenta++;

        try {
            System.out.println("\nPRUEBA " +  cuenta);
            System.out.println("ENTRADA");
            crearCuentas(5, 0x00, codigos, saldos);
            codigos.remove(0);
            System.out.println("  codigos " + toStringList(codigos)) ;
            System.out.println("  saldos  " + toStringList(saldos));

            Banco banco = new Banco(codigos, saldos);

            System.out.println("Deberia haberse producido una excepcion IllegalArgumentExcpetion");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.toString() + " ha sido lanzada correctamente");
        }
        cuenta++;

        try {
            System.out.println("\nPRUEBA " +  cuenta);
            System.out.println("ENTRADA");
            crearCuentas(5, 0x00, codigos, saldos);
            anullarLista(codigos);
            System.out.println("  codigos " + toStringList(codigos)) ;
            System.out.println("  saldos  " + toStringList(saldos));

            Banco banco = new Banco(codigos, saldos);

            System.out.println("Deberia haberse producido una excepcion IllegalArgumentExcpetion");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.toString() + " ha sido lanzada correctamente");
        }
        cuenta++;

        try {
            System.out.println("\nPRUEBA " +  cuenta);
            System.out.println("ENTRADA");
            crearCuentas(5, 0x00, codigos, saldos);
            anullarLista(saldos);
            System.out.println("  codigos " + toStringList(codigos)) ;
            System.out.println("  saldos  " + toStringList(saldos));

            Banco banco = new Banco(codigos, saldos);

            System.out.println("Deberia haberse producido una excepcion IllegalArgumentException");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.toString() + " ha sido lanzada correctamente");
        }
        cuenta++;
    }

    @org.junit.Test
    public void consultaTest() {
        System.out.println("PROBANDO EL METODO CONSULTA");

        List<String> codigos = new LinkedList<>();
        List<Integer> saldos = new LinkedList<>();

        int cuenta = 1;

        for (int i = 0; i < NUMERO_CUENTAS; i++) {
            crearCuentas(i, 0x00,codigos, saldos);

            Banco banco = new Banco(codigos, saldos);

            for (int c = 0; c < codigos.size(); c++) {
                System.out.println("\nPRUEBA " + cuenta);
                System.out.println(banco);
                System.out.println("RESULTADO ESPERADO");
                System.out.println("  consulta(" + codigos.get(c) + ") -> " + saldos.get(c));
                System.out.println("RESULTADO OBTENIDO");

                int resultado = banco.consulta(codigos.get(c));
                System.out.println("  consulta(" + codigos.get(c) + ") -> " + resultado);

                assertEquals((int)saldos.get(c), resultado);
                cuenta++;
            }
        }
    }

    @org.junit.Test
    public void consultaExcepccionesTest() {
        System.out.println("PROBANDO LAS EXCEPECIONES DEL METODO CONSULTA");

        List<String> codigos = new LinkedList<>();
        List<Integer> saldos = new LinkedList<>();

        int cuenta = 1;
        for (int i = 0; i < NUMERO_CUENTAS; i++) {
            crearCuentas(NUMERO_CUENTAS, 0, codigos, saldos);
            System.out.println("\nPRUEBA " + cuenta);
            String c = codigos.get(i);
            codigos.remove(i);
            saldos.remove(i);

            Banco banco = new Banco(codigos, saldos);

            System.out.println(banco);
            System.out.println("consulta(" + c + ")");
            try {
                banco.consulta(c);

                System.out.println("Deberia haberse producido una excepcion IllegalArgumentException");
                Assert.fail();
            } catch (IllegalArgumentException e) {
                System.out.println(e.toString() + " ha sido lanzada correctamente");
            }
            cuenta++;
        }

        System.out.println("\nPRUEBA " + cuenta);
        Banco banco = new Banco(codigos, saldos);

        System.out.println(banco);
        System.out.println("consulta(null)");
        try {
            banco.consulta(null);

            System.out.println("Deberia haberse producido una excepcion IllegalArgumentException");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.toString() + " ha sido lanzada correctamente");
        }
    }

    static private int CANTIDAD_TRANFERENCIA = 2;

    static private List<Transferencia> generarTransferencias(List<String> codigos, int num) {
        List<Transferencia> resultado = new LinkedList<>();
        for (int i = 0; i < codigos.size(); i++)
            for (int j = 0; j < codigos.size(); j++) {
                if (i != j) {
                    int cantidad = CANTIDAD_TRANFERENCIA;
                    for (int k = 0; k < num; k++) {
                        resultado.add(new Transferencia(codigos.get(i), codigos.get(j), cantidad));
                        cantidad *= -2;
                        resultado.add(new Transferencia(codigos.get(j), codigos.get(i), cantidad));
                        cantidad += 1;
                    }
                }
            }

        return resultado;
    }

    static private boolean transferenciaValida(Banco banco, Transferencia tr) {
        return (tr.cantidad >= 0) ? (banco.cuentas.get(tr.origen) - tr.cantidad) >= 0
                : (banco.cuentas.get(tr.destino) + tr.cantidad) >= 0;
    }

    static private void probarAsiento(Banco banco, Transferencia tr) {
        System.out.println(banco);
        int sorigen = banco.cuentas.get(tr.origen);
        int sdestino = banco.cuentas.get(tr.destino);

        boolean realizar = transferenciaValida(banco, tr);

        System.out.println("\n  Se prueba la transferencia " + tr);

        int rorigen =  sorigen;
        int rdestino = sdestino;

        if (realizar) {
            rorigen -= tr.cantidad;
            rdestino += tr.cantidad;
        }

        System.out.println("RESULTADO ESPERADO");
        System.out.println("  asiento(" + tr + ") -> "  + realizar);
        System.out.println("  " + tr.origen + ": " + rorigen);
        System.out.println("  " + tr.destino + ": " + rdestino);

        boolean resultado = banco.asiento(tr);

        System.out.println("RESULTADO OBTENIDO");
        System.out.println("  asiento(" + tr + ") -> "  + resultado);
        System.out.println("  " + tr.origen + ": " + banco.cuentas.get(tr.origen));
        System.out.println("  " + tr.destino + ": " + banco.cuentas.get(tr.destino));

        assertEquals(rorigen, (int)banco.cuentas.get(tr.origen));
        assertEquals(rdestino, (int)banco.cuentas.get(tr.destino));
        assertEquals(realizar, resultado);
    }

    @org.junit.Test
    public void asientoTest() {
        System.out.println("PROBANDO EL METODO ASIENTO");

        List<String> codigos = new LinkedList<>();
        List<Integer> saldos = new LinkedList<>();

        int cuenta = 1;
        crearCuentas(NUMERO_CUENTAS/2, 0, codigos, saldos);

        Banco banco = new Banco(codigos, saldos);

        List<Transferencia> transferencias = generarTransferencias(codigos, 6);
        for (Transferencia tr: transferencias) {
            System.out.println("\nPRUEBA " + cuenta);
            probarAsiento(banco, tr);
            cuenta++;
        }
    }
    
    @org.junit.Test
    public void asientosExcepcionesTest() {
        System.out.println("PROBANDO LAS EXCEPECIONES DEL METODO ASIENTO");

        List<String> codigos = new LinkedList<>();
        List<Integer> saldos = new LinkedList<>();

        int cuenta = 1;
        crearCuentas(NUMERO_CUENTAS/2, 0, codigos, saldos);


        List<Transferencia> transferencias = generarTransferencias(codigos, 6);

        String borrado = codigos.get(0);
        codigos.remove(0);
        saldos.remove(0);

        Banco banco = new Banco(codigos, saldos);
        for (Transferencia tr: transferencias) {
            if (borrado.equals(tr.origen) || borrado.equals(tr.destino)) {
                try {
                    System.out.println("\nPRUEBA " + cuenta);
                    System.out.println(banco);
                    System.out.println("\n  Se prueba la transferencia " + tr);
                    banco.asiento(tr);

                    System.out.println("Deberia haberse producido una excepcion IllegalArgumentException");
                    Assert.fail();
                } catch (IllegalArgumentException e) {
                    System.out.println(e.toString() + " ha sido lanzada correctamente");
                }
            }
        }
    }

    static private List<Transferencia> seleccionarTransferencias(List<Transferencia> transferencias,
                                                                 String origen, String destino) {
        List<Transferencia> resultado = new LinkedList<>();

        for (Transferencia tr: transferencias) {
            if ((tr.origen.equals(origen) && tr.destino.equals(destino)) ||
                    (tr.origen.equals(destino) && tr.destino.equals(origen))) {
                Transferencia aux = new Transferencia(tr);
                if (aux.destino.equals(origen))
                    aux.invertir();
                resultado.add(aux);


            }
        }
        return resultado;
    }

    static private String toStringTransferencias(Collection<Transferencia> col) {
        final StringBuilder sb = new StringBuilder();

        sb.append("[");
        Iterator<Transferencia> iter = col.iterator();
        while (iter.hasNext()) {
            Transferencia elem = iter.next();
            sb.append(elem);
            if (iter.hasNext())
                sb.append(",\n");
        }

        sb.append("] - size: ");
        sb.append(col.size());

        return sb.toString();
    }

    static private void probarHistorico(Banco banco, List<Transferencia> esperado,
                                        String origen, String destino) {

        System.out.println("ENTRADA");
        System.out.println("Para mas informacion antes de la primera prueba aparercen las transferencias realizadas");
        System.out.println("\nhistorico(" + origen + ", " + destino + ")");
        System.out.println("RESULTADO ESPERADO (el orden de la lista no importa)");
        System.out.println(toStringTransferencias(esperado));
        List<Transferencia> resultado = banco.historico(origen, destino);

        System.out.println("RESULTADO OBTENIDO");
        System.out.println(toStringTransferencias(resultado));

        assertEquals(esperado.size(), resultado.size());

        for(Transferencia tr: resultado)
            assertEquals(origen, tr.origen);

        for(Transferencia tr: esperado)
            assertTrue(resultado.remove(tr));
    }

    @org.junit.Test
    public void historicoTest() {
        System.out.println("PROBANDO EL METODO HISTORICO");

        List<String> codigos = new LinkedList<>();
        List<Integer> saldos = new LinkedList<>();

        int cuenta = 1;
        crearCuentas(NUMERO_CUENTAS/2, 0, codigos, saldos);

        Banco banco = new Banco(codigos, saldos);

        System.out.println("Estado incial del banco - " + banco);

        List<Transferencia> transferencias = generarTransferencias(codigos, 3);
        Iterator<Transferencia> iter = transferencias.iterator();
        while(iter.hasNext()) {
            Transferencia tr = iter.next();
            if (!banco.asiento(tr))
                iter.remove();
            else {
                System.out.println("asiento(" + tr + ")");
                System.out.println(banco);
            }
        }

        for (int i = 0; i < codigos.size(); i++) {
            for (int j = 0; j < codigos.size(); j++) {
                System.out.println("\nPRUEBA " + cuenta);
                List<Transferencia> esperado = seleccionarTransferencias(transferencias,
                        codigos.get(i), codigos.get(j));

                probarHistorico(banco, esperado, codigos.get(i), codigos.get(j));

                cuenta++;
            }
        }
    }

    @org.junit.Test
    public void historicoExcepcionesTest() {
        System.out.println("PROBANDO LAS EXCECPCIONES DEL METODO HISTORICO");

        List<String> codigos = new LinkedList<>();
        List<Integer> saldos = new LinkedList<>();

        int cuenta = 1;
        crearCuentas(NUMERO_CUENTAS/2, 0, codigos, saldos);

        String borrado = codigos.get(codigos.size()-1);
        codigos.remove(codigos.size()-1);
        saldos.remove(saldos.size()-1);

        Banco banco = new Banco(codigos, saldos);

        try {
            System.out.println("\nPRUEBA " +  cuenta);
            System.out.println(banco);
            System.out.println("historico (null , " + codigos.get(0) + ")");

            banco.historico(null, codigos.get(0));

            System.out.println("Deberia haberse producido una excepcion IllegalArgumentExcpetion");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.toString() + " ha sido lanzada correctamente");
            cuenta++;
        }

        try {
            System.out.println("\nPRUEBA " +  cuenta);
            System.out.println(banco);
            System.out.println("historico (" + codigos.get(0) + ", null)");

            banco.historico(codigos.get(0), null);

            System.out.println("Deberia haberse producido una excepcion IllegalArgumentExcpetion");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.toString() + " ha sido lanzada correctamente");
            cuenta++;
        }

        try {
            System.out.println("\nPRUEBA " +  cuenta);
            System.out.println(banco);
            System.out.println("historico (" + borrado +", " + codigos.get(0) + ")");

            banco.historico(borrado, codigos.get(0));
            Assert.fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.toString() + " ha sido lanzada correctamente");
            cuenta++;
        }

        try {
            System.out.println("\nPRUEBA " +  cuenta);
            System.out.println(banco);
            System.out.println("historico (" +  codigos.get(0) + ", " + borrado +  ")");

            banco.historico(codigos.get(0), borrado);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.toString() + " ha sido lanzada correctamente");
            cuenta++;
        }

    }

}
