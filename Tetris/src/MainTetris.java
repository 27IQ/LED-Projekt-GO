
import java.awt.event.KeyEvent;

import ledControl.*;
import ledControl.gui.KeyBuffer;


public class MainTetris {
	
	public BoardController c = BoardController.getBoardController(LedConfiguration.LED_20x20_EMULATOR);
	public int[][][] colors;
	int currentx,currenty,offset;
	TetrisPiece currentPiece;
	char lastchar='p';
	
	//Color presets
	public Color Blue=new Color("Blue", 0, 0, 127); 
	public Color Yellow=new Color("Yellow", 127, 127,0); 
	public Color Red=new Color("Red",127,0,0);
	public Color Green=new Color("Green",0,127,0);
	public Color Orange=new Color("Orange",127,63,0);
	public Color Turquoise=new Color("Turquoise",0,127,127);
	public Color Pink=new Color("Pink",127,0,127);
	public Color White=new Color("White",127,127,127);
	public Color Black=new Color("Black",0,0,0);
	

	/**
	 * Adaption des Farbsystems um die Presets nutzen zu können. Setzt den Farbwert direkt in das Array des Controllers.
	 * @param x : X-Koordinate
	 * @param y : Y-Koordinate
	 * @param color : Objekt Color das die drei Farbwerte übergibt
	 */
	public void setMyColor(int x,int y,Color color) {
		c.setColor(x, y, color.getRed(), color.getGreen(), color.getBlue());
	}
	
	
	/**
	 * Adaption des Farbsystems um die Presets nutzen zu können. Setzt den Farbwert in das Array "colors" das später dem Controller übergeben werden kann.
	 * @param x : X-Koordinate
	 * @param y : Y-Koordinate
	 * @param color : Objekt Color das die drei Farbwerte übergibt
	 */
	public void setMyColorArray(int x,int y,Color color) {
		colors[x][y][0]=color.getRed();
		colors[x][y][1]=color.getGreen();
		colors[x][y][2]=color.getBlue();
	}
	
	
	
	/**
	 * Konstruktor für das Spiel
	 */
	public MainTetris() {
		getNewColors();
		generateBorder();
		
		spawnTetrisStein(new TetrisPiece(1));
		spawnTetrisStein(new TetrisPiece(5));
		
	}
	
	public int[][][] getNewColors() {
		colors=c.getColors();
		return colors;
	}
	
	public void refreshColors() {
		colors=c.getColors();
	}
	
	public void setColors() {
		c.setColors(colors);
		c.updateBoard();
	}
	
	public void move() {
		
	}
	/**
	 * Hardcode für den SpielRahmen
	 */
	public void generateBorder() {
		colors=c.getColors();
		
		for(int i=0;i<20;i++) {
			setMyColorArray(0,i,White);
			setMyColorArray(12,i,White);
		}
		
		for(int i=0;i<12;i++) {
			setMyColorArray(i,19,White);
		}
		c.setColors(colors);
		c.updateBoard();
	}
	
	
	public void spawnTetrisStein(TetrisPiece t) {
		currentPiece=t;
		refreshColors();
		currentx=6;
		currenty=0;
		
		while(!willcollide("down")) {
			c.sleep(200);
			createNewPattern(Black);
			currenty++;
			if(c.getKeyBuffer().eventsInBuffer()!=0) {
				keypressManager();
				c.setColors(colors);
			}
			createNewPattern(t.getColor());
		}
		
		createNewPattern(White);
		currentx=6;
		currenty=0;
	}
	
	public void createNewPattern(Color Farbe) {
		for(int i=0; i<currentPiece.getform().length;i++) {
			if(currenty-i<0)
				return;
			for(int j=0;j<currentPiece.getform()[currentPiece.getform().length-i-1].length;j++) {
				if(currentx-j<0)
					return;
				
				if(currentPiece.getform()[currentPiece.getform().length-i-1][currentPiece.getform()[i].length-j-1]==1) {
					setMyColorArray(currentx-j, currenty-i, Farbe);
				}
			}
			
		}
		setColors();
	}

	
	public boolean willcollide(String action) {
		for(int i=0; i<currentPiece.getform().length;i++) {
			
			if(currenty-i+1==-1)
				return false;
			
			for(int j=0;j<currentPiece.getform()[currentPiece.getform().length-i-1].length;j++) {
				if(currentPiece.getform()[currentPiece.getform().length-i-1][currentPiece.getform()[i].length-j-1]==1) {
					
					switch (action) {
					case "down":
						
						if(isColor(currentx-j,currenty-i+1,White))
							return true;
				
						break;
					case "left":
						
						if(isColor(currentx-j-1,currenty-i,White))
							return true;
						
						break;
					case "right":
						
						if(isColor(currentx-j+1,currenty-i,White))
							return true;
						
						break;	
					}
				}
			}
		}	
		return false;
	}

	public boolean isColor(int x, int y, Color color) {
		
			if(x<0||y<0)
				return false;
			
			if(colors[x][y][0]==color.getRed()&&colors[x][y][1]==color.getGreen()&&colors[x][y][0]==color.getBlue()) 
				return true;
		
		return false;
	}
	
	public void keypressManager() {
		KeyBuffer buffer=c.getKeyBuffer();
		
		KeyEvent event=buffer.pop();
		while(buffer.eventsInBuffer()!=0) {
			event=buffer.pop();
		}
		
		//System.out.println(event.getKeyChar());
		switch (event.getKeyChar()) {
		case 'q':	//rotate left
			
			break;
		case 'e':	//rotate right
			
			break;
		case 'a':	//left
			if(!willcollide("left")){
				currentx-=1;
			}
			break;
		case 's':	//down
			if(!willcollide("down")){
				currenty+=1;
			}
			break;
		case 'd':	//right
			if(!willcollide("right")){
				currentx+=1;
			}
			break;
		case 'w':	// toggle store next piece
			
			break;
		}
		
	}

	

	/**
	 * Main-Methode
	 * @param args
	 */
	public static void main(String[] args) {
		new MainTetris();
	}
}
