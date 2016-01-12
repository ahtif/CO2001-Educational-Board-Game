
import java.util.*;
public class Question {

	String type,description;
	int noOfChoices, correct, wrong;
	String[] choices;
	ArrayList<Integer> answers;
	
	public Question(String type, String description, int noOfChoices, 
		String[] choices, ArrayList<Integer> answers, int correct, int wrong) {
			
		this.type = type;	
		this.description = description;
		this.noOfChoices = noOfChoices;
		this.choices = choices;
		this.answers = answers;
		this.correct = correct;	
		this.wrong = wrong;
		
	}
	
	
	
}