import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class GUI extends JFrame implements ActionListener{
	private ArrayList<JToggleButton> choices;
	private JLabel description;
	private Container contentPane;
	private JPanel questionPanel, choicePanel, descriptionPanel, choosePlayers, chooseCounters, turnPanel;
	private JButton doneSelectingPlayers, doneChoosingColours, submitAnswer;
	private JComboBox amtPlayers, counterBox;
	private JComboBox[] colourBox;
	private Question question;
	private Squares squares;
	private ArrayList<Player> players = new ArrayList<>();
	private int noOfPlayers, currentTurn;
	private HashMap<String,Color> colourMap = new HashMap<>();

	public GUI() {
		setSize(1200,800);
		setResizable(false);
		setTitle("The 11+ Experience");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		colourMap.put("Blue",Color.BLUE);
		colourMap.put("Green",Color.GREEN);
		colourMap.put("Orange",Color.ORANGE);
		colourMap.put("Red",Color.RED);

		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());	
	}

	public void actionPerformed(ActionEvent e){
		JButton clicked = (JButton) e.getSource();
		if (clicked == doneSelectingPlayers){
			noOfPlayers = (int) amtPlayers.getSelectedItem();
			System.out.println(noOfPlayers);
			choosePlayers.setVisible(false);
			setCounters(noOfPlayers);
		}
		if (clicked == doneChoosingColours){
			chooseCounters.setVisible(false);
			for(int i=0; i<noOfPlayers; i++){
				Player player = new Player(0,(String) colourBox[i].getSelectedItem());
				players.add(player);
			}
			currentTurn = 0;
			drawBoard();
			displayCurrentTurn();
			displayQuestion();
			setSize(1200,800);
		}
		if (clicked == submitAnswer){
			ArrayList<Integer> selectedAnswers = new ArrayList<>(); 
			for(int i=0; i<choices.size()-1; i++) {
				JToggleButton btn = choices.get(i);
				if(btn.isSelected())
					selectedAnswers.add(i+1);
			}
			Boolean correct = true;
			if (selectedAnswers.size()==question.answers.size()){
				for (int m=0; m<question.answers.size(); m++) {
					if(selectedAnswers.get(m)!=question.answers.get(m))
						correct = false;
				}
			}else
				correct = false;
			System.out.println("Your answer is " + correct);

		}
	}

	public void displayCurrentTurn(){
		turnPanel = new JPanel();
		JLabel currentLbl = new JLabel("Player " + (currentTurn+1) + "'s turn");
		Player currentPlayer = players.get(currentTurn);
		currentLbl.setForeground(colourMap.get(currentPlayer.colour));
		turnPanel.add(currentLbl);
		contentPane.add(turnPanel, BorderLayout.NORTH);
	}

	public void setCounters(int noOfPlayers){
		chooseCounters = new JPanel();
		chooseCounters.setLayout(new BoxLayout(chooseCounters,BoxLayout.Y_AXIS));
		JLabel chooseColour = new JLabel("Please select your colours:");
		chooseColour.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
		chooseCounters.add(chooseColour);		
		JPanel[] playerPanels = new JPanel[noOfPlayers];
		JLabel[] playerLbl = new JLabel[noOfPlayers];
		colourBox = new JComboBox[noOfPlayers];


		for (int i=0; i<noOfPlayers; i++){
			playerPanels[i] = new JPanel();

			playerLbl[i] = new JLabel("Player "+ (i+1));
			String[] colourArr = {"Blue","Green","Orange","Red"};
			colourBox[i] = new JComboBox(colourArr);
			if (i!=0) 
				colourBox[i].setEnabled(false);
			ActionListener colourListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox selectedBox = (JComboBox) e.getSource();
					int index = Arrays.asList(colourBox).indexOf(selectedBox);
					String selectedColour = (String) selectedBox.getSelectedItem();
					for (int m=0; m<noOfPlayers; m++) {
						if(colourBox[m]!=colourBox[index])
							colourBox[m].removeItem(selectedColour);
						if(m == index+1)
							colourBox[m].setEnabled(true);

					}
				}
			};
			colourBox[i].addActionListener(colourListener);

			playerPanels[i].add(playerLbl[i]);
			playerPanels[i].add(colourBox[i]);

			
			playerPanels[i].setPreferredSize(new Dimension(300,40));

			chooseCounters.add(playerPanels[i]);
		}

		doneChoosingColours = new JButton("Done");
		doneChoosingColours.addActionListener(this);

		chooseCounters.add(Box.createRigidArea(new Dimension(5,5)));
		chooseCounters.add(doneChoosingColours);
		chooseCounters.add(Box.createRigidArea(new Dimension(5,5)));
		contentPane.add(chooseCounters,BorderLayout.CENTER);
		pack();
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
		if (question.type.equals("Single Choice")){
			ButtonGroup btnGroup = new ButtonGroup();
			for(int i=0; i<question.choices.length; i++) {
				choices.add(new JToggleButton(question.choices[i]));
				choicePanel.add(Box.createRigidArea(new Dimension(5,5)));
				btnGroup.add(choices.get(i));
				choicePanel.add(choices.get(i));
			}		
		}else {
			for(int i=0; i<question.choices.length; i++) {
				choices.add(new JToggleButton(question.choices[i]));
				choicePanel.add(Box.createRigidArea(new Dimension(5,5)));
				choicePanel.add(choices.get(i));
			}
		}
		
		submitAnswer = new JButton("Submit");
		submitAnswer.addActionListener(this);
		choicePanel.add(Box.createRigidArea(new Dimension(5,10)));
		choicePanel.add(submitAnswer);

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