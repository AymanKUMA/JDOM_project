public class Building {


	//Attributes 
	private String address;
	private int[] a_table = new int[20];


	//compositions
	//No compositions


	//aggregations
	private List<Appartement> app;

	//the Constructors
	public Building(List<Appartement> app){
		this.app = app;
	}
	public Building(String address, int[] a_tableList<Appartement> app, ){
		this.address = address;
		if(a_table.length == 20){
			this.a_table = a_table;
		} else {
			throw new IllegalArgumentException("The provided array must have exactly 20 elements.");
		}
		this.app = app;
	}


	//getters 

	//The address's getter
	public String getAddress(){
		return address;
	}

	//The a_table's getter
	public int[] getA_table(){
		return a_table;
	}


	//setters 

	// The address's setter
	public void setAddress(String address){
		this.address = address;
	}

	// The a_table's setter
	public void setA_table(int[] a_table){
		if(a_table.length == 20){
			this.a_table = a_table;
		} else {
			throw new IllegalArgumentException("The provided array must have exactly 20 elements.");
		}
	}


	//methods
}
