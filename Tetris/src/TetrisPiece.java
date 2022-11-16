
public class TetrisPiece {
	Color color;
	int[][] form;
	int length;
	
	
	/**
	 * Konstruktor für einen Tetrisstein
	 * @param id je nach id wird eine andere form zugewiesen
	 */
	public TetrisPiece(int id) {
		this.color=color;
		
		switch (id) {
		case 1:
			int[][] constructTetrisI={{1},{1},{1},{1}};
			form=constructTetrisI;
			this.color=new Color("Blue", 0, 0, 127); 
			break;
		case 2:
			int[][] constructTetrisREL={{0,1},{0,1},{1,1}};		
			form=constructTetrisREL;
			this.color=new Color("Yellow", 127, 127,0);
			break;
		case 3:
			int[][] constructTetrisL={{1,0},{1,0},{1,1}};
			form=constructTetrisL;
			this.color=new Color("Red",127,0,0);
			break;
		case 4:
			int[][] constructTetrisQ={{1,1},{1,1}};
			form=constructTetrisQ;
			this.color=new Color("Green",0,127,0);
			break;
		case 5:
			int[][] constructTetrisREZ={{0,1,1},{1,1,0}};
			form=constructTetrisREZ;
			this.color=new Color("Orange",127,63,0);
			break;
		case 6:
			int[][] constructTetrisT={{1,1,1},{0,1,0}};
			form=constructTetrisT;
			this.color=new Color("Turquoise",0,127,127);
			break;
		case 7:
			int[][] constructTetrisZ={{1,1,0},{0,1,1}};
			form=constructTetrisZ;
			this.color=new Color("Pink",127,0,127);
			break;
			
		}
 		this.length=form.length;
	}
	
	public int[][] getform(){
		return form;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getlength() {
		return length;
	}
	
	
	/**
	 * Methode zum Prüfen ob der Spielstein schon komplett dargestellt wurde.
	 * @param currentlength
	 * @return true wenn komplett dargestellt
	 */
	public boolean fullydisplayed(int currentlength) {
		return length-currentlength==0;
	}
}
