package geom.math;

import java.util.Random;
import java.util.LinkedList;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.FileSystems;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Funciones de utileria para cálculo y operaciones con vectores.
 *
 * @author Diego Montesinos
 */
public class VectorUtils {

  /**
   * Crea un conjunto de n puntos aleatorios de tercera dimensión contenidos
   * en una caja creada por dos vectores min y max.
   *
   * Los puntos generados son tales que cada una de sus coordenadas:
   * min.x &le; x &le; max.x
   * min.y &le; y &le; max.y
   * min.z &le; z &le; max.z
   *
   * Por lo tanto el vector min debe ser menor que max por cada una de sus
   * entradas.
   * 
   * @param  n   El numero de puntos que se desean.
   * @param  min El vector minimo.
   * @param  max El vector máximo.
   * @param  generalPosition Si se desea posicion general (no colineales, ni coplanares).
   * @return Vector[] El conjunto generado.
   */
  public static Vector[] randomPointsInBox(int n, Vector min, Vector max, boolean generalPosition) {

    // Creamos un generador de números aleatorios
    Random rand = new Random();

    // Creamos el vector de rangos
    Vector range = Vector.sub(max, min);

    // Creamos el arreglo y lo llenamos
    Vector[] points = new Vector[n];
    int idx = 0;

    double x, y, z;
    Vector q;
    boolean store;
    while (idx < n) {

      // Genera un nuevo vector
      x = (rand.nextDouble() * range.x) + min.x;
      y = (rand.nextDouble() * range.y) + min.y;
      z = (rand.nextDouble() * range.z) + min.z;
      q = new Vector(x, y, z);

      // Si queremos posicion general debemos checar
      store = false;
      if (generalPosition) {

        // Si son los primeros solo se guarda
        if (idx < 2) {
          store = true;
        }

        // Se verifica la posicion general
        else {
          store = preserveGeneralPosition(points, q, idx);
        }
      }
      else {
        store = true;
      }

      // Lo guardamos
      if (store) {
        points[idx] = q;
        idx++;
      }
    }

    return points;
  }

  /**
   * Crea un conjunto de n puntos aleatorios de tercera dimensión contenidos
   * en una esfera dada por un centro y un radio.
   * 
   * @param  n               El número de puntos que se desean.
   * @param  center          El centro de la esfera.
   * @param  radius          El radio.
   * @param  generalPosition Si se desea posicion general (no colineales, ni coplanares).
   * @return                 El conjunto generado.
   */
  public static Vector[] randomPointsInSphere(int n, Vector center, double radius, boolean generalPosition) {

    // Creamos un generador de números aleatorios
    Random rand = new Random();

    // Creamos el arreglo y lo llenamos
    Vector[] points = new Vector[n];
    int idx = 0;

    double x, y, z;
    Vector q;
    boolean store;
    while (idx < n) {

      // Genera un nuevo vector en la esfera de radio 1
      x = (rand.nextDouble() * 2.0) - 1.0;
      y = (rand.nextDouble() * 2.0) - 1.0;
      z = (rand.nextDouble() * 2.0) - 1.0;
      q = new Vector(x, y, z);
      q.normalize();

      // Lo multiplicamos por un radio aleatorio y lo ponemos en el centro
      q.mult(rand.nextDouble() * radius);
      q.add(center);

      // Si queremos posicion general debemos checar
      store = false;
      if (generalPosition) {

        // Si son los primeros solo se guarda
        if (idx < 2) {
          store = true;
        }

        // Se verifica la posicion general
        else {
          store = preserveGeneralPosition(points, q, idx);
        }
      }
      else {
        store = true;
      }

      // Lo guardamos
      if (store) {
        points[idx] = q;
        idx++;
      }
    }

    return points;
  }

  private static boolean preserveGeneralPosition(Vector[] points, Vector q, int idx) {

    // Checamos que no haya tres colineales
    boolean areCollinear = false;
    for (int i = 0; i < idx; i++) {
      for (int j = 0; j < idx; j++) {
        if (i != j) {
          boolean test = Vector.areCollinear(points[i], points[j], q);
          areCollinear = areCollinear || test;
        }
      }
    }

    // No hay colineales
    if (!areCollinear) {

      // Checamos que no haya cuatro coplanares
      boolean areCoplanar = false;
      for (int i = 0; i < idx; i++) {
        for (int j = 0; j < idx; j++) {
          for (int k = 0; k < idx; k++) {
            if (i != j && j != k && i != k) {
              double volume = Vector.volume3(points[i], points[j], points[k], q);
              boolean test = -0.5 <= volume && volume <= 0.5;
              areCoplanar = areCoplanar || test; 
            }
          }
        }
      }

      // Si no hay coplanares están en posición general
      return !areCoplanar;
    }

    return false;
  }

  /**
   * Lee los puntos guardados en un archivo de texto.
   * Cada punto debe estar guardado en una línea con el siguiente formato:
   * (x,y,z)
   * 
   * @param  fileName    El nombre del archivo.
   * @return             Los puntos leídos en un arreglo.
   * 
   * @throws IOException              Si el archivo no se puede leer.
   * @throws IllegalArgumentException Si el archivo contiene puntos inválidos.
   */
  public static Vector[] readVectors(String fileName)
    throws IOException, IllegalArgumentException {

    // Obtiene el path para el archivo
    Path path = FileSystems.getDefault().getPath(fileName);

    // Crea el lector del archivo abriendolo
    Charset charset = Charset.forName("UTF-8");
    BufferedReader reader = Files.newBufferedReader(path, charset);

    // Lista de vectores que se irá llenando conforme leamos el archivo
    LinkedList<Vector> readed = new LinkedList<Vector>();
    double x, y, z;

    // Leemos el archivo linea por linea
    String line = null;
    String[] parsedLine = null;
    while ((line = reader.readLine()) != null) {

      // Limpiamos la cadena
      line = line.trim().replace("(", "").replace(")", "").trim();

      // La parseamos
      parsedLine = line.split(",");

      // Checamos que esté correcta
      if (parsedLine.length == 3) {
        x = Double.parseDouble(parsedLine[0]);
        y = Double.parseDouble(parsedLine[1]);
        z = Double.parseDouble(parsedLine[2]);

        readed.add(new Vector(x, y, z));
      } else {
        throw new IllegalArgumentException("El archivo contiene puntos inválidos");
      }
    }

    return readed.toArray(new Vector[0]);
  }

  /**
   * Escribe un arreglo de vectores en un archivo de texto.
   * 
   * @param  vectors     Los vectores a escribirse.
   * @param  fileName    El nombre del archivo.
   * 
   * @throws IOException Si no se puede escribir el archivo.
   */
  public static void saveVectors(Vector[] vectors, String fileName)
    throws IOException {

    // Obtiene el path para el archivo
    Path path = FileSystems.getDefault().getPath(fileName);

    // Creamos el escritor del archivo
    Charset charset = Charset.forName("UTF-8");
    BufferedWriter writer = Files.newBufferedWriter(path, charset);

    // Iteramos los vectores
    String vectorStr;
    for (int i = 0; i < vectors.length; i++) {

      // Obtenemos al vector como cadena y escribimos en el archivo
      vectorStr = vectors[i].toString();
      writer.write(vectorStr, 0, vectorStr.length());
      writer.newLine();
    }

    // Cerramos el escritor
    writer.close();
  }
}
