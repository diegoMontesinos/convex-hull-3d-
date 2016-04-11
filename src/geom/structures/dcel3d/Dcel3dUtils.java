package geom.structures.dcel3d;

/**
 * Clase con algunos métodos de utilería para manejo y 
 * construcción de la estructura Double Connected Edge List (DCEL).
 * 
 * @author Diego Montesinos
 */
public class Dcel3dUtils {

  /**
   * Construye una arista completa, es decir, sus dos medias aristas.
   * Se devuelve un arreglo de dos elementos:
   * [0] = origin -> end
   * [1] = end -> origin
   *
   * @param origin el origen de la arista
   * @param end el final de la arista
   * @return HalfEdge3d[] la arista completa: es decir, dos medias aristas.
   */
  public static HalfEdge3d[] buildEdge(Vertex3d origin, Vertex3d end) {

    // Creamos las dos medias aristas con origen y final correspondientes
    HalfEdge3d he1 = new HalfEdge3d(origin, end);
    HalfEdge3d he2 = new HalfEdge3d(end, origin);

    // Relacion de gemelos
    he1.twin = he2;
    he2.twin = he1;

    // Creamos las dos medias aristas
    HalfEdge3d[] doubleEdge = { he1, he2 };
    return doubleEdge;
  }

  /**
   * Construye una cara dado su id y su componente.
   *
   * @param label     El identificador de la cara.
   * @param halfedges El componente que forma la cara en counterclockwise.
   * 
   * @return Face3d la cara construida
   */
  public static Face3d buildFace(String label, HalfEdge3d[] halfEdges) {

    // Creamos la cara (ponemos como refencia de componente a la primera media arista)
    Face3d face = new Face3d(label, halfEdges[0]);

    // Componemos prev y next
    linkComponent(halfEdges);

    // Asignamos la cara incidente
    for (int i = 0; i < halfEdges.length; i++) {
      halfEdges[i].incidentFace = face;
    }

    return face;
  }

  /**
   * Enlaza un componente, es decir, asigna el previo y el siguiente.
   *
   * @param component el componente a enlazar
   */
  public static void linkComponent(HalfEdge3d[] component) {

    // Iteramos el componente
    for (int i = 0; i < component.length; i++) {
      HalfEdge3d he = component[i];
      HalfEdge3d nextHe = component[(i + 1) % component.length];

      // Asignamos previo y siguiente
      he.next = nextHe;
      nextHe.prev = he;
    }
  }
}