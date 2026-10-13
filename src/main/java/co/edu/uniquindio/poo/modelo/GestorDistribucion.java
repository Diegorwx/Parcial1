package co.edu.uniquindio.poo.modelo;

import co.edu.uniquindio.poo.Algoritmos.Algoritmos;
import co.edu.uniquindio.poo.quindioexpress.MiListaEnlazada;

import java.text.Collator;
import java.util.*;

public class GestorDistribucion {

    private static final Collator ORDENADOR = Collator.getInstance(Locale.forLanguageTag("es-CO"));

    // Registro de llegada y pendientes (A.1: LinkedHashMap, orden de registro + acceso por código)
    private final ArrayList<Paquete> registrados = new ArrayList<>();
    private final LinkedHashMap<String, Paquete> pendientes = new LinkedHashMap<>();
    private final LinkedHashMap<String, Paquete> indicePorCodigo = new LinkedHashMap<>();

    // Municipios (A.1: HashSet para municipios sin repetir)
    private final HashSet<String> municipios = new HashSet<>();
    private final HashMap<String, List<Paquete>> paquetesPorDestino = new HashMap<>();

    // Entregas y despacho (A.1: HashSet, PriorityQueue e historial propio)
    private final HashSet<String> entregados = new HashSet<>();
    private final PriorityQueue<Paquete> colaDespacho = new PriorityQueue<>(CriteriosPaquete.DESPACHO);
    private final MiListaEnlazada<Paquete> historial = new MiListaEnlazada<>();

    // Repartidores
    private final HashMap<String, Repartidor> repartidores = new HashMap<>();
    private final HashMap<String, String> asignaciones = new HashMap<>();

    /** Copia ordenada por código para la búsqueda binaria. Se invalida al registrar. */
    private List<Paquete> paquetesOrdenados = null;

    // ============================ PAQUETES ============================

    public void registrarPaquete(Paquete paquete) {
        Objects.requireNonNull(paquete, "El paquete no puede ser nulo");
        if (indicePorCodigo.containsKey(paquete.getCodigo())) {
            throw new IllegalArgumentException("El paquete ya ha sido registrado: " + paquete.getCodigo());
        }
        registrados.add(paquete);
        pendientes.put(paquete.getCodigo(), paquete);
        indicePorCodigo.put(paquete.getCodigo(), paquete);
        paquetesPorDestino.computeIfAbsent(paquete.getDestino(), k -> new ArrayList<>()).add(paquete);
        municipios.add(paquete.getDestino());
        colaDespacho.offer(paquete);
        paquetesOrdenados = null;
    }

    public Optional<Paquete> consultarPaquete(String codigo) {
        return Optional.ofNullable(indicePorCodigo.get(codigo));
    }

    /** Todos los paquetes registrados, en el orden exacto de llegada. */
    public List<Paquete> paquetesEnOrdenDeLlegada() {
        return Collections.unmodifiableList(registrados);
    }

    /** Paquetes que aún no se han entregado, en orden de llegada. */
    public List<Paquete> pendientesEnOrdenDeLlegada() {
        return Collections.unmodifiableList(new ArrayList<>(pendientes.values()));
    }

    // ============================ MUNICIPIOS ============================

    /** Municipios sin repetir (sin orden garantizado). */
    public Set<String> municipiosRegistrados() {
        return Collections.unmodifiableSet(municipios);
    }

    /** Municipios sin repetir y ordenados alfabéticamente. */
    public Set<String> municipiosOrdenados() {
        Set<String> ordenados = new TreeSet<>(ORDENADOR);
        ordenados.addAll(municipios);
        return Collections.unmodifiableSet(ordenados);
    }

    /** Municipios con al menos un paquete pendiente, ordenados. */
    public Set<String> municipiosPendientes() {
        Set<String> conPendientes = new TreeSet<>(ORDENADOR);
        for (Paquete paquete : pendientes.values()) {
            conPendientes.add(paquete.getDestino());
        }
        return Collections.unmodifiableSet(conPendientes);
    }

    public List<Paquete> paquetesDeMunicipio(String municipio) {
        return Collections.unmodifiableList(
                paquetesPorDestino.getOrDefault(municipio, Collections.emptyList()));
    }

    /** Peso total de un municipio. Usa recursión. */
    public double pesoTotalDeMunicipio(String municipio) {
        String m = Validaciones.nombreLugar(municipio, "El municipio no puede ser nulo ni vacío");
        return Algoritmos.pesoTotalPorDestino(paquetesPorDestino.getOrDefault(m, List.of()), m);
    }

    /** Paquetes con prioridad mayor o igual a la indicada. Usa recursión. */
    public int cantidadPrioridadMinima(int prioridadMinima) {
        return Algoritmos.contarPrioridadMinima(registrados, prioridadMinima);
    }

    // ============================ ATENCIÓN Y DESPACHO ============================

    /** Siguiente pendiente por orden de llegada. No lo retira. */
    public Optional<Paquete> siguientePorLlegada() {
        for (Paquete p : pendientes.values()) {
            if (!asignaciones.containsKey(p.getCodigo())) {
                return Optional.of(p);      // salta como máximo r asignados
            }
        }
        return Optional.empty();
    }

    /** Siguiente pendiente por prioridad, tiempo y código. No lo retira. */
    public Optional<Paquete> siguienteParaDespacho() {
        return Optional.ofNullable(colaDespacho.peek());
    }

    /** Orden completo de despacho. Trabaja sobre una copia; la cola original no cambia. */
    public List<Paquete> ordenDespachoCompleto() {
        PriorityQueue<Paquete> copia = new PriorityQueue<>(colaDespacho);
        List<Paquete> orden = new ArrayList<>(copia.size());
        while (!copia.isEmpty()) {
            orden.add(copia.poll());
        }
        return orden;
    }

    /** Búsqueda binaria por código. La copia ordenada se construye aquí si no existe. */
    public Optional<Paquete> busquedaBinariaPorCodigo(String codigo) {
        if (paquetesOrdenados == null) {
            paquetesOrdenados = new ArrayList<>(registrados);
            Collections.sort(paquetesOrdenados); // orden natural: por código
        }
        int posicion = Algoritmos.busquedaBinaria(paquetesOrdenados, codigo);
        if (posicion < 0) {
            return Optional.empty();
        }
        return Optional.of(paquetesOrdenados.get(posicion));
    }

    /** Paquete de mayor peso. Usa divide y vencerás. */
    public Optional<Paquete> paqueteMayorPeso() {
        if (registrados.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Algoritmos.mayorDivideVenceras(registrados, CriteriosPaquete.PESO_ASC));
    }

    public List<Paquete> ordenNatural() {
        return ordenarPor(Comparator.naturalOrder());
    }

    /** Copia ordenada según un criterio. No modifica el orden de llegada. */
    public List<Paquete> ordenarPor(Comparator<? super Paquete> comparador) {
        List<Paquete> copia = new ArrayList<>(registrados);
        copia.sort(comparador);
        return Collections.unmodifiableList(copia);
    }

    // ============================ REPARTIDORES Y ENTREGAS ============================

    public void registrarRepartidor(Repartidor repartidor) {
        Objects.requireNonNull(repartidor, "El repartidor no puede ser nulo");
        if (repartidores.containsKey(repartidor.getIdentificacion())) {
            throw new IllegalArgumentException(
                    "El repartidor ya ha sido registrado: " + repartidor.getIdentificacion());
        }
        repartidores.put(repartidor.getIdentificacion(), repartidor);
    }

    public List<Repartidor> repartidoresDisponibles() {
        List<Repartidor> disponibles = new ArrayList<>();
        for (Repartidor repartidor : repartidores.values()) {
            if (repartidor.isDisponible()) {
                disponibles.add(repartidor);
            }
        }
        return disponibles;
    }

    /**
     * Asigna un paquete pendiente a un repartidor.
     * Reglas: paquete pendiente y sin asignar, repartidor registrado y disponible,
     * y zona del repartidor igual al destino del paquete.
     */
    public void asignarPaqueteARepartidor(String codigoPaquete, String identificacionRepartidor) {
        Paquete paquete = pendientePorCodigo(codigoPaquete);
        if (asignaciones.containsKey(codigoPaquete)) {
            throw new IllegalStateException("El paquete " + codigoPaquete + " ya tiene repartidor asignado");
        }
        Repartidor repartidor = repartidores.get(identificacionRepartidor);
        if (repartidor == null) {
            throw new IllegalArgumentException("El repartidor no está registrado: " + identificacionRepartidor);
        }
        if (!repartidor.getZona().equals(paquete.getDestino())) {
            throw new IllegalStateException("La zona del repartidor (" + repartidor.getZona()
                    + ") no corresponde al destino del paquete (" + paquete.getDestino() + ")");
        }
        repartidor.asignar(); // lanza excepción si no está disponible
        asignaciones.put(codigoPaquete, identificacionRepartidor);
        colaDespacho.remove(paquete); // // O(n), pero solo ocurre al asignar
    }

    /** Registra la entrega: retira el paquete de pendientes y lo agrega al historial. */
    public void registrarEntrega(String codigoPaquete) {
        Paquete paquete = pendientePorCodigo(codigoPaquete);
        String identificacionRepartidor = asignaciones.get(codigoPaquete);
        if (identificacionRepartidor == null) {
            throw new IllegalStateException("El paquete " + codigoPaquete + " no tiene repartidor asignado");
        }
        asignaciones.remove(codigoPaquete);
        repartidores.get(identificacionRepartidor).liberar();
        pendientes.remove(codigoPaquete);   // O(1)
        entregados.add(codigoPaquete);
        historial.agregar(paquete);         // O(1)
    }

    /** Historial de entregas en el orden real de entrega. */
    public ColeccionGenerica<Paquete> historialEntregas() {
        return historial;
    }

    /** Devuelve el paquete si existe y aún no ha sido entregado. */
    private Paquete pendientePorCodigo(String codigo) {
        Paquete paquete = indicePorCodigo.get(codigo);
        if (paquete == null) {
            throw new IllegalArgumentException("El paquete no está registrado: " + codigo);
        }
        if (entregados.contains(codigo)) {
            throw new IllegalStateException("El paquete " + codigo + " ya fue entregado");
        }
        return paquete;
    }
}