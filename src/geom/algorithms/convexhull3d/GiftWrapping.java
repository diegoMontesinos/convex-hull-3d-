package geom.algorithms.convexHull3d;

import geom.math.Vector;
import geom.structures.dcel3d.Vertex3d;
import geom.structures.dcel3d.HalfEdge3d;
import geom.structures.dcel3d.Face3d;
import geom.structures.dcel3d.Dcel3d;
import geom.structures.dcel3d.Dcel3dUtils;
import java.util.LinkedList;

/**
 * Algoritmo Envoltura de regalo (GiftWrapping) para calcular el
 * Cierre Convexo (Convex Hull) de una colección de puntos.
 *
 * El Cierre Convexo es el mínimo poliedro convexo que contiene
 * al conjunto de puntos.
 *
 * Este código está basado en la descripción del algoritmo del libro
 * Computational Geometry in C, Joseph O'Rourke.
 *
 * @author Diego Montesinos
 */
public class GiftWrapping {

  /**
   * Ejecuta el algoritmo y devuelve una malla 3D representada
   * en una DCEL.
   * 
   * @param  points  El conjunto de puntos de entrada
   * @return         El cierre convexo de los puntos, i.e. un poliedro.
   */
  public static Dcel3d run(Vector[] points) {
    if (points.length >= 4) {

      Dcel3d dcel = new Dcel3d();

      // Cola de aristas sin procesar
      LinkedList<HalfEdge3d> queue = new LinkedList<HalfEdge3d>();

      // Obtenemos la primera cara y metemos sus aristas a la cola
      Face3d face = firstFace(dcel, points);
      HalfEdge3d[] component = face.getComponent();
      for (int i = 0; i < component.length; i++) {
        queue.add(component[i]);
      }

      // La arista actual y el siguiente vertice
      HalfEdge3d currentEdge;
      Vertex3d next;

      // Mientras haya aristas en la cola
      while (!queue.isEmpty()) {

        // Desencolamos una arista
        currentEdge = queue.removeFirst();

        // Si no tiene gemela, la procesamos
        if (currentEdge.twin == null) {

          // Obtenemos el siguiente vertice
          next = nextVertex(currentEdge.origin, currentEdge.end, points, dcel);

          // Creamos la cara
          face = addWrappingFace(next, currentEdge.end, currentEdge.origin, dcel);

          // Iteramos el borde de la cara creada, metiendo las aristas
          // que aún no tengan gemelos y asociando a las que si
          HalfEdge3d he = face.outerComponent, twin = null;
          String twinId;
          while (true) {
            he = he.next;

            // Obtenemos el gemelo de la arista
            twinId = he.end.getLabel() + ":" + he.origin.getLabel();
            twin = dcel.halfEdges.get(twinId);

            // Si existe se asocian
            if (twin != null) {
              twin.twin = he;
              he.twin = twin;
            }

            // Si no existe se mete en la cola para procesarse
            else {
              queue.addLast(he);
            }

            if (he.equals(face.outerComponent)) {
              break;
            }
          }
        }
      }

      return dcel;
    }

    return null;
  }

  /**
   * Obtiene el índice en un arreglo de puntos, del punto
   * con la menor coordenada en el eje Z.
   * 
   * @param  points Arreglo de puntos
   * @return        El índice del mínimo en Z
   */
  private static int indexMinZ(Vector[] points) {
    int indexMin = -1;
    double zMin = 0.0;
    for (int i = 0; i < points.length; i++) {
      if (indexMin == -1 || points[i].z < zMin) {
        indexMin = i;
        zMin = points[i].z;
      }
    }

    return indexMin;
  }

  /**
   * Calcula, construye y agrega la primer cara del Cierre
   * Convexo (primer paso del algoritmo) a la malla.
   * Una vez calculada, construida y agregada la devuelve.
   * 
   * @param  dcel   La malla donde se agregará la cara
   * @param  points El conjunto de puntos de entrada
   * @return        La primer cara de Cierre Convexo
   */
  public static Face3d firstFace(Dcel3d dcel, Vector[] points) {

    // Vertices de la primera cara
    Vertex3d a, b, c;

    // Obtenemos el índice del punto con la menor coordenada
    int index;
    index = indexMinZ(points);

    // Creamos el primer vertice
    a = new Vertex3d(points[index]);
    dcel.addVertex(a);

    // Al segundo vertice lo encontramos con un vector auxiliar 
    // en el plano a.z Es decir (1, 0, 0)
    c = nextVertex(a, new Vector(1, 0, a.z), points, dcel);

    // Tercer vertice
    b = nextVertex(a, c, points, dcel);

    return addWrappingFace(a, b, c, dcel);
  }

  /**
   * Encuentra el siguiente vértice del Cierre Convexo con el cual 
   * se creará una nueva cara.
   * 
   * Este es el paso base del algoritmo:
   * Dada una cara F y una arista (E: a -> b), se dobla el plano generado
   * por F a lo largo de E, hasta topar con un punto.
   * Es decir, se encuentra el punto que haga el menor ángulo con respecto
   * al plano generado por F.
   * 
   * Es este nuevo punto el que formará una nueva cara del Cierre Convexo.
   * 
   * En esta implementación, encontramos a este nuevo vertice de la siguiente
   * manera:
   * Se proyectan todos los puntos sobre el plano del que es normal el vector
   * a - b, devolvemos el punto cuya proyección este a la derecha de todas
   * las demás proyecciones.
   * Con lo anterior, no hace falta calcular el plano de la cara F.
   *
   * Finalmente, si es un nuevo punto que no está en la malla 3D, se crea y
   * se agrega.
   * 
   * @param  a      Punto inicial de la arista E
   * @param  b      Punto final de la arista F
   * @param  points El conjunto de puntos de entrada
   * @param  dcel   La malla donde se agregará
   * @return        Un nuevo vértice en el Cierre Convexo
   */
  public static Vertex3d nextVertex(Vector a, Vector b, Vector[] points, Dcel3d dcel) {

    // edge = norm(b - a)
    Vector edge = Vector.sub(b, a);
    edge.normalize();

    // Índice del candidato actual
    int nextPointIndex = -1;

    // Iteramos buscando el índice del siguiente punto
    for (int i = 0; i < points.length; i++) {

      // Tomamos el punto
      Vector p = points[i];

      // Si el punto no es igual que los de la arista
      if (!p.equals(a) && !p.equals(b)) {

        // Tomamos al primer candidato
        if (nextPointIndex == -1) {
          nextPointIndex = i;
        }
        else {

          // Obtenemos la proyección del punto que estamos analizando
          // sobre el plano tangente
          Vector v = Vector.sub(p, a);
          v = Vector.sub(v, v.projectOver(edge));

          // Obtenemos al candidato actual proyectado sobre el plano tangente
          Vector candidate = Vector.sub(points[nextPointIndex], a);
          candidate = Vector.sub(candidate, candidate.projectOver(edge));

          // Obtenemos la vuelta a la derecha / izquierda con los
          // vectores proyectados
          Vector cross = candidate.cross(v);
          if (cross.dot(edge) < 0 || (cross.dot(edge) == 0 && v.z < candidate.z)) {
            nextPointIndex = i;
          }
        }
      }
    }

    // Obtenemos el siguiente punto
    Vector nextPoint = points[nextPointIndex];

    // Verificamos si ya esta en la DCEL como vertice.
    // Si no esta, lo creamos y lo agregamos a la DCEL
    String nextVertexId = nextPoint.x + "," + nextPoint.y + "," + nextPoint.z;
    Vertex3d nextVertex = dcel.vertices.get(nextVertexId);
    if (nextVertex == null) {
      nextVertex = new Vertex3d(nextPoint);
      dcel.addVertex(nextVertex);
    }

    // Regresamos el vertice
    return nextVertex;
  }

  /**
   * Crea y agrega una nueva cara al Cierre Convexo, dada por sus tres
   * vértices a, b, c.
   * Siempre se agregan caras triangulares.   
   * 
   * @param  a    Primer vértice de la cara
   * @param  b    Segundo vértice de la cara
   * @param  c    Tercer vértice de la cara
   * @param  dcel La malla donde se agregará la cara
   * @return      La cara construida
   */
  public static Face3d addWrappingFace(Vertex3d a, Vertex3d b, Vertex3d c, Dcel3d dcel) {

    // Creamos las aristas y las agregamos
    HalfEdge3d heA = new HalfEdge3d(a, b);
    HalfEdge3d heB = new HalfEdge3d(b, c);
    HalfEdge3d heC = new HalfEdge3d(c, a);
    dcel.addHalfEdge(heA);
    dcel.addHalfEdge(heB);
    dcel.addHalfEdge(heC);

    // Creamos el componente
    HalfEdge3d[] componentFace = { heA, heB, heC };
    Face3d face = Dcel3dUtils.buildFace(dcel.getNextFaceLabel(), componentFace);
    dcel.addFace(face);

    return face;
  }
}