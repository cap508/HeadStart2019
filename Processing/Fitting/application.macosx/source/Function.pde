

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
 
 float getVal(float x){
   if (func == FuncType.LINEAR){
     return ( param[0] + x*param[1] );
   } else if (func == FuncType.QUADRATIC){
     return (param[0] + x*param[1] + x*x*param[2]);
   } else {
     return (param[0] + x*param[1] + x*x*param[2] + x*x*x*param[3]);
   }
 }
 
 
 
 

}
