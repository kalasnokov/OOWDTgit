import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import java.awt.Font;

public class Tester extends JFrame {
	private JTextField txtX;
	private JTextField txtY;
	private JTextPane xpos;
	private JTextPane ypos;
	JRadioButton Jumping;
	JRadioButton Left;
	JRadioButton Right;

	public Tester(String name) {

		super("OOWDT Client manager");
		setAlwaysOnTop(true);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception error) {
		}
		setLocation(800, 100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		ypos = new JTextPane();
		ypos.setEditable(false);

		xpos = new JTextPane();
		xpos.setEditable(false);

		JRadioButton Moving = new JRadioButton("moving");
		Moving.setEnabled(false);

		Jumping = new JRadioButton("jumping");
		Jumping.setEnabled(false);

		Right = new JRadioButton("right");
		Right.setEnabled(false);

		Left = new JRadioButton("left");
		Left.setEnabled(false);

		txtX = new JTextField();
		txtX.setEditable(false);
		txtX.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtX.setText("X");
		txtX.setColumns(10);

		txtY = new JTextField();
		txtY.setText("Y");
		txtY.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtY.setEditable(false);
		txtY.setColumns(10);

		JTextPane namepane = new JTextPane();
		namepane.setEditable(false);
		namepane.setText(name);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																xpos,
																GroupLayout.PREFERRED_SIZE,
																87,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(Jumping)
														.addComponent(Left))
										.addPreferredGap(
												ComponentPlacement.RELATED,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(Moving)
														.addComponent(Right)
														.addComponent(
																ypos,
																GroupLayout.PREFERRED_SIZE,
																87,
																GroupLayout.PREFERRED_SIZE))
										.addContainerGap(9, Short.MAX_VALUE))
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(42)
										.addComponent(txtX,
												GroupLayout.PREFERRED_SIZE, 17,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.RELATED, 78,
												Short.MAX_VALUE)
										.addComponent(txtY,
												GroupLayout.PREFERRED_SIZE, 17,
												GroupLayout.PREFERRED_SIZE)
										.addGap(45))
						.addGroup(
								Alignment.TRAILING,
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(namepane,
												GroupLayout.DEFAULT_SIZE, 179,
												Short.MAX_VALUE)
										.addContainerGap()));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(10)
										.addComponent(namepane,
												GroupLayout.PREFERRED_SIZE, 20,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(
																txtX,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																txtY,
																GroupLayout.PREFERRED_SIZE,
																25,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(
																ypos,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																xpos,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(Right)
														.addComponent(Left))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(Jumping)
														.addComponent(Moving))
										.addContainerGap(46, Short.MAX_VALUE)));
		getContentPane().setLayout(groupLayout);
		
		
		pack();
		setVisible(true);
	}

	public void setx(int x) {
		xpos.setText(Integer.toString(x));
	}

	public void sety(int y) {
		ypos.setText(Integer.toString(y));
	}

	public void jump(boolean b) {
		Jumping.setSelected(b);
	}
	public void ml(boolean b) {
		Left.setSelected(b);
	}
	public void mr(boolean b) {
		Right.setSelected(b);
	}
}
