package pack;

import java.awt.Color;
import java.awt.Font;

public class Constants {

	/////////////////////
	// Frame constants //
	/////////////////////

	public static final String FRAME_TITLE = "Game 2048";
	public static final int FRAME_X_POSITION = 200;
	public static final int FRAME_Y_POSITION = 200;
	public static final int FRAME_SIZE = 600;
	public static final int EXTRA_HEIGHT = 22;

	/////////////////////
	// Style constants //
	/////////////////////

	// box font preferences
	public static final Color BOX_FONT_COLOR = new Color(238, 238, 238);
	public static final String BOX_FONT_NAME = "SansSerif";
	public static final int BOX_FONT_STYLE = Font.BOLD;
	public static final int BOX_FONT_SIZE = 50;

	// message colors
	public static final Color MSG_LOSE_BACKGROUND_COLOR = new Color(231, 76, 60, 150);
	public static final Color MSG_WIN_BACKGROUND_COLOR = new Color(46, 204, 113, 180);

	// message font preferences
	public static final Color MSG_LOSE_FONT_COLOR = new Color(0, 0, 0);
	public static final Color MSG_WIN_FONT_COLOR = new Color(0, 0, 0);
	public static final int MSG_FONT_SIZE = 60;
	public static final int MSG_FONT_STYLE = Font.BOLD;
	public static final String MSG_FONT_NAME = "SansSerif";

	// message score font preferences
	public static final Color MSG_SCORE_FONT_COLOR = new Color(0, 0, 0);
	public static final String MSG_SCORE_FONT_NAME = "SansSerif";
	public static final int MSG_SCORE_FONT_STYLE = Font.BOLD;
	public static final int MSG_SCORE_FONT_SIZE = 30;

	// box colors
	public static final Color BORDER_COLOR = new Color(238, 238, 238);
	// new Color(108, 122, 137);
	public static final Color BOX_0_COLOR = new Color(218, 223, 225);
	public static final Color BOX_2_COLOR = new Color(134, 226, 213);
	public static final Color BOX_4_COLOR = new Color(122, 207, 225);
	public static final Color BOX_8_COLOR = new Color(111, 168, 224);
	public static final Color BOX_16_COLOR = new Color(100, 123, 223);
	public static final Color BOX_32_COLOR = new Color(105, 88, 222);
	public static final Color BOX_64_COLOR = new Color(141, 77, 221);
	public static final Color BOX_128_COLOR = new Color(184, 66, 220);
	public static final Color BOX_256_COLOR = new Color(219, 56, 206);
	public static final Color BOX_512_COLOR = new Color(218, 45, 149);
	public static final Color BOX_1024_COLOR = new Color(217, 34, 87);
	public static final Color BOX_2048_COLOR = new Color(217, 29, 23);
	public static final Color BOX_SURPRISE_COLOR = new Color(241, 196, 15);

	public static final int BORDER_WIDTH = 7;

	////////////////////
	// Game constants //
	////////////////////
	public static final int CHANCE_OF_2 = 8;
	public static final int CHANCE_OF_4 = 5;
	public static final int CHANCE_OF_SURPRISE = 4;
	public static final int MAX_BOX = 2048;
	public static final int NUMBER_OF_BOXES = 5;
	public static final String LOSE_MSG = "GAME OVER";
	public static final String WIN_MSG = "YOU WIN !";
	public static final String SCORE_SIGN = "score";
	public static final String BEST_SCORE_SIGN = "best score";

	public static final int BOX_SURPRISE = -1;
	public static final char BOX_SURPTISE_SIGN = '?';

	public static final String GAME_FILE = "game";
	public static final String SCORE_FILE = "score";

	///////////////////
	// Key constants //
	///////////////////

	public static final int LEFT = 37;
	public static final int UP = 38;
	public static final int RIGHT = 39;
	public static final int DOWN = 40;
	public static final int BACKSPACE = 8;
}
