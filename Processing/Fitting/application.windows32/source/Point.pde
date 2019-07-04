

class Point{
  public float x;
  public float y;
  Shape shape = Shape.CIRCLE;
  
   Point(float _x, float _y){
     x = _x;
     y = _y;
  }
  
  
  void display(Axes a){

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
