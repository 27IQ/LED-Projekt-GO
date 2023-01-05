
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

	public void setRed(int color) {
		this.red = color;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int color) {
		this.green = color;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int color) {
		this.blue = color;
	}

	/**
	 * Erzeugt ein Neues Objekt Color welches die Farbe Dunkler macht bzw die LED
	 * schwacher leuchten lässt (halbe leuchtkraft der Farbe)
	 * 
	 * @return Color mit Farbwerten / 2
	 */
	public Color getweakColor() {
		return new Color("weak" + name, red / 2, green / 2, blue / 2);
	}

	/**
	 * Erzeugt ein Neues Objekt Color welches ein dem Auge identische Farbe
	 * zurückgibt um einen für das Programm erkennbaren unterschied zu erzeugen
	 * damit schon platzierte Pieces vom aktuellen unterschieden werden können.
	 * 
	 * @return Color mit Farbwerten - 1 bei 0 keine Veränderung
	 */
	public Color getStaticColor() {
		Color staticColor = new Color("static" + name, red - 1, green - 1, blue - 1);

		if (staticColor.getRed() < 0)
			staticColor.setRed(1);

		if (staticColor.getGreen() < 0)
			staticColor.setGreen(1);

		if (staticColor.getBlue() < 0)
			staticColor.setBlue(1);

		return staticColor;
	}
}
