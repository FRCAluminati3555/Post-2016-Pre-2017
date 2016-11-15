package org.usfirst.frc.team3555.robot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.border.MatteBorder;

public class ShooterTheory extends JPanel implements KeyListener{
	private static final long serialVersionUID = 1L;

	private JFrame frame = new JFrame();

	private JLabel blackLines = new JLabel("The black lines are the physical boundries of the shooter.");
	private JLabel greenLine = new JLabel("The green line is the physical shooter moving up and down");
	private JLabel redZone = new JLabel("<html>The redzone is the theoretical boundries that the shooter cannot move <br> --in the direction that is closest to the physical boundry (black lines)--</html>");
	
	private int originX, originY;
	private int x, y;
	private int changeBy = 50;
	private final JPanel infoPanel = new JPanel();
	private final JPanel greenPanel = new JPanel();
	private final JPanel redPanel = new JPanel();
	private final JPanel blackPanel = new JPanel();
	
	public ShooterTheory(){
		frame.setSize(800, 600);

		originX = frame.getWidth() / 2;
		originY = frame.getHeight() / 2;

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);
		
		frame.addKeyListener(this);
		setLayout(new BorderLayout(0, 0));
		add(infoPanel, BorderLayout.SOUTH);
		
		infoPanel.setLayout(new BorderLayout(0, 0));
		infoPanel.add(greenPanel, BorderLayout.NORTH);
		
		greenPanel.setBorder(new MatteBorder(2, 2, 1, 2, (Color) new Color(0, 0, 0)));
		greenPanel.add(greenLine);
		
		greenLine.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		redPanel.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));
		redPanel.add(redZone);
		
		redZone.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		blackPanel.add(blackLines);
		blackPanel.setBorder(new MatteBorder(2, 2, 1, 2, (Color) new Color(0, 0, 0)));
		
		blackLines.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		infoPanel.add(redPanel, BorderLayout.SOUTH);
		infoPanel.add(blackPanel, BorderLayout.CENTER);
		
		blackLines.setVisible(true);
		greenLine.setVisible(true);
		redZone.setVisible(true);
		
		frame.setVisible(true);
		frame.getContentPane().add(this);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.clearRect(0, 0, frame.getWidth(), frame.getHeight());
		
		g2.setStroke(new BasicStroke(5));
		
		g2.setColor(Color.RED);
		g2.fillArc(-100, 173, 1000, 250, 180, -25);
		g2.fillArc(348, -196, 100, 1000, 90, 50);
		
		g2.setColor(Color.GREEN);
		g2.drawLine(originX, originY, x, y);

		g2.setColor(Color.BLACK);
		g2.drawLine(originX, originY, originX, 0);
		g2.drawLine(originX, originY, 0, originY);
		
		repaint();
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT && x <= originX - 95){
			if(x >= 0){
				x+=changeBy;
				y-=changeBy;
			}
			if(x < 1){
				x+=changeBy;
				y-=changeBy;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT && y <= originY - 85){
			if(x >= 0){
				x-=changeBy;
				y+=changeBy;
			}
			if(x < 1){
				x-=changeBy;
				y+=changeBy;
			}
		}
//		System.out.println("Here && X: " + x);
	}

	public static void main(String[] args) {
		new ShooterTheory();
	}
	
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
}
