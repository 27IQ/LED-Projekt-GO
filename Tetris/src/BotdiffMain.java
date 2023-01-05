
import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;

import ledControl.*;
import ledControl.gui.KeyBuffer;

public class BotdiffMain {

	private BoardController c = BoardController.getBoardController(LedConfiguration.LED_20x20_EMULATOR);
	private int[][][] colors;
	private int offset, rowsCleared, hold;
	private TetrisPiece currentPiece, nextPiece1, nextPiece2, nextPiece3, heldPiece, previewPiece;
	private boolean lost = false;
	private int counter1 = 0, counter2 = 0, counter3 = 0, counter4 = 0, counter5 = 0, counter6 = 0; // counter for Buffer
	private int framecounter;

	// Color presets
	private static Color Blue = new Color("Blue", 0, 0, 127);
	private static Color Yellow = new Color("Yellow", 127, 127, 0);
	private static Color Red = new Color("Red", 127, 0, 0);
	private static Color Green = new Color("Green", 0, 127, 0);
	private static Color Orange = new Color("Orange", 127, 63, 0);
	private static Color Turquoise = new Color("Turquoise", 0, 127, 127);
	private static Color Pink = new Color("Pink", 127, 0, 127);
	private static Color White = new Color("White", 127, 127, 127);
	private static Color Black = new Color("Black", 0, 0, 0);

	public Color[] colorpreset = { Blue, Yellow, Red, Green, Orange, Turquoise, Pink, White, Black };

	/**
	 * Konstruktor für das Spiel
	 */
	public BotdiffMain() {
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
	private void setMyColorArray(int x, int y, Color color) {
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
	private boolean isColor(int x, int y, Color color) {

		if (x < 0 || x > 19 || y < 0 || y > 19)
			return false;

		if (colors[x][y][0] == color.getRed() && colors[x][y][1] == color.getGreen()
				&& colors[x][y][2] == color.getBlue())
			return true;

		return false;
	}

	private boolean isstaticColor(int x, int y) {

		if (x < 0 || x > 19 || y < 0 || y > 19)
			return false;

		for (Color color : colorpreset) {
			if (isColor(x, y, color.getStaticColor()))
				return true;
		}
		return false;
	}

	private Color getColor(int x, int y) {
		return new Color(null, colors[x][y][0], colors[x][y][1], colors[x][y][2]);
	}

	/**
	 * Startet das Spiel kann auch zum Neustarten verwendet werden
	 */
	private void start() {

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
	private void endGame() {
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
	private void displaynextPieces() {
		nextPiece1.setx(17);
		nextPiece1.sety(3);
		colors = nextPiece1.createPattern(colors, nextPiece1.getColor());

		nextPiece2.setx(17);
		nextPiece2.sety(8);
		colors = nextPiece2.createPattern(colors, nextPiece2.getColor());

		nextPiece3.setx(17);
		nextPiece3.sety(13);
		colors = nextPiece3.createPattern(colors, nextPiece3.getColor());

		if (heldPiece != null) {

			heldPiece.setx(17);
			heldPiece.sety(19);
			colors = heldPiece.createPattern(colors, heldPiece.getColor());
		}
	}

	/**
	 * Setzt die coords von den Nächsten und dem Hold Piece auf Schwarz ohne das
	 * Array colors danach dem Board zu übergeben
	 */
	private void clearnextdisplay() {

		colors = nextPiece1.createPattern(colors, Black);

		colors = nextPiece2.createPattern(colors, Black);

		colors = nextPiece3.createPattern(colors, Black);

		if (heldPiece != null)
			heldPiece.createPattern(colors, Black);

	}

	/**
	 * Setzt das Piece an welches wir anhalten wollen in den Speicher und holt
	 * gegebenenfalls das vorherige heraus.
	 */
	private void setholding() {
		if (heldPiece == null) {
			heldPiece = currentPiece;
			currentPiece = null;
			clearnextdisplay();
		} else {
			clearnextdisplay();
			TetrisPiece t = heldPiece;
			heldPiece = currentPiece;
			currentPiece = t;
			currentPiece.setx(6);
			currentPiece.sety(0);
			displaynextPieces();
		}

		hold = 0;
	}

	/**
	 * Hardcode für den SpielRahmen
	 */
	private void generateBorder() {
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
	private TetrisPiece generiereStein() {
		return new TetrisPiece(getRandomNumber(1, 8));
	}

	/**
	 * Generiert eine zufällige Nummer zwischen min und max
	 * 
	 * @param min
	 * @param max
	 * @return zufällige Nummer
	 */
	private int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}

	/**
	 * Sendet das atuelle Piece auf seine Reise in den Abgrund.Seitdem c.sleep im
	 * Code steht wird das Board manchmal für einen Frame Weiß.
	 */
	private void spawnTetrisStein() {
		hold = 1; // wir dürfen nur einmal pro Stein swappen dehalb der counter

		// set default values
		displaynextPieces();
		currentPiece.setx(6);
		currentPiece.sety(0);

		while (!willcollide("down", currentPiece)) {

			framecounter++;

			// für eine ansteigende Schwierigkeit je mehr Zeilen man cleart
			if (rowsCleared < 30)
				c.sleep(30 - (rowsCleared)); // seitdem c.sleep im Code steht wird das Board manchmal für einen
												// Frame weiß

			colors = currentPiece.createPattern(colors, Black); // vanish Piece
			if (framecounter == 10) {
				currentPiece.addy(1); // drop down 1
				framecounter = 0;
			}

			// verarbeitet Keypresses
			if (c.getKeyBuffer().eventsInBuffer() != 0) {
				keypressManager(c.getKeyBuffer());

				if (currentPiece == null)
					return;

			}
			// setzt Piece an neuer pos mit gegebenenfalls neuer Rotation
			showpreview();
			colors = currentPiece.createPattern(colors, currentPiece.getColor());
			c.setColors(colors);
			c.updateBoard();
		}

		// lässt ein Piece welches nicht weiter bewegt werden kann Weiß werdem um von
		// willcollide() erkannt zu werden
		colors = currentPiece.createPattern(colors, currentPiece.getColor().getStaticColor());
		clearnextdisplay();
		CheckAndConcatRows();

		previewPiece = null;

	}

	/**
	 * erstellt ein preview wo das aktuelle Piece currentPiece gerade hinfällt
	 */
	private void showpreview() {
		if (previewPiece == null) {
			previewPiece = new TetrisPiece(0);
			return;
		}
		previewPiece.createPattern(colors, Black);

		// übernehme werte von currentPiece
		previewPiece.setform(currentPiece.getform());
		previewPiece.setx(currentPiece.getx());
		previewPiece.sety(currentPiece.gety());

		while (!willcollide("down", previewPiece)) { // geht so lange herunter bis es collidiert
			previewPiece.addy(1);
		}

		colors=previewPiece.createPattern(colors, currentPiece.getColor().getweakColor());
	}

	/**
	 * Die Methode prüft ob ein Piece mit anderen schon gesetzten Steinen oder der Border kollidieren würde.
	 * 
	 * @param action (down, left, right, inner)
	 * @param Piece
	 * @return true wenn die gewählte Aktion zu einer kollision führt
	 */
	private boolean willcollide(String action, TetrisPiece Piece) {
		for (int i = 0; i < Piece.getform().length; i++) {

			if (Piece.gety() - i + 1 == -1) // falls das Piece nur teilweise zu sehen ist
				return false;

			for (int j = 0; j < Piece.getform()[Piece.getform().length - i - 1].length; j++) {
				if (Piece.getform()[Piece.getform().length - i - 1][Piece.getform()[i].length - j - 1] == 1) {

					switch (action) {
					case "down":

						if (isstaticColor(Piece.getx() - j, Piece.gety() - i + 1)
								|| isColor(Piece.getx() - j, Piece.gety() - i + 1, White))
							return true;

						break;
					case "left":

						if (isstaticColor(Piece.getx() - j - 1, Piece.gety() - i)
								|| isColor(Piece.getx() - j - 1, Piece.gety() - i, White))
							return true;

						break;
					case "right":

						if (isstaticColor(Piece.getx() - j + 1, Piece.gety() - i)
								|| isColor(Piece.getx() - j + 1, Piece.gety() - i, White))
							return true;

						break;
					case "inner":

						if (isstaticColor(Piece.getx() - j, Piece.gety() - i)
								|| isColor(Piece.getx() - j, Piece.gety() - i, White))
							return true;

						break;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Erstellt ein Array das angibt welceh zeilen "komplett" sind. Wenn Zeile i voll ist dann
	 * rowclear[i]==true. Danch geht die Methode die Zeilen von Unten nach Oben
	 * durch und zieht, wenn eine Zeile gelöscht wird, die darüberliegenden
	 * herunter.
	 */
	private void CheckAndConcatRows() {

		// erstelle rowclear[]
		boolean[] rowclear = new boolean[19];
		for (int j = 0; j < 19; j++) {
			rowclear[j] = true;
			for (int i = 1; i < 12; i++) {

				if (!isColor(i, j, Black) && j == 0) {
					endGame();
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
					setMyColorArray(j, i + offset, getColor(j, i));
			}
		}
	}

	/**
	 * Verarbeitet die Keyevents die im Buffer übergeben werden. Aus der
	 * Dokumentation "Therefore, the result of the getKeyChar method is guaranteed
	 * to be meaningful only for KEY_TYPED events." Trotzdem kreigt man nur doppelte
	 * inputs wenn man den Buffer ausliest. Deshalb i+=2. Jedoch heißt das auch im
	 * Endeffekt das nur KEY_PRESSED und KEY_RELEASED sich im buffer befinden wie
	 * könneten es sonst nur 2 sein. Das Timing ist auch exakt zum Keypress oder realease. 
	 * ???
	 * 
	 * Ich bin verwirrt aber die Methode funtioniert so am besten. Hours wasted: 2.5
	 * 
	 * @param buffer
	 */
	
	private void keypressManager(KeyBuffer buffer) {

		KeyEvent[] event = buffer.popAll();

		for (int i = 0; i < event.length; i += 2) {
			keypress(event[i]);
		}
	}

	private void keypress(KeyEvent event) {
		switch (event.getKeyChar()) {
		case 'q': // rotate left
			counter1++;
			if (counter1 == 2) {
				counter1 = 0;
				return;
			}
			currentPiece.rotateleft();
			if (willcollide("down", currentPiece) || willcollide("inner", currentPiece))
				currentPiece.rotateright();
			break;
		case 'e': // rotate right
			counter2++;
			if (counter2 == 2) {
				counter2 = 0;
				return;
			}
			currentPiece.rotateright();
			if (willcollide("down", currentPiece) || willcollide("inner", currentPiece))
				currentPiece.rotateleft();
			break;
		case 'a': // left
			counter3++;
			if (counter3 == 2) {
				counter3 = 0;
				return;
			}
			if (!willcollide("left", currentPiece))
				currentPiece.addx(-1);

			break;
		case 's': // down
			counter4++;
			if (counter4 == 2) {
				counter4 = 0;
				return;
			}
			while (!willcollide("down", currentPiece))
				currentPiece.addy(1);

			break;
		case 'd': // right

			counter5++;

			if (counter5 == 2) {
				counter5 = 0;
				return;
			}
			if (!willcollide("right", currentPiece))
				currentPiece.addx(1);

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
		new BotdiffMain();
	}
}
