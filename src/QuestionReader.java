import java.io.*;
import java.util.*;
import java.util.regex.*;

public class QuestionReader {
	static List<Question> questions = new ArrayList<>();

	public static void readFile(){
		String type,description;
		int noOfChoices, correct, wrong;
		String[] choices;
		ArrayList<Integer> answers;
		Question question; 
		
		try{
			File questionFile = new File("src/files/questions.txt");
			Scanner scanner = new Scanner(questionFile);
			while (scanner.hasNext()) {
				type = scanner.nextLine();
				description = scanner.nextLine();
				noOfChoices = Integer.parseInt(scanner.nextLine());
				choices = new String[noOfChoices];
				for(int i = 0; i<noOfChoices; i++) {
					choices[i] = scanner.nextLine();
				}
				String tempAnswers = scanner.nextLine();
				Matcher m = Pattern.compile("\\d+").matcher(tempAnswers);
				answers = new ArrayList<Integer>();
				while(m.find()) {
					answers.add(Integer.parseInt(m.group()));
				}
				correct = Integer.parseInt(scanner.nextLine());
				wrong = Integer.parseInt(scanner.nextLine());
				question = new Question(type,description,noOfChoices,
				choices,answers,correct,wrong);
				questions.add(question);
			}
	
		}catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static Question generateQuestion() {
		readFile();
		Random rng = new Random();
		int random = rng.nextInt(questions.size());
		return questions.get(random);
	}
	
	public static void main(String[] args) {
		System.out.println((generateQuestion().answers));
	}
}