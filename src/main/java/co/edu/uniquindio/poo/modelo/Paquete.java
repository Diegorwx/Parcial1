package co.edu.uniquindio.poo.modelo;

import java.util.Objects;

/**
 * Paquete
 */

public final class Paquete implements Comparable<Paquete>{

    private final String codigo;
    private  final String destino;
    private final double peso ;
    private final int prioridad;
    private final int tiempoEstimado;

    public Paquete(String codigo,String destino , double peso, int prioridad, int tiempoEstimado){
        this.codigo = Validaciones.textoObligatorio(codigo ,"El codigo no puede ser nullo ni estar vacio");
        this.destino = Validaciones.textoObligatorio(destino, "El destino no puede estar nullo ni vacio ");

        if (!(peso>0)){
            throw new IllegalArgumentException("El peso debe ser mayor a cero");
        }

        if(prioridad < 1 || prioridad > 5) {
            throw new IllegalArgumentException ("La prioridad debe estar entre 1 y 5 ");
        }

        if(tiempoEstimado <= 0){
            throw new IllegalArgumentException ("El tiempo estimado debe ser mayor que cero");
        }
        this.peso = peso;
        this.prioridad = prioridad;
        this.tiempoEstimado =tiempoEstimado;
            

    }

    public String getCodigo() { return codigo; }



    public String getDestino() { return destino; }



    public double getPeso() { return peso; }



    public int getPrioridad() { return prioridad; }



    public int getTiempoEstimado() { return tiempoEstimado; }

    @Override 
    public int compareTo(Paquete otro){
        return this.codigo.compareTo(otro.codigo);

    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paquete)) return false;
        return codigo.equals(((Paquete) o).codigo);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }


    @Override
    public String toString() {
        return String.format("%s (%s, %.1f kg, prioridad %d, %d min)",
                codigo, destino, peso, prioridad, tiempoEstimado);
    }


}
