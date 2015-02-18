package UI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import main.Game;

@SuppressWarnings("serial")
public class Chat extends JFrame {
	private JTextField input;
	JTextArea log;
	private boolean visible = false;
	private Game game;

	public Chat(Game game) {
		this.game = game;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setAlwaysOnTop(true);
		setLocation(500, 100);
		JScrollPane scrollPane = new JScrollPane();

		log = new JTextArea();
		log.setEditable(false);
		scrollPane.setViewportView(log);

		input = new JTextField();
		input.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(
																input,
																Alignment.LEADING)
														.addComponent(
																scrollPane,
																Alignment.LEADING,
																GroupLayout.DEFAULT_SIZE,
																516,
																Short.MAX_VALUE))
										.addGap(0)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addGroup(
				Alignment.TRAILING,
				groupLayout
						.createSequentialGroup()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE,
								146, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(input, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)));
		getContentPane().setLayout(groupLayout);
		pack();
		setVisible(visible);

		input.addKeyListener(new keys());
	}

	public void ap(String txt) {
		log.append(txt + "\n");
		log.setCaretPosition(log.getDocument().getLength());
	}

	public String getChatText() {
		String s = input.getText();
		input.setText("");
		return s;
	}

	public void toggleVisible() {
		if (visible) {
			requestFocus();
			setVisible(false);
			visible = false;
		} else {
			requestFocus();
			input.requestFocus();
			setVisible(true);
			visible = true;
		}
	}

	public void Destroy() {
		setVisible(false); // you can't see me!
		dispose(); // Destroy the JFrame object
	}

	class keys extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_ENTER) {
				if (input.getText().length() < 1000) {
					if (!input.getText().equals("")) {
						try {
							game.giveText(input.getText(), false);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						input.setText("");
					} else {
						try {
							game.giveText(input.getText(), true);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}
}
