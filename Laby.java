import java.util.ArrayList;
import java.util.Random;

public class Laby{
	
	/*Premiere methode permettant de creer un labyrinthe a l'aide d'un parcours lineaire de la grille (dans l'ordre des identifiant des cellules).
	 *On parcourt la grille dans l'ordre des id des cellules(de gauche vers la droite et du haut vers le bas de la grille).
	 *Tant que toute les cellules de la grille ne sont pas connectees entre elles on cree une liste des voisins non connectes a la cellule courante.
	 *Si la liste n'est pas vide alors il faut connecter un des voisins choisit aleatoirement, on casse le mur entre les deux cellules et on les 
	 *connecte entre elles. Si le voisins nouvellement connecte etait connecte avec d'autre cellules celle-ci sont a present connectees a la cellule
	 *courante.
	 */
	public static Grid makeLabyA(int n, int m){
		Grid grille = new Grid(n,m);
		while(!grille.touteCelluleConnectee()){
			for(int i = 0; i < grille.rows; i++){
				for(int j = 0; j < grille.columns; j++){
					ArrayList<Integer> casPossible = grille.cell[i][j].voisinsNonConnectes();
					if(casPossible.size() != 0){
						Random rand = new Random();
						int position = (int)(rand.nextInt(casPossible.size()));
						int orientation = casPossible.get(position);
						grille.casserMurs(grille.cell[i][j],orientation);
					}
				}
			}
		}
		return grille;
	}

	/*Seconde methode permettant de creer un labyrinthe a l'aide d'un parcours aleatoire de la grille(suite a une permutation aleatoire des id).
	 *on cree un tableau dans lequel il y a les id de toutes les cellules de la grille ranges sans ordre precis car permutes aleatoirement.
	 *Tant que toutes les cellules de la grille ne sont pas connectees entre elles, pour chaque cellules rencontree, on stocke ses voisins non
	 *connectes a elle, puis on en choisit un aleatoirement on casse le mur adjacent aux deux et on les connecte ainsi que leurs voisins precedemment
	 *connectes.
	 */
	public static Grid makeLaby(int n,int m){
		Grid grille = new Grid(n,m);
		while(!grille.touteCelluleConnectee()){
			int [] l = grille.permutation(grille.rows*grille.columns);
			for(int i = 0; i < l.length; i++){
				int colonne = grille.trouverColonne(l,m,i);
				int ligne = grille.trouverLigne(l,m,i);
				ArrayList<Integer> casPossible = grille.cell[ligne][colonne].voisinsNonConnectes();
				if(casPossible.size() != 0){
					Random rand = new Random();
					int position = (int)(rand.nextInt(casPossible.size()));
					int orientation = casPossible.get(position);
					grille.casserMurs(grille.cell[ligne][colonne],orientation);		
				}
			}
		}
		return grille;
		//return new Grid(n,m);
	}

	public static void main(String[] args){
		//method a completer
		//makeLabyA(30,10).showGrid();
		//makeLabyA(30,10).showGrid(true);
		makeLaby(30,35).showGrid();
		//makeLaby(4,6).showGrid(true);
	}
	
}
