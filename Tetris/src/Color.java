
public class Color {
	int red,green,blue;
	String name;
	
	/**
	 * Konstruktor für eine Farbe
	 * @param name
	 * @param red
	 * @param green
	 * @param blue
	 */
	public Color(String name, int red,int green,int blue) {
		this.red=red;
		this.green=green;
		this.blue=blue;
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
}
