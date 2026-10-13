package co.edu.uniquindio.poo.App;

import co.edu.uniquindio.poo.Algoritmos.Algoritmos;
import co.edu.uniquindio.poo.modelo.CriteriosPaquete;
import co.edu.uniquindio.poo.modelo.Paquete;
import co.edu.uniquindio.poo.quindioexpress.MiListaEnlazada;

import java.util.ArrayList;
import java.util.List;

public class Main {
    static void main() {

        MiListaEnlazada<String> lista = new MiListaEnlazada<>();
        lista.agregar("A"); lista.agregar("B"); lista.agregar("C");
        lista.eliminar("C");              // elimina la cola
        lista.agregar("D");               // la cola debe seguir funcionando
        for (String s : lista) System.out.println(s);

        List<Paquete> d = new ArrayList<>(List.of(
                new Paquete("PQ420", "Armenia", 1.0, 3, 30),
                new Paquete("PQ731", "Salento", 1.0, 5, 45),
                new Paquete("PQ105", "Armenia", 1.0, 5, 25),
                new Paquete("PQ942", "Calarcá", 1.0, 4, 20)));
        d.sort(CriteriosPaquete.DESPACHO);
        d.forEach(p -> System.out.print(p.getCodigo() + " "));   // PQ105 PQ731 PQ942 PQ420

        List<Paquete> p = List.of(
                new Paquete("PQ101", "Armenia", 4.5, 5, 10),
                new Paquete("PQ203", "Salento", 3.0, 2, 10),
                new Paquete("PQ415", "Armenia", 7.2, 4, 10),
                new Paquete("PQ730", "Armenia", 2.3, 1, 10));
        System.out.printf("%.1f%n", Algoritmos.pesoTotalPorDestino(p, "Armenia"));  // 14.0
        System.out.println(Algoritmos.contarPrioridadMinima(p, 4));                 // 2
        System.out.println(Algoritmos.mayorDivideVenceras(p, CriteriosPaquete.PESO_ASC)); // PQ415
        System.out.println(Algoritmos.busquedaBinaria(p, "PQ415"));  // la lista p ya está ordenada por código: 2
    }
}
