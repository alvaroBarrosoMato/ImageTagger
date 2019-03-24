package ImageTagger.ImageTagger;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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


public class GameFrame {
	private  CanvasImage currentImage;
	private  CanvasImage Optic;
	private Rectangle timer;
	private String OPTIC_PATH;
	private  double zoom;
	private  double zoomStrength = 0.01;
	private Options o;
	
	private List<Rectangle> tagRects;
	private List<String> tags;
	String currentTag;


	private int DEFAULT_WIDTH, DEFAULT_HEIGHT;
	private int DEFAULT_TIMER_WIDTH, DEFAULT_TIMER_HEIGHT;
	private int DEFAULT_TIMER_Y = 30;
	private  Point currentMousePosition;
	private  BufferedImage lastCut;
	
	private int numShots = 0;
	private int shotLimit = 5;
	private  CanvasImage shot;
	
	private int captureCount;
	
	public GameFrame(int width, int height, List<String> Tags, Options op) {
		o = op;
		tagRects = new LinkedList<Rectangle>();
		tags = Tags;
		for(int i = 0; i<tags.size(); i++) {
			System.out.println("i -> " + i);
			tagRects.add(new Rectangle(width - 120, 100 + i*200 , 100, 100));
			//tagRects.add(new Rectangle(400, 400 , 400, 400));
		}
		currentTag = tags.get(tags.size()-1);
		setCaptureCount(0);
		DEFAULT_WIDTH	= width;
		DEFAULT_HEIGHT	= height;
		DEFAULT_TIMER_WIDTH = 500;
		DEFAULT_TIMER_HEIGHT = 30;
		OPTIC_PATH = System.getProperty("user.dir")+"/sprites/cameraFrame.png";
		
		Optic = new CanvasImage(OPTIC_PATH);
		shot = new CanvasImage(System.getProperty("user.dir")+"/sprites/shot.png");

	}
	public BufferedImage getCut(int mouseX, int mouseY) {

		if(o.disparosB) {
			numShots++;
		}
		
		try {
			//System.out.println(mouseX + " / " + mouseY);
			return currentImage.getCut(mouseX, mouseY, Optic.getImageWidth(), Optic.getImageHeight());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean prePaint(Point mousePosition, String image) {
		
		
		if(currentImage==null) {
			System.out.println(image);
			currentImage = new CanvasImage(image);
			return true;
		}
		if(!mousePosition.equals(currentMousePosition)) {
			currentMousePosition = mousePosition; 
			
			return true;
		}
		if (!currentImage.getLocalURL().equals(image)){
			
			changeImage(image);
			return true;
		}else {
			return false;
		}
		
	}
	public String getTag(Point mousePosition){
		for(int i = 0 ; i< tagRects.size(); i++) {
			if(isInRect(tagRects.get(i), mousePosition.x, mousePosition.y)) {
				currentTag = tags.get(i);
				return tags.get(i);
			}
		}
		return null;
	}
	public void zoomOptic(int offset) {
		
		zoom = 1 + (zoomStrength * offset);
		
		Optic.setImageHeight((int) (Optic.getImageHeight() * zoom));
		Optic.setImageWidth((int) (Optic.getImageWidth() * zoom));
		System.out.println(Optic.getImageHeight());
		
	}
	private void changeImage(String url) {
		numShots = 0;
		currentImage = new CanvasImage(url);
		currentImage.checkResize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		//if()
		
	}
	public void draw(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		//System.out.println("acsackla");
		if(currentImage==null) {

			//currentImage = new CanvasImage(URL);
		}
		if(Optic != null && currentMousePosition!= null) {
			Optic.xPos = (int) (currentMousePosition.getX() - (Optic.getImageWidth() / 2));
			Optic.yPos =  (int) (currentMousePosition.getY() - (Optic.getImageHeight() / 2));
		}
		currentImage.paint(g);
		g.setColor(Color.darkGray);
		g.drawRect(currentImage.xPos, currentImage.yPos, currentImage.getImageWidth(), currentImage.getImageHeight());
		g.setColor(Color.BLACK);
		g.drawRect(Optic.xPos, Optic.yPos, Optic.getImageWidth(), Optic.getImageHeight());

		//Screen Cover 
		if(o.scopeBlock) {
			g.setColor(Color.lightGray);
			
			//Estructura dividida en cuatro rectangulos alrededor de el rectangulo original.
			
			g.fillRect(Optic.xPos, 0, this.DEFAULT_WIDTH, Optic.yPos);
			
			g.fillRect(Optic.xPos + Optic.getImageWidth(), Optic.yPos, this.DEFAULT_WIDTH, this.DEFAULT_HEIGHT);
			
			g.fillRect(0, Optic.yPos + Optic.getImageHeight(), Optic.xPos + Optic.getImageWidth(), this.DEFAULT_HEIGHT);
			
			g.fillRect(0, 0, Optic.xPos, Optic.yPos + Optic.getImageHeight());

			g.drawRect(Optic.xPos, Optic.yPos, Optic.getImageWidth(), Optic.getImageHeight());

			

		}
		if(o.disparosB) {
			int yPos = 0;
				if(timer != null) {
					 yPos = DEFAULT_HEIGHT - shot.getImageHeight() - timer.height - 20;
				}else {
					 yPos = DEFAULT_HEIGHT - shot.getImageHeight() - 20;
				}
			int spacing = DEFAULT_WIDTH / ((shotLimit - numShots)+2);
			shot.xPos = 0;
			//System.out.println("Disparos -> "+ (shotLimit - numShots));
			for(int i = 0; i < (shotLimit - numShots); i++) {
				shot.xPos += spacing;
				shot.yPos = yPos;
				shot.paint(g);
			}
		}
		
		//Panel de Seleccion de Tag
		for(int i = 0 ; i< tagRects.size(); i++) {
			
			if(tags.get(i)==currentTag) {
				g.setColor(Color.green);
			}else {
				g.setColor(Color.BLACK);
			}
			g.fillRect(tagRects.get(i).x, tagRects.get(i).y, tagRects.get(i).width, tagRects.get(i).height);
			g.setColor(Color.white);
			g.drawRect(tagRects.get(i).x, tagRects.get(i).y, tagRects.get(i).width, tagRects.get(i).height);
			g.setColor(Color.white);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
			g.drawString(tags.get(i) , tagRects.get(i).x, tagRects.get(i).y + tagRects.get(i).height / 2);
		}
		
		
		drawTime(g);
		//Optic.paint(g);
		
	}
	
	public void saveImage(String tag, BufferedImage bi) throws IOException {
		if(new File(System.getProperty("user.dir")+"/"+tag).isDirectory()) {
			new File(System.getProperty("user.dir")+"/"+tag);
		}else {
			new File(System.getProperty("user.dir")+"/"+tag).mkdirs();
		}
		
		File f = new File(System.getProperty("user.dir") + "/"+ tag + "/" + tag + ".jpg");
		int fileNo = 0;
		while(f.exists()){
            fileNo++;
            f = new File(System.getProperty("user.dir") + "/"+ tag + "/" + tag + "" + fileNo + ".jpg");
        }
		//System.out.println(f.getAbsolutePath());
		ImageIO.write(bi, "jpg", f);
	}
	public void JSONcut(double d, double e, String tag) {
		if(o.disparosB) {
			numShots++;
		}
		currentImage.getCoordCut((int)d,(int) e, Optic.getImageWidth(), Optic.getImageHeight(), tag);
	}
	public String getJSONlabel() {
		return currentImage.finish(); 
	}
	public BufferedImage getLastCut() {
		return lastCut;
	}
	public void setLastCut(BufferedImage lastCut) {
		this.lastCut = lastCut;
	}
	public void setTime(double t) {
		if(t > 0) {
			//System.out.println(DEFAULT_TIMER_WIDTH+" + "+t+" = "+ (int)(DEFAULT_TIMER_WIDTH/t));
			DEFAULT_TIMER_Y = DEFAULT_HEIGHT - (DEFAULT_TIMER_HEIGHT + 30);
			int width =  (int) (DEFAULT_TIMER_WIDTH*t);
			int xpos = (int) (((DEFAULT_WIDTH-100)/2)- width/2);
			timer = new Rectangle(xpos, DEFAULT_TIMER_Y, width, DEFAULT_TIMER_HEIGHT);
			//System.out.println("t -> " + t);
			//System.out.println(xpos + "->" + DEFAULT_TIMER_Y + "->" + width + "->" + DEFAULT_TIMER_HEIGHT);
		}else {
			timer = null;
		}
		
	}
	public void drawTime(Graphics g) {
		if(timer != null) {
			//System.out.println("skclmakcnlka");
			//System.out.println(timer.x + "->" + timer.y + "->" + timer.width + "->" + timer.height);
			g.setColor(Color.BLUE);
			g.drawRect(timer.x, timer.y, timer.width, timer.height);
			g.fillRect(timer.x, timer.y, timer.width, timer.height);
		}
		
	}
	public void setScreenCover(boolean b) {
		this.o.scopeBlock = b;
	}
	public boolean isLimitedShots() {
		return o.disparosB;
	}
	public void setLimitedShots(boolean limitedShots) {
		this.o.disparosB = limitedShots;
	}
	public int getNumShots() {
		return numShots;
	}
	public void setNumShots(int numShots) {
		this.numShots = numShots;
	}
	public int getShotLimit() {
		return shotLimit;
	}
	public void setShotLimit(int shotLimit) {
		this.shotLimit = shotLimit;
	}
	public CanvasImage getShot() {
		return shot;
	}
	public void setShot(CanvasImage shot) {
		this.shot = shot;
	}
	public int getCaptureCount() {
		return captureCount;
	}
	public void setCaptureCount(int captureCount) {
		this.captureCount = captureCount;
	}
	public boolean isInRect(Rectangle r, int x, int y) {
		return (r.x <= x ) && ( x <= (r.getWidth() + r.x))&&(r.y <= y ) && (y <= (r.getHeight() + r.y));
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