public class Appartement extends Building {
		//the Constructors 


	//Attributes 
	private String owner;
	private int floor;
	private int number;
	private List<Float> a_list;


	//compositions
	private Room kitchen;
	private List<Room> bedrooms;


	//aggregations
	private List<Furniture> furniture;


	//getters 

	//The owner's getter
	public String getOwner(){
		return owner;
	}

	//The floor's getter
	public int getFloor(){
		return floor;
	}

	//The number's getter
	public int getNumber(){
		return number;
	}

	//The a_list's getter
	public List<Float> getA_list(){
		return a_list;
	}


	//setters 

	// The owner's setter
	public void setOwner(String owner){
		this.owner = owner;
	}

	// The floor's setter
	public void setFloor(int floor){
		this.floor = floor;
	}

	// The number's setter
	public void setNumber(int number){
		this.number = number;
	}

	// The a_list's setter
	public void setA_list(List<Float> a_list){
		this.a_list = a_list;
	}


	//methods
	public float printFloor(int floorNumber){
		//Method's body make sure to add the necessary code
	}
	public float printOwner(List<String> ownerFirstName, String[] ownerLastName){
		//Method's body make sure to add the necessary code
	}
}
