
import ledControl.*;


public class MainTetris {
	
	public BoardController c = BoardController.getBoardController(LedConfiguration.LED_20x20_EMULATOR);
	public int[][][] colors;
	int currentx,currenty,offset;
	
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
		refreshColors();
		currentx=6;
		currenty=0;
		int i,j=0;
		for(i=0; i<t.getlength()&&!willcollide(t);i++) {
			for(j=0;j<t.getform()[i].length&&!willcollide(t);j++) {
				if(t.getform()[i][j]==1) {
					setMyColorArray(currentx+j, currenty+i, t.getColor());
				}
			}			
			setColors();
		}
		currenty+=i-1;
		currentx+=j-1;
		
		while(!willcollide(t)) {
			deletepattern(t);
			currenty++;
			createNewPattern(t,t.getColor());
		}
		
		createNewPattern(t, White);
		currentx=6;
		currenty=0;
	}
	
	public void deletepattern(TetrisPiece t) {
		for(int i=0; i<t.getlength();i++) {
			for(int j=0;j<t.getform()[t.getform().length-i-1].length;j++) {
				if(t.getform()[t.getform().length-i-1][t.getform()[i].length-j-1]==1) {
					setMyColorArray(currentx-j, currenty-i, Black);
				}
			}
			
		}
		setColors();
	}
	
	public void createNewPattern(TetrisPiece t,Color Farbe) {
		for(int i=0; i<t.getlength();i++) {
			for(int j=0;j<t.getform()[t.getform().length-i-1].length;j++) {
				if(t.getform()[t.getform().length-i-1][t.getform()[i].length-j-1]==1) {
					setMyColorArray(currentx-j, currenty-i, Farbe);
				}
			}
			
		}
		setColors();
	}
	
	
	
	
	public boolean willcollide(TetrisPiece t) {
		for(int i=0; i<t.getlength();i++) {
			
			if(currenty-i+1==-1)
				return false;
			
			for(int j=0;j<t.getform()[t.getform().length-i-1].length;j++) {
				if(t.getform()[t.getform().length-i-1][t.getform()[i].length-j-1]==1) {
					if(colors[currentx-j][currenty-i+1][0]==127&&colors[currentx-j][currenty-i+1][1]==127&&colors[currentx-j][currenty-i+1][2]==127) {
						return true;
					}
				}
			}
			
		}
		
		return false;
	}
	



	/**
	 * Main-Methode
	 * @param args
	 */
	public static void main(String[] args) {
		new MainTetris();
	}
}
