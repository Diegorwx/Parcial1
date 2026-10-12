package co.edu.uniquindio.poo.modelo;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class MiListaEnlazada<T> implements ColeccionGenerica<T> {

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamanio;

    private static class Nodo<E> {
        E valor;
        Nodo<E> siguiente;

        Nodo(E valor) {
            this.valor = valor;
        }
    }

    @Override
    public void agregar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            cola.siguiente = nuevo;
        }
        cola = nuevo;
        tamanio++;
    }

    @Override
    public boolean eliminar(T elemento) {
        Nodo<T> anterior = null;
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (Objects.equals(actual.valor, elemento)) {
                desenlazar(anterior, actual);
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    @Override
    public T eliminarEnPosicion(int posicion) {
        validarPosicion(posicion);
        Nodo<T> anterior = null;
        Nodo<T> actual = cabeza;
        for (int i = 0; i < posicion; i++) {
            anterior = actual;
            actual = actual.siguiente;
        }
        desenlazar(anterior, actual);
        return actual.valor;
    }



    @Override
    public T ObtenerEnPosicion(int posicion) {
        validarPosicion(posicion);
        Nodo<T> actual = cabeza;
        for (int i = 0; i < posicion; i++) {
            actual = actual.siguiente;
        }
        return actual.valor;
    }

    @Override
    public int tamanio() {
        return tamanio;
    }

    @Override
    public boolean estaVacia() {
        return tamanio == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = cabeza;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (actual == null) {
                    throw new NoSuchElementException();
                }
                T valor = actual.valor;
                actual = actual.siguiente;
                return valor;
            }
        };
    }

    // Quita 'actual' de la lista; 'anterior' es null si actual es la cabeza
    private void desenlazar(Nodo<T> anterior, Nodo<T> actual) {
        if (anterior == null) {
            cabeza = actual.siguiente;
        } else {
            anterior.siguiente = actual.siguiente;
        }
        if (actual == cola) {
            cola = anterior;
        }
        tamanio--;
    }

    private void validarPosicion(int posicion) {
        if (posicion < 0 || posicion >= tamanio) {
            throw new IndexOutOfBoundsException(
                    "Posición " + posicion + " fuera de rango (tamaño " + tamanio + ")");
        }
    }
}