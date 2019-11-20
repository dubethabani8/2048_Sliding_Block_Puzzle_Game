/*
 * This class runs the whole game and its graphics in the main method
 * It contains a game object and runs this through with graphics and button Listeners*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class _2048 extends JComponent {
	
	private Game game; //Game object with all the methods related to game play
	private _2048.InfoCanvas infoCanvas; //Canvas that contains the moves counter and informaion
	String function; //for checking confirmation of quit and new game by setting to appropriate string
	
	public _2048() { //Costructor that sets a new Graphical represantation of the game
		game = new Game();
		JFrame frame = new JFrame("2048");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		infoCanvas = new InfoCanvas();
		this.setPreferredSize(new Dimension(700, 700));
		infoCanvas.setPreferredSize(new Dimension(100, 100));
		
		frame.add(this, BorderLayout.CENTER);
		frame.add(infoCanvas, BorderLayout.NORTH);
		frame.pack();
		frame.setVisible(true);
		
		ButtonListener buttonListener = new ButtonListener();
		this.addKeyListener(buttonListener);
		infoCanvas.addKeyListener(buttonListener);
		this.setFocusable(true);
		this.function = "";
	}
	
	
	protected class ButtonListener implements KeyListener{ //Listener for the game keys

		@Override
		public void keyPressed(KeyEvent e) {
			//Print to console key pressed and whether it is a valid move
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				System.out.println("Key pressed: Left");
				break;
			case KeyEvent.VK_RIGHT:
				System.out.println("Key pressed: Right");
				break;
			case KeyEvent.VK_UP:
				System.out.println("Key pressed: Up");
				break;
			case KeyEvent.VK_DOWN:
				System.out.println("Key pressed: Down");
				break;
			default:
			System.out.println("Key pressed: " + e.getKeyChar());
			break;
			}
			if(game.win) { //Check winnning condition
				repaint();
				infoCanvas.repaint();
				return;
			}
			if(game.gameOver || game.win) { //Restart and Quit keys
				if(e.getKeyCode() == KeyEvent.VK_R) {
					game = new Game();
					repaint();
					infoCanvas.repaint();
					return;
				}
				else if(e.getKeyCode() == KeyEvent.VK_Q) {
					System.exit(0);
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_Y) { //Keys for confirmation of quit and restart
				if(function.equals("Restart")) {
					game = new Game();
					repaint();
					infoCanvas.repaint();
				}
				else if(function.equals("Quit")) {
					System.exit(0);
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_N) {
			game.gameOn = true;
			}
			else if(!game.gameOn) {
				System.out.println("Invalid move");
				System.out.println("Moves: " + game.moves);
				System.out.println();
			}
			if(!game.gameOn && !game.gameOver) { //is game is not On (maybe waiting for confirmation of restart or quit) then no moves allowed)
				repaint();
				infoCanvas.repaint();
				return;
				}
			//Listen to keys and make appropriate play
			switch(e.getKeyCode()) {
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				game.move('l');
				break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				game.move('r');
				break;
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
				game.move('u');
				break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				game.move('d');
				break;
			case KeyEvent.VK_Q:
				function = "Quit";
				game.gameOn = false;
				System.out.println("Waiting for quit confirmation");
				System.out.println("Moves: " + game.moves);
				System.out.println();
				break;
			case KeyEvent.VK_R:
				function = "Restart";
				game.gameOn = false;
				System.out.println("Waiting for restart confirmation");
				System.out.println("Moves: " + game.moves);
				System.out.println();
				break;
			default:
				if(e.getKeyCode() != KeyEvent.VK_Y || e.getKeyCode() != KeyEvent.VK_N) {
					System.out.println("Invalid move");
					System.out.println("Moves: " + game.moves);
					System.out.println();
				}
				else {
					System.out.println(function + " is confirmed");
					System.out.println("Moves: " + game.moves);
				}
			}
			repaint();
			infoCanvas.repaint();
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	@Override
	public void paintComponent(Graphics q) { //PaintComponent for the game grid
		int w = getWidth();
		int h = getHeight();
		int unitH = h/45;
		int unitW = w/45;
		int sqW = unitW * 10;
		int sqH = unitH * 10;
		q.setColor(new Color(160,160,160));
		q.fillRect(0, 0, w, h);
		int y1 = unitH;
		for(int i=0; i<game.array.length; i++) {
			int x1 = unitW;
			for(int j=0; j<game.array[i].length; j++) {
				switch(game.array[i][j]) {
				case 2:
					q.setColor(new Color(255, 229, 204));
					q.fillRect(x1, y1, sqW, sqH);
					q.setColor(new Color(64,64,64));
					q.setFont(new Font("Haveltica", Font.BOLD, 50));
					q.drawString(Integer.toString(game.array[i][j]), x1+unitW*4, y1+unitH*6);
					break;
				case 4:
					q.setColor(new Color(255,204,153));
					q.fillRect(x1, y1, sqW, sqH);
					q.setColor(new Color(64,64,64));
					q.setFont(new Font("Haveltica", Font.BOLD, 50));
					q.drawString(Integer.toString(game.array[i][j]), x1+unitW*4, y1+unitH*6);
					q.setColor(new Color(255,204,153));
					break;
				case 8:
					q.setColor(new Color(255,178,102));
					q.fillRect(x1, y1, sqW, sqH);
					q.setColor(Color.WHITE);
					q.setFont(new Font("Haveltica", Font.BOLD, 50));
					q.drawString(Integer.toString(game.array[i][j]), x1+unitW*4, y1+unitH*6);
					q.setColor(new Color(255,178,102));
					break;
				case 16:
					q.setColor(new Color(255,153,153));
					q.fillRect(x1, y1, sqW, sqH);
					q.setColor(Color.WHITE);
					q.setFont(new Font("Haveltica", Font.BOLD, 50));
					q.drawString(Integer.toString(game.array[i][j]), x1+unitW*3, y1+unitH*6);
					break;
				case 32:
					q.setColor(new Color(238,102,102));
					q.fillRect(x1, y1, sqW, sqH);
					q.setColor(Color.WHITE);
					q.setFont(new Font("Haveltica", Font.BOLD, 50));
					q.drawString(Integer.toString(game.array[i][j]), x1+unitW*3, y1+unitH*6);
					break;
				case 64:
					q.setColor(new Color(255,51,51));
					q.fillRect(x1, y1, sqW, sqH);
					q.setColor(Color.WHITE);
					q.setFont(new Font("Haveltica", Font.BOLD, 50));
					q.drawString(Integer.toString(game.array[i][j]), x1+unitW*3, y1+unitH*6);
					break;
				case 128:
					q.setColor(new Color(255,215,0));
					q.fillRect(x1, y1, sqW, sqH);
					q.setColor(Color.WHITE);
					q.setFont(new Font("Haveltica", Font.BOLD, 50));
					q.drawString(Integer.toString(game.array[i][j]), x1+unitW*2, y1+unitH*6);
					break;
				case 256:
					q.setColor(new Color(255,215,0));
					q.fillRect(x1, y1, sqW, sqH);
					q.setColor(Color.WHITE);
					q.setFont(new Font("Haveltica", Font.BOLD, 50));
					q.drawString(Integer.toString(game.array[i][j]), x1+unitW*2, y1+unitH*6);
					break;
				case 512:
					q.setColor(new Color(255,215,0));
					q.fillRect(x1, y1, sqW, sqH);
					q.setColor(Color.WHITE);
					q.setFont(new Font("Haveltica", Font.BOLD, 50));
					q.drawString(Integer.toString(game.array[i][j]), x1+unitW*2, y1+unitH*6);
					break;
				case 1024:
					q.setColor(new Color(255,215,0));
					q.fillRect(x1, y1, sqW, sqH);
					q.setColor(Color.WHITE);
					q.setFont(new Font("Haveltica", Font.BOLD, 50));
					q.drawString(Integer.toString(game.array[i][j]), (int) (x1+unitW*.5), y1+unitH*6);
					break;
				case 2048:
					q.setColor(new Color(0,255,0));
					q.fillRect(x1, y1, sqW, sqH);
					q.setColor(Color.WHITE);
					q.setFont(new Font("Haveltica", Font.BOLD, 50));
					q.drawString(Integer.toString(game.array[i][j]), (int) (x1+unitW*.5), y1+unitH*6);
					break;
				default:
					q.setColor(new Color(192,192,192));
					q.fillRect(x1, y1, sqW, sqH);
					break;
					
				}
				
				x1 += unitW * 11;
			}
			y1 += unitH * 11;
		}
		if(game.win) {
			q.setColor(Color.RED);;
			q.setFont(new Font("Haveltica", Font.BOLD, 100));
			q.drawString("YOU WIN!!", getWidth()/30, 2*getHeight()/4);
			q.setFont(new Font("Haveltica", Font.BOLD, 30));
			q.drawString("Press key R to restart or Q to quit", getWidth()/25, 6*getHeight()/10);
		}
		else if(!function.equals("") && !game.gameOn && !game.gameOver) {
			q.setColor(Color.RED);;
			q.setFont(new Font("Haveltica", Font.BOLD, 30));
			q.drawString("Are you sure you want to " + function +"?", getWidth()/15, getHeight()/3);
			q.drawString("Press key Y to comfirm or N to cancel", getWidth()/15, 2*getHeight()/4);
			
		}
		else if(game.gameOver) {
			q.setColor(Color.RED);;
			q.setFont(new Font("Haveltica", Font.BOLD, 100));
			q.drawString("Game Over", getWidth()/30, 2*getHeight()/4);
			q.setFont(new Font("Haveltica", Font.BOLD, 30));
			q.drawString("Press key R to restart or Q to quit", getWidth()/25, 6*getHeight()/10);
		}
	}
	
	protected class InfoCanvas extends JComponent { //InfoCnvas class for displaying info and move counter
		
		public InfoCanvas(){
			setLayout(new FlowLayout());
			int w = getWidth();
			int h = getHeight();
		}
		
		@Override
		public void paintComponent(Graphics q) {
			int w = getWidth();
			int h = getHeight();
			q.setColor(new Color(224,224,224));
			q.fillRect(0, 0, w, h);
			q.setColor(new Color(0,0,102));
			q.setFont(new Font("Haveltica", Font.BOLD, 50));
			q.drawString("MOVES: " + game.moves, w/3, h-(h/4));
			q.setFont(new Font("Haveltica", Font.BOLD, 30));
			q.drawString("press Q to quit and R to restart game", w/15, h/4);
		}
		
	}
	
	public static void main(String[] args) {
		//RUN GAME HERE
		_2048 _2048 = new _2048();

		
	}
}
		




