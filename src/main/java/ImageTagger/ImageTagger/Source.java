package ImageTagger.ImageTagger;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
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


public class Source {
	
	CanvasImage Text;
	static CanvasImage continuar;
	//TextField input = new TextField();
	private int height;
	private int width;
	private String inputText;
	public Source(int Width, int Height) {
		// TODO Auto-generated constructor stub
		height = Height;
		width = Width;
		inputText = "";
		
		
		Text = new CanvasImage(System.getProperty("user.dir")+"/sprites/source.png");
		Text.xPos = 50;
		Text.yPos= 50;
		
		continuar = new CanvasImage(System.getProperty("user.dir")+"/sprites/continue.png");
		continuar.xPos= 30;
		continuar.yPos= Text.yPos + Text.getImageHeight() + 500;
		
		
	}
	
	public void paint(Graphics g, Frame f){
		if(g!=null) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, width, height);
			continuar.paint(g);
			Text.paint(g);
		    g.setFont(new Font("TimesRoman", Font.PLAIN, 70));
		    g.setColor(Color.WHITE);
			g.drawString(inputText, 100, 400);
		}
	}
	public static String getClickedButton(int x, int y) {
		if(isInArea(continuar, x, y)) {
			return "TAG";
		}else {
			return "SRC";
		}
	}
	public static boolean isInArea(CanvasImage i, int x, int y) {
		return (i.xPos <= x ) && ( x <= (i.getImageWidth() + i.xPos))&&(i.yPos <= y ) && (y <= (i.getImageHeight() + i.yPos));
	}

	public String getInputText() {
		return inputText;
	}

	public void setInputText(String inputText) {
		this.inputText = inputText;
	}
	
	
}
