package co.edu.uniquindio.poo.modelo;

/**
 * Validaciones
 */
public class Validaciones {

    private Validaciones(){}

    public static String textoObligatorio(String valor, String mensaje){
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor.trim();
    }

    public static String nombreLugar(String valor, String mensaje) {
        String limpio = textoObligatorio(valor, mensaje).replaceAll("\\s+", " ");
        StringBuilder sb = new StringBuilder();
        for (String palabra : limpio.split(" ")) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(palabra.substring(0, 1).toUpperCase())
                    .append(palabra.substring(1).toLowerCase());
        }
        return sb.toString();
    }
}