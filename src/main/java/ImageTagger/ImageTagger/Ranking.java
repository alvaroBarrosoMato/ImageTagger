package ImageTagger.ImageTagger;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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

public class Ranking {
	
	
	
	
	HighScores hs;
	

	CanvasImage back;
	
	int height;
	int width;
	
	int horMargin = 30;
	int vertMargin = 30;
	
	String nameBoxes;
	String pointBoxes;
	String cutBoxes;
	String dateBoxes;
	public Ranking(int Width, int Height, HighScores hsInput) {
		// TODO Auto-generated constructor stub
		hs = hsInput;
		height = Height;
		width = Width;
		
		nameBoxes  = "";  
		pointBoxes = ""; 
		cutBoxes   = "";   
		dateBoxes  = "";  
		
		

		
		
		
		back = new CanvasImage(System.getProperty("user.dir")+"/sprites/continue.png");
		back.xPos = width - back.getImageWidth() - 30;
		back.yPos = height - back.getImageHeight() - 30;
	}
	
	void paint(Graphics g, HighScores hsInput){
		hs = hsInput;
			//background.paint(g);
		if(g!=null&&hs.highScores!=null) {
			
			g.setColor(Color.gray);
			g.fillRect(0, 0, width, height);

			
			g.setColor(Color.black); 
			g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
			
			if(hs.highScores.size()>0) {
				for(int i = 0; i < hs.highScores.size(); i++) {
					HighScore h = hs.highScores.get(i);
					g.drawString(h.name, 50, 50 * (i+3));
					g.drawString(h.Points + "", 300,50 * (i+3));
					g.drawString(h.Cortes + "", 550, 50 * (i+3));
					g.drawString(h.Date, 800,50 * (i+3));
					
				}
				back.paint(g);
			}
			
		}
			
		
	}
	public String getClickedButton(int x, int y) {
		if(isInArea(back, x, y)) {
			return "MENU";
		}else {
			return "HIGHSCORES";
		}
	}
	public boolean isInArea(CanvasImage i, int x, int y) {
		return (i.xPos <= x ) && ( x <= (i.getImageWidth() + i.xPos))&&(i.yPos <= y ) && (y <= (i.getImageHeight() + i.yPos));
	}
	
}
