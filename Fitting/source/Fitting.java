import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import g4p_controls.*; 
import peasy.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Fitting extends PApplet {

// Need G4P library

// You can remove the PeasyCam import if you are not using
// the GViewPeasyCam control or the PeasyCam library.



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
class Axes {
  
  int [] margin = {20,20,20,20}; // l, b, r, t
  int minTick = 5; // the length of a tick
  int maxTick = 10;
  
  float xmin = 0.0f;
  float xmax = 20.0f;
  float ymin = 0.0f;
  float ymax = 20.0f;
  
  float unit = 1; // a major tick will appear at each unit.
  
  int xAxisPos;
  int yAxisPos;
  
  float xUnit, yUnit;
  
  // note max units = xrange*10. yrange*10 by definition.
  Axes(float _xMin, float _xMax, float _yMin, float _yMax, float _unit){
    
    // xmin is at location margin[0]
    // xmax is at location width - margin[2]
    // ymin is at height - margin[1]
    // ymax is at margin[3] 
    
    xmin = _xMin;
    xmax = _xMax;
    ymin = _yMin;
    ymax = _yMax;
    
    unit = _unit;
  }
  
  public void reset(float _xMin, float _xMax, float _yMin, float _yMax, float _unit){
    
    // xmin is at location margin[0]
    // xmax is at location width - margin[2]
    // ymin is at height - margin[1]
    // ymax is at margin[3] 
    
    xmin = _xMin;
    xmax = _xMax;
    ymin = _yMin;
    ymax = _yMax;
    
    unit = _unit;
  }
  
  public void display(){
    
    xAxisPos = Math.round(((float)(height - margin[1] - margin[3])) * (ymax/(ymax-ymin)));
    yAxisPos = Math.round(((float)(width - margin[2] - margin[0])) * (1-(xmax/(xmax-xmin)))) + margin[0];
    
    line(yAxisPos, margin[1], yAxisPos, height - margin[3]); // y-axis
    line(margin[0], xAxisPos, width - margin[2], xAxisPos); // x-axis
    addXTicks();    
    addYTicks();
}
  
  public void addXTicks(){
    
    xUnit = (width-margin[0]-margin[2])*unit/(xmax - xmin)/10;
    
    int count = 0;
    for (float i=yAxisPos; i<width-margin[2]; i+=xUnit){
      if (count%10 == 0)
        line(i, xAxisPos, i, xAxisPos+maxTick);
      else
        line(i, xAxisPos, i, xAxisPos+minTick);
      count ++;
    }
    count = 0;
    for (float i=yAxisPos; i>margin[0]; i-=xUnit){
      if (count%10 == 0)
        line(i, xAxisPos, i, xAxisPos+maxTick);
      else
        line(i, xAxisPos, i, xAxisPos+minTick);
      count ++;
    }
    

  }
 
  public void addYTicks(){
    
    yUnit = (height-margin[1]-margin[3])*unit/(ymax - ymin)/10;
    int count = 0;
    for (float i=xAxisPos; i<height-margin[1]; i+=yUnit){
      if (count%10 == 0)
        line(yAxisPos, i, yAxisPos-maxTick, i);
      else
        line(yAxisPos, i, yAxisPos-minTick, i);
      count ++;
    }
    count =0;
    for (float i=xAxisPos; i>margin[3]; i-=yUnit){
      if (count%10 == 0)
        line(yAxisPos, i, yAxisPos-maxTick, i);
      else
        line(yAxisPos, i, yAxisPos-minTick, i);
      count ++;
    }

  }
  
  
  /*
  * Note that x and y should be in the range [0, xrange], [0, yrange]
  * The Position returned is then a location on the screen
  */
  public Position getPos(float x, float y){
    Position p = new Position();
    
    p.y = xAxisPos - y/(unit/10)*yUnit;
    
    p.x = yAxisPos + x/(unit/10)*xUnit;

    return p;
     
  }
  
}
class Cluster{

  public ArrayList<Point> points = new ArrayList<Point>();
  Cluster(){
  }
  
  public void display(Axes a){
    for (int i=0; i<points.size(); i++){
      Point p = points.get(i);
      if (p != null)
        p.display(a);
    }
  }
  
  public void populate(float _x, float _y, float sd, int size, Shape s){
    points.clear();
    for (int i=0; i< size; i++){
      float x = _x + randomGaussian()*sd;
      float y = _y + randomGaussian()*sd;
      Point p = new Point(x, y);
      p.shape = s;
      points.add(p);
    }
  }
  
}


class Function{
  public FuncType func = FuncType.LINEAR;
  
  public float [] param = {0,1,0,0};
  

 public void setLinear(){
   func = FuncType.LINEAR;
 }  

  
 public void setQuadratic(){
   func = FuncType.QUADRATIC;
 }

  public void setPoly3(){
   func = FuncType.POLY3;
 }  
 
 public void setParam(int which, float val){
   param[which] = val;
 }
 
 public void display(Axes a){
   
   for (float i=a.xmin; i<=(a.xmax-a.unit/10); i+=a.unit/10){
     float y1 = getVal(i);
     float y2 = getVal(i+a.unit/10);
     
     Position p1 = a.getPos(i,y1);
     Position p2 = a.getPos(i+a.unit/10,y2);
     stroke(255,0,0);
     line(p1.x, p1.y, p2.x, p2.y);
     stroke(0,0,0);
   }
 }
 
 public float getVal(float x){
   if (func == FuncType.LINEAR){
     return ( param[0] + x*param[1] );
   } else if (func == FuncType.QUADRATIC){
     return (param[0] + x*param[1] + x*x*param[2]);
   } else {
     return (param[0] + x*param[1] + x*x*param[2] + x*x*x*param[3]);
   }
 }
 
 
 
 

}


class Point{
  public float x;
  public float y;
  Shape shape = Shape.CIRCLE;
  
   Point(float _x, float _y){
     x = _x;
     y = _y;
  }
  
  
  public void display(Axes a){

    Position p = a.getPos(x, y);
    
    
    if (shape == Shape.CIRCLE){
        fill(255,0,0);
        ellipse(p.x, p.y, 10,10);
    } else if (shape == Shape.SQUARE) {
      fill(0,0,255);
        rect(p.x, p.y, 5, 5);
    }  
    
  }
}
class Position{
 public float x;
 public float y;
}
/* =========================================================
 * ====                   WARNING                        ===
 * =========================================================
 * The code in this tab has been generated from the GUI form
 * designer and care should be taken when editing this file.
 * Only add/edit code inside the event handlers i.e. only
 * use lines between the matching comment tags. e.g.

 void myBtnEvents(GButton button) { //_CODE_:button1:12356:
     // It is safe to enter your event code here  
 } //_CODE_:button1:12356:
 
 * Do not rename this tab!
 * =========================================================
 */

synchronized public void win_draw1(PApplet appc, GWinData data) { //_CODE_:cntrlWndw:300958:
  appc.background(230);
} //_CODE_:cntrlWndw:300958:

public void ddType_click(GDropList source, GEvent event) { //_CODE_:ddType:261571:
  if (ddType.getSelectedIndex() == 0){
    axis.reset(-10,10,-10,10,1);
    slider1.setLimits(axis.ymin, axis.ymax);

    slider3.setVisible(false);
    lbSlider3.setVisible(false);
    slider4.setVisible(false);
    lbSlider4.setVisible(false);
    func.setLinear();
    
     c1.populate(-3, -2, 1, 30, Shape.CIRCLE);
     c2.populate(5, 2, 1, 30, Shape.SQUARE);
     
     
  } else if (ddType.getSelectedIndex() == 1){
    axis.reset(-10,10,-10,10,1);
    slider1.setLimits(axis.ymin, axis.ymax);

    func.setQuadratic();
    slider3.setValue(func.param[2]);
    lbSlider3.setText(nf(func.param[2],0,2));
    slider3.setVisible(true);
    lbSlider3.setVisible(true);
    slider4.setVisible(false);
    lbSlider4.setVisible(false);
    
     c1.populate(-5, 7, 1, 20, Shape.CIRCLE);
     c2.populate(3, -1, 1, 10, Shape.CIRCLE);
     c3.populate(-1, -2, 1, 7, Shape.SQUARE);
  } else if (ddType.getSelectedIndex() == 2){
    axis.reset(-10,10,-20,20,1);
    slider1.setLimits(axis.ymin, axis.ymax);

    func.setPoly3();
    slider4.setValue(func.param[3]);
    lbSlider4.setText(nf(func.param[3],0,2));
    slider3.setVisible(true);
    lbSlider3.setVisible(true);
    slider4.setVisible(true);
    lbSlider4.setVisible(true);
    
    c1.populate(-7, 2, 1, 10, Shape.CIRCLE);
    c2.populate(-3, -2, 1, 20, Shape.SQUARE);
    c3.populate(1, 2, 1, 20, Shape.CIRCLE);
    c4.populate(5, -5, 1, 20, Shape.SQUARE);
  }
} //_CODE_:ddType:261571:

public void slider1_change(GCustomSlider source, GEvent event) { //_CODE_:slider1:621782:
  func.setParam(0,slider1.getValueF());
  lbSlider1.setText(nf(slider1.getValueF(),0,2));
} //_CODE_:slider1:621782:

public void slider2_change(GCustomSlider source, GEvent event) { //_CODE_:slider2:420130:
  func.setParam(1,slider2.getValueF());
  lbSlider2.setText(nf(slider2.getValueF(),0,2));
} //_CODE_:slider2:420130:

public void slider3_change(GCustomSlider source, GEvent event) { //_CODE_:slider3:775200:
  func.setParam(2,slider3.getValueF());
  lbSlider3.setText(nf(slider3.getValueF(),0,2));
} //_CODE_:slider3:775200:

public void slider4_change(GCustomSlider source, GEvent event) { //_CODE_:slider4:263354:
  func.setParam(3,slider4.getValueF());
  lbSlider4.setText(nf(slider4.getValueF(),0,2));
} //_CODE_:slider4:263354:

public void btnCluster(GButton source, GEvent event) { //_CODE_:btnClusters:708304:
  if (func.func == FuncType.LINEAR){
     c1.populate(-3, -2, 1, 20, Shape.CIRCLE);
     c2.populate(5, 2, 1, 20, Shape.SQUARE);
  } else if (func.func == FuncType.QUADRATIC){
     c1.populate(-5, 7, 1, 20, Shape.CIRCLE);
     c2.populate(3, -1, 1, 10, Shape.CIRCLE);
     c3.populate(-1, -2, 1, 7, Shape.SQUARE);
  } else if (func.func == FuncType.POLY3){
    c1.populate(-7, 2, 1, 10, Shape.CIRCLE);
    c2.populate(-3, -2, 1, 20, Shape.SQUARE);
    c3.populate(1, 2, 1, 20, Shape.CIRCLE);
    c4.populate(5, 2, 1, 20, Shape.SQUARE);
  }

} //_CODE_:btnClusters:708304:



// Create all the GUI controls. 
// autogenerated do not edit
public void createGUI(){
  G4P.messagesEnabled(false);
  G4P.setGlobalColorScheme(GCScheme.BLUE_SCHEME);
  G4P.setMouseOverEnabled(false);
  surface.setTitle("Sketch Window");
  cntrlWndw = GWindow.getWindow(this, "Control Window", 0, 0, 240, 240, JAVA2D);
  cntrlWndw.noLoop();
  cntrlWndw.setActionOnClose(G4P.KEEP_OPEN);
  cntrlWndw.addDrawHandler(this, "win_draw1");
  ddType = new GDropList(cntrlWndw, 120, 3, 90, 80, 3, 10);
  ddType.setItems(loadStrings("list_261571"), 0);
  ddType.addEventHandler(this, "ddType_click");
  txtType = new GLabel(cntrlWndw, 3, 3, 105, 20);
  txtType.setText("Function to Fit");
  txtType.setOpaque(false);
  slider1 = new GCustomSlider(cntrlWndw, 5, 36, 187, 20, "grey_blue");
  slider1.setLimits(0.5f, 0.0f, 1.0f);
  slider1.setNumberFormat(G4P.DECIMAL, 2);
  slider1.setOpaque(false);
  slider1.addEventHandler(this, "slider1_change");
  slider2 = new GCustomSlider(cntrlWndw, 5, 62, 187, 20, "grey_blue");
  slider2.setLimits(0.5f, 0.0f, 1.0f);
  slider2.setNumberFormat(G4P.DECIMAL, 2);
  slider2.setOpaque(false);
  slider2.addEventHandler(this, "slider2_change");
  slider3 = new GCustomSlider(cntrlWndw, 5, 88, 188, 20, "grey_blue");
  slider3.setLimits(0.5f, 0.0f, 1.0f);
  slider3.setNumberFormat(G4P.DECIMAL, 2);
  slider3.setOpaque(false);
  slider3.addEventHandler(this, "slider3_change");
  slider4 = new GCustomSlider(cntrlWndw, 5, 114, 189, 20, "grey_blue");
  slider4.setLimits(0.5f, 0.0f, 1.0f);
  slider4.setNumberFormat(G4P.DECIMAL, 2);
  slider4.setOpaque(false);
  slider4.addEventHandler(this, "slider4_change");
  lbSlider1 = new GLabel(cntrlWndw, 193, 35, 43, 20);
  lbSlider1.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  lbSlider1.setText("0.0");
  lbSlider1.setOpaque(false);
  lbSlider2 = new GLabel(cntrlWndw, 193, 61, 43, 20);
  lbSlider2.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  lbSlider2.setText("0.0");
  lbSlider2.setOpaque(false);
  lbSlider3 = new GLabel(cntrlWndw, 194, 88, 43, 20);
  lbSlider3.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  lbSlider3.setText("0.0");
  lbSlider3.setOpaque(false);
  lbSlider4 = new GLabel(cntrlWndw, 195, 113, 43, 20);
  lbSlider4.setTextAlign(GAlign.CENTER, GAlign.MIDDLE);
  lbSlider4.setText("0.0");
  lbSlider4.setOpaque(false);
  btnClusters = new GButton(cntrlWndw, 70, 201, 80, 30);
  btnClusters.setText("New Clusters");
  btnClusters.addEventHandler(this, "btnCluster");
  cntrlWndw.loop();
}

// Variable declarations 
// autogenerated do not edit
GWindow cntrlWndw;
GDropList ddType; 
GLabel txtType; 
GCustomSlider slider1; 
GCustomSlider slider2; 
GCustomSlider slider3; 
GCustomSlider slider4; 
GLabel lbSlider1; 
GLabel lbSlider2; 
GLabel lbSlider3; 
GLabel lbSlider4; 
GButton btnClusters; 
  public void settings() {  size(1024, 768); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Fitting" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
