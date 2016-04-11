package ch3d;

import ch3d.gw.GWApp;
import processing.core.PApplet;

/**
 * Clase principal.
 *
 * Inicia la aplicación.
 */
public class Main {

  /**
   * Método principal.
   *
   * Punto de entrada de la aplicación.
   * 
   * @param args - Argumentos de la línea de comandos
   */
  public static void main(String[] args) {
    GWApp app = new GWApp(45, -100, 100);

    String[] appletArgs = { "" };
    PApplet.runSketch(appletArgs, app);
  }
}