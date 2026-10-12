package co.edu.uniquindio.poo;

import co.edu.uniquindio.poo.modelo.MiListaEnlazada;

public class Main {
    static void main() {

        MiListaEnlazada<String> lista = new MiListaEnlazada<>();
        lista.agregar("A"); lista.agregar("B"); lista.agregar("C");
        lista.eliminar("C");              // elimina la cola
        lista.agregar("D");               // la cola debe seguir funcionando
        for (String s : lista) System.out.println(s);

    }
}
