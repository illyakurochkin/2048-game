package pack;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static pack.Constants.BACKSPACE;
import static pack.Constants.DOWN;
import static pack.Constants.EXTRA_HEIGHT;
import static pack.Constants.FRAME_SIZE;
import static pack.Constants.FRAME_TITLE;
import static pack.Constants.FRAME_X_POSITION;
import static pack.Constants.FRAME_Y_POSITION;
import static pack.Constants.LEFT;
import static pack.Constants.NUMBER_OF_BOXES;
import static pack.Constants.RIGHT;
import static pack.Constants.UP;
import static pack.GameCondition.PLAY;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class Window {

	private JFrame frame;
	private MainPanel panel;

	public Window() {
		frame = new JFrame(FRAME_TITLE);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setLocation(FRAME_X_POSITION, FRAME_Y_POSITION);
		Dimension dimension = new Dimension(FRAME_SIZE, FRAME_SIZE + EXTRA_HEIGHT);
		frame.setPreferredSize(dimension);
		frame.setSize(dimension);
		frame.setResizable(false);
		panel = MainPanel.getMainPanel(frame);
		panel = panel == null ? new MainPanel(NUMBER_OF_BOXES, frame) : panel;
		panel.setSize(dimension);
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				int keyCode = keyEvent.getKeyCode();
				if (keyEvent.isControlDown() && keyCode == BACKSPACE) {
					panel.restart();
				} else if (panel.getGameCondition() == PLAY
						&& (keyCode == LEFT || keyCode == RIGHT || keyCode == UP || keyCode == DOWN)
						|| keyCode == BACKSPACE) {
					panel.nextStap(keyEvent.getKeyCode());

				}
			}
		});
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (panel.getGameCondition() != PLAY) {
					panel.restart();
				}
			}

		});
		frame.getContentPane().add(CENTER, panel);
		frame.setVisible(true);
	}
}
