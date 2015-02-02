import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;

public class Sender extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 413368187036038772L;
	private JTextField txtX;
	private JTextField txtY;
	private JTextPane xpos;
	private JTextPane ypos;
	JRadioButton Jumping;
	JRadioButton Left;
	JRadioButton Right;
	String msg;
	public String rcvd;
	private DatagramSocket s = new DatagramSocket();
	boolean c=false;
	

	public Sender() throws IOException {
		
		super("Sender");
		setAlwaysOnTop(true);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception error) {
		}
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
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher());
		pack();
		setVisible(true);
		try {
			s("§:Tester:");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true){
			byte[] buf = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buf, buf.length);
			s.receive(dp);
			rcvd = new String(dp.getData());
			rcvd = rcvd.trim();
			String[] Spart = rcvd.split(":");
			String FL = Spart[0];
			System.out.println(rcvd);
			System.out.println("received something...");
			if(FL.equals("$")){
				if(Spart[1].equals("")){
					
				}
			}else{
				System.out.println("Received illegal package");
			}
		}
	}
	
	
    private class MyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
        	int key = e.getKeyCode();
            if (e.getID() == KeyEvent.KEY_PRESSED) {
            	
    			if (key == KeyEvent.VK_W) {
    				msg="$:^:";
    				try {
    					s(msg);
    				} catch (IOException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
    			}
    			if (key == KeyEvent.VK_A&&!c) {
    				msg="$:<:P:";
    				try {
    					s(msg);
    				} catch (IOException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
    			}
    			if (key == KeyEvent.VK_D&&!c) {
    				msg="$:>:P:";
    				try {
    					s(msg);
    				} catch (IOException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
    			}
            	
                c=true;
            }
            if (e.getID() == KeyEvent.KEY_RELEASED) {
            	
    			if (key == KeyEvent.VK_A) {
    				msg="$:<:R:";
    				try {
    					s(msg);
    				} catch (IOException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
    			}
    			if (key == KeyEvent.VK_D) {
    				msg="$:>:R:";
    				try {
    					s(msg);
    				} catch (IOException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
    			}
            	
                c=false;
            }
            return false;
        }
    }
	
	
	
	class keydealer extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			System.out.println(e);
			if (key == KeyEvent.VK_W) {
				msg="$:^:";
				try {
					s(msg);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (key == KeyEvent.VK_A) {
				msg="$:<:P:";
				try {
					s(msg);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (key == KeyEvent.VK_D) {
				msg="$:>:P:";
				try {
					s(msg);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		public void keyReleased(KeyEvent e){
			int key = e.getKeyCode();
			System.out.println(e);
			if (key == KeyEvent.VK_A) {
				msg="$:<:R:";
				try {
					s(msg);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (key == KeyEvent.VK_D) {
				msg="$:>:R:";
				try {
					s(msg);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	public void setx(int x) {
		xpos.setText(Integer.toString(x));
	}

	public void sety(int y) {
		xpos.setText(Integer.toString(y));
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
	public void s(String msg) throws IOException {
		String ip="localhost";
		byte[] buf = new byte[1024];
		InetAddress hostAddress = InetAddress.getByName(ip);
		buf = msg.getBytes();
		DatagramPacket out = new DatagramPacket(buf, buf.length, hostAddress,
				25565);
		s.send(out);
	}
	public static void main(String[] args) throws IOException {
		new Sender();
	}

}
