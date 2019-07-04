// Need G4P library
import g4p_controls.*;

ArrayList<GTextField> textfields = new ArrayList<GTextField>();
PImage img;

String imgName = "TestImage.jpg";

int w = 120; // mouse box size


int MAX_SCREEN_WIDTH = 1024;
int MAX_SCREEN_HEIGHT = 768;


float [][] kernel = {{0,0,0},
                     {0,1,0},
                     {0,0,0}};

public void setup(){
  size(200,200);
//  selectInput("Select a file to work with", "fileSelected");
//  img = loadImage(imgName);

  surface.setResizable(true);
  createGUI();
  customGUI();
  imgName = G4P.selectInput("Select a file to work with",  "png,gif,jpg,jpeg", "Image files");
  if (imgName == null) imgName = "TestImage.jpg";
  
  
  img = loadImage(imgName);
  surface.setSize(min(img.width, MAX_SCREEN_WIDTH), min(img.height,MAX_SCREEN_HEIGHT));

  // Place your setup code here
  
}

public void draw(){
  
  background(230);
  if ((img.width > MAX_SCREEN_WIDTH) || (img.height > MAX_SCREEN_HEIGHT)){
    img.resize(MAX_SCREEN_WIDTH, MAX_SCREEN_HEIGHT);
  }
  image(img,0,0);
  
  
   // Calculate the small rectangle we will process
  int xstart = constrain(mouseX - w/2, 0, img.width);
  int ystart = constrain(mouseY - w/2, 0, img.height);
  int xend = constrain(mouseX + w/2, 0, img.width);
  int yend = constrain(mouseY + w/2, 0, img.height);
  int kernelsize = 3;
  loadPixels();
  
  // Begin our loop for every pixel in the smaller image
  for (int x = xstart; x < xend; x++) {
    for (int y = ystart; y < yend; y++ ) {
      color c = convolution(x, y, kernel, kernelsize, img);
      int loc = x + y*img.width;
      pixels[loc] = c;
    }
  }
  updatePixels();
  
}


public void textfield_change(GTextField source, GEvent event) { //_CODE_:textfield1:927129:
  println("textfield1 - GTextField >> GEvent." + event + " @ " + millis());
  
  if (event == GEvent.CHANGED){
    try{
      
      float f = Float.parseFloat(source.getText());
      int tag = Integer.parseInt(source.tag);
      
      kernel[tag/3][tag%3] = f;
      
      source.setLocalColorScheme(G4P.BLUE_SCHEME);
      
    } catch(NumberFormatException nfe){
      source.setLocalColorScheme(G4P.GOLD_SCHEME);
    }
  }
  
} //_CODE_:textfield1:927129:


// Use this method to add additional statements
// to customise the GUI controls
public void customGUI(){
  for (int i=0; i<9; i++){
    int row = i/3;
    int col = i%3;
    GTextField textfield = new GTextField(wdKernel, 15+col*65, 15+row*35, 60, 30, G4P.SCROLLBARS_NONE);
    textfield.setOpaque(true);
    textfield.addEventHandler(this, "textfield_change");
    textfield.tag = Integer.toString(i);
    textfields.add(textfield);
  }  
  
  updateKernelDisplay();
  
//  img = loadImage(imgName);
  
  
  GTabManager gtm = new GTabManager();
  for (int i=0; i<9; i++){
    gtm.addControl(textfields.get(i));
  }
}


public void updateKernelDisplay(){
  for (int i=0; i<9; i++){
    textfields.get(i).setText(String.valueOf(kernel[i/3][i%3]));
  }
}


color convolution(int x, int y, float[][] matrix, int matrixsize, PImage img)
{
  float rtotal = 0.0;
  float gtotal = 0.0;
  float btotal = 0.0;
  int offset = matrixsize / 2;
  for (int i = 0; i < matrixsize; i++){
    for (int j= 0; j < matrixsize; j++){
      // What pixel are we testing
      int xloc = x+i-offset;
      int yloc = y+j-offset;
      int loc = xloc + img.width*yloc;
      // Make sure we haven't walked off our image, we could do better here
      loc = constrain(loc,0,img.pixels.length-1);
      // Calculate the convolution
      rtotal += (red(img.pixels[loc]) * matrix[j][i]);
      gtotal += (green(img.pixels[loc]) * matrix[j][i]);
      btotal += (blue(img.pixels[loc]) * matrix[j][i]);
    }
  }
  // Make sure RGB is within range
  rtotal = constrain(rtotal, 0, 255);
  gtotal = constrain(gtotal, 0, 255);
  btotal = constrain(btotal, 0, 255);
  // Return the resulting color
  return color(rtotal, gtotal, btotal);
}

void keyPressed(){
  if ((key == 'e')){
    kernel[0][0] = -1;    kernel[0][1] = -1;    kernel[0][2] = -1;
    kernel[1][0] = 0;    kernel[1][1] = 0;    kernel[1][2] = 0;
    kernel[2][0] = 1;    kernel[2][1] = 1;    kernel[2][2] = 1;
    updateKernelDisplay();
  }
  if ((key == 'E')){
    kernel[0][0] = -10;    kernel[0][1] = -10;    kernel[0][2] = -10;
    kernel[1][0] = 0;    kernel[1][1] = 0;    kernel[1][2] = 0;
    kernel[2][0] = 10;    kernel[2][1] = 10;    kernel[2][2] = 10;
    updateKernelDisplay();
  }

 
  if ((key == 'L') || (key == 'l')){
    imgName = G4P.selectInput("Select a file to work with",  "png,gif,jpg,jpeg", "Image files");
    img = loadImage(imgName);
    surface.setSize(min(img.width, MAX_SCREEN_WIDTH), min(img.height, MAX_SCREEN_HEIGHT));
  }
}
