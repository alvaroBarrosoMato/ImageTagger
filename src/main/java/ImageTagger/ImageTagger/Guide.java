package ImageTagger.ImageTagger;
import java.awt.Graphics;



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


public class Guide {
	CanvasImage howTo;
	CanvasImage back;
	CanvasImage background;
		
	int height;
	int width;
	
	public Guide(int Width, int Height) {
		// TODO Auto-generated constructor stub
		height = Height;
		width = Width;
		
		howTo = new CanvasImage(System.getProperty("user.dir")+"/sprites/guide.png");
		howTo.xPos = 30;
		howTo.yPos = 30;
		
		back = new CanvasImage(System.getProperty("user.dir")+"/sprites/continue.png");
		back.xPos = 30;
		back.yPos = howTo.yPos + howTo.getImageHeight() + 30;
		
		background = new CanvasImage(System.getProperty("user.dir")+"/sprites/background.jpg");     
		background.xPos=1;           
		background.yPos=1;
	}
	
	void paint(Graphics g){
			//background.paint(g);
		if(g!=null) {

			background.paint(g);
			howTo.paint(g);
			back.paint(g);
		}
			
		
	}
	public String getClickedButton(int x, int y) {
		if(isInArea(back, x, y)) {
			return "MENU";
		}else {
			return "GUIDE";
		}
	}
	public void prePlay() {
		
	}
	public boolean isInArea(CanvasImage i, int x, int y) {
		return (i.xPos <= x ) && ( x <= (i.getImageWidth() + i.xPos))&&(i.yPos <= y ) && (y <= (i.getImageHeight() + i.yPos));
	}
	
}
