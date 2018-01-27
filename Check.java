public class Check {
	public static void main(String[] args) {
		Grid myLaby = Laby.makeLaby(15, 10);//two dimensional labyrinth
		//Grid myLaby1 = Laby.makeLabyA(15, 20);
		myLaby.showGrid();
		System.out.println("Check passed");
	}
}