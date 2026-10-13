package co.edu.uniquindio.poo.Pruebas;

import co.edu.uniquindio.poo.Algoritmos.Algoritmos;
import co.edu.uniquindio.poo.modelo.CriteriosPaquete;
import co.edu.uniquindio.poo.modelo.GestorDistribucion;
import co.edu.uniquindio.poo.modelo.Paquete;
import co.edu.uniquindio.poo.modelo.Repartidor;

import java.util.ArrayList;
import java.util.List;

public class PruebasDistribucion {

    private static int aprobadas = 0;
    private static int fallidas = 0;

    public static void main(String[] args) {
        System.out.println("=== Pruebas de Algoritmos ===");
        pruebasRecursion();
        pruebasBusquedaBinaria();
        pruebasDivideVenceras();

        System.out.println("\n=== Pruebas de GestorDistribucion ===");
        pruebasRegistro();
        pruebasMunicipios();
        pruebasEntregas();

        System.out.println("\nAprobadas: " + aprobadas + " | Fallidas: " + fallidas);
    }

    // ---------------- Recursión ----------------

    private static void pruebasRecursion() {
        List<Paquete> paquetes = List.of(
                new Paquete("PQ001", "Armenia", 2.0, 3, 30),
                new Paquete("PQ002", "Salento", 3.0, 5, 20),
                new Paquete("PQ003", "Armenia", 1.5, 1, 40));

        verificar("peso total de Armenia = 3.5",
                Math.abs(Algoritmos.pesoTotalPorDestino(paquetes, "Armenia") - 3.5) < 0.001);
        verificar("peso total de un destino sin paquetes = 0",
                Algoritmos.pesoTotalPorDestino(paquetes, "Circasia") == 0);
        verificar("peso total de lista vacía = 0",
                Algoritmos.pesoTotalPorDestino(new ArrayList<Paquete>(), "Armenia") == 0);
        verificar("prioridad >= 3 cuenta 2 paquetes",
                Algoritmos.contarPrioridadMinima(paquetes, 3) == 2);
        verificar("prioridad >= 5 cuenta 1 paquete",
                Algoritmos.contarPrioridadMinima(paquetes, 5) == 1);
    }

    // ---------------- Búsqueda binaria ----------------

    private static void pruebasBusquedaBinaria() {
        List<Paquete> ordenados = List.of(
                new Paquete("PQ100", "Armenia", 1.0, 1, 10),
                new Paquete("PQ200", "Salento", 1.0, 1, 10),
                new Paquete("PQ300", "Calarcá", 1.0, 1, 10));

        verificar("busca PQ100 (primero) en posición 0",
                Algoritmos.busquedaBinaria(ordenados, "PQ100") == 0);
        verificar("busca PQ200 (medio) en posición 1",
                Algoritmos.busquedaBinaria(ordenados, "PQ200") == 1);
        verificar("busca PQ300 (último) en posición 2",
                Algoritmos.busquedaBinaria(ordenados, "PQ300") == 2);
        verificar("PQ250 no existe, devuelve -1",
                Algoritmos.busquedaBinaria(ordenados, "PQ250") == -1);
        verificar("lista vacía devuelve -1",
                Algoritmos.busquedaBinaria(new ArrayList<Paquete>(), "PQ100") == -1);
    }

    // ---------------- Divide y vencerás ----------------

    private static void pruebasDivideVenceras() {
        List<Paquete> paquetes = List.of(
                new Paquete("PQ001", "Armenia", 2.0, 3, 30),
                new Paquete("PQ002", "Salento", 9.0, 5, 20),
                new Paquete("PQ003", "Armenia", 4.0, 1, 40));

        Paquete mayor = Algoritmos.mayorDivideVenceras(paquetes, CriteriosPaquete.PESO_ASC);
        verificar("el de mayor peso es PQ002",
                mayor.getCodigo().equals("PQ002"));

        List<Paquete> unico = List.of(new Paquete("PQ009", "Armenia", 1.0, 1, 10));
        verificar("con un solo elemento devuelve ese elemento",
                Algoritmos.mayorDivideVenceras(unico, CriteriosPaquete.PESO_ASC).getCodigo().equals("PQ009"));

        List<Paquete> vacia = new ArrayList<>();
        verificar("lista vacía lanza IllegalArgumentException",
                lanza(IllegalArgumentException.class, () ->
                        Algoritmos.mayorDivideVenceras(vacia, CriteriosPaquete.PESO_ASC)));
    }

    // ---------------- Registro y consultas ----------------

    private static void pruebasRegistro() {
        GestorDistribucion gestor = new GestorDistribucion();
        gestor.registrarPaquete(new Paquete("PQ010", "Armenia", 2.0, 3, 30));
        gestor.registrarPaquete(new Paquete("PQ005", "Salento", 4.0, 5, 15));
        gestor.registrarPaquete(new Paquete("PQ020", "Circasia", 1.0, 2, 25));

        verificar("consulta PQ005 lo encuentra",
                gestor.consultarPaquete("PQ005").isPresent());
        verificar("consulta de código inexistente está vacía",
                gestor.consultarPaquete("PQ999").isEmpty());
        verificar("búsqueda binaria encuentra PQ020",
                gestor.busquedaBinariaPorCodigo("PQ020").isPresent());
        verificar("búsqueda binaria no encuentra PQ015",
                gestor.busquedaBinariaPorCodigo("PQ015").isEmpty());
        verificar("código duplicado lanza IllegalArgumentException",
                lanza(IllegalArgumentException.class, () ->
                        gestor.registrarPaquete(new Paquete("PQ010", "Calarcá", 1.0, 1, 10))));
        verificar("pendientes conservan el orden de llegada (PQ010 primero)",
                gestor.pendientesEnOrdenDeLlegada().get(0).getCodigo().equals("PQ010"));
        verificar("siguiente por llegada es PQ010",
                gestor.siguientePorLlegada().get().getCodigo().equals("PQ010"));
        verificar("siguiente para despacho es PQ005 (prioridad 5)",
                gestor.siguienteParaDespacho().get().getCodigo().equals("PQ005"));
    }

    // ---------------- Municipios ----------------

    private static void pruebasMunicipios() {
        GestorDistribucion gestor = new GestorDistribucion();
        gestor.registrarPaquete(new Paquete("PQ001", "Quimbaya", 1.0, 1, 10));
        gestor.registrarPaquete(new Paquete("PQ002", "Armenia", 1.0, 1, 10));
        gestor.registrarPaquete(new Paquete("PQ003", "Calarcá", 1.0, 1, 10));
        gestor.registrarPaquete(new Paquete("PQ004", "Armenia", 1.0, 1, 10));
        gestor.registrarPaquete(new Paquete("PQ005", "Circasia", 1.0, 1, 10));

        verificar("hay 4 municipios sin repetir",
                gestor.municipiosRegistrados().size() == 4);
        verificar("municipios ordenados en español",
                new ArrayList<>(gestor.municipiosOrdenados())
                        .equals(List.of("Armenia", "Calarcá", "Circasia", "Quimbaya")));
        verificar("Armenia tiene 2 paquetes",
                gestor.paquetesDeMunicipio("Armenia").size() == 2);
    }

    // ---------------- Asignación y entregas ----------------

    private static void pruebasEntregas() {
        GestorDistribucion gestor = new GestorDistribucion();
        gestor.registrarPaquete(new Paquete("PQ001", "Armenia", 2.0, 3, 30));
        gestor.registrarPaquete(new Paquete("PQ002", "Salento", 3.0, 5, 20));
        gestor.registrarRepartidor(new Repartidor("R01", "Laura", "Armenia"));
        gestor.registrarRepartidor(new Repartidor("R02", "Andrés", "Armenia"));

        verificar("zona distinta al destino lanza IllegalStateException",
                lanza(IllegalStateException.class, () ->
                        gestor.asignarPaqueteARepartidor("PQ002", "R01")));

        gestor.asignarPaqueteARepartidor("PQ001", "R01");
        verificar("tras asignar, solo queda 1 repartidor disponible",
                gestor.repartidoresDisponibles().size() == 1);
        verificar("asignar un paquete ya asignado lanza IllegalStateException",
                lanza(IllegalStateException.class, () ->
                        gestor.asignarPaqueteARepartidor("PQ001", "R02")));

        gestor.registrarEntrega("PQ001");
        verificar("tras entregar, queda 1 pendiente",
                gestor.pendientesEnOrdenDeLlegada().size() == 1);
        verificar("el historial guarda PQ001",
                gestor.historialEntregas().iterator().next().getCodigo().equals("PQ001"));
        verificar("entregar dos veces lanza IllegalStateException",
                lanza(IllegalStateException.class, () ->
                        gestor.registrarEntrega("PQ001")));
        verificar("R01 vuelve a estar disponible",
                gestor.repartidoresDisponibles().size() == 2);
    }

    // ---------------- Utilidades ----------------

    private static void verificar(String descripcion, boolean condicion) {
        if (condicion) {
            aprobadas++;
            System.out.println("  OK     " + descripcion);
        } else {
            fallidas++;
            System.out.println("  FALLA  " + descripcion);
        }
    }

    private static boolean lanza(Class<? extends RuntimeException> tipo, Runnable accion) {
        try {
            accion.run();
            return false;
        } catch (RuntimeException e) {
            return tipo.isInstance(e);
        }
    }
}