package geom.structures.dcel3d;

import java.util.LinkedList;

/**
 * Cara de una DCEL de tres dimensiones.
 *
 * La orientación de las aristas sigue la regla de la mano
 * derecha, es decir, si se orientan los dedos de la mano
 * derecha con las aristas, el dedo pulgar debe apuntar
 * hacia afuera de la DCEL.
 * Esto indica también la dirección del vector normal de la cara.
 *
 * @author Diego Montesinos
 */
public class Face3d {

  // Etiqueta única para identificar dentro de la DCEL
  private String label;

  // Apuntador a una media arista que compone la componente exterior
  public HalfEdge3d outerComponent;

  /**
   * Constuye una cara con su id, componente externo y sus componentes internos.
   *
   * @param label Etiqueta para identificación.
   * @param outerComponent Arista del componente externo.
   */
  public Face3d(String label, HalfEdge3d outerComponent) {

    // Guardamos las propiedades
    this.label = label;
    this.outerComponent = outerComponent;
  }

  /**
   * Obtiene la etiqueta de la cara.
   *
   * @return String La etiqueta de la cara.
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * Regresa el componente externo de la cara como un arreglo.
   *
   * @return HalfEdge3d[] El componente externo en un arreglo.
   */
  public HalfEdge3d[] getComponent() {

    // Creamos una lista para ir agregando
    LinkedList<HalfEdge3d> component = new LinkedList<HalfEdge3d>();

    // Empezamos en la arista marcada
    HalfEdge3d he = this.outerComponent;
    while(true) {
      component.add(he);
      he = he.next;
      if(he.equals(this.outerComponent))
        break;
    }

    // Regresamos como array
    return component.toArray(new HalfEdge3d[0]);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Face3d) {
      Face3d face = (Face3d) obj;
      return this.getLabel().equals(face.getLabel());
    }

    return false;
  }
}