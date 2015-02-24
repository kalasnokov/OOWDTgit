package UI;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import main.Game;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Button;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class Connector extends JFrame {
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private final long serialVersionUID = 2643206995134951451L;
	private String[] races = new String[1000];
	public DefaultListModel<String> lm = new DefaultListModel<String>();
	public DefaultComboBoxModel<String> cm = new DefaultComboBoxModel<String>();
	int var;
	int race;
	String Sname;
	String Sip;
	boolean b;
	boolean c;
	private JTextField IP;
	private JTextField Name;
	private JList<String> list;
	private JComboBox<String> comboBox;
	private JCheckBox chckbxHost;

	public Connector(Game game) throws InterruptedException,
			FileNotFoundException, IOException {
		setResizable(false);
		getContentPane().setLayout(null);

		IP = new JTextField();
		IP.setBounds(10, 32, 236, 20);
		getContentPane().add(IP);
		IP.setColumns(10);

		Name = new JTextField();
		Name.setBounds(10, 76, 236, 20);
		getContentPane().add(Name);
		Name.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(101, 127, 145, 84);
		getContentPane().add(scrollPane);

		list = new JList<String>(lm);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cm.removeAllElements();
				cm.setSelectedItem(1);
				int l = list.getSelectedIndex() + 1;
				for (int i = 1; true; i++) {
					File f = new File("res/char" + l + "/var" + i);
					if (f.exists()) {
						cm.addElement(Integer.toString(i));
					} else {
						break;
					}
				}
			}
		});
		scrollPane.setViewportView(list);

		JTextPane txtpnIp = new JTextPane();
		txtpnIp.setBackground(SystemColor.menu);
		txtpnIp.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtpnIp.setText("IP");
		txtpnIp.setEditable(false);
		txtpnIp.setBounds(114, 0, 23, 31);
		getContentPane().add(txtpnIp);

		JTextPane txtpnName = new JTextPane();
		txtpnName.setText("Name");
		txtpnName.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtpnName.setEditable(false);
		txtpnName.setBackground(SystemColor.menu);
		txtpnName.setBounds(101, 53, 51, 23);
		getContentPane().add(txtpnName);

		JTextPane txtpnRace = new JTextPane();
		txtpnRace.setText("Race");
		txtpnRace.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtpnRace.setEditable(false);
		txtpnRace.setBackground(SystemColor.menu);
		txtpnRace.setBounds(149, 96, 51, 31);
		getContentPane().add(txtpnRace);

		JTextPane txtpnVariation = new JTextPane();
		txtpnVariation.setText("Variation");
		txtpnVariation.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtpnVariation.setEditable(false);
		txtpnVariation.setBackground(SystemColor.menu);
		txtpnVariation.setBounds(10, 96, 86, 31);
		getContentPane().add(txtpnVariation);

		comboBox = new JComboBox<String>(cm);
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		comboBox.setBounds(20, 127, 55, 31);
		getContentPane().add(comboBox);

		Button button = new Button("Done");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox.getItemCount() != 0) {
					b = true;
				}
			}
		});
		button.setFont(new Font("Dialog", Font.PLAIN, 18));
		button.setBounds(10, 176, 85, 31);
		getContentPane().add(button);

		chckbxHost = new JCheckBox("Host");
		chckbxHost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c = chckbxHost.isSelected();
				IP.setEditable(!chckbxHost.isSelected());
				IP.setEnabled(!chckbxHost.isSelected());
			}
		});
		chckbxHost.setFont(new Font("Dialog", Font.BOLD, 18));
		chckbxHost.setBounds(10, 0, 70, 31);
		getContentPane().add(chckbxHost);

		for (int i = 1; true; i++) {
			File f = new File("res/char" + i);
			if (f.exists()) {
				f = new File("res/char" + i + "/opt.ccf");
				if (f.exists()) {
					try (BufferedReader br = new BufferedReader(new FileReader(
							"res/char" + i + "/opt.ccf"))) {
						StringBuilder sb = new StringBuilder();
						String line = br.readLine();
						while (line != null) {
							sb.append(line);
							line = br.readLine();
						}
						races[i] = sb.toString();
					}
				} else {
					f.getParentFile().mkdirs();
					try {
						f.createNewFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					PrintWriter writer = null;
					try {
						writer = new PrintWriter("res/char" + i + "/opt.ccf",
								"UTF-8");
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
					writer.println("Unknown race");
					writer.close();
					races[i] = "Unknown race";
				}
				boolean loop = true;
				for (int u = 1; loop; u++) {
					f = new File("res/char" + i + "/var" + u);
					if (f.exists()) {
						f = new File("res/char" + i + "/var" + u + "/opt.cvcf");
						if (!f.exists()) {//if you want up udtae ALL .cvcf files to annew format, remove the ! and run once
							f.getParentFile().mkdirs();
							try {
								f.createNewFile();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							PrintWriter writer = null;
							try {
								writer = new PrintWriter("res/char" + i
										+ "/var" + u + "/opt.cvcf", "UTF-8");
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (UnsupportedEncodingException e1) {
								e1.printStackTrace();
							}
							writer.println("walkspeed:10:");
							writer.println("jumpspeed:20:");
							writer.println("idlespeed:25:");
							writer.close();
							System.out.println("New opt.cvcf created for race "+i+"\'s variation "+u);
						}
					} else {
						loop = false;
					}
				}
			} else {
				break;
			}
		}

		for (int i = 0; i < races.length; i++) {
			if (races[i] != null) {
				lm.addElement(races[i]);
			}
		}
		var = 1;
		race = 1;
		for (int u = 1; true; u++) {
			File f = new File("res/char1/var" + u);
			if (f.exists()) {
				cm.addElement(Integer.toString(u));
			} else {
				break;
			}
		}
		cm.setSelectedItem(1);
		list.setSelectedIndex(0);
		// pack();
		setVisible(true);
		setSize(261, 245);
		while (!b) {
			Thread.sleep(1000);
		}
		String sip;
		if (c) {
			game.Host();
			sip = "";
			Thread.sleep(1000);
			while (true) {
				Thread.sleep(1000);
				if (game.server.getready()) {
					break;
				}
			}
		} else {
			sip = IP.getText();
		}
		var = Integer.parseInt(cm.getSelectedItem().toString());
		race = list.getSelectedIndex() + 1;
		game.setSendervalues(sip, Name.getText(), var, race);
		setVisible(false); // you can't see me!
		dispose(); // Destroy the JFrame object
	}

	public boolean getb() {
		return b;
	}
}
