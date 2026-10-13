package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class AttackGaugeUI {

	private JFrame frame = new JFrame();
	private JProgressBar allyBar = new JProgressBar();
	private JProgressBar foeBar = new JProgressBar();
	private JLabel instruction = new JLabel("Keep Smashing! Whoever hits 100% will attack first!");
	private JLabel allyLabel = new JLabel("Ally");
	private JLabel foeLabel = new JLabel("Foe");
	private JButton button = new JButton("SMASH!");
	
	private Random random = new Random();

	private int allyPower = 0;
	private int foePower = 0;
	private boolean isCharging = true;

	public AttackGaugeUI() {

		allyBar.setValue(allyPower);
		allyBar.setBounds(0, 100, 420, 50);
		allyBar.setStringPainted(true);

		foeBar.setValue(foePower);
		foeBar.setBounds(0, 200, 420, 50);
		foeBar.setStringPainted(true);
		
		instruction.setBounds(0, 20, 420, 30);
		instruction.setHorizontalAlignment(JLabel.CENTER);

		allyLabel.setBounds(0, 70, 420, 30);
		allyLabel.setHorizontalAlignment(JLabel.CENTER);

		foeLabel.setBounds(0, 170, 420, 30);
		foeLabel.setHorizontalAlignment(JLabel.CENTER);

		button.setBounds(135, 300, 150, 50);
		button.setEnabled(true);

		frame.add(allyBar);
		frame.add(foeBar);
		frame.add(instruction);
		frame.add(allyLabel);
		frame.add(foeLabel);
		frame.add(button);

		frame.setTitle("Attack Gauge");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(420, 420);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playerCharge();
			}
		});
		
		opponentCharge();
	}
	
	private void playerCharge() {
		allyPower += 3;
		allyBar.setValue(allyPower);
		allyBar.setString(allyPower + "%");
		
		if (allyPower > 100) {
			allyPower = 100;
			allyBar.setString("Attack First!");
			button.setEnabled(false);
			isCharging = false;
		}
	}
	
	private void opponentCharge() {
		while (foePower <= 100) {
			
			foeBar.setValue(foePower);
			foeBar.setString(foePower + "%");
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			foePower += random.nextInt(3);
			
			if (foePower > 100) {
				foePower = 100;
				foeBar.setString("Attack First!");
				button.setEnabled(false);
				isCharging = false;
			}
			
			if (!isCharging) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.dispose();
				break;
			}
		}
	}

	public boolean isPlayerAttackFirst() {
		return allyPower == 100;
	}
}
