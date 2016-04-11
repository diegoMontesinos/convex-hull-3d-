package geom.structures.dcel3d;

import java.util.TreeMap;
import java.util.Iterator;
import processing.core.PGraphics;

/**
 * Implementación de la estructura de datos Doubly-connected Edge List
 * para representar una malla de tres dimensiones que cumple la formula
 * de Euler.
 * Se mantienen tres registros: Caras, Aristas y Vertices.
 * Dado que una arista es adyacente a dos caras, una arista
 * se guarda en dos registros llamados half-edge.
 *
 * Ocupa la implementación en la JavaAPI de los árboles rojo-negro para
 * guardar los registros.
 *
 * @author Diego Montesinos
 */
public class Dcel3d {

  // Registros comunes de la DCEL
  public TreeMap<String, Vertex3d> vertices;
  public TreeMap<String, HalfEdge3d> halfEdges;
  public TreeMap<String, Face3d> faces;

  /**
   * Construye una DCEL vacía.
   * Inicia los registros de caras, aristas y vertices vacios.
   *
   */
  public Dcel3d() {

    // Inicalizamos los registros
    this.vertices  = new TreeMap<String, Vertex3d>();
    this.halfEdges = new TreeMap<String, HalfEdge3d>();
    this.faces     = new TreeMap<String, Face3d>();
  }

  /**
   * Construye una DCEL con los registros dados.
   *
   * @param vertices Registro de vertices en un árbol rojo-negro.
   * @param halfEdges Registro de aristas en un árbol rojo-negro.
   * @param faces Registro de caras en un árbol rojo-negro.
   */
  public Dcel3d(TreeMap<String, Vertex3d> vertices, TreeMap<String, HalfEdge3d> halfEdges, TreeMap<String, Face3d> faces) {

    // Asignamos los registros dados
    this.vertices = vertices;
    this.halfEdges = halfEdges;
    this.faces = faces;
  }

  /**
   * Agrega una cara al registro de caras.
   *
   * @param face La cara a agregar.
   */
  public void addFace(Face3d face) {
    if (!this.faces.containsKey(face.getLabel())) {
      this.faces.put(face.getLabel(), face);
    }
  }

  /**
   * Agrega una arista al registro de aristas.
   *
   * @param halfEdge La arista por agregar.
   */
  public void addHalfEdge(HalfEdge3d halfEdge) {
    if (!this.halfEdges.containsKey(halfEdge.getLabel())) {
      this.halfEdges.put(halfEdge.getLabel(), halfEdge);
    }
  }

  /**
   * Agrega un vertice al registro de vertices.
   *
   * @param vertex El vertice por agregar.
   */
  public void addVertex(Vertex3d vertex) {
    if (!this.vertices.containsKey(vertex.getLabel())) {
      this.vertices.put(vertex.getLabel(), vertex);
    }
  }

  /**
   * Elimina una cara del registro de caras
   *
   * @param face la cara a eliminar
   * @return Face3d la cara a eliminada
   */
  public Face3d deleteFace(Face3d face) {
    return this.faces.remove(face.getLabel());
  }

  /**
   * Elimina una arista del registro de aristas
   *
   * @param halfEdge la arista a eliminar
   * @return HalfEdge3d la arista a eliminada
   */
  public HalfEdge3d deleteHalfEdge(HalfEdge3d halfEdge) {
    return this.halfEdges.remove(halfEdge.getLabel());
  }

  /**
   * Elimina un vertice del registro de vertices
   *
   * @param vertex el vertice a eliminar
   * @return Vertex3d el vertice eliminado
   */
  public Vertex3d deleteVertex(Vertex3d vertex) {
    return this.vertices.remove(vertex.getLabel());
  }

  /**
   * Dibuja la DCEL en un sketch de Processing.
   * Esto debe hacer el dibujo en tres dimensiones.
   *
   * @param processing El sketch donde se debe pintar la DCEL.
   * @param colorfill Color para rellenar -1 si no se quiere
   * @param colorstroke Color para lineas -1 si no se quiere
   */
  public void draw(PGraphics pGraphics, int colorfill, int colorstroke) {

    // Asignamos colores
    if (colorfill == -1) {
      pGraphics.noFill();
    } else {
      pGraphics.fill(colorfill);
    }
    if (colorstroke == -1) {
      pGraphics.noStroke();
    } else {
      pGraphics.stroke(colorstroke);
    }

    // Iteramos todas las caras
    Iterator<Face3d> facesIterator = this.faces.values().iterator();
    while (facesIterator.hasNext()) {

      // Obtenemos la cara
      Face3d face = facesIterator.next();

      // Dibujamos la cara
      HalfEdge3d he = face.outerComponent;
      float x, y, z;

      pGraphics.beginShape();
      while (true) {
        x = (float) he.origin.x;
        y = (float) he.origin.y;
        z = (float) he.origin.z;

        // Dibujamos
        pGraphics.vertex(x, y, z);

        he = he.next;
        if (he.equals(face.outerComponent)) {

          // Ultimo vertice
          x = (float) he.origin.x;
          y = (float) he.origin.y;
          z = (float) he.origin.z;
          pGraphics.vertex(x, y, z);
          break;
        }
      }
      pGraphics.endShape();
    }
  }
}