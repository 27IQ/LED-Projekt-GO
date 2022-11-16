public class TetrisPiece {
	Color color;
	int[][] form;
		
	/**
	 * Konstruktor für einen Tetrisstein
	 * @param id je nach id wird eine andere form zugewiesen
	 */
	public TetrisPiece(int id) {
		
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
		default:
			int[][] constructTetrisZ2={{1,1,0},{0,1,1}};
			form=constructTetrisZ2;
			this.color=new Color("Pink",127,0,127);
			break;
		}			
	}
	
	public int[][] getform(){
		return form;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void rotateleft() {
		
		int[][] rotated=new int[form[0].length][form.length];
		
		for(int i=0;i<form.length;i++) {
			for(int j=form[i].length-1;j>=0;j--) {
				rotated[j][i]=form[i][form[i].length-1-j];
			}
		}		
		form=rotated;
	}
	
	public void rotateright() {
		rotateleft();
		rotateleft();
		rotateleft();
	}
	
	@Override
	public String toString() {
		String s="";
		
		for (int[] is : form) {
			for (int i : is) {
				s+=Integer.toString(i)+" ";
			}
			s+="\n";
		}
		

		return s;
	}
}
