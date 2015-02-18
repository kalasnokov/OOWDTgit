package UI;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import main.Game;

@SuppressWarnings("serial")
public class Connector extends JFrame {
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private final long serialVersionUID = 2643206995134951451L;
	private JTextField ip;
	private JTextField name;
	private String[] races = { "Dog", "Human", "Dragonkin", "Construct" };
	public DefaultListModel<String> lm = new DefaultListModel<String>();
	private JList<String> list;
	private JRadioButton radioButton, radioButton_1, radioButton_2;
	int var;
	int race;
	String Sname;
	String Sip;
	boolean b;

	public Connector(Game game) throws InterruptedException {
		for (int i = 0; i < races.length; i++) {
			lm.addElement(races[i]);
		}
		setAutoRequestFocus(false);

		ip = new JTextField();
		ip.setColumns(10);

		name = new JTextField();
		name.setColumns(10);

		list = new JList<String>(lm);

		JTextPane txtpnIp = new JTextPane();
		txtpnIp.setEditable(false);
		txtpnIp.setBackground(SystemColor.menu);
		txtpnIp.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtpnIp.setText("IP");

		JTextPane txtpnName = new JTextPane();
		txtpnName.setEditable(false);
		txtpnName.setText("Name");
		txtpnName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtpnName.setBackground(SystemColor.menu);

		JTextPane txtpnRace = new JTextPane();
		txtpnRace.setEditable(false);
		txtpnRace.setText("Race");
		txtpnRace.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtpnRace.setBackground(SystemColor.menu);

		JTextPane txtpnVariation = new JTextPane();
		txtpnVariation.setEditable(false);
		txtpnVariation.setText("Variation");
		txtpnVariation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtpnVariation.setBackground(SystemColor.menu);

		JTextPane txtpnA = new JTextPane();
		txtpnA.setEditable(false);
		txtpnA.setText("A");
		txtpnA.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtpnA.setBackground(SystemColor.menu);

		JTextPane txtpnB = new JTextPane();
		txtpnB.setEditable(false);
		txtpnB.setText("B");
		txtpnB.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtpnB.setBackground(SystemColor.menu);

		JTextPane txtpnC = new JTextPane();
		txtpnC.setEditable(false);
		txtpnC.setText("C");
		txtpnC.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtpnC.setBackground(SystemColor.menu);

		radioButton = new JRadioButton("");
		radioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				var = 1;
				radioButton_1.setSelected(false);
				radioButton_2.setSelected(false);

			}
		});

		radioButton_1 = new JRadioButton("");
		radioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				var = 2;
				radioButton.setSelected(false);
				radioButton_2.setSelected(false);

			}
		});

		radioButton_2 = new JRadioButton("");
		radioButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				var = 3;
				radioButton_1.setSelected(false);
				radioButton.setSelected(false);

			}
		});
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			private JButton button;

			public void actionPerformed(ActionEvent e) {
				button = (JButton) e.getSource();
				if (!name.equals("")) {
					b = true;
				} else {
					JOptionPane.showMessageDialog(null, "Invalid unput");
				}
			}
		});
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(ip,
												GroupLayout.DEFAULT_SIZE, 213,
												Short.MAX_VALUE).addGap(11))
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(104)
										.addComponent(txtpnIp,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addContainerGap(109, Short.MAX_VALUE))
						.addGroup(
								groupLayout.createSequentialGroup().addGap(89)
										.addComponent(txtpnName).addGap(98))
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.TRAILING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(10)
																		.addComponent(
																				txtpnVariation,
																				GroupLayout.DEFAULT_SIZE,
																				69,
																				Short.MAX_VALUE)
																		.addGap(59)
																		.addComponent(
																				txtpnRace,
																				GroupLayout.DEFAULT_SIZE,
																				47,
																				Short.MAX_VALUE)
																		.addGap(39))
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addComponent(
																				name,
																				GroupLayout.DEFAULT_SIZE,
																				213,
																				Short.MAX_VALUE)
																		.addGap(11))))
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(60)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(10)
																		.addComponent(
																				btnDone,
																				GroupLayout.DEFAULT_SIZE,
																				84,
																				Short.MAX_VALUE)
																		.addGap(70))
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.TRAILING)
																						.addComponent(
																								txtpnB,
																								GroupLayout.PREFERRED_SIZE,
																								17,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								txtpnA,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								txtpnC,
																								GroupLayout.PREFERRED_SIZE,
																								17,
																								GroupLayout.PREFERRED_SIZE))
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.TRAILING)
																						.addComponent(
																								radioButton_1,
																								GroupLayout.PREFERRED_SIZE,
																								32,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								radioButton,
																								GroupLayout.PREFERRED_SIZE,
																								32,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								radioButton_2,
																								GroupLayout.PREFERRED_SIZE,
																								32,
																								GroupLayout.PREFERRED_SIZE))
																		.addPreferredGap(
																				ComponentPlacement.UNRELATED)
																		.addComponent(
																				list,
																				GroupLayout.DEFAULT_SIZE,
																				107,
																				Short.MAX_VALUE)
																		.addGap(10)))
										.addContainerGap()));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(8)
										.addComponent(txtpnIp,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addComponent(ip,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addComponent(txtpnName,
												GroupLayout.PREFERRED_SIZE, 26,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addComponent(name,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																txtpnRace,
																GroupLayout.PREFERRED_SIZE,
																26,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																txtpnVariation,
																GroupLayout.PREFERRED_SIZE,
																26,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(
																list,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								txtpnA,
																								GroupLayout.DEFAULT_SIZE,
																								26,
																								Short.MAX_VALUE)
																						.addComponent(
																								radioButton,
																								GroupLayout.DEFAULT_SIZE,
																								26,
																								Short.MAX_VALUE))
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.LEADING,
																								false)
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addComponent(
																												txtpnB,
																												GroupLayout.PREFERRED_SIZE,
																												26,
																												GroupLayout.PREFERRED_SIZE)
																										.addPreferredGap(
																												ComponentPlacement.RELATED,
																												GroupLayout.DEFAULT_SIZE,
																												Short.MAX_VALUE)
																										.addComponent(
																												txtpnC,
																												GroupLayout.PREFERRED_SIZE,
																												26,
																												GroupLayout.PREFERRED_SIZE))
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addComponent(
																												radioButton_1,
																												GroupLayout.PREFERRED_SIZE,
																												26,
																												GroupLayout.PREFERRED_SIZE)
																										.addPreferredGap(
																												ComponentPlacement.RELATED,
																												GroupLayout.DEFAULT_SIZE,
																												Short.MAX_VALUE)
																										.addComponent(
																												radioButton_2,
																												GroupLayout.PREFERRED_SIZE,
																												26,
																												GroupLayout.PREFERRED_SIZE)))))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addComponent(btnDone).addGap(3)));
		getContentPane().setLayout(groupLayout);
		pack();
		setVisible(true);
		radioButton.setSelected(true);
		var = 1;
		list.setSelectedIndex(0);
		while (!b) {
			Thread.sleep(1000);
		}
		int i=list.getSelectedIndex()+1;
		game.setSendervalues(ip.getText(), name.getText(), var,
				i);
		setVisible(false); // you can't see me!
		dispose(); // Destroy the JFrame object
	}

	public boolean getb() {
		return b;
	}
}
