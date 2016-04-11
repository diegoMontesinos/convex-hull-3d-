package ch3d.gw;

import geom.math.Vector;
import geom.math.VectorUtils;
import geom.structures.dcel3d.Dcel3d;
import geom.algorithms.convexHull3d.GiftWrapping;
import processing.core.PApplet;
import peasy.*;

public class GWApp extends PApplet {

  // Input
  private Vector[] input;

  // Convex Hull
  private Dcel3d convexHull;

  // Camara, ejes y puntos
  private PeasyCam camera;
  private boolean toggleAxis;
  private boolean toggleInput;

  public GWApp(int nPoints, double start, double end) {

    // Creamos la entrada para el algoritmo
    Vector min = new Vector(start, start, start);
    Vector max = new Vector(end, end, end);
    this.input = VectorUtils.randomPointsInBox(nPoints, min, max, false);

    this.convexHull = GiftWrapping.run(this.input);
  }

  @Override
  public void settings() {
    size(600, 600, P3D);

    pixelDensity(displayDensity());
    smooth();
  }

  @Override
  public void setup() {

    // Inicia la camara
    camera = new PeasyCam(this, 100);
    camera.setMinimumDistance(50);
    camera.setMaximumDistance(800);
    
    toggleAxis  = true;
    toggleInput = true;
  }

  @Override
  public void draw() {
    background(255);

    // Ejes
    if (toggleAxis) axis3D();

    // Puntos de entrada
    if (toggleInput) renderInput();

    // Convex Hull
    strokeWeight(1.0f);
    this.convexHull.draw(g, -1, color(255, 0, 0));
  }

  private void axis3D() {
    strokeWeight(1.5f);

    // X
    stroke(255, 0, 0);
    line(-width * 0.25f, 0, 0, width * 0.6f, 0, 0);

    // Y
    stroke(0, 255, 0);
    line(0, -height * 0.25f, 0, 0, height * 0.6f, 0);

    // Z
    stroke(0, 0, 255);
    line(0, 0, -height * 0.25f, 0, 0, height * 0.6f);
  }

  private void renderInput() {
    strokeWeight(3.0f);
    stroke(0);

    float x, y, z;
    for (int i = 0; i < this.input.length; i++) {
      x = (float) input[i].x;
      y = (float) input[i].y;
      z = (float) input[i].z;
      point(x, y, z);
    }
  }

  public void keyPressed() {
    switch (keyCode) {

      // I
      case 73:
        toggleInput = !toggleInput;
        break;

      // E
      case 69:
        toggleAxis = !toggleAxis;
        break;
    }
  }
}