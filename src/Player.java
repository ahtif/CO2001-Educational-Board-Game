import java.io.Serializable;
public class Player implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int position;
	String colour;
	
	public Player(int position, String colour) {
		this.position = position;
		this.colour = colour;
	}
}