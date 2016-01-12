import java.io.Serializable;
public class Player implements Serializable{
	int position;
	String colour;
	
	public Player(int position, String colour) {
		this.position = position;
		this.colour = colour;
	}
}