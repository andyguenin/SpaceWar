import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class SpaceWar {
		Ship ship1;
		Ship ship2;
		Sun sun = new Sun(1000, 0, 0);
		
		private static final double PROP = 50;
		
		Point[] bgstars = new Point[100];
		private int framecount;
		private boolean disabled = false;
		int player1Score, player2Score = 0;
		public Queue<Character> keyq = new LinkedList<Character>();
		
		public SpaceWar()
		{
			ship1 = new Ship(100, -300, 300, 3*Math.PI/2, 50, 0, 0);
			ship2 = new Ship(100, 300, -300, Math.PI/2, 50, 0, 0);
			Random r = new Random();
			for(int i = 0; i < bgstars.length; i++)
			{
				Point p = new Point(r.nextInt(800), r.nextInt(800));
				bgstars[i] = p;
			}
			int framecount = 0;
		}
		
		
		public void step()
		{
			boolean t1 = false;
			boolean t2 = false;
			boolean turn1 = false;
			boolean turn2 = false;
			boolean f1 = false;
			boolean f2 = false;
			Iterator<Character> it = keyq.iterator();
			while(it.hasNext())
			{
				char c = it.next().charValue();
				switch(c)
				{
				case 'a':
					if(!turn1)
					{
						ship1.rotateLeft();
						turn1 = true;
					}
					break;
				case 'd':
					if(!turn1)
					{
						ship1.rotateRight();
						turn1 = true;
					}
					break;
				case 'w':
					if(!t1)
					{
						ship1.fireRockets();
						t1 = true;
					}
					break;
				case 's':
					if(!f1)
					{
						ship1.fireBullet();
						f1 = true;
					}
					break;
				}
			}
			if(!disabled)
			{
				setGravity(ship1);
				setGravity(ship2);
				
				ship1.step(1.0/30.0);
				ship2.step(1.0/30.0);
				
				Bullet[] s1b = ship1.getBullets();
				Bullet[] s2b = ship2.getBullets();
				
				boolean p1lost = false;
				boolean p2lost = false;
				
				for(int i = 0; i < s1b.length; i++)
				{
					if(s1b[i] != null && ship2.collision(s1b[i].getPosition()[0], s1b[i].getPosition()[1]))
					{
						p2lost=true;
						i = s1b.length;
					}
				}
				
				for(int i = 0; i < s2b.length; i++)
				{
					if(s2b[i] != null && ship1.collision(s2b[i].getPosition()[0], s2b[i].getPosition()[1]))
					{
						p1lost=true;
						i = s1b.length;
					}
				}
				
				if(sun.collision(ship1.getPosition()[0], ship1.getPosition()[1]))
					p1lost = true;
				if(sun.collision(ship2.getPosition()[0], ship2.getPosition()[1]))
					p2lost = true;
				
				if(ship1.collision(ship2.getPosition()[0], ship2.getPosition()[1]))
				{
					p1lost = true;
					p2lost = true;
				}
				
				if(p1lost || p2lost)
				{
					if(p1lost)
						player2Score++;
					if(p2lost)
						player1Score++;
					if(p1lost && p2lost)
					{
						player2Score-=2;
						player1Score-=2;
					}
						
					disabled = true;
					framecount = 0;
				}
				
			}
			else
			{
				if(framecount == 30)
				{
					ship1 = new Ship(100, -300, 300, 3*Math.PI/2, 50, 0, 0);
					ship2 = new Ship(100, 300, -300, Math.PI/2, 50, 0, 0);
					framecount++;
				}
				else
				{
					if(framecount == 90)
						disabled = false;
					else
						framecount++;
				}
			}
			
			keyq = new LinkedList<Character>();
		}
		
		
		private void setGravity(Ship s)
		{
			int x = s.getPosition()[0];
			int y = s.getPosition()[1];
			double m = s.getMass();
			int dy = sun.getPosition()[1]-y;
			int dx = sun.getPosition()[0]-x;
			double theta = Math.atan2(dy, dx);
			if(dx*dx + dy * dy <= 5)
			{
				s.externalForces(0, 0);
				return;
			}
			double f = PROP * (sun.getMass() * m)/(dx*dx + dy*dy);
			
			
			s.externalForces((int)(f * Math.cos(theta)), (int)(f * Math.sin(theta)));
			
			
		}
		
		
		public void drawScene(Image offscreen)
		{
			Graphics g = offscreen.getGraphics();
			
			drawBackground(offscreen);
			drawStars(g);
			
			
			
			drawSun(g);
			drawText(g);
			drawShips(g);

			g.setColor(Color.BLACK);
			
		}
		
		
		private void drawBackground(Image i)
		{
			Graphics g = i.getGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, i.getHeight(null), i.getWidth(null));			
				
		}
		
		private void drawStars(Graphics g)
		{
			g.setColor(Color.WHITE);
			for(int i = 0; i < bgstars.length; i++)
			{
				g.fillOval(bgstars[i].x, bgstars[i].y, 1, 2);
			}
		}
		
		private void drawSun(Graphics g)
		{
			int rayLength = 20;
			Color c = new Color(125, 227, 255);
			g.setColor(c);
			g.fillOval(395, 395, 10, 10);
			for(int i = 0; i < 8; i++)
			{
				g.drawLine(400, 400, 400+(int)(rayLength*Math.cos(3.141592/4*i)), 400+(int)(rayLength*Math.sin(3.141592/4*i)));
			}
			
		}
		
		private void drawText(Graphics g)
		{
			g.setColor(Color.WHITE);
			g.drawString("Player 1: " + player2Score, 80, 20);
			g.drawString("Player 2: " + player1Score, 580, 20);
			
			
			Font f = new Font("Dialog", Font.PLAIN, 32);
			g.setFont(f);
			g.setColor(Color.WHITE);
			g.drawString("SpaceWars!", 280, 30);
		}
		private int deg = 0;
		private void drawShips(Graphics g)
		{
			//ship 1
			Color s1 = new Color(255, 125, 186);
			g.setColor(s1);

			drawSingleShip(g, ship1);
		/*	
			Font f = new Font("Dialog", Font.PLAIN, 12);
			g.setFont(f);
			g.setColor(Color.WHITE);
			g.drawString("s1 px " + ship1.position[0], 0, 20);
			g.drawString("s1 py " + ship1.position[1], 0, 30);
			g.drawString("s1 vx " + ship1.velocity[0], 0, 40);
			g.drawString("s1 vy " + ship1.velocity[1], 0, 50);
			g.drawString("s1 ax " + ship1.acceleration[0], 0, 60);
			g.drawString("s1 ay " + ship1.acceleration[1], 0, 70);
			*/
			//ship 2
			Color s2 = new Color(29, 207, 50);
			g.setColor(s2);
			drawSingleShip(g, ship2);
		}
		
		private void drawSingleShip(Graphics g, Ship s)
		{
			int ox = x(s.getPosition()[0]);
			int oy = y(s.getPosition()[1]);
			
			double o = s.getOrientation();
			
			
			
			for(int i =-1; i <= 1; i++)
			{
				for(int j = -1; j<=1; j++)
				{
					int cx = i*800 + ox;
					int cy = j*800 + oy;
					Ellipse2D rocket = new Ellipse2D.Float(cx - 20, cy-9, 40, 18);
					AffineTransform at = AffineTransform.getRotateInstance(o, cx, cy);
					((Graphics2D)(g)).fill(at.createTransformedShape(rocket));
				}
			}
		}
		
		private int x(int v)
		{
			return v + 400;
		}
		
		private int y(int v)
		{
			return -v + 400;
		}
}
