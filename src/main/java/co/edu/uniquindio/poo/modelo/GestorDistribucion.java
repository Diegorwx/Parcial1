package co.edu.uniquindio.poo.modelo;

import co.edu.uniquindio.poo.Algoritmos.Algoritmos;
import co.edu.uniquindio.poo.quindioexpress.MiListaEnlazada;

import java.text.Collator;
import java.util.*;

public class GestorDistribucion {
    private static final  Collator ORDENADOR = Collator.getInstance(Locale.forLanguageTag("es-CO"));

    private final ArrayList<Paquete> registrados = new ArrayList<>();
    private final ArrayList<Paquete> pendientes = new ArrayList<>();
    private final HashMap<String,Paquete> indicePorCodigo = new HashMap<>();
    private final HashSet<String> entregados = new HashSet<>();
    private final HashMap<String, List<Paquete>> paquetesPorDestino = new HashMap<>();
    private final HashSet<String> municipios = new HashSet<>();
    private final PriorityQueue<Paquete> colaDespacho = new PriorityQueue<>(CriteriosPaquete.DESPACHO);
    private final MiListaEnlazada<Paquete> historial = new MiListaEnlazada<>();
    private final HashMap<String,Repartidor> repartidores = new HashMap<>();
    private final HashMap<String, String> asignaciones = new HashMap<>();


    private List<Paquete> paquetesOrdenados = null;

    public void registrarPaquete(Paquete paquete){
        Objects.requireNonNull(paquete, "El paquete no puede ser nulo");
        if(indicePorCodigo.containsKey(paquete.getCodigo())){
            throw new IllegalArgumentException("El paquete ya ha sido registrado");
        }
        registrados.add(paquete);
        pendientes.add(paquete);
        indicePorCodigo.put(paquete.getCodigo(), paquete);
        paquetesPorDestino.computeIfAbsent(paquete.getDestino(), k -> new ArrayList<>()).add(paquete);
        municipios.add(paquete.getDestino());
        colaDespacho.offer(paquete);
        paquetesOrdenados = null;
    }

    public Optional<Paquete> consultarPaquete(String codigo){
        return Optional.ofNullable(indicePorCodigo.get(codigo));
    }

    public List<Paquete> paquetesEnOrdenDeLlegada(){
        return Collections.unmodifiableList(pendientes);
    }


    public Set<String> municipiosRegistrados(){
        return Collections.unmodifiableSet(municipios);
    }



    public Set<String> municipiosPendientes(){
        Set<String> conPendientes = new TreeSet<>(ORDENADOR);
        for (Paquete paquete : pendientes) {
            conPendientes.add(paquete.getDestino());
        }
        return Collections.unmodifiableSet(conPendientes);
    }


    public List<Paquete> paquetesDeMunicipio(String municipio){
        return Collections.unmodifiableList(paquetesPorDestino.getOrDefault(municipio,Collections.emptyList()));
    }

    public double pesoTotalDeMunicipio(String municipio){
        return Algoritmos.pesoTotalPorDestino(registrados, municipio);
    }

    public int cantidadPrioriuidadMinima(int prioridadMinima){
        return Algoritmos.contarPrioridadMinima(registrados, prioridadMinima);
    }


    public Optional<Paquete> siguientePorLlegada(){
        return pendientes.isEmpty() ? Optional.empty() : Optional.of(pendientes.get(0));
    }
    

    public Optional<Paquete> siguienteParDespacho(){
        return Optional.ofNullable(colaDespacho.peek());
    }

    public List<Paquete> ordenDespachoCompleto(){
        PriorityQueue<Paquete> copia = new PriorityQueue<>(colaDespacho);
        List<Paquete> orden = new ArrayList<>(copia.size());
        while (!copia.isEmpty()) {
            orden.add(copia.poll());
        }
        return orden;
    }

    public Optional<Paquete> busquedaBinariaPorCodigo(String codigo) {
        if (paquetesOrdenados == null) {
            paquetesOrdenados = new ArrayList<>(registrados);
            Collections.sort(paquetesOrdenados);        // orden natural: por código
        }
        int posicion = Algoritmos.busquedaBinaria(paquetesOrdenados, codigo);
        if (posicion < 0) {
            return Optional.empty();
        }
        return Optional.of(paquetesOrdenados.get(posicion));
    }

    public Optional<Paquete> paqueteMayorPeso(){
        if (registrados.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Algoritmos.mayorDivideVenceras(registrados, CriteriosPaquete.PESO_ASC));
    }
}
