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
