package co.edu.uniquindio.poo.Algoritmos;

import co.edu.uniquindio.poo.modelo.Paquete;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class Algoritmos {

    private Algoritmos() { }

    //Recursión: peso total por destino
    public static double pesoTotalPorDestino(List<Paquete> paquetes, String destino) {
        Objects.requireNonNull(paquetes, "La lista no puede ser nula");
        Objects.requireNonNull(destino, "El destino no puede ser nulo");
        return pesoTotal(paquetes, destino, 0);
    }

    private static double pesoTotal(List<Paquete> paquetes, String destino, int indice) {
        // Caso base: ya no quedan paquetes por revisar
        if (indice == paquetes.size()) {
            return 0;
        }
        Paquete actual = paquetes.get(indice);
        double aporte = actual.getDestino().equals(destino) ? actual.getPeso() : 0;
        // Caso recursivo: este paquete + el resto de la lista
        return aporte + pesoTotal(paquetes, destino, indice + 1);
    }

    //Recursión: contar prioridad >= mínima
    public static int contarPrioridadMinima(List<Paquete> paquetes, int prioridadMinima) {
        Objects.requireNonNull(paquetes, "La lista no puede ser nula");
        return contar(paquetes, prioridadMinima, 0);
    }

    private static int contar(List<Paquete> paquetes, int minima, int indice) {
        if (indice == paquetes.size()) {
            return 0;
        }
        int cuenta = paquetes.get(indice).getPrioridad() >= minima ? 1 : 0;
        return cuenta + contar(paquetes, minima, indice + 1);
    }

    // Búsqueda binaria por código
    // PRECONDICIÓN: 'ordenados' debe estar en orden ascendente por código
    // (orden natural de Paquete). Devuelve la posición o -1 si no existe.
    public static int busquedaBinaria(List<Paquete> ordenados, String codigo) {
        Objects.requireNonNull(ordenados, "La lista no puede ser nula");
        Objects.requireNonNull(codigo, "El código no puede ser nulo");
        int bajo = 0;
        int alto = ordenados.size() - 1;
        while (bajo <= alto) {
            int medio = bajo + (alto - bajo) / 2;
            int comparacion = codigo.compareTo(ordenados.get(medio).getCodigo());
            if (comparacion == 0) {
                return medio;
            } else if (comparacion < 0) {
                alto = medio - 1;      // descartar la mitad derecha
            } else {
                bajo = medio + 1;      // descartar la mitad izquierda
            }
        }
        return -1;
    }

    // Divide y vencerás: mayor elemento según un criterio
    public static <T> T mayorDivideVenceras(List<T> elementos, Comparator<? super T> criterio) {
        Objects.requireNonNull(elementos, "La lista no puede ser nula");
        Objects.requireNonNull(criterio, "El criterio no puede ser nulo");
        if (elementos.isEmpty()) {
            throw new IllegalArgumentException("No hay elementos para comparar");
        }
        return mayor(elementos, criterio, 0, elementos.size() - 1);
    }

    private static <T> T mayor(List<T> elementos, Comparator<? super T> criterio,
                               int inicio, int fin) {
        // Caso base: un solo elemento
        if (inicio == fin) {
            return elementos.get(inicio);
        }
        // Dividir
        int medio = inicio + (fin - inicio) / 2;
        // Resolver subproblema izquierdo y derecho
        T mayorIzquierda = mayor(elementos, criterio, inicio, medio);
        T mayorDerecha = mayor(elementos, criterio, medio + 1, fin);
        // Combinar
        return criterio.compare(mayorIzquierda, mayorDerecha) >= 0
                ? mayorIzquierda : mayorDerecha;
    }

}