package co.edu.uniquindio.poo.modelo;

public interface ColeccionGenerica <T> extends  Iterable<T> {
    void agregar(T elemento);
    boolean eliminar(T elemento);
    
    T eliminarEnPosicion(int posicion);
    T ObtenerEnPosicion(int posicion);

    int tamanio();
    boolean estaVacia();
    
}
