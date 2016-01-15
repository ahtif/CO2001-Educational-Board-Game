import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class PlayerSerializer {
	
	public void saveState(GameState state, String fileName) throws Exception{
		File file = new File(fileName);
		FileOutputStream outFileStream = new FileOutputStream(file);
		ObjectOutputStream outObjectStream = new ObjectOutputStream(outFileStream);
		outObjectStream.writeObject(state);
		outObjectStream.close();
	}
	
	public GameState loadState(String fileName) throws Exception{
		File file = new File(fileName);
		FileInputStream inFileStream = new FileInputStream(file);
		ObjectInputStream inObjectStream = new ObjectInputStream(inFileStream);
		GameState loadedState = (GameState) inObjectStream.readObject();
		inObjectStream.close();
		return loadedState;
	}
}
