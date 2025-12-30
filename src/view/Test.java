package view;

import javax.swing.SwingUtilities;

public class Test {
	 public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> {
	            try {
	                new GameFrame();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        });
	    }
	}