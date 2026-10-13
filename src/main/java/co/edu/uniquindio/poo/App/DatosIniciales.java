package co.edu.uniquindio.poo.App;

import co.edu.uniquindio.poo.modelo.GestorDistribucion;
import co.edu.uniquindio.poo.modelo.Paquete;
import co.edu.uniquindio.poo.modelo.Repartidor;

public final class DatosIniciales {

    private DatosIniciales() { }

    public static void cargar(GestorDistribucion gestor) {
        gestor.registrarPaquete(new Paquete("PQ731", "Armenia", 8.0, 5, 45));
        gestor.registrarPaquete(new Paquete("PQ105", "Salento", 2.5, 5, 25));
        gestor.registrarPaquete(new Paquete("PQ942", "Armenia", 3.2, 4, 20));
        gestor.registrarPaquete(new Paquete("PQ318", "Calarcá", 6.1, 2, 60));
        gestor.registrarPaquete(new Paquete("PQ567", "Salento", 1.8, 3, 35));
        gestor.registrarPaquete(new Paquete("PQ829", "Armenia", 12.0, 1, 90));
        gestor.registrarPaquete(new Paquete("PQ420", "Montenegro", 5.5, 3, 30));
        gestor.registrarPaquete(new Paquete("PQ101", "Armenia", 4.5, 2, 40));
        gestor.registrarPaquete(new Paquete("PQ203", "Salento", 3.0, 2, 50));
        gestor.registrarPaquete(new Paquete("PQ415", "Armenia", 7.2, 3, 25));
        gestor.registrarPaquete(new Paquete("PQ730", "Armenia", 2.3, 4, 15));
        gestor.registrarPaquete(new Paquete("PQ512", "Circasia", 9.4, 1, 70));
        gestor.registrarPaquete(new Paquete("PQ088", "Filandia", 0.9, 5, 10));
        gestor.registrarPaquete(new Paquete("PQ674", "Quimbaya", 15.6, 2, 110));
        gestor.registrarPaquete(new Paquete("PQ249", "Montenegro", 4.0, 4, 35));
        gestor.registrarPaquete(new Paquete("PQ833", "Circasia", 2.2, 3, 40));
        gestor.registrarPaquete(new Paquete("PQ157", "Calarcá", 6.8, 5, 55));
        gestor.registrarPaquete(new Paquete("PQ690", "Filandia", 1.4, 1, 80));
        gestor.registrarPaquete(new Paquete("PQ352", "Quimbaya", 7.7, 4, 65));
        gestor.registrarPaquete(new Paquete("PQ978", "Armenia", 0.6, 3, 20));
        gestor.registrarPaquete(new Paquete("PQ214", "Salento", 4.9, 1, 75));
        gestor.registrarPaquete(new Paquete("PQ446", "Montenegro", 10.3, 2, 95));
        gestor.registrarPaquete(new Paquete("PQ061", "Circasia", 3.6, 5, 15));
        gestor.registrarPaquete(new Paquete("PQ795", "Quimbaya", 5.2, 3, 45));
        gestor.registrarPaquete(new Paquete("PQ523", "Filandia", 2.8, 4, 30));
        gestor.registrarPaquete(new Paquete("PQ289", "Calarcá", 8.9, 2, 85));
        gestor.registrarPaquete(new Paquete("PQ612", "Armenia", 1.1, 5, 12));
        gestor.registrarPaquete(new Paquete("PQ947", "Salento", 6.4, 3, 50));
        gestor.registrarPaquete(new Paquete("PQ358", "Montenegro", 0.3, 1, 100));
        gestor.registrarPaquete(new Paquete("PQ821", "Quimbaya", 11.0, 4, 60));

        gestor.registrarRepartidor(new Repartidor("R01", "Laura Gómez", "Armenia"));
        gestor.registrarRepartidor(new Repartidor("R02", "Andrés Ríos", "Armenia"));
        gestor.registrarRepartidor(new Repartidor("R03", "Carolina Mejía", "Salento"));
        gestor.registrarRepartidor(new Repartidor("R04", "Julián Ospina", "Calarcá"));
        gestor.registrarRepartidor(new Repartidor("R05", "Daniela Torres", "Montenegro"));

        Repartidor r06 = new Repartidor("R06", "Felipe Herrera", "Circasia");
        r06.marcarNoDisponible();
        gestor.registrarRepartidor(r06);

        gestor.registrarRepartidor(new Repartidor("R07", "Sofía Vargas", "Quimbaya"));
    }
}