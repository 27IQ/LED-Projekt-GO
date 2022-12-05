
public class Color {
	int red, green, blue;
	String name;

	/**
	 * Konstruktor für eine Farbe. Werte für Farben bis max 127.
	 * 
	 * @param name
	 * @param red
	 * @param green
	 * @param blue
	 */
	public Color(String name, int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}
	/**
	 * erzeugt ein Neues Objekt Color welches die Farbe Dunkler macht bzw die LED schwacher leuchten lässt
	 * @return Color
	 */
	public Color getweakColor() {
		return new Color("weak"+name, red/2, green/2, blue/2);
	}
}
