import java.awt.*;
import javax.swing.*;
import java.util.*;
public class GUI extends JFrame{
	private ArrayList<JButton> choices;
	private JLabel description;
	private Container contentPane;
	private JPanel questionPanel, choicePanel, descriptionPanel;
	private Question question;
	public GUI() {
		setSize(1000,800);
		setResizable(false);
		setTitle("The 11+ Experience");
		
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		questionPanel = new JPanel();
		questionPanel.setLayout(new BoxLayout(questionPanel,BoxLayout.Y_AXIS));

		choicePanel = new JPanel();
		choicePanel.setLayout(new BoxLayout(choicePanel,BoxLayout.Y_AXIS));
		
		question = QuestionReader.generateQuestion();
		
		choices = new ArrayList<>();
		for(int i=0; i<question.choices.length; i++) {
			choices.add(new JButton(question.choices[i]));
			choicePanel.add(choices.get(i));
		}		
		
		description = new JLabel("<html>"+question.type + " : <br />" +question.description + "</html>");
		description.setMaximumSize(new Dimension(150, 100));
		questionPanel.add(description);
		questionPanel.add(choicePanel);
		
		
		
		contentPane.add(questionPanel, BorderLayout.EAST);
		
	}
	
	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.setVisible(true);
		
	}
	
}