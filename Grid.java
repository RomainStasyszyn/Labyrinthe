public class Grid {
	public final int rows, columns;
	final Cellule[][] cell;

	public Grid(int n, int m) {
		this.rows = n;
		this.columns = m;
		this.cell = new Cellule[n][m];

		/* Initialize all the cells */
		for (int i = 0; i < n; i++)
			for (int j = 0; j < m; j++)
				cell[i][j] = new Cellule(this.columns * i + j);

		/* Set the neighbors */
		for (int i = 0; i < n; i++)
			for (int j = 0; j < m; j++) {
				
				/* Set EAST */
				this.cell[i][j].setNeighbor(Cellule.EAST, (j == this.columns - 1) ? null : this.cell[i][j + 1]);
				
				/* Set WEST */
				this.cell[i][j].setNeighbor(Cellule.WEST, (j == 0) ? null : this.cell[i][j - 1]);
				
				/* Set SOUTHEAST */
				if (i == this.rows - 1)
					this.cell[i][j].setNeighbor(Cellule.SOUTHEAST, null);
				else if (i % 2 == 0)
					this.cell[i][j].setNeighbor(Cellule.SOUTHEAST, this.cell[i + 1][j]);
				else if (i % 2 == 1 && j < this.columns - 1)
					this.cell[i][j].setNeighbor(Cellule.SOUTHEAST, this.cell[i + 1][j + 1]);
				else
					this.cell[i][j].setNeighbor(Cellule.SOUTHEAST, null);

				/* Set SOUTHWEST */
				if (i == this.rows - 1)
					this.cell[i][j].setNeighbor(Cellule.SOUTHWEST, null);
				else if (i % 2 == 0 && j != 0)
					this.cell[i][j].setNeighbor(Cellule.SOUTHWEST, this.cell[i + 1][j - 1]);
				else if (i % 2 == 1)
					this.cell[i][j].setNeighbor(Cellule.SOUTHWEST, this.cell[i + 1][j]);
				else
					this.cell[i][j].setNeighbor(Cellule.SOUTHWEST, null);

				/* Set NORTHEAST */
				if (i <= 0)
					this.cell[i][j].setNeighbor(Cellule.NORTHEAST, null);
				else if (i % 2 == 0)
					this.cell[i][j].setNeighbor(Cellule.NORTHEAST, this.cell[i - 1][j]);
				else if (i % 2 == 1 && j < this.columns - 1)
					this.cell[i][j].setNeighbor(Cellule.NORTHEAST, this.cell[i - 1][j + 1]);
				else
					this.cell[i][j].setNeighbor(Cellule.NORTHEAST, null);

				/* Set NORTHWEST */
				if (i <= 0)
					this.cell[i][j].setNeighbor(Cellule.NORTHWEST, null);
				else if (i % 2 == 0 && j != 0)
					this.cell[i][j].setNeighbor(Cellule.NORTHWEST, this.cell[i - 1][j - 1]);
				else if (i % 2 == 1)
					this.cell[i][j].setNeighbor(Cellule.NORTHWEST, this.cell[i - 1][j]);
				else
					this.cell[i][j].setNeighbor(Cellule.NORTHWEST, null);
			}
	}
	
	public String showContent(boolean show, int i, int j) {
		if (show) return Auxiliaire.numInThree(this.cell[i][j].getId());
		else return "   ";
	}
	
	/* Don't judge me */	
	public void showGrid(boolean show) {
		for (int j = 0; j < this.columns; j++)
			System.out.print(" / \\");
		System.out.println("");
		for (int i = 0; i < this.rows; i++) {
			if (i % 2 == 1)
				System.out.print("  ");
			for (int j = 0; j < this.columns; j++)
				System.out.print(
						((this.cell[i][j].getNeighbor(Cellule.WEST).isWall() && (i!=0 || j!=0))?"|":" ") + showContent(show, i, j));
			if (i!=this.rows-1)
				System.out.println("|");
			else System.out.println(" ");
			if (i < this.rows - 1)
				if (i % 2 == 1) {
					for (int j = 0; j < this.columns; j++)
						System.out.print(" " + ((j == 0 || this.cell[i][j - 1].getNeighbor(Cellule.SOUTHEAST).isWall() ? "/" : " "))
										+ " " + (this.cell[i][j].getNeighbor(Cellule.SOUTHWEST).isWall() ? "\\" : " "));
					System.out.println(" /");
				} else {
					for (int j = 0; j < this.columns; j++)
						System.out.print(" " + ((j == 0 || this.cell[i][j].getNeighbor(Cellule.SOUTHWEST).isWall() ? "\\" : " "))
										+ " " + (this.cell[i][j].getNeighbor(Cellule.SOUTHEAST).isWall() ? "/" : " "));
					System.out.println(" \\");
				}
		}
		if (this.rows % 2 == 0)
			System.out.print("  ");
		for (int j = 0; j < this.columns; j++)
			System.out.print(" \\ /");
		System.out.println("");

	}
	
	//Retourne vrai si toutes les cellules de la grille sont connectees(meme id pour toute) sinon renvoie false.
		public boolean touteCelluleConnectee(){
			for(int i = 0; i < this.rows; i++){
				for(int j = 0; j < this.columns; j++){
					if(this.cell[i][j].getId() != this.cell[0][0].getId()){
						return false;
					}
				}
			}
			return true;
		}
	
	/*Methode pour casser un mur entre deux cellules puis les connecter et celle qui l'etaient deja avec le voisins choisit.
	 *On stocke l'id de la cellule voisine choisie, on casse le mur entre la cellule courante et le voisin puis on change l'id des cellules
	 *connectees au voisin par l'id de la celulle courante afin qu'elles soient a present connectees entre elles.
	 *On parcourt toute la grille et des que l'id de la cellule courante est equivalent a celui du voisin choisit on change l'id par celui
	 *de la cellule qui vient de casser le mur avec son voisin. Ainsi on connecte la cellule avec son voisin et celles qui etaient precedemment 
	 *connectees avec le voisin.
	 */
	public void casserMurs(Cellule cellule, int orientation){
		Integer id = cellule.getNeighborId(orientation);
		cellule.breakWallWith(orientation);
		for(int i = 0; i < this.rows; i++){
			for(int j = 0; j < this.columns; j++){
				if(this.cell[i][j].getId() == id){
					this.cell[i][j].setId(cellule.getId());
				}
			}
		}
	}
	
	//Methode creant un tableau contenant les id de toute les cellules de la grille permuttees de maniere aleatoire.
	public int[] permutation(int n){
		int [] p = new int[n];
		for(int i = 1; i < n; i++){
			p[i] = i;
		}
		for(int i = 1; i < n; i++){
			int alea = (int)(Math.random()*i);
			int echange = p[alea];
			p[alea] = p[i];
			p[i] = echange;
		}
		return p;
	}
	
	//Methode permettant de trouver le numero de colonne de la cellule courante dans la grille a partir de son id contenu dans le tableau de permutation.
	public int trouverColonne(int [] tableauPermute,int nbColonnes, int position){
		if((tableauPermute[position]%nbColonnes) == 0){
			return nbColonnes-1;
		}
		else{
			return (tableauPermute[position]%nbColonnes-1);
		}
	}
	
	//Methode permettant de trouver le numero de ligne de la cellule courante dans la grille a partir de son id contenu dans le tableau de permutation. 
	public int trouverLigne(int [] tableauPermute,int nbColonnes, int position){
		return (int)(tableauPermute[position]/(nbColonnes+1));
	}
	
	public void showGrid() { this.showGrid(false);	}
}
