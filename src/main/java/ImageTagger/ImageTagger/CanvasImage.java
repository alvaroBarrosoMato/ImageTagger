package ImageTagger.ImageTagger;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**********************************************************************
* Project           : Image Tagger
*
* Description     	: Program to create Tagged Image Sets 
*
* Author            : Alvaro Barroso Mato
*
* Date created      : 11/01/2019
*
**********************************************************************/


public class CanvasImage {
	
	private Image image;
	private BufferedImage bImage;
	private int imageWidth;
	private boolean modified = false;

	private int imageHeight;
	private String localURL;
	public int xPos = 0;
	public int yPos = 0;
	
	private float factor = 0;
	
	private int index = 0;
	private String str = "";
	
	private String modelOutput;
	public CanvasImage(String URL) {
		localURL = URL;
		
		try {
			//System.out.println(localURL);
			image = ImageIO.read(new File(localURL));
			
			bImage = ImageIO.read(new File(localURL));
			
            imageWidth = bImage.getWidth();
            imageHeight = bImage.getHeight();
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		public void checkResize(int frameWidth, int frameHeight) {
			
			modified = false;
			if((this.imageHeight < frameHeight)&&(this.imageWidth < frameWidth)) {	
				modified = false;
			}else if((this.imageWidth/frameWidth)>(this.imageHeight/frameHeight)) {
				
				factor = (float)frameWidth/(float)this.imageWidth;
				 this.imageWidth = frameWidth;
				 this.imageHeight = (int) (this.imageHeight * factor);
				 image = image.getScaledInstance( this.imageWidth, this.imageHeight, Image.SCALE_DEFAULT);
				 modified = true;
			}else if((this.imageWidth/frameWidth)<=(this.imageHeight/frameHeight)) {
				
				factor = (float)frameHeight/(float)this.imageHeight;
				 this.imageHeight = frameHeight;
				 this.imageWidth = (int) (this.imageWidth * factor);
				 image = image.getScaledInstance( this.imageWidth, this.imageHeight, Image.SCALE_DEFAULT);
				 modified = true;
			}
		}
	
	   public BufferedImage getCut(int x, int y, int i,int j) throws IOException {
	    	
	    	int cutWidth = i;
	    	int cutHeight = j;
			
	    	//Calibrando el eje para que el corte este centrado
	    	x = x - cutWidth/2;
	    	y = y - cutHeight/2;
	    	// Comprobando si la longitud del corte sobrepasa la superficie de la imagen original
	    	if((x + i)>= imageWidth){
	    		cutWidth = imageWidth - x;
	    	}
	    	if(x<0) {
	    		x=0;
	    	}
	    	if((y + j)>= imageHeight){
	    		cutHeight = imageHeight - y;
	    	}
	    	if(y<0) {
	    		y=0;
	    	}
	    	// EN el caso de que el tamano de la imagen haya sido modificado se procede a aplicar
	    	// los factores modificadores oportunos para los ejes 'y' y 'z'
	    	if(modified) {
	    		x = (int) (x / factor);
	    		cutWidth = (int) (cutWidth / factor);
	    		y = (int) (y / factor);
	    		cutHeight = (int) (cutHeight / factor);

	    	}
	        BufferedImage out = bImage.getSubimage(x, y, cutWidth, cutHeight);
	        
	        return out;
	        
	        /*
	         * 	  (x,y)
	         * 		+------------------+  ^
					|                  |  |
					|  +-----------+   |  |
					|  |           |   |  |
					|  |           |   |  | (j)
					|  |           |   |  |
					|  |           |   |  |
					|  +-----------+   |  |
					|                  |  |
					+------------------+  v
					<--------(i)------->
	         *
	         * 
	         */
	    }
	   //Usado para crear conjuentos etiquetados de Object Detection mediante Salesforce Einstein Vision.
	   public String finish() {
		   if(index == 0) {
			   
		   }else {
			   for (int i = index; i<7; i++) {
				   str += ",";

			   } 
		   }
		   
		   //System.out.println(str);
		   str += "@";
		   return str;
	   }
	   //Usado para crear conjuentos etiquetados de Object Detection mediante Salesforce Einstein Vision.
	   public void getCoordCut(int x, int y, int i,int j, String currentLabel){
		   int cutWidth = i;
	    	int cutHeight = j;
			
	    	//Calibrando el eje para que el corte este centrado
	    	x = x - cutWidth/2;
	    	y = y - cutHeight/2;
	    	// Comprobando si la longitud del corte sobrepasa la superficie de la imagen original
	    	if((x + i)>= imageWidth){
	    		cutWidth = imageWidth - x;
	    	}
	    	if(x<0) {
	    		x=0;
	    	}
	    	if((y + j)>= imageHeight){
	    		cutHeight = imageHeight - y;
	    	}
	    	if(y<0) {
	    		y=0;
	    	}
	    	// EN el caso de que el tamano de la imagen haya sido modificado se procede a aplicar
	    	// los factores modificadores oportunos para los ejes 'y' y 'z'
	    	if(modified) {
	    		x = (int) (x / factor);
	    		cutWidth = (int) (cutWidth / factor);
	    		y = (int) (y / factor);
	    		cutHeight = (int) (cutHeight / factor);

	    	}
		   if(index ==0)
		   {
			   String[] URL = localURL.split("/");
			   str += URL[URL.length-1] + "," ;			   
			   str += "\"{\"\"height\"\":" + cutHeight + ",\"\"y\"\":" + y + ",\"\"label\"\":\"\"" + currentLabel + "\"\",\"\"width\"\":" + cutWidth + ",\"\"x\"\":" + x + "}\"";
		   }
		   else {
			   
			   str += ",\"{\"\"height\"\":" + cutHeight + ",\"\"y\"\":" + y + ",\"\"label\"\":\"\"" + currentLabel + "\"\",\"\"width\"\":" + cutWidth + ",\"\"x\"\":" + x + "}\"";
		   }
		   index++;
	   }
	   public void paint(Graphics g) {
		   g.drawImage(image, xPos, yPos, imageWidth, imageHeight, null);
		   
	   }


	public String getLocalURL() {
		return localURL;
	}


	public void setLocalURL(String localURL) {
		this.localURL = localURL;
	}
	public int getImageWidth() {
		return imageWidth;
	}


	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}


	public int getImageHeight() {
		return imageHeight;
	}


	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	
	
	
}
/*
Copyright [2019] [Alvaro Barroso Mato]

Licensed under the Apache License, Version 2.0;
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/