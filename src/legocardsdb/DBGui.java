package legocardsdb;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DBGui {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Lego Card Database");
		frame.setSize(550, 500);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		LegoDB db = new LegoDB();
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
		            "Are you sure to close this window?", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	db.disconnect();
		            System.exit(0);
		        }
		    }
		});
		
		LegoComp comp = new LegoComp(db);
		frame.add(comp);
		frame.setVisible(true);
	}

}
