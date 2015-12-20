import java.io.Serializable;
public class Player implements Serializable{
	int position, counter;
	
	public Player(int position, int counter) {
		this.position = position;
		this.counter = counter;
	}
}