import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class SudokuSolver{
	
	private static JFrame frame;
	private static JPanel sudokuBoard;
	private static JPanel[][] subGrid = new JPanel[3][3];
	private static JButton solve;
	private static JTextField[][] box = new JTextField[9][9];
	private static int board[][] = new int[9][9];
	private static JButton reset;
	 
	
	private static void setFrame() {
		frame = new JFrame("Sudoku Solver");
		frame.setSize(450,530);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.getContentPane().setBackground(Color.gray);
		frame.setLocationRelativeTo(null);
		
		
		// setting Name
		JPanel namePanel = new JPanel();
		namePanel.setBounds(40,5,360,40);
		//namePanel.setBorder(BorderFactory.createLineBorder(Color.blue));
		namePanel.setBackground(new Color(175,214,255));
		
		//JLabel to display name
		JLabel label = new JLabel();
		label.setText("SUDOKU SOLVER");
		label.setForeground(Color.BLUE);
		label.setFont(new Font("Arial",Font.BOLD,25));
		label.setVisible(true);
		
		//panel for Sudoku Board
		sudokuBoard = new JPanel();
		sudokuBoard.setBounds(40,50,360,360);
		sudokuBoard.setLayout(new GridLayout(3,3));
		sudokuBoard.setBackground(Color.DARK_GRAY);
		sudokuBoard.setBorder(BorderFactory.createLineBorder(Color.blue));
		for(int srow=0; srow<9; srow += 3) {
			for(int scol=0; scol<9; scol+= 3) {
				setTextFields(srow,scol);
			}
		}
		
		//Solve Button
		solve = new JButton("Solve");
		solve.setBounds(100,430,100,30);
		solve.setBackground(Color.cyan);
		
		//Adding action listener to the solve button
		solve.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(getData() && solver(0,0)) {
					updateBoard();
					solve.setEnabled(false);
				}
				else {
					JOptionPane.showMessageDialog(frame, "No Solution Exists", "Sudoku Solver",JOptionPane.ERROR_MESSAGE);
					resetBoard();
				}
				reset.setEnabled(true);
			}	
			});
		
		//Adding reset Button
		reset = new JButton("Reset");
		reset.setBackground(Color.yellow);
		reset.setForeground(Color.black);
		reset.setBounds(230,430,100,30);
		reset.setEnabled(false);
		//adding actionListener to reset button
		reset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				resetBoard();
				reset.setEnabled(false);
				solve.setEnabled(true);
			}
		});
		
		//Adding all components to Frame
		
		namePanel.add(label);
		frame.add(namePanel);
		frame.add(sudokuBoard);
		frame.add(solve);
		frame.add(reset);
		frame.setVisible(true);
		
	}

	//Adding textfields to corresponding subGrids
	private static void setTextFields(int srow,int scol) {
		subGrid[srow/3][scol/3] = new JPanel();
		subGrid[srow/3][scol/3].setLayout(new GridLayout(3,3));
		subGrid[srow/3][scol/3].setBorder(BorderFactory.createLineBorder(new Color(51,204,255)));
		for(int i=srow; i<srow+3; i++) {
			for(int j=scol; j<scol+3; j++) {
				box[i][j] = new JTextField();
				box[i][j].setHorizontalAlignment(JTextField.CENTER);
				box[i][j].setFont(new Font("Arial", Font.PLAIN,18));
				box[i][j].setForeground(new Color(0,0,255));
				subGrid[srow/3][scol/3].add(box[i][j]);
			}
		}
		sudokuBoard.add(subGrid[srow/3][scol/3]);
	}
	
	//getting data from textfields to board array
	public static boolean getData() {
		for(int row=0; row<9; row++) {
			for(int col=0; col<9; col++) {
				String value = box[row][col].getText();
				if(value.isEmpty()) {
					board[row][col] = 0;
				}
				else {
					box[row][col].setBackground(new Color(175,214,255));
					try {
						int data = Integer.parseInt(value);
						if(data >9 || data<0 || !isSafe(data , row, col) ) {
							return false;
						}
						else
							board[row][col] = data;
					}
					catch( NumberFormatException e) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	// Sudoku Solver
	public static boolean solver(int row,int col) {
		if(row == board.length) {
			return true;
		}
		int newrow = 0;
		int newcol = 0;
		if( col != board.length-1) {
			newrow = row;
			newcol = col +1;
		}
		else {
			newrow = row+1;
			newcol = 0;
		}
		if( board[row][col] != 0) {
			if(solver(newrow, newcol))
				return true;
		}
		else{
			for( int num = 1; num<= 9; num++) {
				if( isSafe(num,row,col)){
					board[row][col] = num;
					if(solver(newrow, newcol))
						return true;
					else {
						board[row][col] = 0;
					}
				}
			}
		}
		return false;
	}
	
	// safety function
	public static boolean isSafe(int num, int row, int col) {
		// Checking rows and columns
		for( int i=0; i<board.length; i++) {
			if( board[i][col] == num || board[row][i] == num )
				return false;
		}
		
		// checking grid
		int sr = (row/3)*3;
		int sc = (col/3)*3;
		
		for( int i=sr; i<sr+3; i++) {
			for( int j=sc; j<sc+3; j++) {
				if(board[i][j] == num) {
					return false;
				}
			}
		}
		return true;
	}
	
	//final output
	private static void updateBoard() {
		for(int row=0; row<9; row++) {
			for(int col=0; col<9; col++) {
				box[row][col].setText(Integer.toString(board[row][col]));
			}
		}
	}
	
	//Reset Board Funtion
	public static void resetBoard() {
		for(int row=0; row<9; row++) {
			for(int col=0; col<9; col++) {
				board[row][col] = 0;
				box[row][col].setText("");
				box[row][col].setBackground(Color.WHITE);
			}
			
		}
	}
	
	// main method
	public static void main( String[] args) {
		setFrame();
	}

}

