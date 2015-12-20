import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class GUI extends JFrame implements ActionListener{
	private ArrayList<JButton> choices;
	private JLabel description;
	private Container contentPane;
	private JPanel questionPanel, choicePanel, descriptionPanel, choosePlayers;
	private JButton doneSelectingPlayers;
	private JComboBox amtPlayers;
	private Question question;
	private Squares squares;
	private ArrayList<Player> players;

	public GUI() {
		setSize(1200,800);
		setResizable(false);
		setTitle("The 11+ Experience");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());	
	}

	public void actionPerformed(ActionEvent e){
		JButton clicked = (JButton) e.getSource();
		if (clicked == doneSelectingPlayers){
			choosePlayers.setVisible(false);
		}
	}

	public void setPlayers(){
		choosePlayers = new JPanel();
		JLabel choose = new JLabel("<html><center>Please select the number of players</center></html>");
		choose.setPreferredSize(new Dimension(300,20));

		Integer[] arr = {1,2,3,4};
		amtPlayers = new JComboBox(arr);
		amtPlayers.setPreferredSize(new Dimension(100,20));

		doneSelectingPlayers = new JButton("Done");
		doneSelectingPlayers.addActionListener(this);

		choosePlayers.add(choose);
		choosePlayers.add(amtPlayers);
		choosePlayers.add(doneSelectingPlayers);
		contentPane.add(choosePlayers,BorderLayout.CENTER);
		pack();
	}

	public void drawBoard(){
		Squares squares = new Squares();
	    contentPane.add(squares,BorderLayout.CENTER);
	    for (int i = 0; i < 9; i++) {
	   		squares.addSquare(i * 100, 0, 80, 80);
	    }
	    squares.addSquare(800, 100, 80, 80);

	    for (int i = 8; i >= 0; i--) {
	   		squares.addSquare(i * 100, 200, 80, 80);
	    }
	    squares.addSquare(0, 300, 80, 80);
	    for (int i = 0; i < 9; i++) {
	   		squares.addSquare(i * 100, 400, 80, 80);
	    }
	    squares.addSquare(800, 500, 80, 80);	    
	}

	public void displayQuestion(){
		questionPanel = new JPanel();
		questionPanel.setLayout(new BoxLayout(questionPanel,BoxLayout.Y_AXIS));
		
		choicePanel = new JPanel();
		choicePanel.setLayout(new BoxLayout(choicePanel,BoxLayout.Y_AXIS));
		
		question = QuestionReader.generateQuestion();
		
		choices = new ArrayList<>();
		for(int i=0; i<question.choices.length; i++) {
			choices.add(new JButton(question.choices[i]));
			choicePanel.add(Box.createRigidArea(new Dimension(5,5)));
			choicePanel.add(choices.get(i));
		}		
		
		description = new JLabel("<html>"+question.type + " : <br /><br />" +question.description + "<br /><br /></html>");
		description.setPreferredSize(new Dimension(200, 100));
		questionPanel.add(description);
		questionPanel.add(choicePanel);
				
		contentPane.add(questionPanel, BorderLayout.EAST);
	}
	
	public static void main(String[] args) {
		GUI gui = new GUI();
		//gui.displayQuestion();
		gui.setPlayers();
		//gui.drawBoard();
		gui.setVisible(true);
		
	}
	
}