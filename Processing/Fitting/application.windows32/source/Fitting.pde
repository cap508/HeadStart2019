// Need G4P library
import g4p_controls.*;
// You can remove the PeasyCam import if you are not using
// the GViewPeasyCam control or the PeasyCam library.
import peasy.*;


static enum Shape {
  CIRCLE,
  SQUARE
}

static enum FuncType {
  LINEAR,
  QUADRATIC,
  POLY3
}

Axes axis;
Cluster c1 = new Cluster();
Cluster c2 = new Cluster();
Cluster c3 = new Cluster();
Cluster c4 = new Cluster();


Function func = new Function();

public void setup(){
 size(1024, 768);
 createGUI();
 customGUI();
 // Place your setup code here
 axis = new Axes(-10,10,-10,10,1);
 slider1.setLimits(axis.ymin, axis.ymax);
 slider1.setValue(1);
 
 slider2.setLimits(-20, 20); // initial limits on linear gradient
 slider2.setValue(1);

 slider3.setLimits(-20, 20); // initial limits 
 slider3.setValue(0);

 slider4.setLimits(-2, 2); // initial limits 
 slider4.setValue(0);
 

 c1.populate(-3, -2, 1, 30, Shape.CIRCLE);
 c2.populate(5, 2, 1, 30, Shape.SQUARE);
      
  
  
 rectMode(RADIUS);
}

// Use this method to add additional statements
// to customise the GUI controls
public void customGUI(){
    slider3.setVisible(false);
    slider4.setVisible(false);
    lbSlider3.setVisible(false);
    lbSlider4.setVisible(false);
}

public void draw(){
  background(255);
  axis.display();
  if (c1 != null){
    c1.display(axis);
    c2.display(axis);
    if (func.func == FuncType.QUADRATIC){
      c3.display(axis);
    } else if (func.func == FuncType.POLY3){
      c3.display(axis);
      c4.display(axis);
    }
  }
  func.display(axis);
 }
