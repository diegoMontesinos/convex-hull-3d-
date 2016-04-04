package geom.math;

/**
 * Representa un vector en dos o tres dimensiones, contiene operaciones básicas
 * de vectores.
 *
 * @author Diego Montesinos
 */
public class Vector {

  // Coordenadas
  public double x, y, z;

  /**
   * Construye el vector en el origen.
   *
   */
  public Vector() {
    this.x = this.y = this.z = 0;
  }

  /**
   * Construye un vector de dos dimensiones dadas sus coordenadas
   *
   * @param x La coordenada x del vector.
   * @param y La coordenada y del vector.
   */
  public Vector(double x, double y) {
    this.x = x;
    this.y = y;
    this.z = 0;
  }

  /**
   * Construye un vector de tres dimensiones dadas sus coordenadas
   *
   * @param x La coordenada x del vector.
   * @param y La coordenada y del vector.
   * @param z La coordenada z del vector.
   */
  public Vector(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Suma este vector con otro.
   *
   * @param v El otro vector.
   */
  public void add(Vector v) {
    this.x = this.x + v.x;
    this.y = this.y + v.y;
    this.z = this.z + v.z;
  }

  /**
   * Calcula la suma de dos vectores y lo regresa
   * como un vector nuevo.
   *
   * @param u El primer vector.
   * @param v El segundo vector.
   * @return Vector El vector suma (u + v).
   */
  public static Vector add(Vector u, Vector v) {
    return new Vector(u.x + v.x, u.y + v.y, u.z + v.z);
  }

  /**
   * Resta este vector con otro.
   *
   * @param v El otro vector.
   */
  public void sub(Vector v) {
    this.x = this.x - v.x;
    this.y = this.y - v.y;
    this.z = this.z - v.z;
  }

  /**
   * Calcula la resta de dos vectores y lo regresa
   * como un vector nuevo.
   *
   * @param u El primer vector.
   * @param v El segundo vector.
   * @return Vector El vector resta (u - v).
   */
  public static Vector sub(Vector u, Vector v) {
    return new Vector(u.x - v.x, u.y - v.y, u.z - v.z);
  }

  /**
   * Multiplicación escalar del vector.
   * Multiplica cada una entrada del vector por un escalar.
   * 
   * @param a El escalar.
   */
  public void mult(double a) {
    this.x *= a;
    this.y *= a;
    this.z *= a;
  }

  /**
   * Multiplicación escalar de un vector.
   * Multiplica cada una de las entradas del vector por un escalar
   * y regresa un vector nuevo.
   * 
   * @param  v El vector.
   * @param  a El escalar
   * @return Vector El vector por el escalar (av).
   */
  public static Vector mult(Vector v, double a) {
    return new Vector(v.x * a, v.y * a, v.z * a);
  }

  /**
   * Regresa la magnitud del vector, es decir, su longitud.
   *
   * @return double La magnitud del vector
   */
  public double mag() {
    return Math.sqrt(Math.pow(this.x, 2.0) + Math.pow(this.y, 2.0) + Math.pow(this.z, 2.0));
  }

  /**
   * Normaliza la norma del vector para que mida 1
   * (hace al vector unitario).
   */
  public void normalize() {
    double mag = this.mag();
    if(mag != 0.0f) {
      double lenInv = 1.0f / mag;

      this.x *= lenInv;
      this.y *= lenInv;
      this.z *= lenInv;
    }
  }

  /**
   * Calcula el producto punto del vector dado
   * con este vector.
   *
   * @param v El vector a operar
   * @return double El resultado del producto punto
   */
  public double dot(Vector v) {
    return (this.x * v.x) + (this.y * v.y) + (this.z * v.z);
  }

  /**
   * Calcula el vector resultante del producto cruz
   * de un vector dado con este vector.
   *
   * @param v El vector a operar.
   * @return Vector El resultante del producto cruz
   */
  public Vector cross(Vector v) {
    double x = this.y * v.z - this.z * v.y;
    double y = this.z * v.x - this.x * v.z;
    double z = this.x * v.y - this.y * v.x;

    return new Vector(x, y, z);
  }

  /**
   * Calcula el vector resultante de proyectar
   * un vector dado con este vector.
   *
   * @param v El vector a proyectar.
   * @return Vector La proyección del vector.
   */
  public Vector projectOver(Vector v) {
    double length = this.dot(v);
    double x = length * v.x;
    double y = length * v.y;
    double z = length * v.z;

    return new Vector(x, y, z);
  }

  /**
   * Evalua si tres vectores son colineales.
   * 
   * @param a Primer punto 
   * @param b Segundo punto
   * @param c Tercer punto
   * @return boolean si son colineales
   */
  public static boolean areCollinear(Vector a, Vector b, Vector c) {
    return ((c.z - a.z) * (b.y - a.y)) - ((b.z - a.z) * (c.y - a.y)) == 0.0
        && ((b.z - a.z) * (c.x - a.x)) - ((b.x - a.x) * (c.z - a.z)) == 0.0
        && ((b.x - a.x) * (c.y - a.y)) - ((b.y - a.y) * (c.x - a.x)) == 0.0;
  }

  /**
   * Calcula el volumen con signo del tetraedro formado por cuatro
   * vectores basado en el producto cruz.
   * 
   * @param a Primer vector del tetraedro
   * @param b Segundo vector del tetraedro
   * @param c Tercer vector del tetraedro
   * @param d Cuarto vector del tetreaedro
   * @return double volumen con signo calculada
   */
  public static double volume3(Vector a, Vector b, Vector c, Vector d) {

    // Transladamos los puntos para que d quede en el origen
    double ax = a.x - d.x, ay = a.y - d.y, az = a.z - d.z;
    double bx = b.x - d.x, by = b.y - d.y, bz = b.z - d.z;
    double cx = c.x - d.x, cy = c.y - d.y, cz = c.z - d.z;

    // Calculamos el volumen
    double vol = ax * (by * cz - bz * cy) +
           ay * (bz * cx - bx * cz) +
           az * (bx * cy - by * cx);
    return vol;
  }

  /**
   * Calcula el ángulo formado entre dos vectores.
   * El ángulo calculado es el menor entre el primer vector y el segundo.
   * 
   * @param  v1 El primer vector.
   * @param  v2 El segundo vector.
   * @return double El ángulo entre vectores.
   */
  public static double angleBetween(Vector v1, Vector v2) {

    // Cuidamos que no sean cero
    if (v1.x == 0 && v1.y == 0 && v1.z == 0 ) return 0.0f;
    if (v2.x == 0 && v2.y == 0 && v2.z == 0 ) return 0.0f;

    double dot = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    double v1mag = Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
    double v2mag = Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);

    double amt = dot / (v1mag * v2mag);

    if (amt <= -1) {
      return Math.PI;
    } else if (amt >= 1) {
      return 0;
    }

    return Math.acos(amt);
  }

  @Override
  public boolean equals(Object obj) {
    Vector vec = (Vector) obj;
    return this.x == vec.x && this.y == vec.y && this.z == vec.z;
  }

  @Override
  public String toString() {
    return "(" + this.x + ", " + this.y + ", " + this.z + ")";
  }
}