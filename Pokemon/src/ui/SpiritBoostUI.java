package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

public class SpiritBoostUI {
	private int spirit;
	
	private JFrame frame;
	private JButton button;
	private Timer timer;

	public SpiritBoostUI() {
		button = new JButton("Spirit: " + spirit);
		button.setBounds(0, 0, 420, 420);
	
		frame = new JFrame();
		frame.setTitle("Spirit Boost");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(420, 420);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);
		
		frame.add(button);
		
		timer = new Timer(5000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button.setEnabled(false);
				button.setText("Times up!");
				frame.dispose();
			}
		});

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spirit++;
				button.setText("Spirit: " + spirit);
			}
		});
	}

	public int chargeSpirit() {
		frame.setVisible(true);
		timer.setRepeats(false);
		timer.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return spirit;
	}
}
