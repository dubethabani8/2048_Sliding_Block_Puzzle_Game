/*
 * This is the class of the actual game and its related methods of play, etc
 * This class is run as an object in the _2048 class
 * */

import java.util.Arrays;
import java.util.Random;

public class Game {
	
	int[][] array; //grid
	int moves; //moves counter
	boolean gameOver; //check for loosing condition
	boolean gameOn; //check for paused condition (waiting for player confirmation for restart or quit)
	boolean win;
	
	public Game() { //new game starts with array with all zeros and generates two random numbers on it
		moves = 0;
		//initialize array
		array = new int[4][4];
		for(int i=0; i<array.length; i++) {
			for(int j = 0; j< array[i].length; j++ ) {
				array[i][j] = 0;
			}
		}
		//randomly fill with two numbers for new game
		randFill();
		randFill();
		//set conditions for new game in play:
		gameOn = true;
		gameOver = false;
		win = false;
	}
	
	
	
	//METHOD THAT PERFOMS ALL MOVE OPERATIONS AS PER DIRECTION
	public void move(char direction) {
		if(win) { //Check for winning condition
			return;
		}
		//Store array in array temp before modifications
		int[][] temp = new int[4][4];
		for(int i=0; i<array.length; i++) {
			temp[i] = Arrays.copyOf(array[i], 4);
		}
		
		//Flip the array accordingly then perform move and flip back
		flipArray(direction);
		
		//Move all the numbers to one side -->
		moveNumbers();
		
		//Add same non zero numbers in each row -->
		for(int i=0; i < array.length; i++) {
			for(int j=array.length - 2 ; j >= 0; j--) {
				int num = array[i][j];
				int prev = array[i][j+1];
				if(num!=0 && num == prev) {
					num = prev+num;
					prev = 0;
					array[i][j] = num;
					if(num == 2048) win = true;
					array[i][j+1] = prev;
					j--;
				}
			}
		}
		//Move all the numbers to one side again -->
		moveNumbers();
		
		//Flip array back
		flipBackArray(direction);
		
		
		//check if there was any changes (if valid move was made) and update #moves and update num accordingly
		boolean equal = false; //for checking if temp and array are still equal(if equal, then invalid move was made)
		
		for(int i=0; i<temp.length; i++) {
			if(Arrays.equals(temp[i], array[i]))
				equal = true;
			else {
				equal = false;
				break;
			}
		}
		
		 // if there is still open space, keep gameOver as false
		gameOver = true;
		for(int i = 0; i<array.length; i++) {
			for(int j=0; j<array[i].length; j++) {
				if(array[i][j] == 0) {
					gameOver = false;
				}
			}
		}
		// if there is no space and no more possible move at the end set gameOver to true
		if(gameOver) { 
			for(int i = 0; i < array.length; i++) {
				for(int j=0; j<array[i].length; j++) {
					int u=0, d=0, l=0, r=0;
					int num = array[i][j];
					if(i!=0) u = array[i-1][j];
					if(i!=3) d = array[i+1][j];
					if(j!=0) l = array[i][j-1];
					if(j!=3) r = array[i][j+1];
					if(num == u || num == d || num == l || num == r) gameOver = false;
				}
			}
		}
		if(gameOver) gameOn = false;
		
		//If valid move was made, then update moves counter and add another 2 or 4
		if(!equal) {
			System.out.println("Valid move");
			randFill();
			moves++;
		}
		else System.out.println("Invalid move");
		System.out.println("Moves: " + moves);
		System.out.println();
	}
	
	private void randFill(){ //randomly fill with 2 or 4
		int[] pos = randPos(); //get random position [i][j] that is not filled as an array {i,j}
		int i = pos[0]; int j = pos[1];
		int n = randNum(); //generate random number by appropriate probability
		array[i][j] = n;
		
	}
	
	private int randNum(){ //return a 2 or 4 with 20% and 80% probability respectively
		Random rn = new Random();
		double probability = rn.nextDouble();
		if(probability <= 0.2) return 4;
		else return 2;
	}
	
	private int[] randPos() { //return random position [i][j] that is not filled as an array {i,j}
		int i = 0,j = 0;
		int pos[] = {0,0};
		Random rn = new Random();
		do {
			i = rn.nextInt((3 - 0) + 1) + 0;
			j = rn.nextInt((3 - 0) + 1) + 0;
		} while (array[i][j] != 0);
		pos[0] = i;
		pos[1] = j;
		return pos;
	}
	
	
	
	//Method for moving all the numbers to one side -->
	private void moveNumbers() {
		
		for(int i = 0; i < array.length; i++) {
			boolean posSet = false;
			int x = 0,y = 0;
			for(int j=array.length-1; j >= 0; j--) {
				int num = array[i][j];
				if(num == 0 && !posSet) {
					x = i; y = j;
					posSet = true;
				}
				if(num != 0 && j!=array.length-1 && array[i][j+1] == 0) {
					array[x][y] = num;
					array[i][j] = 0;
					posSet = false;
					j++;j++;
				}
			}
		}
	}
	
	//Method for flipping the grid in the appropriate direction
	private void flipArray(char direction) {
		int[][] temp = new int[4][4];
		if(direction != 'r' ) {
			for(int i = 0; i<array.length; i++) {
				for(int j = 0; j<array.length; j++) {
					switch(direction) {
					case 'l':
						temp[(array.length-1) - i][(array.length-1) - j] = array[i][j];
						break;
					case 'u':
						temp[j][(array.length-1) - i] = array[i][j];
						break;
					case 'd':
						temp[(array.length-1) - j][i] = array[i][j];
						break;
					}
				}
			}
			array = temp;
		}
	}
	
	//Method for reversing the flip of the grid
	private void flipBackArray(char direction){
		int[][] temp = new int[4][4];
		if(direction != 'r' ) {
			for(int i = 0; i<array.length; i++) {
				for(int j = 0; j<array.length; j++) {
					switch(direction) {
					case 'l':
						temp[(array.length-1) - i][(array.length-1) - j] = array[i][j];
						break;
					case 'u':
						temp[(array.length-1) - j][i] = array[i][j];
						break;
					case 'd':
						temp[j][(array.length-1) - i] = array[i][j];
						break;
					}
				}
			}
			array = temp;
		}
	}
	

}
