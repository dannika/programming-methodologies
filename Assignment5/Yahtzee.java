/*
 * File: Yahtzee.java
 * ------------------
 * Name: Dannika Thompson
 * Class: CS106A
 * Section Leader: Jacob Hoffman
 * 
 * This program plays the Yahtzee game. There are five dice and one to four players. A round
 * of the game consists of each player taking a turn. On each turn, a player rolls the five 
 * dice with the hope of getting them into a configuration that corresponds to one of 13 
 * categories (see the following section). If the first roll doesn’t get there, the player 
 * may choose to roll any or all of the dice again. If the second roll is still unsuccessful,
 * the player may roll any or all of the dice once more. By the end of the third roll, 
 * however, the player must assign the final dice configuration to one of the thirteen 
 * categories on the scorecard. If the dice configuration meets the criteria for that 
 * category, the player receives the appropriate score for that category; otherwise the 
 * score for that category is 0. Since there are thirteen categories and each category is 
 * used exactly once, a game consists of thirteen rounds. After the thirteenth round, all 
 * players will have received scores for all categories. The player with the total highest 
 * score is declared the winner.
 * 
 * The categories of dice configurations are scored as followed:
 * 
 * (1 - 6) Ones, Twos, Threes, Fours, Fives, Sixes. Any dice configuration is valid for these
 * categories. The score is equal to the sum of all of the respective numbers showing on the
 * dice, which is 0 if there are none showing.
 * 
 * (7) Three of a Kind. At least three of the dice must show the same value. The score is 
 * equal to the sum of all of the values showing on the dice.
 * 
 * (8) Four of a Kind. At least four of the dice must show the same value. The score is equal
 * to the sum of all of the values showing on the dice.
 * 
 * (9) Full House. The dice must show three of one value and two of another. The score is
 * 25 points.
 * 
 * (10) Small Straight. The dice must contain at least four consecutive values. The score is
 * 30 points.
 * 
 * (11) Large Straight. The dice must contain five consecutive values. The score is 40 points.
 * 
 * (12) Yahtzee! All of the dice must show the same value. The score is 50 points.
 * 
 * (13) Chance. Any dice configuration is valid for this category. The score is equal to 
 * the sum of all of the values showing on the dice.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;
import java.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	/* Method: main() */
	/* Initializes the Yahtzee program. */
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	
	/* Method: run() */
	/* 
	 * Runs the Yahtzee game. Prompts the user for number of players (at least 1, but max of
	 * MAX_PLAYERS) and each player's name, storing names in an array. The canvas is 
	 * initialized, displaying the scoreboard graphics implemented in YahtzeeDisplay(). The 
	 * method playGame() is called to begin gameplay.
	 */
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		while (nPlayers > MAX_PLAYERS || nPlayers < 1) {
			if (nPlayers > MAX_PLAYERS) {
				nPlayers = dialog.readInt("Too many players! Enter number of players");
			} else {
				nPlayers = dialog.readInt("Too few players! Enter number of players");
			}
		}
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	
	/* Method: playGame() */
	/*
	 * Begins Yahtzee gameplay. Initializes a 2D Array scorecard of dimensions N_CATEGORIES
	 * x nPlayers that will eventually tally each player's score per category. Array 
	 * rollDice of length N_DICE will eventually hold dice values for each roll. Array
	 * selectedCategories keeps track of which categories have already been selected by
	 * the player.
	 * 
	 * For each round (up to N_CATEGORIES), each player is alloted three dice rolls to 
	 * obtain an desired combination, and then each player must select an appropriate
	 * category for their turn.
	 * 
	 * Once the players have exhausted all of their rounds (implying that the scorecard is 
	 * full), the final scores are computed (including whatever bonuses may apply), and
	 * the winning player is determined.
	 */
	private void playGame() {
		scorecard = new int[N_CATEGORIES][nPlayers];
		rollDice = new int[N_DICE];
		int[][] selectedCategories = new int[N_CATEGORIES][nPlayers];
		for (int rounds = 0; rounds < N_SCORING_CATEGORIES; rounds++) {
			for (int player = 1; player <= nPlayers; player++) {
				firstRoll(player);
				secondAndThirdRolls();
				selectCategory(player, selectedCategories);
			}
		}
		computeFinalScore();
		declareWinner();
	}
	
	
	/* Method: firstRoll(int player) */
	/*
	 * Executes each player's first roll per turn. Takes an integer parameter player, which
	 * allows for the player's name to be printed. The player is prompted to click "Roll 
	 * Dice". Once the button is clicked, N_DICE dice are "rolled", generating a random dice
	 * value between [1, 6] (six-sided dice) for each die. The dice values are displayed in
	 * the graphics window.
	 */
	private void firstRoll(int player) {
		display.printMessage(playerNames[player - 1] + "'s turn! Click \"Roll Dice\" button to roll the dice.");
		display.waitForPlayerToClickRoll(player);
		for (int dice = 0; dice < N_DICE; dice++) {
			rollDice[dice] = rgen.nextInt(1, 6);
		}
		display.displayDice(rollDice);
	}
	
	
	/* Method: secondAndThirdRolls() */
	/*
	 * Executes each player's second and third rolls per turn. For each additional roll,
	 * the player is prompted to click the dice they wish to re-roll and then click "Roll
	 * Again". The user must click "Roll Again" regardless of whether any dice have been
	 * selected to re-roll.
	 * 
	 * Once "Roll Again" has been clicked, any dice that have been selected are re-assigned
	 * a randomly generated value between [1,6], and the dice values are printed to the
	 * graphics window.
	 */
	private void secondAndThirdRolls() {
		for (int roll = 0; roll < 2; roll++) {
			display.printMessage("Select the dice you wish to re-roll and Click \"Roll Again\".");
			display.waitForPlayerToSelectDice();
			for (int dice = 0; dice < N_DICE; dice++) {
				if (display.isDieSelected(dice)) {
					rollDice[dice] = rgen.nextInt(1, 6);
				}
			}
			display.displayDice(rollDice);
		}
	}
	
	
	/* Method: selectCategory() */
	/*
	 * Prompts the player to select a category for their roll. The program enters a while
	 * loop that cannot be broken from until the player selects a category that they have
	 * not already chosen in a previous turn. Once the player selects a valid category,
	 * calculateCategoryScore() is called to calculate the player's score for that category,
	 * and that category cannot be chosen again.
	 */
	private void selectCategory(int player, int[][] selectedCategories) {
		while (true) {
			display.printMessage("Select a category for this roll.");
			int category = display.waitForPlayerToSelectCategory();
			if (selectedCategories[category - 1][player - 1] == 0) {
				selectedCategories[category - 1][player - 1] = 1;
				calculateCategoryScore(player, category);
				break;
			}
			display.printMessage("Please select a valid category.");
		}
	}
	
	
	/* Method: calculateCategoryScore(int player, int category) */
	/*
	 * Calculates the player's score for their selected category. Accepts integers player
	 * and category as parameters. To receive points in an upper level category (ONES to
	 * SIXES), the player's roll must include at least one die of the respective category.
	 * To receive points in a lower level category, checkCategory(category) is called to 
	 * ensure that the player's assortment of dice is a qualifying instance of that category,
	 * otherwise the player receives no points for that category.
	 * 
	 * Once the player's category score has been calculated, that score is added to both the
	 * graphics window and the scorecard, and the player's total is updated on the graphics
	 * window and the scorecard,
	 * 
	 * See Yahtzee file description above for dice category point values.
	 */
	private void calculateCategoryScore(int player, int category) {
		int score = 0;
		int totalScore;
		if (category >= ONES && category <= SIXES) {
			for (int die = 0; die < N_DICE; die++) {
				if (rollDice[die] == category) {
					score += category;
				}
			}
		} else if (category == THREE_OF_A_KIND || category == FOUR_OF_A_KIND) {
			if (checkCategory(category)) {
				for (int dice = 0; dice < N_DICE; dice++) {
					score += rollDice[dice];
				}
			} else {
				score = 0;
			}
		} else if (category == FULL_HOUSE) {
			if (checkCategory(category)) {
				score = FULL_HOUSE_SCORE;
			} else {
				score = 0;
			}
		} else if (category == SMALL_STRAIGHT) {
			if (checkCategory(category)) {
				score = SMALL_STRAIGHT_SCORE;
			} else {
				score = 0;
			}
		} else if (category == LARGE_STRAIGHT) {
			if (checkCategory(category)) {
				score = LARGE_STRAIGHT_SCORE;
			} else {
				score = 0;
			}
		} else if (category == YAHTZEE) {
			if (checkCategory(category)) {
				score = YAHTZEE_SCORE;
			} else {
				score = 0;
			}
		} else if (category == CHANCE) {
			for (int dice = 0; dice < N_DICE; dice++) {
				score += rollDice[dice];
			}
		}
		scorecard[category - 1][player - 1] = score;
		scorecard[TOTAL - 1][player - 1] += score;
		totalScore = scorecard[TOTAL - 1][player - 1];
		display.updateScorecard(category, player, score);
		display.updateScorecard(TOTAL, player, totalScore);
	}
	
	
	/* Method: checkCategory(int category) */
	/*
	 * Determines if the player's roll is a qualifying instance of their selected category.
	 * Takes a integer parameter category, which represents the player's selected category.
	 * Six ArrayLists (ones - sixes) are created to store of each the occurrences of each
	 * die value in the player's roll. The player's roll is iterated through, and each
	 * occurrence of a die value is added to the corresponding ArrayList.
	 * 
	 * If the player's roll is an instance of their selected category, the method returns
	 * true, and calculateCategoryScore() will calculate the points to be awarded. Otherwise,
	 * the method will return false, and calculateCategoryScore() will award 0 points to 
	 * the player.
	 * 
	 * See Yahtzee file description above for detailed rules for categories.
	 */
	private boolean checkCategory(int category) {
		ArrayList<Integer> ones = new ArrayList<Integer>();
		ArrayList<Integer> twos = new ArrayList<Integer>();
		ArrayList<Integer> threes = new ArrayList<Integer>();
		ArrayList<Integer> fours = new ArrayList<Integer>();
		ArrayList<Integer> fives = new ArrayList<Integer>();
		ArrayList<Integer> sixes = new ArrayList<Integer>();
		
		for (int dice = 0; dice < N_DICE; dice++) {
			if (rollDice[dice] == 1) {
				ones.add(1);
			} else if (rollDice[dice] == 2) {
				twos.add(2);
			} else if (rollDice[dice] == 3) {
				threes.add(3);
			} else if (rollDice[dice] == 4) {
				fours.add(4);
			} else if (rollDice[dice] == 5) {
				fives.add(5);
			} else if (rollDice[dice] == 6) {
				sixes.add(6);
			}
		}
		if (category == THREE_OF_A_KIND) {
			if (ones.size() >= 3 || twos.size() >= 3 || threes.size() >= 3 ||
					fours.size() >= 3 || fives.size() >= 3 || sixes.size() >= 3) {
				return true;
			}
		} else if (category == FOUR_OF_A_KIND) {
			if (ones.size() >= 4 || twos.size() >= 4 || threes.size() >= 4 ||
					fours.size() >= 4 || fives.size() >= 4 || sixes.size() >= 4) {
				return true;
			}
		} else if (category == FULL_HOUSE) {
			if (ones.size() >= 3 || twos.size() >= 3 || threes.size() >= 3 ||
					fours.size() >= 3 || fives.size() >= 3 || sixes.size() >= 3) {
				if (ones.size() >= 2 || twos.size() >= 2 || threes.size() >= 2 ||
						fours.size() >= 2 || fives.size() >= 2 || sixes.size() >= 2) {
					return true;
				}
			}
		} else if (category == SMALL_STRAIGHT) {
			if (ones.size() >= 1 && twos.size() >= 1 && threes.size() >= 1 && fours.size() >= 1) {
				return true;
			} else if (twos.size() >= 1 && threes.size() >= 1 && fours.size() >= 1 && fives.size() >= 1) {
				return true;
			} else if (threes.size() >= 1 && fours.size() >= 1 && fives.size() >= 1 && sixes.size() >= 1) {
				return true;
			}
		} else if (category == LARGE_STRAIGHT) {
			if (ones.size() == 1 && twos.size() == 1 && threes.size() == 1 && 
					fours.size() == 1 && fives.size() == 1) {
				return true;
			} else if (twos.size() == 1 && threes.size() == 1 && 
					fours.size() == 1 && fives.size() == 1 && sixes.size() == 1) {
				return true;
			}
		} else if (category == YAHTZEE) {
			if (ones.size() == 5 || twos.size() == 5 || threes.size() == 5 || 
					fours.size() == 5 || fives.size() == 5 || sixes.size() == 5) {
				return true;
			}
		} else if (category == CHANCE) {
			return true;
		}
		return false;
	}
	
	
	/* Method: computeFinalScore() */
	/*
	 * Computes each player's final score. Iterates through each player's column in the
	 * scorecard array. Each score in the upper and lower categories are added to upperScore
	 * and lowerScore, respectively. If a player's upperScore is greater than 63, that
	 * player receives an upperBonus score of 35 points (which is otherwise 0). The player's
	 * upperScore, upperBonus, and lowerScore are combined to for the player's final 
	 * totalScore.
	 * 
	 * All scoring values are added to the scorecard array and are printed to the graphics
	 * window.
	 */
	private void computeFinalScore() {
		for (int player = 0; player < nPlayers; player++) {
			int upperScore = 0;
			int upperBonus = 0;
			int lowerScore = 0;
			int totalScore = 0;
			for (int category = 0; category < SIXES; category++) {
				upperScore += scorecard[category][player];
			}
			if (upperScore >= 63) {
				upperBonus = UPPER_BONUS_SCORE;
			}
			for (int category = 8; category < CHANCE; category++) {
				lowerScore += scorecard[category][player];
			}
			totalScore = upperScore + upperBonus + lowerScore;
			
			scorecard[UPPER_SCORE - 1][player] = upperScore;
			scorecard[UPPER_BONUS - 1][player] = upperBonus;
			scorecard[LOWER_SCORE - 1][player] = lowerScore;
			scorecard[TOTAL - 1][player] = totalScore;
			
			display.updateScorecard(UPPER_SCORE, player + 1, upperScore);
			display.updateScorecard(UPPER_BONUS, player + 1, upperBonus);
			display.updateScorecard(LOWER_SCORE, player + 1, lowerScore);
			display.updateScorecard(TOTAL, player + 1, totalScore);
		}
	}
	
	
	/* Method: declareWinner() */
	/*
	 * Determines the winner of the game. Loops through the total scores of each player.
	 * The player with the highest score is declared the winner, and a winning message is
	 * printed to the graphics window.
	 */
	private void declareWinner() {
		int winningPlayer = 0;
		int score = 0;
		for (int player = 0; player < nPlayers; player++) {
			if (scorecard[TOTAL - 1][player] > score) {
				score = scorecard[TOTAL - 1][player];
				winningPlayer = player;
			}
		}
		display.printMessage("Congratulations, " + playerNames[winningPlayer] + ", " + 
				"you're the winner with a final score of " + score + "!");
	}
		
	
	/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	
	/* 2D Array that will store each player's scores per category */
	private int[][] scorecard;
	
	/* Will hold values of each die for every roll */
	private int[] rollDice;

}
