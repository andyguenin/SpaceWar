import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class SWDisp extends Applet implements Runnable, KeyListener{
	SpaceWar game;
	Graphics g;
	Image offscreen;
	Dimension dim;
	Thread animator;
	int frame;
	int delay = 1000/30;
	
	
	public void init()
	{
		dim = new Dimension();
		dim.height = 800;
		dim.width = 800;
		
		offscreen = createImage(800, 800);
		g = offscreen.getGraphics();
		setSize(dim);
		setName("SpaceWars!");
		game = new SpaceWar();
		addKeyListener(this);
	}
	
	public void start() {
		animator = new Thread(this);
		animator.start();
	}
	
	public void paint(Graphics g)
	{
		if(offscreen != null)
			g.drawImage(offscreen, 0, 0, null);
		((Graphics2D)g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

	}
	

    public void run() {
    	// Remember the starting time
    	long tm = System.currentTimeMillis();
    	while (Thread.currentThread() == animator) {
    	    // Display the next frame of animation.
    	    repaint();

    	    // Delay depending on how far we are behind.
    	    try {
    		tm += delay;
    		Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
    	    } catch (InterruptedException e) {
    		break;
    	    }

    	    // Advance the frame
    	    frame++;
    	}
      }

	public void update(Graphics g)
	{
		Dimension d = size();
		Graphics og = offscreen.getGraphics();
		game.step();
		game.drawScene(offscreen);
		
		og.drawString("Frame " + frame, 0, 300);
		g.drawImage(offscreen, 0, 0, null);

	}
	
	public void stop() {
		animator = null;
	}


	
	public void keyPressed(KeyEvent e) {
		char c = e.getKeyChar();
		if(c!= KeyEvent.CHAR_UNDEFINED)
		{
			game.keyq.add(c);
			e.consume();
		}
		
	}

	public void keyReleased(KeyEvent arg0) {
		boolean t  =true;
		
	}

	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		if(c!= KeyEvent.CHAR_UNDEFINED)
		{
			game.keyq.add(c);
			e.consume();
		}
	}
}
