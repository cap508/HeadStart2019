class Axes {
  
  int [] margin = {20,20,20,20}; // l, b, r, t
  int minTick = 5; // the length of a tick
  int maxTick = 10;
  
  float xmin = 0.0;
  float xmax = 20.0;
  float ymin = 0.0;
  float ymax = 20.0;
  
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
  
  void display(){
    
    xAxisPos = Math.round(((float)(height - margin[1] - margin[3])) * (ymax/(ymax-ymin)));
    yAxisPos = Math.round(((float)(width - margin[2] - margin[0])) * (1-(xmax/(xmax-xmin)))) + margin[0];
    
    line(yAxisPos, margin[1], yAxisPos, height - margin[3]); // y-axis
    line(margin[0], xAxisPos, width - margin[2], xAxisPos); // x-axis
    addXTicks();    
    addYTicks();
}
  
  void addXTicks(){
    
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
 
  void addYTicks(){
    
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
