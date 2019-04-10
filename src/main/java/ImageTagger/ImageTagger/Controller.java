package ImageTagger.ImageTagger;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;


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


public class Controller implements Runnable {
	
	//Canvas and screen
	
	public Frame frame;
	public JPanel jp;
	public Canvas canvas;
	private BufferStrategy bStrat;
	private Graphics graphics;
	
	public boolean paint = true;
	
	public int DEFAULT_WIDTH, DEFAULT_HEIGHT;
	public String title = "ImageTagger";
	
		//GameScreen
			private GameFrame gameFrame;
			Menu menu;	
			Source srcMenu;
			Tag tagMenu;
			EndScreen endScreen;
			Guide guideScreen;
			Settings settingsScreen;
			Ranking rankingScreen;
	//Functionalities
		
		// files and directories
			private String theme = "";
			private String currentTag = "";
			private List<String> tags;
			private String myDirectoryPath;
			private String currentImage;
			private int imageIndex = -1;
			private File[] directoryListing;
			
		//Logical and Funtion
			private Thread t;
			private boolean running;
			
			//Mouse
			public Point mouse;
			public  Point mouseExit;
			public Point enterPos;
			int zoomCorrection = 0;
			String inputText;
		//Settings
			public boolean arcade;
			//Barra de tiempo
			//public boolean timeBar = false;
			private long time;
			private long lastRender;
			//cobertura de la pantalla
//			public boolean screenCover = true;
//			public boolean timerB = false;
//			public boolean limitedShotsB = false;
			String JSONlabel;
			Options options;
			

			String highscoreURL;
			HighScores highscores;
	
	//ScreenTypes
	private final String MENU = "MENU";
	private final String SETSRC = "SRC";
	private final String SETTAG = "TAG";
	private final String GAME = "FREEMODE";
	private final String GUIDE = "GUIDE";
	private final String SETTINGS = "SETTINGS";
	private final String ARCADE = "ARCADE";
	private final String ENDGAME = "END";
	private final String HIGHSCORES = "HIGHSCORES";
	
	private String currentScreen;
	
	
	
	public Controller(String title, int height, int width ) {
		
		highscoreURL = "https://highscores-imagetagger.herokuapp.com";
		String filename = "sprites/settings.ser";
        tags = new LinkedList<String>();
		try
        {    
            FileInputStream file = new FileInputStream(filename); 
            ObjectInputStream in = new ObjectInputStream(file);
            options = (Options)in.readObject(); 
            in.close(); 
            file.close(); 
        } 
		catch(IOException | ClassNotFoundException ex) 
        { 
            System.out.println("IOException is caught"); 
        }
//		options = new Options(); 
		
        
		//options = new Options();
		JSONlabel = "";
		
		DEFAULT_WIDTH = height;
		DEFAULT_HEIGHT = width;
		this.title = title;
	}
	
	
	public void run(){	// Metodo Principal, alberga el algoritmo y la secuencializacion principal del juego ademas de inicializar los procesos
        
		
		init();
		while (running) {
			update();
			render();
            try {
				Thread.sleep(33);					// Se duerme el hilo 33 milisegundos para mantener un bucle de actualizaciones estable 
			} catch (InterruptedException e) {		// se limitan las iteraciones a 30 por segundo 33 x 30 =  
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		stop();
		
	}
	
	
	



	
	public synchronized void start() {
		if (running == true)
			return;
		running = true;
		t = new Thread(this);
		t.start();
	}

	private void init() {
		
		//Init Variables
		myDirectoryPath = System.getProperty("user.dir");
		mouseExit = new Point(0, 0); 
		mouse = new Point(0, 0);
		inputText = "";
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		 
		
		frame = new Frame();
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLayout(new BorderLayout());
	    frame.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		frame.setResizable(true);
		frame.setTitle(title);
		frame.addWindowListener(new WindowListener() {
			
			
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub				
				running=false;
				

			}

			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		canvas = new Canvas();
		

	    canvas.setPreferredSize(new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT));
	    //canvas.setMaximumSize(new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT));
	    //canvas.setMinimumSize(new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT));
	    
	    frame.add(canvas);
	    initListeners();
	    frame.setVisible(true);
	    
		launchMenu();
		
	}
	private void update() {
		// TODO Auto-generated method stub
		//System.out.println(mouse.toString());
		
		
//		System.out.println(currentScreen);
		if(currentScreen.equals(MENU)) {
			
			if(mouseExit != null && (mouseExit.getX()!=0)) {

				currentScreen = menu.getClickedButton((int)mouseExit.getX(), (int)mouseExit.getY());
				//System.out.println("currentScreen -> " + currentScreen + " / paint -> " + paint);
				
				
				if(currentScreen!=MENU) {
					System.out.println(currentScreen);
					if(currentScreen.equals(ARCADE)) {
						arcade = true;
						currentScreen = SETSRC;
					}else if(currentScreen.equals(GAME)) {
						arcade = false;
						currentScreen = SETSRC;
					}else {
						arcade = false;
					}
					paint = true;
				}
				mouseExit = new Point(0,0);
			}
			
		}else if(currentScreen.equals(GAME)){
			if((mouseExit.getX()!=0) && (mouseExit.getY()!=0)) {
				if(gameFrame.getTag(mouse)!=null) {
					currentTag = gameFrame.getTag(mouse);
				}else {
					gameFrame.setCaptureCount(gameFrame.getCaptureCount()+1);
					if(!options.isJSON) {
						BufferedImage bi = gameFrame.getCut((int)mouseExit.getX(), (int)mouseExit.getY());
					try {
						gameFrame.saveImage(currentTag, bi);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}else {
						gameFrame.JSONcut(mouseExit.getX(), mouseExit.getY(), currentTag);
					}
				}	
				

				
				mouseExit = new Point(0,0);
			}
			paint = gameFrame.prePaint(mouse, currentImage);
			
			
			if(zoomCorrection != 0) {
				//System.out.println(zoomCorrection);
				gameFrame.zoomOptic(zoomCorrection);
				zoomCorrection = 0;
				paint = true;
			}
			//System.out.println("arcade -> "+arcade);
			if(arcade) {
				
				if(options.timeBar) {
					if(System.currentTimeMillis() > (lastRender+1000)) {
						gameFrame.setTime((options.time - (float)(System.currentTimeMillis() - time))/options.time);
						paint = true;
						lastRender = System.currentTimeMillis();
					}
					if((System.currentTimeMillis() - time) > options.time) {
						getNextImage();
					}
				}
				if(options.scopeBlock) {
					gameFrame.setScreenCover(options.scopeBlock);
				}
				if(options.disparosB) {
					gameFrame.setLimitedShots(options.disparosB);
					gameFrame.setShotLimit(options.numDisparos);;
					if(gameFrame.getNumShots()==options.numDisparos) {
						getNextImage();
					}
				}
				
			}else {
				gameFrame.setTime(-100); //no pintamos
			}
			
		}else if(currentScreen.equals(SETSRC)){
			if(mouseExit != null && (mouseExit.getX()!=0)) {
				
				currentScreen = Source.getClickedButton((int)mouseExit.getX(), (int)mouseExit.getY());
				theme = inputText;
				if(currentScreen!=SETSRC && directoryListing!=null) {
					paint = true;
					inputText = "";
				}else if(directoryListing==null) {
					
					//System.out.println("Hola");
					
					getDirectory();
					
					if(directoryListing!=null) {
						paint = true;
						inputText = "";
						currentScreen=SETTAG;
					}else {
						currentScreen=SETSRC;
						inputText = "";
					}
				}
				mouseExit = new Point(0,0);
				//System.out.println("currentScreen -> " + currentScreen + " / paint -> " + paint);

			}
			
			//System.out.println(srcMenu.getInputText() + " / " + inputText);

			if(srcMenu.getInputText() != inputText) {
				//System.out.println("-> "+ inputText + " / " + srcMenu.getInputText());
				srcMenu.setInputText(inputText);
				paint = true;
			}
							
			
		}else if(currentScreen.equals(SETTAG)){
			if(mouseExit != null && (mouseExit.getX()!=0)) {
				currentScreen = Tag.getClickedButton((int)mouseExit.getX(), (int)mouseExit.getY());
				
				if(inputText!=null && inputText!="") {
					currentTag = inputText;
					tags.add(currentTag);
				}
				if(tags.isEmpty()) {
					currentScreen = SETTAG;
				}
				if(currentScreen=="NEXT") {
					paint = true;
					inputText = "";
					currentScreen = SETTAG;
				}else if(currentScreen!=SETTAG) {
					paint = true;
					inputText = "";
				}
				System.out.println(tags.toString());
				mouseExit = new Point(0,0);
			}
			
			//System.out.println(srcMenu.getInputText() + " / " + inputText);

			if(tagMenu.getInputText() != inputText) {
				//System.out.println("-> "+ inputText + " / " + srcMenu.getInputText());
				tagMenu.setInputText(inputText);
				paint = true;
			}		
			
		}else if(currentScreen.equals(ENDGAME)){
			
			if(mouseExit != null && (mouseExit.getX()!=0)) {
				
				currentScreen = EndScreen.getClickedButton((int)mouseExit.getX(), (int)mouseExit.getY());
				mouseExit = new Point(0,0);
				if(currentScreen!=ENDGAME) {
					if(options.isJSON) {
						labelJSONOutput();
					}
					Date date = new Date();
				    String strDateFormat = "dd/MM/yyyy HH:mm:ss";
				    DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
				    String formattedDate= dateFormat.format(date);
				    
				    int bonus = options.numDisparos + 100000/options.time;
				    
					HighScore hs = new HighScore(inputText, gameFrame.getCaptureCount()*bonus, formattedDate, gameFrame.getCaptureCount());
					RestTemplate restTemplate = new RestTemplate();
					//POST
					restTemplate.postForLocation(highscoreURL + "/highscore", hs);
					
					
					
					inputText = "";
					paint = true;
					reset();
				}
				mouseExit = new Point(0,0);

			}
			if(EndScreen.getInputText() != inputText) {
				//System.out.println("-> "+ inputText + " / " + srcMenu.getInputText());
				EndScreen.setInputText(inputText);
				paint = true;
			}
							
			
		}else if(currentScreen.equals(GUIDE)){
			if(mouseExit != null && (mouseExit.getX()!=0)) {
				
				currentScreen = guideScreen.getClickedButton((int)mouseExit.getX(), (int)mouseExit.getY());
				mouseExit = new Point(0,0);
				if(currentScreen!=GUIDE) {
					reset();
					paint = true;
				}
				

			}
							
			
		}else if( currentScreen.equals(HIGHSCORES)){
			if(mouseExit != null && (mouseExit.getX()!=0)) {
				
				currentScreen = rankingScreen.getClickedButton((int)mouseExit.getX(), (int)mouseExit.getY());
				mouseExit = new Point(0,0);
				if(currentScreen!=HIGHSCORES) {
					
					paint = true;
				}
				

			}
							
			
		}else if(currentScreen.equals(SETTINGS)){
			if(mouseExit != null && (mouseExit.getX()!=0)) {
				
				currentScreen = settingsScreen.getClickedButton((int)mouseExit.getX(), (int)mouseExit.getY());
				mouseExit = new Point(0,0);
				if(currentScreen!=SETTINGS) {
					paint = true;
				}
				if(currentScreen == "TIMER") {
					currentScreen = SETTINGS;
					if(options.timeBar) {
						options.timeBar = false;
					}else {
						options.timeBar = true;
					}
				}else if(currentScreen == "LIMITEDSHOTS") {
					currentScreen = SETTINGS;
					if(options.disparosB) {
						options.disparosB = false;
					}else {
						options.disparosB = true;
					}
				}else if(currentScreen == "RESTRICTEDFOV") {
					currentScreen = SETTINGS;
					if(options.scopeBlock) {
						options.scopeBlock = false;
					}else {
						options.scopeBlock = true;
					}
				}else if(currentScreen == "JSON") {
					currentScreen = SETTINGS;
					if(options.isJSON) {
						options.isJSON = false;
					}else {
						options.isJSON = true;
					}
				}else if(currentScreen == "UPSHOTS") {
					currentScreen = SETTINGS;
					options.numDisparos++;
				}else if(currentScreen == "DOWNSHOTS") {
					currentScreen = SETTINGS;
					options.numDisparos--;
				}else if(currentScreen == "UPTIME") {
					currentScreen = SETTINGS;
					options.time = options.time + 1000;
				}else if(currentScreen == "DOWNTIME") {
					currentScreen = SETTINGS;
					options.time = options.time - 1000;
				}
				settingsScreen.setO(options);
//				System.out.println(options.timeBar + " " + options.scopeBlock + " " + options.disparosB);
//				System.out.println("currentScreen -> " + currentScreen + " / paint -> " + paint);

			}
			
	
			
		}

	}
	
	private void render() {
		
		//Init
		if(canvas.getBufferStrategy() == null) {
			canvas.createBufferStrategy(2); // 1 buffer
			bStrat = canvas.getBufferStrategy();
			return;
		}else {
			bStrat = canvas.getBufferStrategy();
		}
		graphics = bStrat.getDrawGraphics();
		
		
		if(paint == true) {
		//System.out.println("Dibujando");
			if(currentScreen.equals(MENU)) {
				menu = new Menu(DEFAULT_WIDTH, DEFAULT_HEIGHT);
				menu.paint(graphics);
				
			}else if(currentScreen.equals(SETSRC)){
				if(srcMenu == null) {
					srcMenu = new Source(DEFAULT_WIDTH, DEFAULT_HEIGHT);
				}
				srcMenu.paint(graphics, frame);
			}else if(currentScreen.equals(SETTAG)){
				if(tagMenu == null) {
					tagMenu = new Tag(DEFAULT_WIDTH, DEFAULT_HEIGHT);
				}
				tagMenu.paint(graphics, frame);
			}else if(currentScreen.equals(GAME)){
				if(gameFrame == null) {
					initGameFrame();
				}
				gameFrame.draw(graphics, arcade);
			}else if(currentScreen.equals(ENDGAME)){
				if(endScreen == null) {
					endScreen = new EndScreen(DEFAULT_WIDTH, DEFAULT_HEIGHT);
				}
				endScreen.paint(graphics, frame);
			}else if(currentScreen.equals(GUIDE)){
				if(guideScreen == null) {
					guideScreen = new Guide(DEFAULT_WIDTH, DEFAULT_HEIGHT);
				}
				guideScreen.paint(graphics);
			}else if(currentScreen.equals(HIGHSCORES)){
				RestTemplate restTemplate = new RestTemplate();
				String msg  = restTemplate.getForObject(highscoreURL + "/highscore", String.class);
				Gson g = new Gson();
				System.out.println(msg);
				highscores = g.fromJson(msg, HighScores.class);
				if(rankingScreen == null) {
					rankingScreen = new Ranking(DEFAULT_WIDTH, DEFAULT_HEIGHT, highscores);
				}
				rankingScreen.paint(graphics, highscores);
			}else if(currentScreen.equals(SETTINGS)){
				if(settingsScreen == null) {
					settingsScreen = new Settings(DEFAULT_WIDTH, DEFAULT_HEIGHT, options);
				}
				settingsScreen.paint(graphics);
			}
			
		}
		paint = false;
		
		
		//End
		bStrat.show();
		graphics.dispose();
		
	    frame.setVisible(true);

		
	}
	private void reset() {
		mouseExit = new Point(0, 0); 
		mouse = new Point(0, 0);
		inputText = "";
		directoryListing = null;
		JSONlabel = "";
		tags = new LinkedList<String>();
		menu 		= null;
		srcMenu 	= null;
		tagMenu 	= null;
		endScreen 	= null;
		guideScreen = null;
		settingsScreen = null;
		gameFrame	= null;
	}
	public synchronized void stop() {
        frame.dispose();

		if(currentScreen == MENU && options.isJSON) {
			labelJSONOutput();
		}
		
		if (running == false)
			return;
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void labelJSONOutput(){
		String outputName = "";
		for(int i=0 ; i< tags.size(); i++) {
			outputName += "-"+ tags.get(i);
		}
		String[] JSONfile = JSONlabel.split("@"); 
		try (PrintWriter out = new PrintWriter("tags" + outputName + ".json")) {
		    for(int i =0; i<JSONfile.length;i++) {
		    	out.println(JSONfile[i]);	
		    }
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initGameFrame() {
	// TODO Auto-generated method stub
		if(directoryListing==null) {
			getDirectory();
		}
		getNextImage();
		gameFrame = new GameFrame(DEFAULT_WIDTH, DEFAULT_HEIGHT, tags, options);
		//System.out.println();
		paint = gameFrame.prePaint(mouse, currentImage);
	}
	private void getNextImage() {
		time = System.currentTimeMillis();
		
		imageIndex++;
		if(options.isJSON && gameFrame!=null) {
			JSONlabel += gameFrame.getJSONlabel();
			
		}else {
			
		}
		if(imageIndex<directoryListing.length) {
			//System.out.println(directoryListing[imageIndex].getPath());
			currentImage = directoryListing[imageIndex].getPath(); //Check for no more images and return or endGame
		}else {
			currentImage = null;
			imageIndex = 0;
			directoryListing = null;
			paint = true;
			currentScreen = ENDGAME;
			
		}
		//System.out.println(currentImage);
	}


	public void initListeners( ) {
		
		canvas.addMouseWheelListener(new MouseWheelListener() {
			
			
			public void mouseWheelMoved(MouseWheelEvent e) {
				zoomCorrection = e.getWheelRotation();
				//System.out.println(zoomCorrection);

			}
		});
	    canvas.addMouseMotionListener(new MouseMotionListener() {
			
			
			public void mouseMoved(MouseEvent e) {
				mouse = new Point(e.getX(), e.getY());
			}
			
			
			
			
			
			
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	    canvas.addKeyListener(new KeyListener() {
			
			
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
				
					
			}
			
			
			public void keyReleased(KeyEvent e) {
				if(e.getKeyChar()=='n' && currentScreen.equals(GAME)) {
					getNextImage();
				}else if(currentScreen.equals(SETSRC)||currentScreen.equals(SETTAG)||currentScreen.equals(ENDGAME)) {
					//System.out.println(e.getKeyChar());
					//System.out.println(e.getKeyChar() +  " -> " + e.getKeyText(13));

					if(inputText == null) {
						inputText = "" + e.getKeyChar();
					}else if((e.getKeyCode()==KeyEvent.VK_ENTER) && (inputText!="")){
//						currentScreen = GAME;
//						mouseExit = new Point(0,0);
//						paint = true;
					}else if((e.getKeyCode()==KeyEvent.VK_BACK_SPACE) && (inputText.length()>0)){
						inputText = inputText.substring(0, inputText.length()-1);
					}
					else {
						if(e.getKeyChar() != KeyEvent.CHAR_UNDEFINED)
							inputText = inputText + e.getKeyChar(); 
					}
					
				}
				
			}
			
			
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	    canvas.addMouseListener(new MouseListener() {
	        
			
			public void mouseClicked(java.awt.event.MouseEvent e) {
				
				if(mouseExit == null) {
					mouseExit= new Point(e.getX(), e.getY());;

				}else {
					mouseExit.setLocation(e.getX(), e.getY());
				}


				
			}
			public void mousePressed(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				enterPos= new Point(e.getX(), e.getY());
			}
			public void mouseReleased(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				if(mouseExit == null) {
					mouseExit= new Point(e.getX(), e.getY());;

				}else {
					mouseExit.setLocation(e.getX(), e.getY());
				}			}
			
			public void mouseEntered(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseExited(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
	    });
	}
	
	
	private void launchMenu(){
		currentScreen = MENU;
		
	}
	
	private void getDirectory() {
		File[] homeFolders = (new File(myDirectoryPath)).listFiles();

		//System.out.println(myDirectoryPath);
		for(File f : homeFolders) {

			if(f.isDirectory()) {
				
				System.out.println("--------");
				System.out.println(theme.toString() + "--" + f.getName().toString());
				System.out.println("Equals -> " + f.getName().toString().equals(theme.toString()));
				if(f.getName().equals(theme)) {
					System.out.println(f.getPath());
					directoryListing = f.listFiles();
					return;
				}
			}
		}
		System.out.println("No hay directorio");
		return;
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


