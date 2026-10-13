package co.edu.uniquindio.poo.modelo;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import co.edu.uniquindio.poo.quindioexpress.MiListaEnlasada;

public class GestorDistribucion {
    private static final  Collator ORDENADOR = Collator.getInstance("es,CO");

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


    private List<String> codigosOrdenados = null;

    public void registrarPaquete(Paquete paquete){
        Object.requireNonNull(paquete, "El paquete no puede ser nulo");
        if(indicePorCodigo.containsKey(paquete.getCodigo())){
            throw new IllegalArgumentException("El paquete ya ha sido registrado");
        }
        registrados.add(paquete);
        pendientes.add(paquete);
        indicePorCodigo.put(paquete.getCodigo(), paquete);
        paquetesPorDestino.computeIfAbsent(paquete.getDestino(), k -> new ArrayList<>()).add(paquete);
        municipios.add(paquete.getDestino());
        colaDespacho.offer(paquete);
        codigosOrdenados = null;
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

    public Optional<Paquete> busquedaBinariaPorCodigo(String codigo){
        if (codigosOrdenados == null) {
            codigosOrdenados = new ArrayList<>(indicePorCodigo.keySet());
            Collections.sort(codigosOrdenados, ORDENADOR);
        }
        int posicion = Algortitmos.busquedaBinaria(codigosOrdenados, codigo, ORDENADOR);
        if (posicion < 0) {
            return Optional.empty();
        }
        return Optional.of(indicePorCodigo.get(codigosOrdenados.get(posicion)));
    }

    public Optional<Paquete> paqueteMayorPeso(){
        if (registrados.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Algoritmos.mayorDivideVenceras(registrados, CriteriosPaquete.PESO));
    }
}
