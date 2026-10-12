package co.edu.uniquindio.poo.modelo;

/**
 * Validaciones
 */
public class Validaciones {

    public static String textoObligatorio(String valor, String mensaje){
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor.trim();
    }
}