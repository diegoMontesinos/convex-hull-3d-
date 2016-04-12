package ch3d;

import geom.math.Vector;
import geom.math.VectorUtils;
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
   * @param args Argumentos de la línea de comandos
   */
  public static void main(String[] args) {

    // Obtenemos el conjunto de entrada
    Vector[] input = getInputSet(args);

    // Optenemos la app que se quiere ejecutar y la ejecutamos
    PApplet app = getApp(args, input);

    String[] appletArgs = { "" };
    PApplet.runSketch(appletArgs, app);
  }

  /**
   * Obtiene el conjunto de entrada de los algoritmos.
   * 
   * @param args Argumentos de la línea de comandos
   * @return Vector[] El conjunto de entrada del algoritmo
   */
  private static Vector[] getInputSet(String[] args) {

    // Obtenemos la opción del conjunto de entrada
    String inputOption = args[1];

    // Puntos aleatorios en una caja
    if (inputOption.equals("randbox")) {

      // Parametros
      int n = getNValue(args[2]);
      Vector min = getVectorValue(args[3], 0.0);
      Vector max = getVectorValue(args[5], 100.0);

      return VectorUtils.randomPointsInBox(n, min, max, false);
    }

    // Puntos aleatorioes en una esfera
    else if (inputOption.equals("randsphere")) {

      // Parametros
      int n = getNValue(args[2]);
      Vector center = getVectorValue(args[4], 0.0);
      double radius = getVectorValue(args[6], 100.0).x;

      return VectorUtils.randomPointsInSphere(n, center, radius, false);
    }

    // Puntos en un archivo
    else if (inputOption.equals("file")) {
      String fileOption = args[7];

      if (fileOption.indexOf("${") != -1) {
        System.out.println("Se debe especificar el archivo de puntos.");
        System.exit(0);
      }
      else {

        try {
          return VectorUtils.readVectors(fileOption);
        } catch(Exception e) {
          System.out.println("Hubo un error al leer el archivo.");
          System.exit(0);
        }
      }
    }

    // Default
    else {
      return VectorUtils.randomPointsInBox(10, new Vector(0, 0, 0), new Vector(100, 100, 100), false);
    }

    return null;
  }

  /**
   * Parsea el valor del numero de puntos a generar.
   * 
   * @param  nOption La opción de los numeros de puntos.
   * @return         El número de puntos a generar.
   */
  private static int getNValue(String nOption) {
    if (nOption.equals("${n}")) {
      System.out.println("Se debe especificar el número de puntos.");
      System.exit(0);
    } else {
      return Integer.parseInt(nOption);
    }

    return 0;
  }

  /**
   * Parsea el valor de un vector.
   * 
   * @param  vectorOption La opción del vector.
   * @param  defaultValue Coordenada de default.
   * @return              El vector parseado.
   */
  private static Vector getVectorValue(String vectorOption, double defaultValue) {
    double x, y, z;

    // Valor por default
    if (vectorOption.indexOf("{") != -1) {
      x = y = z = defaultValue;
    }

    // Coordenadas
    else if (vectorOption.indexOf(",") != -1) {
      String[] coords = vectorOption.split(",");
      x = Double.parseDouble(coords[0]);
      y = Double.parseDouble(coords[1]);
      z = Double.parseDouble(coords[2]);
    }

    // Coordenadas iguales
    else {
      x = y = z = Double.parseDouble(vectorOption);
    }

    return new Vector(x, y, z);
  }

  /**
   * Obtiene la app a ejecutar en base a los argumentos dados.
   * 
   * @param   args Argumentos de la línea de comandos.
   * @param  input El conjunto de entrada.
   * @return       La aplicación a ejecutarse.
   */
  private static PApplet getApp(String[] args, Vector[] input) {
    String algorithmOption = args[0];

    // Gift wrapping
    if (algorithmOption.equals("giftwrapping") || algorithmOption.equals("gw")) {
      return new GWApp(input);
    }

    // Default
    else {
      System.out.println("Se debe especificar un algoritmo.");
      System.exit(0);
    }

    return null;
  }
}