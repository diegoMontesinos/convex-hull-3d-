package geom.structures.dcel3d;

/**
 * Media arista de una DCEL de tres dimensiones.
 *
 * Esta representa una parte, o un sentido de una arista
 * en una malla tridimensional.
 *
 * vi -----> vj
 *
 * Se guarda un registro de su arista gemela, es decir,
 * la que la complementa en sentido.
 *
 * Gemela:
 * vj -----> vi
 *
 * @author Diego Montesinos
 */
public class HalfEdge3d {

  // Etiqueta única para identificarlo en la DCEL
  private String label;

  // Vertice de inicio
  public Vertex3d origin;

  // Vertice de final
  public Vertex3d end;

  // Arista gemela
  public HalfEdge3d twin;

  // Arista siguiente
  public HalfEdge3d next;

  // Arista previa
  public HalfEdge3d prev;

  // Cara incidente
  public Face3d incidentFace;

  /**
   * Constuye una media arista con su origen y final.
   * Este constructor asigna automaticamente que la arista
   * incidente al origen es la creada.
   *
   * NOTA: La arista se crea separada, es decir, sin gemela, ni previo, ni siguiente.
   *
   * @param origin Vertice de origen.
   * @param end Vertice de final.
   */
  public HalfEdge3d(Vertex3d origin, Vertex3d end) {

    // Seteamos origen y final
    this.origin = origin;
    this.end = end;

    // Esta será la arista incidente del origen
    this.origin.incidentEdge = this;

    // Apuntadoreas a gemela, siguiente, previa y cara incidente
    this.twin = null;
    this.next = null;
    this.prev = null;
    this.incidentFace = null;

    // Se crea el identificador
    this.label = this.origin.getLabel() + ":" + this.end.getLabel();
  }

  /**
   * Devuelve la etiqueta de la arista.
   *
   * @return String El identificador de la arista.
   */
  public String getLabel () {
    return this.label;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof HalfEdge3d) {
      HalfEdge3d he = (HalfEdge3d) obj;
      return this.origin.equals(he.origin) && this.end.equals(he.end);
    }

    return false;
  }

  @Override
  public String toString() {
    return this.origin.toString() + " -> " + this.end.toString();
  }
}