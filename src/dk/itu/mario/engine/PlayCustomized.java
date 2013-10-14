package dk.itu.mario.engine;

import java.awt.*;

import javax.swing.*;

import geomario.util.Test;

public class PlayCustomized {

    public static void main(String[] args)
    {
	if (args.length == 1) {
	    if (args[0].equals("no-hazards")) {
		Test.noHazards = true;
	    }
	}

	JFrame frame = new JFrame("Mario Experience Showcase");
	MarioComponent mario = new MarioComponent(640, 480,true);

	frame.setContentPane(mario);
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.pack();

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	frame.setLocation((screenSize.width-frame.getWidth())/2, (screenSize.height-frame.getHeight())/2);

	frame.setVisible(true);

	mario.start();   
    }	

}
