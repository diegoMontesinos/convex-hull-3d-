package geom.math.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import geom.math.Vector;

public class VectorTest {

  @Test
  public void defaultConstructor() {
    Vector v = new Vector();
    assertEquals(v.x, 0.0, 0.0);
    assertEquals(v.y, 0.0, 0.0);
    assertEquals(v.z, 0.0, 0.0);
  }

  @Test
  public void byCoordinatesConstructor() {
    Vector v2d = new Vector(1.0, 1.0);
    assertEquals(v2d.x, 1.0, 0.0);
    assertEquals(v2d.y, 1.0, 0.0);
    assertEquals(v2d.z, 0.0, 0.0);

    Vector v3d = new Vector(1.0, 1.0, 1.0); 
    assertEquals(v3d.x, 1.0, 0.0);
    assertEquals(v3d.y, 1.0, 0.0);
    assertEquals(v3d.z, 1.0, 0.0);
  }

  @Test
  public void add() {
    Vector vA = new Vector(1.0, 1.0, 1.0);
    Vector vB = new Vector(3.0, 1.0, 0.0);

    vA.add(vB);
    assertEquals(vA.x, 4.0, 0.0);
    assertEquals(vA.y, 2.0, 0.0);
    assertEquals(vA.z, 1.0, 0.0);

    Vector vC = Vector.add(vA, vB);
    assertEquals(vC.x, 7.0, 0.0);
    assertEquals(vC.y, 3.0, 0.0);
    assertEquals(vC.z, 1.0, 0.0);
  }

  @Test
  public void sub() {
    Vector vA = new Vector(1.0, 1.0, 1.0);
    Vector vB = new Vector(1.0, 1.0, 1.0);

    vA.sub(vB);
    assertEquals(vA.x, 0.0, 0.0);
    assertEquals(vA.y, 0.0, 0.0);
    assertEquals(vA.z, 0.0, 0.0);

    Vector vC = Vector.sub(vA, vB);
    assertEquals(vC.x, -1.0, 0.0);
    assertEquals(vC.y, -1.0, 0.0);
    assertEquals(vC.z, -1.0, 0.0);
  }

  @Test
  public void mult() {
    Vector vA = new Vector(1.0, 1.0, 1.0);

    vA.mult(5.0);
    assertEquals(vA.x, 5.0, 0.0);
    assertEquals(vA.y, 5.0, 0.0);
    assertEquals(vA.z, 5.0, 0.0);

    Vector vB = Vector.mult(vA, 5.0);
    assertEquals(vB.x, 25.0, 0.0);
    assertEquals(vB.y, 25.0, 0.0);
    assertEquals(vB.z, 25.0, 0.0);
  }
}