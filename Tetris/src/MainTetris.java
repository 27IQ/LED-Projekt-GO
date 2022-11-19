
import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;

import ledControl.*;
import ledControl.gui.KeyBuffer;

public class MainTetris {

	public BoardController c = BoardController.getBoardController(LedConfiguration.LED_20x20_EMULATOR);
	public int[][][] colors;
	int currentx, currenty, offset, rowsCleared, hold;
	TetrisPiece currentPiece, nextPiece1, nextPiece2, nextPiece3, heldPiece;
	boolean lost = false;
	int counter1 = 0, counter2 = 0, counter3 = 0, counter4 = 0, counter5 = 0, counter6 = 0;

	// Color presets
	public Color Blue = new Color("Blue", 0, 0, 127);
	public Color Yellow = new Color("Yellow", 127, 127, 0);
	public Color Red = new Color("Red", 127, 0, 0);
	public Color Green = new Color("Green", 0, 127, 0);
	public Color Orange = new Color("Orange", 127, 63, 0);
	public Color Turquoise = new Color("Turquoise", 0, 127, 127);
	public Color Pink = new Color("Pink", 127, 0, 127);
	public Color White = new Color("White", 127, 127, 127);
	public Color Black = new Color("Black", 0, 0, 0);

	/**
	 * Konstruktor für das Spiel
	 */
	public MainTetris() {
		start();
	}

	/**
	 * Adaption des Farbsystems um die Presets nutzen zu können. Setzt den Farbwert
	 * in das Array "colors" das später dem Controller übergeben werden kann.
	 * 
	 * @param x
	 * @param y
	 * @param color
	 */
	public void setMyColorArray(int x, int y, Color color) {
		colors[x][y][0] = color.getRed();
		colors[x][y][1] = color.getGreen();
		colors[x][y][2] = color.getBlue();
	}

	/**
	 * @param x
	 * @param y
	 * @param color
	 * @return true wenn color im Array colors auf x,y abgebildet ist
	 */
	public boolean isColor(int x, int y, Color color) {

		if (x < 0 || y < 0)
			return false;

		if (colors[x][y][0] == color.getRed() && colors[x][y][1] == color.getGreen()
				&& colors[x][y][2] == color.getBlue())
			return true;

		return false;
	}

	/**
	 * Startet das Spiel kann auch zum Neustarten verwendet werden
	 */
	public void start() {

		// set default values
		c.resetColors();
		colors = c.getColors();
		generateBorder();
		rowsCleared = 0;
		heldPiece = null;
		currentPiece = generiereStein();
		nextPiece1 = generiereStein();
		nextPiece2 = generiereStein();
		nextPiece3 = generiereStein();

		// manager für die Pieces
		while (!lost) {
			spawnTetrisStein();
			currentPiece = nextPiece1;
			nextPiece1 = nextPiece2;
			nextPiece2 = nextPiece3;
			nextPiece3 = generiereStein();

		}
	}

	/**
	 * Menü mit Restart option
	 */
	public void endGame() {
		if (!lost) {
			int s = JOptionPane.showConfirmDialog(null, "Lines Cleared " + rowsCleared + "\n Restart?",
					"Du hast verloren!", JOptionPane.YES_NO_OPTION);
			;
			if (s == JOptionPane.YES_OPTION) {
				start();
			} else {
				System.exit(0);
			}
		}

		lost = true;
	}

	/**
	 * Setzt die Next Pieces
	 */
	public void displaynextPieces() {
		createPattern(17, 3, nextPiece1.getColor(), nextPiece1);
		createPattern(17, 8, nextPiece2.getColor(), nextPiece2);
		createPattern(17, 13, nextPiece3.getColor(), nextPiece3);

		if (heldPiece != null)
			createPattern(17, 19, heldPiece.getColor(), heldPiece);
	}

	/**
	 * Setzt die coords von den Nächsten und dem Hold Piece auf Schwarz ohne das
	 * Array colors danach dem Board zu übergeben
	 */
	public void clearnextdisplay() {
		createPattern(17, 3, Black, nextPiece1);
		createPattern(17, 8, Black, nextPiece2);
		createPattern(17, 13, Black, nextPiece3);

		if (heldPiece != null)
			createPattern(17, 19, Black, heldPiece);
	}

	/**
	 * Setzt das Piece an welches wir anhalten wollen in den Speicher und holt
	 * gegebenenfalls das vorherige heraus.
	 */
	public void setholding() {
		if (heldPiece == null) {
			heldPiece = currentPiece;
			currentPiece = null;
			clearnextdisplay();
		} else {
			clearnextdisplay();
			TetrisPiece t = heldPiece;
			heldPiece = currentPiece;
			currentPiece = t;
			currentx = 6;
			currenty = 0;
			displaynextPieces();
		}

		hold = 0;
	}

	/**
	 * Hardcode für den SpielRahmen
	 */
	public void generateBorder() {
		colors = c.getColors();

		for (int i = 0; i < 20; i++) {
			setMyColorArray(0, i, White);
			setMyColorArray(12, i, White);
		}

		for (int i = 0; i < 12; i++) {
			setMyColorArray(i, 19, White);
		}
		c.setColors(colors);
		c.updateBoard();
	}

	/**
	 * generiert eine Stein basierend auf einer zufalls Nummer
	 * 
	 * @return
	 */
	public TetrisPiece generiereStein() {
		return new TetrisPiece(getRandomNumber(1, 8));
	}

	/**
	 * Generiert eine zufällige Nummer zwischen min und max
	 * 
	 * @param min
	 * @param max
	 * @return zufällige Nummer
	 */
	public int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}

	/**
	 * Sendet das atuelle Piece auf seine Reise in den Abgrund.Seitdem c.sleep im
	 * Code steht wird das Board manchmal für einen Frame Weiß.
	 */
	public void spawnTetrisStein() {
		hold = 1; // wir dürfen nur einmal pro Stein swappen dehalb der counter

		// set default values
		displaynextPieces();
		currentx = 6;
		currenty = 0;

		while (!willcollide("down")) {

			// für eine ansteigende Schwierigkeit je mehr Zeilen man cleart
			if (rowsCleared < 30)
				c.sleep(300 - (rowsCleared * 10)); // seitdem c.sleep im Code steht wird das Board manchmal für einen
													// Frame weiß

			createPattern(currentx, currenty, Black, currentPiece); // vanish Piece
			currenty++; // drop down 1

			// verarbeitet Keypresses
			if (c.getKeyBuffer().eventsInBuffer() != 0) {
				keypressManager(c.getKeyBuffer());

				if (currentPiece == null)
					return;

			}
			// setzt Piece an neuer pos mit gegebenenfalls neuer Rotation
			createPattern(currentx, currenty, currentPiece.getColor(), currentPiece);
			c.setColors(colors);
			c.updateBoard();
		}

		// lässt ein Piece welches nicht weiter bewegt werden kann Weiß werdem um von
		// willcollide() erkannt zu werden
		createPattern(currentx, currenty, White, currentPiece);
		clearnextdisplay();
		CheckAndConcatRows();
		;
	}

	/**
	 * Überträgt die Form eines TetrisPiece in das Array "colors" wobei x und y die
	 * untere rechte Ecke darstellen. Für Werte außerhalb des Arrays "colors" wird
	 * nichts gemacht weshalb diese Methode auch zum Einfügen der Teile am Anfang,
	 * wo diese nur teilweise Sichtbar sind, funktioniert. Für die Farbe Schwarz
	 * wird das Array nicht direkt an das Board übergeben um nicht unnötig
	 * zusätzliche Frames zu generieren.
	 * 
	 * @param x
	 * @param y
	 * @param Farbe
	 * @param Piece
	 */
	public void createPattern(int x, int y, Color Farbe, TetrisPiece Piece) {
		for (int i = 0; i < Piece.getform().length; i++) {

			if (y - i < 0)
				return;

			for (int j = 0; j < Piece.getform()[Piece.getform().length - i - 1].length; j++) {

				if (x - j < 0)
					return;

				if (Piece.getform()[Piece.getform().length - i - 1][Piece.getform()[i].length - j - 1] == 1) {
					setMyColorArray(x - j, y - i, Farbe);
				}
			}
		}
	}

	/**
	 * Die Methode durchsucht die position von currentPiece nach weißen Pixeln
	 * down-unter dem Piece left-links neben dem Piece rigth-rechts neben dem Piece
	 * inner-die exakte position den piece
	 * 
	 * @param aktion
	 * @return true wenn die gewählte Aktion zu einer kollision führt
	 */
	public boolean willcollide(String action) {
		for (int i = 0; i < currentPiece.getform().length; i++) {

			if (currenty - i + 1 == -1)
				return false;

			for (int j = 0; j < currentPiece.getform()[currentPiece.getform().length - i - 1].length; j++) {
				if (currentPiece.getform()[currentPiece.getform().length - i - 1][currentPiece.getform()[i].length - j
						- 1] == 1) {

					switch (action) {
					case "down":

						if (isColor(currentx - j, currenty - i + 1, White))
							return true;

						break;
					case "left":

						if (isColor(currentx - j - 1, currenty - i, White))
							return true;

						break;
					case "right":

						if (isColor(currentx - j + 1, currenty - i, White))
							return true;

						break;
					case "inner":

						if (isColor(currentx - j, currenty - i, White))
							return true;

						break;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Erstellt ein Array mit allen Zeilen die voll sind. Wenn Zeile i voll ist dann
	 * rowclear[i]==true. Danch geht die Methode die Zeilen von Unten nach Oben
	 * durch und zieht, wenn eine Zeile gelöscht wird, die darüberliegenden
	 * herunter.
	 */
	public void CheckAndConcatRows() {

		// erstelle rowclear[]
		boolean[] rowclear = new boolean[19];
		for (int j = 0; j < 19; j++) {
			rowclear[j] = true;
			for (int i = 1; i < 12; i++) {

				if (isColor(i, j, White) && j == 0) {
					endGame();
					setMyColorArray(i, j, Red);
				}

				if (isColor(i, j, Black)) {
					rowclear[j] = false;
				}

			}

		}

		// concat rows
		offset = 0;

		for (int i = 18; i > 0; i--) {
			if (rowclear[i]) {
				offset++;
				rowsCleared++;
				continue;
			}

			for (int j = 1; j < 12; j++) {
				if (isColor(j, i, White)) {
					setMyColorArray(j, i + offset, White);
				} else {
					setMyColorArray(j, i + offset, Black);
				}
			}
		}
	}

	/**
	 * Verarbeitet die Keyevents die im Buffer übergeben werden. Aus der
	 * Dokumentation "Therefore, the result of the getKeyChar method is guaranteed
	 * to be meaningful only for KEY_TYPED events." Trotzdem kreigt man nur doppelte
	 * inputs wenn man den Buffer ausliest. Deshalb i+=2. Jedoch heißt das auch im
	 * Endeffekt das nur KEY_PRESSED und KEY_RELEASED sich im buffer befinden wie
	 * könneten es sonst nur 2 sein.
	 * 
	 * Ich bin verwirrt aber die Methode funtioniert so am besten. Hours wasted: 1.5
	 * 
	 * @param buffer
	 */
	public void keypressManager(KeyBuffer buffer) {

		KeyEvent[] event = buffer.popAll();

		for (int i = 0; i < event.length; i += 2) {
			keypress(event[i]);
		}
	}

	public void keypress(KeyEvent event) {
		switch (event.getKeyChar()) {
		case 'q': // rotate left
			counter1++;
			if (counter1 == 2) {
				counter1 = 0;
				return;
			}
			currentPiece.rotateleft();
			if (willcollide("down") || willcollide("inner"))
				currentPiece.rotateright();
			break;
		case 'e': // rotate right
			counter2++;
			if (counter2 == 2) {
				counter2 = 0;
				return;
			}
			currentPiece.rotateright();
			if (willcollide("down") || willcollide("inner"))
				currentPiece.rotateleft();
			break;
		case 'a': // left
			counter3++;
			if (counter3 == 2) {
				counter3 = 0;
				return;
			}
			if (!willcollide("left"))
				currentx -= 1;

			break;
		case 's': // down
			counter4++;
			if (counter4 == 2) {
				counter4 = 0;
				return;
			}
			while (!willcollide("down"))
				currenty += 1;

			break;
		case 'd': // right

			counter5++;

			if (counter5 == 2) {
				counter5 = 0;
				return;
			}
			if (!willcollide("right"))
				currentx += 1;

			break;

		case 'w': // hold
			counter6++;
			if (counter6 == 2) {
				counter6 = 0;
				return;
			}

			if (hold == 1)
				setholding();

			break;
		}

	}

	/**
	 * Main-Methode
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new MainTetris();
	}
}
