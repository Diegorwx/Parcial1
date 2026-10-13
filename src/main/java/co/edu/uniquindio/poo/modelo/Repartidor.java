package co.edu.uniquindio.poo.modelo;

import java.util.Objects;

/**
 * Repartidor
 */
public class Repartidor {

    private final String identificacion;
    private final String nombre;
    private final String zona;
    private boolean disponible;

    public Repartidor(String identificacion, String nombre, String zona) {
        this.identificacion = Validaciones.textoObligatorio(identificacion,"La identificacion no puede ser nula ni estar vacia");
        this.nombre = Validaciones.textoObligatorio(nombre,"El nombre no puede ser nulo ni estar vacio");
        this.zona = Validaciones.nombreLugar(zona, "La zona no puede ser nula ni vacía");
        this.disponible = true;
    }

    public String getIdentificacion() { return identificacion; }

    public String getNombre() { return nombre; }

    public String getZona() { return zona; }

    public boolean isDisponible() { return disponible; }
    
    public void liberar() { this.disponible = true; }

    public void marcarNoDisponible() { this.disponible = false; }

    public void asignar() {
        if (!disponible) {
            throw new IllegalStateException("El repartidor con identificación " + identificacion + " ya está asignado a un paquete.");
        }
        this.disponible = false;
    }

    @Override 
    public  boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if(!(obj instanceof Repartidor)) {
            return false;
        }
        return this.identificacion.equals(((Repartidor) obj).identificacion);
    }

    @Override 
    public int hashCode() {
        return Objects.hash(identificacion);
    }
       
    @Override 
    public String toString() {
        return String.format("%s %s (zona %s, %s)", identificacion, nombre, zona,
                disponible ? "disponible" : "no disponible");
    }

    
    
}