# Parcial 1 – Gestión de operaciones de un centro de distribución (QuindíoExpress)

Estructuras de Datos · Universidad del Quindío · 2026-2

Sistema en Java que gestiona paquetes y repartidores de un centro de distribución: registro, consulta, atención por orden de llegada y por prioridad, asignación a repartidores, entregas e historial. Incluye una lista simplemente enlazada propia y algoritmos recursivos, de búsqueda binaria y de divide y vencerás.

## Integrantes

| Integrante | Usuario Git |
|---|---|
| Diego Alejandro Florez Serna | Diegorwx|
| Yesuu Esteban Aros Tique | Yesuu45 |

## Requisitos y ejecución

- JDK 25 y Maven (ver `pom.xml`).
- Compilar: `mvn compile`
- Ejecutar el programa (menú): clase `co.edu.uniquindio.poo.App.Main`
  `java -cp target/classes co.edu.uniquindio.poo.App.Main`
- Ejecutar las pruebas: clase `co.edu.uniquindio.poo.Pruebas.PruebasDistribucion`
  `java -cp target/classes co.edu.uniquindio.poo.Pruebas.PruebasDistribucion`

Al iniciar se cargan datos de ejemplo (30 paquetes, 7 destinos, 7 repartidores).

## Estructura

```
src/main/java/co/edu/uniquindio/poo/
├── App/            Main, MenuPrincipal, DatosIniciales
├── modelo/         Paquete, Repartidor, GestorDistribucion, CriteriosPaquete,
│                   ColeccionGenerica, Validaciones
├── quindioexpress/ MiListaEnlazada (lista simplemente enlazada propia)
├── Algoritmos/     Algoritmos (recursión, búsqueda binaria, divide y vencerás)
└── Pruebas/        PruebasDistribucion
```

## Estructuras de datos elegidas

| Necesidad | Estructura | Operaciones principales |
|---|---|---|
| Paquetes pendientes en orden de registro | `LinkedHashMap<String, Paquete>` | put/remove/get O(1); primero O(1) |
| Consultar y asociar código → `Paquete` | `LinkedHashMap<String, Paquete>` (registro) | containsKey/get O(1) |
| Municipios sin duplicados | `HashSet<String>` | add/contains O(1) |
| Municipios ordenados alfabéticamente | `TreeSet<String>` con `Collator` es-CO, construido al consultar | O(k log k) |
| Atender por mayor prioridad | `PriorityQueue<Paquete>` con `Comparator` (prioridad ↓, tiempo ↑, código ↑) | offer/poll O(log n); peek O(1) |
| Historial de entregas | Lista simplemente enlazada propia (con cola y contador) | agregar O(1) |

La justificación completa, con las alternativas descartadas, está en el documento PDF (Parte A).

## Algoritmos

- **Recursión:** peso total por destino y conteo de paquetes con prioridad mínima.
- **Búsqueda binaria:** por código, sobre la lista ordenada. Precondición: orden ascendente por código.
- **Divide y vencerás:** paquete de mayor peso (caso base de un elemento, división por la mitad, combinación).
