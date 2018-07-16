package pack;

import static pack.Constants.BACKSPACE;
import static pack.Constants.BEST_SCORE_SIGN;
import static pack.Constants.BORDER_COLOR;
import static pack.Constants.BORDER_WIDTH;
import static pack.Constants.BOX_0_COLOR;
import static pack.Constants.BOX_1024_COLOR;
import static pack.Constants.BOX_128_COLOR;
import static pack.Constants.BOX_16_COLOR;
import static pack.Constants.BOX_2048_COLOR;
import static pack.Constants.BOX_256_COLOR;
import static pack.Constants.BOX_2_COLOR;
import static pack.Constants.BOX_32_COLOR;
import static pack.Constants.BOX_4_COLOR;
import static pack.Constants.BOX_512_COLOR;
import static pack.Constants.BOX_64_COLOR;
import static pack.Constants.BOX_8_COLOR;
import static pack.Constants.BOX_FONT_COLOR;
import static pack.Constants.BOX_FONT_NAME;
import static pack.Constants.BOX_FONT_SIZE;
import static pack.Constants.BOX_FONT_STYLE;
import static pack.Constants.BOX_SURPRISE;
import static pack.Constants.BOX_SURPRISE_COLOR;
import static pack.Constants.BOX_SURPTISE_SIGN;
import static pack.Constants.CHANCE_OF_2;
import static pack.Constants.CHANCE_OF_4;
import static pack.Constants.CHANCE_OF_SURPRISE;
import static pack.Constants.DOWN;
import static pack.Constants.FRAME_SIZE;
import static pack.Constants.FRAME_TITLE;
import static pack.Constants.GAME_FILE;
import static pack.Constants.LEFT;
import static pack.Constants.LOSE_MSG;
import static pack.Constants.MAX_BOX;
import static pack.Constants.MSG_FONT_NAME;
import static pack.Constants.MSG_FONT_SIZE;
import static pack.Constants.MSG_FONT_STYLE;
import static pack.Constants.MSG_LOSE_BACKGROUND_COLOR;
import static pack.Constants.MSG_LOSE_FONT_COLOR;
import static pack.Constants.MSG_SCORE_FONT_COLOR;
import static pack.Constants.MSG_SCORE_FONT_NAME;
import static pack.Constants.MSG_SCORE_FONT_SIZE;
import static pack.Constants.MSG_SCORE_FONT_STYLE;
import static pack.Constants.MSG_WIN_BACKGROUND_COLOR;
import static pack.Constants.MSG_WIN_FONT_COLOR;
import static pack.Constants.RIGHT;
import static pack.Constants.SCORE_FILE;
import static pack.Constants.SCORE_SIGN;
import static pack.Constants.UP;
import static pack.Constants.WIN_MSG;
import static pack.GameCondition.LOOSE;
import static pack.GameCondition.PLAY;
import static pack.GameCondition.WIN;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static Random random = new Random();
	private static int bestScore;
	public static File gameFile = new File(GAME_FILE);
	public static File scoreFile = new File(SCORE_FILE);

	static {
		getBestScore();
	}

	private ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
	private ArrayList<ArrayList<Integer>> lastMatrix;
	private int panelSize;
	private JFrame frame;
	private GameCondition gameCondition = PLAY;

	@SuppressWarnings("unchecked")
	public static MainPanel getMainPanel(JFrame frame) {
		try {
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(gameFile));
			Object inputMatrix = input.readObject();
			Object inputLastMatrix = input.readObject();
			Object inputGameCondition = input.readObject();
			input.close();
			if (inputMatrix instanceof ArrayList<?> && inputLastMatrix instanceof ArrayList<?>
					&& inputGameCondition instanceof String) {
				MainPanel result = new MainPanel((ArrayList<ArrayList<Integer>>) inputMatrix,
						(ArrayList<ArrayList<Integer>>) inputLastMatrix,
						GameCondition.valueOf((String) inputGameCondition));
				result.setFrame(frame);
				return result;
			} else {
				return null;
			}
		} catch (IOException | ClassNotFoundException e) {
			return null;
		}
	}

	public static int getBestScore() {
		try {
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(scoreFile));
			bestScore = input.readInt();
			input.close();
		} catch (IOException e) {
		}
		return bestScore;
	}

	public void saveGame() {
		try {
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(gameFile));
			output.writeObject(matrix);
			output.writeObject(lastMatrix);
			output.writeObject(gameCondition.toString());
			output.flush();
			output.close();
		} catch (IOException e) {
		}
	}

	public MainPanel(int panelSize, JFrame frame) {
		this.frame = frame;
		this.panelSize = panelSize;
		defaultSettings();
	}

	private MainPanel(ArrayList<ArrayList<Integer>> matrix, ArrayList<ArrayList<Integer>> lastMatrix,
			GameCondition gameCondition) {
		this.matrix = matrix;
		this.lastMatrix = lastMatrix;
		this.gameCondition = gameCondition;
		panelSize = matrix.size();
	}

	private ArrayList<ArrayList<Integer>> getEmptyMatrix() {
		ArrayList<ArrayList<Integer>> result = new ArrayList<>();
		for (int i = 0; i < panelSize; i++) {
			ArrayList<Integer> tempList = new ArrayList<>();
			for (int k = 0; k < panelSize; k++, tempList.add(0))
				;
			result.add(tempList);
		}
		return result;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	private void defaultSettings() {
		matrix = getEmptyMatrix();
		addRandomBox();
		addRandomBox();
		gameCondition = PLAY;
	}

	public void restart() {
		defaultSettings();
		repaint();
		saveGame();
	}

	private void addRandomBox() {
		int x = random.nextInt(panelSize);
		int y = random.nextInt(panelSize);
		int twoFourSurprise = random.nextInt(CHANCE_OF_2 + CHANCE_OF_4 + CHANCE_OF_SURPRISE - 1);
		if (matrix.get(x).get(y) == 0) {
			matrix.get(x).set(y, twoFourSurprise < CHANCE_OF_2 - 1 ? 2
					: twoFourSurprise < CHANCE_OF_2 + CHANCE_OF_4 ? 4 : BOX_SURPRISE);
		} else {
			addRandomBox();
		}
	}

	public void nextStap(int keyCode) {
		// return to last position if BACKSPACE
		if (keyCode == BACKSPACE) {
			lastStep();
			return;
		}

		// make a copy of MATRIX to modify it if need
		ArrayList<ArrayList<Integer>> copy = matrixCopy();
		lastMatrix = matrixCopy();

		// move boxes in direction
		moveBoxes(keyCode);

		// add random box
		if (!matrixEquals(copy)) {
			addRandomBox();
		}

		// continue playing or end the game
		continuePlaying();

		// set best score
		setBestScore();

		// save game
		saveGame();

		// repaint panel
		this.repaint();
	}

	public int getScore() {
		int result = 0;
		for (ArrayList<Integer> line : matrix) {
			for (Integer box : line) {
				result += box != BOX_SURPRISE ? box : 0;
			}
		}
		return result;
	}

	private void setBestScore() {
		int score = getScore();
		bestScore = score > bestScore ? score : bestScore;
		ObjectOutputStream output;
		try {
			output = new ObjectOutputStream(new FileOutputStream(scoreFile));
			output.writeInt(bestScore);
			output.flush();
			output.close();
		} catch (IOException e) {
		}
	}

	private void continuePlaying() {
		if (matrixContains(MAX_BOX)) {
			gameCondition = WIN;
		} else if (!hasEmpties() == true && !canMove() == true) {
			gameCondition = LOOSE;
		} else if (hasEmpties()) {
			gameCondition = PLAY;
		}
	}

	private void moveBoxes(int direction) {
		ArrayList<Integer> tempList;
		int tempListSize;
		switch (direction) {
		case LEFT:
		case RIGHT:
			for (int y = 0; y < panelSize; y++) {
				tempList = addIfCan(withOutEmpties(matrix.get(y)), direction);
				tempListSize = tempList.size();
				for (int i = 0; i < panelSize - tempListSize; i++) {
					if (direction == RIGHT) {
						tempList.add(0, 0);
					} else if (direction == LEFT) {
						tempList.add(0);
					}
				}
				matrix.set(y, tempList);
			}
			break;
		case UP:
		case DOWN:
			for (int x = 0; x < panelSize; x++) {
				tempList = new ArrayList<>();
				for (int y = 0; y < panelSize; y++) {
					tempList.add(matrix.get(y).get(x));
				}
				tempList = addIfCan(withOutEmpties(tempList), direction);
				tempListSize = tempList.size();
				for (int i = 0; i < matrix.get(x).size() - tempListSize; i++) {
					if (direction == UP) {
						tempList.add(0);
					} else if (direction == DOWN) {
						tempList.add(0, 0);
					}
				}
				for (int y = 0; y < panelSize; y++) {
					matrix.get(y).set(x, tempList.get(y));
				}
			}
		}
	}

	public void lastStep() {
		if (lastMatrix != null) {
			matrix = lastMatrix;
			gameCondition = PLAY;
			repaint();
			saveGame();
		}
	}

	private boolean matrixContains(Integer integer) {
		for (ArrayList<Integer> line : matrix) {
			for (Integer box : line) {
				if (box.equals(integer)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean canMove() {// TODO set
		int last = 0;
		for (ArrayList<Integer> y : matrix) {
			last = y.get(0);
			for (int x = 1; x < panelSize; x++) {
				if (y.get(x).equals(last)) {
					return true;
				} else {
					last = y.get(x);
				}
			}
		}
		for (int x = 0; x < panelSize; x++) {
			last = matrix.get(0).get(x);
			for (int y = 1; y < panelSize; y++) {
				if (matrix.get(y).get(x).equals(last)) {
					return true;
				} else {
					last = matrix.get(y).get(x);
				}
			}
		}
		return false;
	}

	private boolean matrixEquals(ArrayList<ArrayList<Integer>> otherMatrix) {
		for (int y = 0; y < panelSize; y++) {
			for (int x = 0; x < panelSize; x++) {
				if (!matrix.get(y).get(x).equals(otherMatrix.get(y).get(x))) {
					return false;
				}
			}
		}
		return true;
	}

	private ArrayList<ArrayList<Integer>> matrixCopy() {
		ArrayList<ArrayList<Integer>> resultMatrix = new ArrayList<>();
		for (int y = 0; y < panelSize; y++) {
			ArrayList<Integer> line = new ArrayList<>();
			for (Integer box : matrix.get(y)) {
				line.add(box);
			}
			resultMatrix.add(line);
		}
		return resultMatrix;
	}

	private static ArrayList<Integer> withOutEmpties(ArrayList<Integer> arrayList) {
		ArrayList<Integer> result = new ArrayList<>();
		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i) != 0) {
				result.add(arrayList.get(i));
			}
		}
		return result;
	}

	private static ArrayList<Integer> addIfCan(ArrayList<Integer> arrayList, int direction) {
		ArrayList<Integer> result = new ArrayList<>();
		if (arrayList.size() == 0) {
			return result;
		}
		int last;
		if (direction == RIGHT || direction == DOWN) {
			Collections.reverse(arrayList);
		}
		last = arrayList.get(0);
		for (int i = 1; i < arrayList.size(); i++) {
			if (last == 0) {
				last = arrayList.get(i);
			} else if (last == BOX_SURPRISE) {
				if (arrayList.get(i) != BOX_SURPRISE) {
					result.add(arrayList.get(i) * 2);
					last = 0;
				} else {
					result.add(last);
				}
			} else if (arrayList.get(i) == BOX_SURPRISE || arrayList.get(i) == last) {
				result.add(last * 2);
				last = 0;
			} else {
				result.add(last);
				last = arrayList.get(i);
			}
		}
		if (last != 0) {
			result.add(last);
		}
		if (direction == RIGHT || direction == DOWN) {
			Collections.reverse(result);
		}
		return result;
	}

	private boolean hasEmpties() {
		for (ArrayList<Integer> line : matrix) {
			for (Integer box : line) {
				if (box == 0) {
					return true;
				}
			}
		}
		return false;
	}

	public GameCondition getGameCondition() {
		return gameCondition;
	}

	@Override
	public void paint(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		FontMetrics metrix;
		String text;
		graphics2D.setFont(new Font(BOX_FONT_NAME, BOX_FONT_STYLE, BOX_FONT_SIZE));
		graphics2D.setStroke(new BasicStroke(BORDER_WIDTH));
		metrix = graphics.getFontMetrics();
		int boxSize = this.getParent().getWidth() / panelSize;
		int xPosition = 0;
		int yPosition = 0;
		int textX;
		int textY;

		for (ArrayList<Integer> line : matrix) {
			for (int box : line) {
				graphics2D.setColor(getColorForBox(box));
				graphics2D.fillRect(xPosition * boxSize, yPosition * boxSize, boxSize, boxSize);
				graphics2D.setColor(BORDER_COLOR);
				graphics2D.drawRect(xPosition * boxSize, yPosition * boxSize, boxSize, boxSize);
				text = box == 0 ? "" : "" + (box != BOX_SURPRISE ? box : "" + BOX_SURPTISE_SIGN);
				if (text.length() != 0) {
					graphics2D.setColor(BOX_FONT_COLOR);
					textX = (boxSize - metrix.stringWidth(text)) / 2;
					textY = (boxSize - metrix.getHeight()) / 2 + metrix.getHeight() - 10;
					graphics2D.drawString(text, xPosition * boxSize + textX, yPosition * boxSize + textY);
				}
				xPosition++;
			}
			xPosition = 0;
			yPosition++;
		}
		switch (gameCondition) {
		case PLAY:
			break;
		case LOOSE:
			graphics2D.setColor(MSG_LOSE_BACKGROUND_COLOR);
			graphics2D.fillRect(0, 0, getParent().getWidth(), getParent().getHeight());

			graphics2D.setFont(new Font(MSG_FONT_NAME, MSG_FONT_STYLE, MSG_FONT_SIZE));
			metrix = graphics2D.getFontMetrics();

			graphics2D.setColor(MSG_LOSE_FONT_COLOR);
			graphics2D.drawString(LOSE_MSG, (FRAME_SIZE - metrix.stringWidth(LOSE_MSG)) / 2,
					(FRAME_SIZE - metrix.getHeight()) / 2 + metrix.getHeight() - 10);

			graphics2D.setColor(MSG_SCORE_FONT_COLOR);
			graphics2D.setFont(new Font(MSG_SCORE_FONT_NAME, MSG_SCORE_FONT_STYLE, MSG_SCORE_FONT_SIZE));
			text = SCORE_SIGN + " : " + getScore();
			graphics2D.drawString(text, (FRAME_SIZE - metrix.stringWidth(text)) / 2,
					(frame.getWidth() - metrix.getHeight()) / 2 + 2 * metrix.getHeight() - 10);
			break;
		case WIN:

			graphics2D.setColor(MSG_WIN_BACKGROUND_COLOR);
			graphics2D.fillRect(0, 0, getParent().getWidth(), getParent().getHeight());

			graphics2D.setFont(new Font(MSG_FONT_NAME, MSG_FONT_STYLE, MSG_FONT_SIZE));
			metrix = graphics2D.getFontMetrics();

			graphics2D.setColor(MSG_WIN_FONT_COLOR);
			graphics2D.drawString(WIN_MSG, (FRAME_SIZE - metrix.stringWidth(WIN_MSG)) / 2,
					(FRAME_SIZE - metrix.getHeight()) / 2 + metrix.getHeight() - 10);

			graphics2D.setColor(MSG_SCORE_FONT_COLOR);
			graphics2D.setFont(new Font(MSG_SCORE_FONT_NAME, MSG_SCORE_FONT_STYLE, MSG_SCORE_FONT_SIZE));
			text = SCORE_SIGN + " : " + getScore();
			graphics2D.drawString(text, (FRAME_SIZE - metrix.stringWidth(text)) / 2,
					(FRAME_SIZE - metrix.getHeight()) / 2 + 2 * metrix.getHeight() - 10);
		}

		frame.setTitle(FRAME_TITLE + " ( " + SCORE_SIGN + " : " + getScore() + " / " + BEST_SCORE_SIGN + " : "
				+ bestScore + " )");
	}

	private Color getColorForBox(Integer box) {
		switch (box) {
		case 0:
			return BOX_0_COLOR;
		case 2:
			return BOX_2_COLOR;
		case 4:
			return BOX_4_COLOR;
		case 8:
			return BOX_8_COLOR;
		case 16:
			return BOX_16_COLOR;
		case 32:
			return BOX_32_COLOR;
		case 64:
			return BOX_64_COLOR;
		case 128:
			return BOX_128_COLOR;
		case 256:
			return BOX_256_COLOR;
		case 512:
			return BOX_512_COLOR;
		case 1024:
			return BOX_1024_COLOR;
		case 2048:
			return BOX_2048_COLOR;
		case BOX_SURPRISE:
			return BOX_SURPRISE_COLOR;
		}
		return BOX_2048_COLOR; // TODO for big numbers
	}

}