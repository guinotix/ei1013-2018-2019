package practica3;


public class Transferencia {
    public String origen;       // Código de la cuenta de origen
    public String destino;      // Códido de la cuenta destino
    public int cantidad;     // Cantidad de la transferencia

    public Transferencia(String origen, String destino, int cantidad) {
        if (origen == null || destino == null || origen.equals(destino))
            throw new IllegalArgumentException();

        this.origen = origen;
        this.destino = destino;
        this.cantidad = cantidad;
    }

    public Transferencia(Transferencia tr) {
        origen = tr.origen;
        destino = tr.destino;
        cantidad = tr.cantidad;
    }

    public void invertir() {
        String aux = origen;
        origen = destino;
        destino = aux;
        cantidad  *= -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transferencia that = (Transferencia) o;

        if (origen.equals(that.origen) && destino.equals(that.destino)
                && cantidad == that.cantidad)
            return true;

        if (origen.equals(that.destino) && destino.equals(that.origen)
                && cantidad == -that.cantidad)
            return true;

        return false;
    }


    public String toString() {
        return "(" + origen + " -> " + destino + ": " + cantidad + ")";
    }
}
