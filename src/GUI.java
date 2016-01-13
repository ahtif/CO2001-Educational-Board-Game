import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;
import java.util.*;

public class GUI extends JFrame implements ActionListener{
	private ArrayList<JToggleButton> choices;
	private JLabel description;
	private Container contentPane;
	private JPanel questionPanel, choicePanel, choosePlayers, chooseCounters, turnPanel; 
	private JButton doneSelectingPlayers, doneChoosingColours, submitAnswer;
	private JComboBox amtPlayers, counterBox;
	private JComboBox[] colourBox;
	private Question question;
	private Board board;
	private ArrayList<Player> players = new ArrayList<>();
	private int noOfPlayers;
	private int currentTurn = 0;
	private HashMap<String,Color> colourMap = new HashMap<>();
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem newGame, saveGame, loadGame;
	public int state;

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

		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		newGame = new JMenuItem("New Game");
		saveGame = new JMenuItem("Save Game");
		loadGame = new JMenuItem("Load Game");
		
		MenuListener menuListener = new MenuListener();
		newGame.addActionListener(menuListener);
		saveGame.addActionListener(menuListener);
		loadGame.addActionListener(menuListener);
		
		fileMenu.add(newGame);
		fileMenu.add(saveGame);
		fileMenu.add(loadGame);

		menuBar.add(fileMenu);
		
		setPlayers();
		setVisible(true);

	}
	
	class MenuListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			if(item.equals(newGame)){
				String message = "<html><p>Are you sure you want to start a new game?</p></html>" ;
				int chosenOption = JOptionPane.showConfirmDialog(getParent(), message, "Start a New Game", 0);
				if (chosenOption == JOptionPane.YES_OPTION){
					dispose();
					new GUI();
				}
			}
			if(item.equals(saveGame)){
				showSaveDialog();
			}
			if(item.equals(loadGame)){
				showLoadDialog();
			}			
		}
	}

	private void showLoadDialog() {
		JFileChooser chooser = new JFileChooser("src/");
		int chosen = chooser.showOpenDialog(getParent());
		if (chosen == JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			try {
				FileInputStream inFileStream = new FileInputStream(file);
				ObjectInputStream inObjectStream = new ObjectInputStream(inFileStream);
				ArrayList<Player> loadedPlayers = (ArrayList<Player>) inObjectStream.readObject();
				inObjectStream.close();
				System.out.println(loadedPlayers.get(0).colour);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	private void showSaveDialog() {
		JFileChooser chooser = new JFileChooser();
		int chosen = chooser.showSaveDialog(getParent());
		if (chosen == JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			try {
				FileOutputStream outFileStream = new FileOutputStream(file);
				ObjectOutputStream outObjectStream = new ObjectOutputStream(outFileStream);
				outObjectStream.writeObject(players);
				System.out.println("save");
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}

	public void actionPerformed(ActionEvent e){
		JButton clicked = (JButton) e.getSource();
		if (clicked == doneSelectingPlayers){
			noOfPlayers = (int) amtPlayers.getSelectedItem();
			choosePlayers.setVisible(false);
			setCounters(noOfPlayers);
		}
		if (clicked == doneChoosingColours){
			chooseCounters.setVisible(false);
			for(int i=0; i<noOfPlayers; i++){
				Player player = new Player(0,(String) colourBox[i].getSelectedItem());
				players.add(player);
			}
			drawBoard();
			setJMenuBar(menuBar);
			initialisePlayers();
			displayCurrentTurn();
			displayQuestion();
			setSize(1200,800);
		}
		if (clicked == submitAnswer){
			ArrayList<Integer> selectedAnswers = new ArrayList<>(); 
			for(int i=0; i<choices.size(); i++) {
				JToggleButton btn = choices.get(i);
				if(btn.isSelected())
					selectedAnswers.add(i+1);
			}
			if(selectedAnswers.size()>0){
				
				boolean correct = checkIfCorrect(selectedAnswers);
				movePlayers(correct);
				drawPlayers();
				
				if (currentTurn+1 >= noOfPlayers) {
					currentTurn = 0;
				} else
					currentTurn++;
				turnPanel.setVisible(false);
				questionPanel.setVisible(false);
				displayCurrentTurn();
				displayQuestion();
				
			}
		}
	}
	
	public void finishGame(int playerNo){
		String winMessage = "<html><p>Congratulations! Player "+playerNo+" has won the game!</p>"
				+ "<p>Press OK to start a new game or cancel to quit</p></html>" ;
		int chosenOption = JOptionPane.showConfirmDialog(this, winMessage, "Player "+ playerNo + " Has Won!", 2);
		if (chosenOption == JOptionPane.OK_OPTION){
			this.dispose();
			new GUI();
		}else{
			System.exit(1);
		}
	}
	
	public boolean checkIfCorrect(ArrayList<Integer> selectedAnswers ){
		Boolean correct = true;
		if (selectedAnswers.size()==question.answers.size()){
			for (int m=0; m<question.answers.size(); m++) {
				if(selectedAnswers.get(m)!=question.answers.get(m))
					correct = false;
			}
		}else{
			correct = false;
		}
		return correct;
	}
	
	public void movePlayers(boolean correct){
		int currentPos = players.get(currentTurn).position;
		if (correct) {
			int moveAmt = question.correct;
			if (currentPos+moveAmt>=29){
				players.get(currentTurn).position = 29;
				finishGame(currentTurn+1);
			}else
				players.get(currentTurn).position += moveAmt;
		} else {
			int moveAmt = question.wrong;
			if (currentPos-moveAmt<0)
				players.get(currentTurn).position = 0;
			else
				players.get(currentTurn).position -= moveAmt;
		}
	}
	
	public void displayCurrentTurn(){
		turnPanel = new JPanel();
		JLabel currentLbl = new JLabel("Player " + (currentTurn+1) + "'s turn");
		Player currentPlayer = players.get(currentTurn);
		currentLbl.setForeground(colourMap.get(currentPlayer.colour));
		turnPanel.add(currentLbl);
		contentPane.add(turnPanel,BorderLayout.NORTH);
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
		board = new Board();
	    contentPane.add(board,BorderLayout.CENTER);
	    for (int i = 0; i < 9; i++) {
	   		board.addSquare(i * 100, 0, 80, 80);
	    }
	    board.addSquare(800, 100, 80, 80);

	    for (int i = 8; i >= 0; i--) {
	   		board.addSquare(i * 100, 200, 80, 80);
	    }
	    board.addSquare(0, 300, 80, 80);
	    for (int i = 0; i < 9; i++) {
	   		board.addSquare(i * 100, 400, 80, 80);
	    }
	    board.addSquare(800, 500, 80, 80);	    
	}

	public void initialisePlayers(){
		for(int i=0; i<noOfPlayers; i++){
			Player currentPlayer = players.get(i);
			Color playerColor = colourMap.get(currentPlayer.colour);
			
			switch (i) {
				case 0: board.addCircle(5,5,20,20,playerColor);
						break;
				case 1: board.addCircle(50,5,20,20,playerColor);
						break;
				case 2: board.addCircle(5,50,20,20,playerColor);
						break;
				case 3: board.addCircle(50,50,20,20,playerColor);
						break;
			}
		}
	}

	public void drawPlayers(){
		for (int i=0; i<noOfPlayers; i++){
			Player currentPlayer = players.get(i);
			int position = currentPlayer.position;
			Rectangle currentSquare = board.squares.get(position);
		 	int x = (int) currentSquare.getX();
		 	int y = (int) currentSquare.getY();
			switch (i) {
				case 0: board.updateCircle(i, x+5, y+5);		
						break;
				case 1: board.updateCircle(i, x+50, y+5);
						break;
				case 2: board.updateCircle(i, x+5, y+50);
						break;
				case 3: board.updateCircle(i, x+50, y+50);
						break;
			}
		}
	}

	public void displayQuestion(){
		questionPanel = new JPanel();
		questionPanel.setLayout(new BoxLayout(questionPanel,BoxLayout.Y_AXIS));
		
		choicePanel = new JPanel();
		choicePanel.setLayout(new BoxLayout(choicePanel,BoxLayout.Y_AXIS));
		
		question = null;
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
		
	}
	
}