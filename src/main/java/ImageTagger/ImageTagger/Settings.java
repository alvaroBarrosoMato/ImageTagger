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


public class Settings {
	CanvasImage back;
	CanvasImage background;
	CanvasImage upTime;
	CanvasImage downTime;
	CanvasImage upShots;
	CanvasImage downShots;
	
	
	Rectangle timer;
	Rectangle limitedShots;
	Rectangle restrictedFOV;
	Rectangle JSON;

	
	Options o;
	

	
	int height;
	int width;
	
	int horMargin = 30;
	int vertMargin = 30;
	
	public Settings(int Width, int Height, Options op) {
		// TODO Auto-generated constructor stub
		
		height = Height;
		width = Width;
		o = op;
		
		timer = new Rectangle();
		limitedShots = new Rectangle();
		restrictedFOV = new Rectangle();
		JSON = new Rectangle();
		
		int horSize = 500;
		int vertSize = 140;
		
		timer.setSize(horSize, vertSize);
		limitedShots.setSize(horSize, vertSize);
		restrictedFOV.setSize(horSize, vertSize);
		JSON.setSize(horSize, vertSize);
		
		timer.setLocation(horMargin, vertMargin);
		limitedShots.setLocation(horMargin, (int) (timer.y + timer.getHeight() + vertMargin));
		restrictedFOV.setLocation(horMargin, (int) (limitedShots.y + limitedShots.getHeight() + vertMargin));
		JSON.setLocation(horMargin, (int) (restrictedFOV.y + restrictedFOV.getHeight() + vertMargin));
		
		
		background = new CanvasImage(System.getProperty("user.dir")+"/sprites/background.jpg");     
		background.xPos=1;           
		background.yPos=1;
		
		back = new CanvasImage(System.getProperty("user.dir")+"/sprites/continue.png");
		back.xPos = width - back.getImageWidth() - 30;
		back.yPos = height - back.getImageHeight() - 30;
		
		upTime = new CanvasImage(System.getProperty("user.dir")+"/sprites/up.png");
		upTime.xPos = (int) (timer.getX() + timer.getWidth() + 10);
		upTime.yPos = (int) (timer.getY() + timer.getHeight()/2 - upTime.getImageHeight());
		
		downTime = new CanvasImage(System.getProperty("user.dir")+"/sprites/down.png");
		downTime.xPos = (int) (timer.getX() + timer.getWidth() + 10);
		downTime.yPos = (int) (timer.getY() + timer.getHeight()/2);
		
		upShots = new CanvasImage(System.getProperty("user.dir")+"/sprites/up.png");
		upShots.xPos = (int) (limitedShots.getX() + limitedShots.getWidth() + 10);
		upShots.yPos = (int) (limitedShots.getY() + limitedShots.getHeight()/2 - upTime.getImageHeight());
		
		downShots = new CanvasImage(System.getProperty("user.dir")+"/sprites/down.png");
		downShots.xPos = (int) (limitedShots.getX() + limitedShots.getWidth() + 10);
		downShots.yPos = (int) (limitedShots.getY() + limitedShots.getHeight()/2);
		
		
	}
	
	void paint(Graphics g){
			//background.paint(g);
		if(g!=null&&o!=null) {

			background.paint(g);
			upShots.paint(g);
			downShots.paint(g);
			upTime.paint(g);
			downTime.paint(g);
			back.paint(g);
			
			if(o.timeBar) {
				g.setColor(Color.green);
			}else {
				g.setColor(Color.red);
			}
			g.fillRect(timer.x, timer.y, timer.width, timer.height);
			if(o.disparosB) {
				g.setColor(Color.green);
			}else {
				g.setColor(Color.red);
			}
			g.fillRect(limitedShots.x, limitedShots.y, limitedShots.width, limitedShots.height);
			if(o.scopeBlock) {
				g.setColor(Color.green);
			}else {
				g.setColor(Color.red);
			}
			g.fillRect(restrictedFOV.x, restrictedFOV.y, restrictedFOV.width, restrictedFOV.height);
			if(o.isJSON) {
				g.setColor(Color.green);
			}else {
				g.setColor(Color.red);
			}
			g.fillRect(JSON.x, JSON.y, JSON.width, JSON.height);
			
			g.setColor(Color.black); 
			g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
			
			g.drawString("Timer", timer.x + 10, timer.y + timer.height/2);
			g.drawString("Limited Shots", limitedShots.x + 10, limitedShots.y + limitedShots.height/2);
			g.drawString("Restricted Field Of View", restrictedFOV.x + 10, restrictedFOV.y + restrictedFOV.height/2);
			g.drawString("JSON", JSON.x + 10, JSON.y + JSON.height/2);

			g.setColor(Color.black);
			g.drawRect(timer.x, timer.y, timer.width, timer.height);
			g.drawRect(limitedShots.x, limitedShots.y, limitedShots.width, limitedShots.height);
			g.drawRect(restrictedFOV.x, restrictedFOV.y, restrictedFOV.width, restrictedFOV.height);
			g.drawRect(JSON.x, JSON.y, JSON.width, JSON.height);
			
			
			g.drawString(o.time/1000+"", (int) (timer.x + timer.getWidth() + 50), (int) (timer.y + timer.getHeight()/2 + 10));
			g.drawString(o.numDisparos+"", (int) (limitedShots.x + limitedShots.getWidth() + 50), (int) (limitedShots.y + limitedShots.getHeight()/2 + 10));
			
			
		}
			
		
	}
	public String getClickedButton(int x, int y) {
//		if(isInArea(back, x, y)) {
//			return "MENU";
//		}else 
		if(isInRect(timer, x, y)) {
			return "TIMER";
		}else if(isInRect(limitedShots, x, y)) {
			return "LIMITEDSHOTS";
		}else if(isInRect(restrictedFOV, x, y)) {
			return "RESTRICTEDFOV";
		}else if(isInRect(JSON, x, y)) {
			return "JSON";
		}else if(isInArea(upShots, x, y)) {
			return "UPSHOTS";
		}else if(isInArea(downShots, x, y)) {
			return "DOWNSHOTS";
		}else if(isInArea(upTime, x, y)) {
			return "UPTIME";
		}else if(isInArea(downTime, x, y)) {
			return "DOWNTIME";
		}else if(isInArea(back, x, y)) {
			String filename = "sprites/settings.ser";
			try {
				FileOutputStream file = new FileOutputStream(filename); 
				ObjectOutputStream out = new ObjectOutputStream(file);
	            out.writeObject(o); 
	            out.close(); 
	            file.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return "MENU";
		}else {
			return "SETTINGS";
		}
	}
	public boolean isInArea(CanvasImage i, int x, int y) {
		return (i.xPos <= x ) && ( x <= (i.getImageWidth() + i.xPos))&&(i.yPos <= y ) && (y <= (i.getImageHeight() + i.yPos));
	}
	public boolean isInRect(Rectangle r, int x, int y) {
		return (r.x <= x ) && ( x <= (r.getWidth() + r.x))&&(r.y <= y ) && (y <= (r.getHeight() + r.y));
	}
	public Options getO() {
		return o;
	}

	public void setO(Options o) {
		this.o = o;
	}
	
}
