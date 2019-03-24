package ImageTagger.ImageTagger;
import java.awt.Color;
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


public class Menu {
	CanvasImage arcade;
	CanvasImage freePlay;
	CanvasImage options;
	CanvasImage settings;
	CanvasImage highscores;	

	Boolean onMainMenu = false;
	
	int height;
	int width;
	
	public Menu(int Width, int Height) {
		// TODO Auto-generated constructor stub
		height = Height;
		width = Width;
		
		arcade = new CanvasImage(System.getProperty("user.dir")+"/sprites/arcade.png");
		//arcade.xPos=((Width/2)-arcade.getImageWidth());
		arcade.xPos= 30;
		arcade.yPos=((Height/4)-arcade.getImageHeight());
		
		freePlay = new CanvasImage(System.getProperty("user.dir")+"/sprites/freePlay.png");
		//freePlay.xPos=((Width/2)-freePlay.getImageWidth());
		freePlay.xPos=30;
		freePlay.yPos= arcade.yPos + arcade.getImageHeight() + 30;
		
		options = new CanvasImage(System.getProperty("user.dir")+"/sprites/options.png");
		//options.xPos=((Width/2)-options.getImageWidth());
		options.xPos=30;
		options.yPos= freePlay.yPos + freePlay.getImageHeight() + 30;
		
		settings = new CanvasImage(System.getProperty("user.dir")+"/sprites/settings.png");
		//options.xPos=((Width/2)-options.getImageWidth());
		settings.xPos= 30;
		settings.yPos= options.yPos + options.getImageHeight() + 30;
		
		highscores = new CanvasImage(System.getProperty("user.dir")+"/sprites/Rankings.png");
		//options.xPos=((Width/2)-options.getImageWidth());
		highscores.xPos= 30;
		highscores.yPos= settings.yPos + settings.getImageHeight() + 30;
		
		
		
	}
	
	void paint(Graphics g){
			//background.paint(g);
		if(g!=null) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, width, height);
			arcade.paint(g);
			freePlay.paint(g);
			settings.paint(g);
			options.paint(g);
			highscores.paint(g);
		}
			
		
	}
	public String getClickedButton(int x, int y) {
		if(isInArea(arcade, x, y)) {
			return "ARCADE";
		} else if(isInArea(freePlay, x, y)) {
			return "FREEMODE";
		}else if(isInArea(options, x, y)) {
			return "GUIDE";
		}else if(isInArea(settings, x, y)) {
			return "SETTINGS";
		}else if(isInArea(highscores, x, y)) {
			return "HIGHSCORES";
		}else {
			return "MENU";
		}
	}
	public void prePlay() {
		
	}
	public boolean isInArea(CanvasImage i, int x, int y) {
		return (i.xPos <= x ) && ( x <= (i.getImageWidth() + i.xPos))&&(i.yPos <= y ) && (y <= (i.getImageHeight() + i.yPos));
	}
	
}
