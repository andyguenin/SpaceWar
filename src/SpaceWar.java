import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class SpaceWar extends Applet implements Runnable, KeyListener{
	private static final long serialVersionUID = 6660245705088726828L;
	SpaceWarGame game;
	Graphics g;
	Image offscreen;
	Dimension dim;
	Thread animator;
	int frame;
	private static final int FPS = 30;
	int delay = 1000/FPS;
	
	
	public void init()
	{
		dim = new Dimension();
		dim.height = 800;
		dim.width = 800;
		
		offscreen = createImage(800, 800);
		g = offscreen.getGraphics();
		setSize(dim);
		setName("SpaceWars!");
		game = new SpaceWarGame(FPS);
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
		game.step();	
		game.drawScene(offscreen);
		g.drawImage(offscreen, 0, 0, null);
	}
	
	public void stop() {
		animator = null;
	}


	
	public void keyPressed(KeyEvent e) {
		char c = e.getKeyChar();
		if(c!= KeyEvent.CHAR_UNDEFINED)
		{
			GameKeyEvent ke = new GameKeyEvent(c, GameKeyEvent.PressType.DOWN);
			game.addKeyEvent(ke);
			e.consume();
		}
		
	}

	public void keyReleased(KeyEvent e) {	
		char c = e.getKeyChar();
		if(c!= KeyEvent.CHAR_UNDEFINED)
		{
			GameKeyEvent ke = new GameKeyEvent(c, GameKeyEvent.PressType.UP);
			game.addKeyEvent(ke);
			e.consume();
		}		
	}

	public void keyTyped(KeyEvent e) {

	}
}
