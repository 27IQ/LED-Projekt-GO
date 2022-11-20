public class TetrisPiece {
	Color color;
	int[][] form;
	int x,y;

	/**
	 * Konstruktor für einen Tetrisstein
	 * 
	 * @param id je nach id wird eine andere Form zugewiesen
	 */
	public TetrisPiece(int id) {
		this.x=6;
		this.y=0;
		
		switch (id) {
		case 1:
			int[][] constructTetrisI = { { 1 }, { 1 }, { 1 }, { 1 } };
			form = constructTetrisI;
			this.color = new Color("Blue", 0, 0, 127);
			break;
		case 2:
			int[][] constructTetrisREL = { { 0, 1 }, { 0, 1 }, { 1, 1 } };
			form = constructTetrisREL;
			this.color = new Color("Yellow", 127, 127, 0);
			break;
		case 3:
			int[][] constructTetrisL = { { 1, 0 }, { 1, 0 }, { 1, 1 } };
			form = constructTetrisL;
			this.color = new Color("Red", 127, 0, 0);
			break;
		case 4:
			int[][] constructTetrisQ = { { 1, 1 }, { 1, 1 } };
			form = constructTetrisQ;
			this.color = new Color("Green", 0, 127, 0);
			break;
		case 5:
			int[][] constructTetrisREZ = { { 0, 1, 1 }, { 1, 1, 0 } };
			form = constructTetrisREZ;
			this.color = new Color("Orange", 127, 63, 0);
			break;
		case 6:
			int[][] constructTetrisT = { { 1, 1, 1 }, { 0, 1, 0 } };
			form = constructTetrisT;
			this.color = new Color("Turquoise", 0, 127, 127);
			break;
		case 7:
			int[][] constructTetrisZ = { { 1, 1, 0 }, { 0, 1, 1 } };
			form = constructTetrisZ;
			this.color = new Color("Pink", 127, 0, 127);
			break;
		default:
			int[][] constructTetrisZ2 = { { 1, 1, 0 }, { 0, 1, 1 } };
			form = constructTetrisZ2;
			this.color = new Color("Pink", 127, 0, 127);
			break;
		}
	}

	public int[][] getform() {
		return form;
	}

	public Color getColor() {
		return color;
	}
	
	public void setx(int value) {
		x=value;
	}

	public void addx(int value) {
		x+=value;
	}
	
	public void sety(int value) {
		y=value;
	}
	
	public void addy(int value) {
		y+=value;
	}
	
	public int getx() {
		return x;
	}

	public int gety() {
		return y;
	}
	
	public void setxandy(int[] a) {
		x=a[0];
		y=a[1];
	}

	
	/**
	 * Rotiert das Array das die Form des Pieces enhält nach links
	 */
	public void rotateleft() {

		int[][] rotated = new int[form[0].length][form.length];

		for (int i = 0; i < form.length; i++) {
			for (int j = form[i].length - 1; j >= 0; j--) {
				rotated[j][i] = form[i][form[i].length - 1 - j];
			}
		}
		form = rotated;
	}

	
	/**
	 * Da war man dann auch zu faul aber dreimal Links ist auch einmal Rechts nicht?
	 */
	public void rotateright() {
		rotateleft();
		rotateleft();
		rotateleft();
	}

	/**
	 * toString aus Testgründen
	 * @returns die aktuelle Form des Pieces
	 */
	@Override
	public String toString() {
		String s = "";

		for (int[] is : form) {
			for (int i : is) {
				s += Integer.toString(i) + " ";
			}
			s += "\n";
		}

		return s;
	}
	
	/**
	 * Überträgt die Form eines TetrisPiece in das Array "colors" wobei x und y die
	 * untere rechte Ecke darstellen. Für Werte außerhalb des Arrays "colors" wird
	 * nichts gemacht weshalb diese Methode auch zum Einfügen der Teile am Anfang,
	 * wo diese nur teilweise Sichtbar sind, funktioniert.
	 * 
	 * @param x
	 * @param y
	 * @param Farbe
	 * @param Piece
	 */
	public int[][][] createPattern(int[][][] colors,Color color){
		for (int i = 0; i < form.length; i++) {

			if (y - i < 0)
				return colors;

			for (int j = 0; j < form[form.length - i - 1].length; j++) {

				if (x - j < 0)
					return colors;

				if (form[form.length - i - 1][form[i].length - j - 1] == 1) {
					
					colors[x-j][y-i][0] = color.getRed();
					colors[x-j][y-i][1] = color.getGreen();
					colors[x-j][y-i][2] = color.getBlue();
				}
			}
		}
		return colors;
	}
}
