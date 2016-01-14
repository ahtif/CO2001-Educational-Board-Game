import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class PlayerSerializer {
	
	public void savePlayers(ArrayList<Player> players, String fileName) throws Exception{
		File file = new File(fileName);
		FileOutputStream outFileStream = new FileOutputStream(file);
		ObjectOutputStream outObjectStream = new ObjectOutputStream(outFileStream);
		outObjectStream.writeObject(players);
		outObjectStream.close();
	}
	
	public ArrayList<Player> loadPlayers(String fileName) throws Exception{
		File file = new File(fileName);
		FileInputStream inFileStream = new FileInputStream(file);
		ObjectInputStream inObjectStream = new ObjectInputStream(inFileStream);
		ArrayList<Player> loadedPlayers = (ArrayList<Player>) inObjectStream.readObject();
		inObjectStream.close();
		return loadedPlayers;
	}
}
