package co.edu.uniquindio.poo.App;

import co.edu.uniquindio.poo.modelo.CriteriosPaquete;
import co.edu.uniquindio.poo.modelo.GestorDistribucion;
import co.edu.uniquindio.poo.modelo.Paquete;
import co.edu.uniquindio.poo.modelo.Repartidor;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuPrincipal {

    private final GestorDistribucion gestor;
    private final Scanner entrada = new Scanner(System.in);

    public MenuPrincipal(GestorDistribucion gestor) {
        this.gestor = gestor;
    }

    public void ejecutar() {
        boolean activo = true;
        while (activo) {
            mostrarMenu();
            String opcion = leerTexto("Elija una opción: ");
            try {
                switch (opcion) {
                    case "1" -> registrarPaquete();
                    case "2" -> consultarPaquete();
                    case "3" -> mostrarMunicipios();
                    case "4" -> mostrarPaquetesDeMunicipio();
                    case "5" -> pesoTotalMunicipio();
                    case "6" -> contarPrioridad();
                    case "7" -> mostrarAtencion();
                    case "8" -> busquedaBinaria();
                    case "9" -> paqueteMayorPeso();
                    case "10" -> ordenarPaquetes();
                    case "11" -> registrarRepartidor();
                    case "12" -> mostrarRepartidoresDisponibles();
                    case "13" -> asignarPaquete();
                    case "14" -> registrarEntrega();
                    case "15" -> mostrarHistorial();
                    case "0" -> activo = false;
                    default -> System.out.println("Opción no válida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
        System.out.println("Hasta luego.");
    }

    private void mostrarMenu() {
        System.out.println("\n=== QuindíoExpress ===");
        System.out.println(" 1) Registrar paquete");
        System.out.println(" 2) Buscar paquete");
        System.out.println(" 3) Municipios");
        System.out.println(" 4) Paquetes por municipio");
        System.out.println(" 5) Peso por municipio");
        System.out.println(" 6) Contar prioridad");
        System.out.println(" 7) Siguiente a despachar");
        System.out.println(" 8) Búsqueda binaria");
        System.out.println(" 9) Paquete más pesado");
        System.out.println("10) Ordenar paquetes");
        System.out.println("11) Registrar repartidor");
        System.out.println("12) Repartidores disponibles");
        System.out.println("13) Asignar paquete");
        System.out.println("14) Registrar entrega");
        System.out.println("15) Historial");
        System.out.println(" 0) Salir");
    }
    
    private void registrarPaquete() {
        String codigo = leerTexto("Código: ");
        String destino = leerTexto("Destino: ");
        double peso = leerDecimal("Peso (kg): ");
        int prioridad = leerEntero("Prioridad (1-5): ");
        int tiempo = leerEntero("Tiempo estimado (min): ");
        gestor.registrarPaquete(new Paquete(codigo, destino, peso, prioridad, tiempo));
        System.out.println("Paquete registrado.");
    }

    private void consultarPaquete() {
        String codigo = leerTexto("Código a consultar: ");
        mostrarPaquete(gestor.consultarPaquete(codigo));
    }

    private void mostrarMunicipios() {
        System.out.println("Sin repetir: " + gestor.municipiosRegistrados());
        System.out.println("Ordenados: " + gestor.municipiosOrdenados());
        System.out.println("Con entregas pendientes: " + gestor.municipiosPendientes());
    }

    private void mostrarPaquetesDeMunicipio() {
        String municipio = leerTexto("Municipio: ");
        List<Paquete> paquetes = gestor.paquetesDeMunicipio(municipio);
        if (paquetes.isEmpty()) {
            System.out.println("No hay paquetes para " + municipio + ".");
        }
        paquetes.forEach(System.out::println);
    }

    private void pesoTotalMunicipio() {
        String municipio = leerTexto("Municipio: ");
        System.out.printf("Peso total: %.1f kg%n", gestor.pesoTotalDeMunicipio(municipio));
    }

    private void contarPrioridad() {
        int minima = leerEntero("Prioridad mínima (1-5): ");
        System.out.println("Paquetes con prioridad >= " + minima + ": "
                + gestor.cantidadPrioridadMinima(minima));
    }

    private void mostrarAtencion() {
        gestor.siguientePorLlegada().ifPresentOrElse(
                p -> System.out.println("Siguiente por llegada: " + p),
                () -> System.out.println("No hay paquetes pendientes."));
        gestor.siguienteParaDespacho().ifPresentOrElse(
                p -> System.out.println("Siguiente por despacho: " + p),
                () -> System.out.println("No hay paquetes pendientes."));
        System.out.println("Orden completo de despacho:");
        gestor.ordenDespachoCompleto().forEach(p -> System.out.println("  " + p));
    }

    private void busquedaBinaria() {
        String codigo = leerTexto("Código a buscar: ");
        mostrarPaquete(gestor.busquedaBinariaPorCodigo(codigo));
    }

    private void paqueteMayorPeso() {
        mostrarPaquete(gestor.paqueteMayorPeso());
    }

    private void ordenarPaquetes() {
        System.out.println("1) Código (natural)   2) Prioridad   3) Peso   4) Tiempo");
        String opcion = leerTexto("Criterio: ");
        List<Paquete> lista = switch (opcion) {
            case "1" -> gestor.ordenNatural();
            case "2" -> gestor.ordenarPor(CriteriosPaquete.POR_PRIORIDAD_DESC);
            case "3" -> gestor.ordenarPor(CriteriosPaquete.POR_PESO_DESC);
            case "4" -> gestor.ordenarPor(CriteriosPaquete.POR_TIEMPO_ASC);
            default -> throw new IllegalArgumentException("Criterio no válido");
        };
        lista.forEach(System.out::println);
    }

    private void registrarRepartidor() {
        String id = leerTexto("Identificación: ");
        String nombre = leerTexto("Nombre: ");
        String zona = leerTexto("Zona: ");
        gestor.registrarRepartidor(new Repartidor(id, nombre, zona));
        System.out.println("Repartidor registrado.");
    }

    private void mostrarRepartidoresDisponibles() {
        List<Repartidor> disponibles = gestor.repartidoresDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println("No hay repartidores disponibles.");
        }
        disponibles.forEach(System.out::println);
    }

    private void asignarPaquete() {
        String codigo = leerTexto("Código del paquete: ");
        String id = leerTexto("Identificación del repartidor: ");
        gestor.asignarPaqueteARepartidor(codigo, id);
        System.out.println("Paquete asignado.");
    }

    private void registrarEntrega() {
        String codigo = leerTexto("Código del paquete entregado: ");
        gestor.registrarEntrega(codigo);
        System.out.println("Entrega registrada.");
    }

    private void mostrarHistorial() {
        boolean vacio = true;
        for (Paquete p : gestor.historialEntregas()) {
            System.out.println(p);
            vacio = false;
        }
        if (vacio) {
            System.out.println("Aún no hay entregas registradas.");
        }
    }

    // ---------- utilidades de entrada ----------

    private void mostrarPaquete(Optional<Paquete> paquete) {
        System.out.println(paquete.map(Paquete::toString).orElse("No existe un paquete con ese código."));
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine().trim();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero.");
            }
        }
    }

    private double leerDecimal(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje).replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número (puede usar coma decimal).");
            }
        }
    }
}