package geom.structures.dcel3d;

import geom.math.Vector;

/**
 * Vertice de una DCEL de tres dimensiones.
 *
 * Hereda de la clase Vector, dado que también es un punto en el
 * espacio y facilita las operaciones de la DCEL.
 *
 * @author Diego Montesinos
 */
public class Vertex3d extends Vector {

  // Etiqueta única para identificarlo en la DCEL
  private String label;

  // Arista incidente (una de la cual es origen)
  public HalfEdge3d incidentEdge;

  /**
   * Constuye un vértice con sus coordenadas x, y, z.
   *
   * NOTA: El vértice se crea separado, es decir, sin arista incidente.
   *
   * @param x La coordenada x del vertice.
   * @param y La coordenada y del vertice.
   * @param z La coordenada z del vertice.
   */
  public Vertex3d(double x, double y, double z) {
    super(x, y, z);

    // Seteamos su arista incidente
    this.incidentEdge = null;

    // Su etiqueta está dada por sus tres coordenadas
    this.label = this.x + "," + this.y + "," + this.z;
  }
  
  /**
   * Constuye un vértice a partir de un vector de tres dimensiones.
   *
   * NOTA: El vértice se crea separado, es decir, sin arista incidente.
   *
   * @param vector El vector de tres dimensiones.
   */
  public Vertex3d(Vector vector) {
    super(vector.x, vector.y, vector.z);

    // Seteamos su arista incidente
    this.incidentEdge = null;

    // Su etiqueta está dada por sus tres coordenadas
    this.label = this.x + "," + this.y + "," + this.z;
  }

  /**
   * Devuelve la etiqueta del vertice.
   *
   * @return String La etiqueta del vertice.
   */
  public String getLabel () {
    return this.label;
  }
}
