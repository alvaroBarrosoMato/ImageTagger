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

public class EndScreen {
	
	CanvasImage backGround;
	CanvasImage Text;
	static CanvasImage END;
	private static String inputText;

	public EndScreen(int Width, int Height) {
		
		inputText = "";
		
//		Text = new CanvasImage(System.getProperty("user.dir")+"/sprites/freePlay.png");
//		Text.xPos=((Width/2)-Text.getImageWidth());
//		Text.yPos= Text.yPos + Text.getImageHeight() + 30;
		
		END = new CanvasImage(System.getProperty("user.dir")+"/sprites/continue.png");
		END.xPos=((Width/2)+ 100 -END.getImageWidth());
		END.yPos= END.yPos + END.getImageHeight() + 80;
		
		backGround = new CanvasImage(System.getProperty("user.dir")+"/sprites/background.jpg");     
		backGround.xPos=1;           
		backGround.yPos=1;
		
	}
	
	public void paint(Graphics g, Frame f){
			//background.paint(g);
		if(g!=null) {
			backGround.paint(g);
			END.paint(g);
			
			g.setColor(Color.DARK_GRAY);
			//Text.paint(g);
		    g.setFont(new Font("TimesRoman", Font.PLAIN, 70));
		    g.setColor(Color.WHITE);
		    
			g.drawString(inputText, 100, 400);
		}
		
		//f.add(input);
		
	}
	void exit(Frame f) {
		//f.remove(input);
	}
	public static String getClickedButton(int x, int y) {
		if(isInArea(END, x, y)) {
			return "MENU";
		}else {
			return "END";
		}
	}
	public void prePlay() {
		
	}
	public static boolean isInArea(CanvasImage i, int x, int y) {
//		System.out.println(i.xPos  + " - " + x );
//		System.out.println(i.imageWidth  + " - " + x );
//		System.out.println(i.yPos  + " - " + y );
//		System.out.println(i.imageHeight  + " - " + y );

		return (i.xPos <= x ) && ( x <= (i.getImageWidth() + i.xPos))&&(i.yPos <= y ) && (y <= (i.getImageHeight() + i.yPos));
	}
	
	public static String getInputText() {
		return inputText;
	}

	public static void setInputText(String inputText2) {
		inputText = inputText2;
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