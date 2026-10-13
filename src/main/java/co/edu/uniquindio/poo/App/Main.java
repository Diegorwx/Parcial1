package co.edu.uniquindio.poo.App;

import co.edu.uniquindio.poo.modelo.GestorDistribucion;

public class Main {

    public static void main(String[] args) {
        GestorDistribucion gestor = new GestorDistribucion();
        DatosIniciales.cargar(gestor);
        new MenuPrincipal(gestor).ejecutar();
    }
}