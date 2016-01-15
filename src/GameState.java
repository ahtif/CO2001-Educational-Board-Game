import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable{
	ArrayList<Player> players;
	int currentPlayer;
	
	public GameState(ArrayList<Player> players, int currentPlayer){
		this.players = players;
		this.currentPlayer = currentPlayer;
	}
}
