import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;


public class SpaceWarGame {
		Ship ship1;
		Ship ship2;
		Sun sun = new Sun(250, 0, 0);
		
		private static final double PROP = 50;
		private static enum ShipEvent{ Left, Right, Boost, Fire};
		
		Point[] bgstars = new Point[100];
		private int framecount;
		private boolean disabled = false;
		int player1Score, player2Score = 0;
		private Set<ShipEvent> ship1Events;
		private Set<ShipEvent> ship2Events;
		private double fps;
		
		public SpaceWarGame(double fps)
		{
			this.fps = fps;
			reset();
			
			Random r = new Random();
			for(int i = 0; i < bgstars.length; i++)
			{
				Point p = new Point(r.nextInt(800), r.nextInt(800));
				bgstars[i] = p;
			}
			
			
			
		}
		
		private void reset()
		{
			ship1 = new Ship(200, -300, 300, 3*Math.PI/2, 15, 0, 0,fps,18,40);
			ship2 = new Ship(200, 300, -300, Math.PI/2, 15, 0, 0, fps,18,40);
			ship1Events = new HashSet<ShipEvent>();
			ship2Events = new HashSet<ShipEvent>();
			framecount = 0;
			disabled = false;
		}
		
		public void step()
		{
			Iterator<ShipEvent> its1 = ship1Events.iterator();
			while(its1.hasNext())
			{
				switch(its1.next())
				{
				case Left:
					ship1.rotateLeft();
					break;
				case Right:
					ship1.rotateRight();
					break;
				case Fire:
					ship1.fireBullet();
					break;
				case Boost:
					ship1.fireRockets();
					break;				
				}
			}
			
			Iterator<ShipEvent> its2 = ship2Events.iterator();
			while(its2.hasNext())
			{
				switch(its2.next())
				{
				case Left:
					ship2.rotateLeft();
					break;
				case Right:
					ship2.rotateRight();
					break;
				case Fire:
					ship2.fireBullet();
					break;
				case Boost:
					ship2.fireRockets();
					break;				
				}
			}

			if(!disabled)
			{
				setGravity(ship1);
				setGravity(ship2);
				
				ship1.step();
				ship2.step();
				
				Bullet[] s1b = ship1.getBullets();
				Bullet[] s2b = ship2.getBullets();
				
				boolean p1lost = false;
				boolean p2lost = false;
				
				for(int i = 0; i < s1b.length; i++)
				{
					if(s1b[i] != null)
					{
						setGravity(s1b[i]);
						s1b[i].step();
						if(ship2.collision(s1b[i].getPosition()[0], s1b[i].getPosition()[1]))
						{
							p2lost=true;
							i = s1b.length;
						}
					}
				}
				
				for(int i = 0; i < s2b.length; i++)
				{
					if(s2b[i] != null)
					{
						setGravity(s2b[i]);
						s2b[i].step();
						if(ship1.collision(s2b[i].getPosition()[0], s2b[i].getPosition()[1]))
						{
							p1lost=true;
							i = s1b.length;
						}
					}
				}
				
				int s1cpx;
				int s1cpy;
				int s2cpx;
				int s2cpy;
				
				double theta1 = Math.atan2(-ship1.getPosition()[1], -ship1.getPosition()[0]);
				double theta2 = Math.atan2(-ship2.getPosition()[1], -ship2.getPosition()[0]);
				
				s1cpx = (int)(ship1.getWidth() * Math.cos(ship1.getOrientation() - theta1)/2) + ship1.getPosition()[0];
				s1cpy = (int)(ship1.getHeight() * Math.sin(ship1.getOrientation() - theta1)/2) + ship1.getPosition()[1];
				s2cpx = (int)(ship2.getWidth() * Math.cos(ship2.getOrientation() - theta2)/2) + ship2.getPosition()[0];
				s2cpy = (int)(ship2.getHeight() * Math.sin(ship2.getOrientation() - theta2)/2) + ship2.getPosition()[1];
				
				
				if(sun.collision(s1cpx, s1cpy) || sun.collision(ship1.getPosition()[0], ship1.getPosition()[1]))
					p1lost = true;
				if(sun.collision(s2cpx, s2cpy)  || sun.collision(ship2.getPosition()[0], ship2.getPosition()[1]))
					p2lost = true;
				
				if(ship1.collision(ship2.getPosition()[0], ship2.getPosition()[1]))
				{
					p1lost = true;
					p2lost = true;
				}
				
				if(p1lost || p2lost)
				{
					if(p1lost)
					{
						player2Score++;
					}
					if(p2lost)
					{
						player1Score++;
					}
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
					reset();
					framecount++;
				}
				else
				{
					if(framecount == 90)
					{
						disabled = false;
						reset();
					}
					else
						framecount++;
				}
				
			}
		}
		
		
		public void setGravity(SWObject s)
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
			drawBullets(g);
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
			g.fillOval(390, 390, 20, 20);
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
		
		private void drawBullets(Graphics g)
		{
			g.setColor(Color.WHITE);
			Bullet[] s1b = ship1.getBullets();
			Bullet[] s2b = ship2.getBullets();
			for(int b = 0; b < s1b.length; b++)
			{
				if(s1b[b] != null)
				{
					for(int i =-1; i <= 1; i++)
					{
						for(int j = -1; j<=1; j++)
						{
							int cx = i*800 + x(s1b[b].getPosition()[0]-1);
							int cy = j*800 + y(s1b[b].getPosition()[1]-1);
							Ellipse2D bullet = new Ellipse2D.Float(cx - 2, cy-2, 4, 4);
							((Graphics2D)(g)).fill(bullet);
						}
					}
				}
			}
			
			for(int b = 0; b < s2b.length; b++)
			{
				if(s2b[b] != null)
				{
					for(int i =-1; i <= 1; i++)
					{
						for(int j = -1; j<=1; j++)
						{
							int cx = i*800 + x(s2b[b].getPosition()[0]-1);
							int cy = j*800 + y(s2b[b].getPosition()[1]-1);
							Ellipse2D bullet = new Ellipse2D.Float(cx - 2, cy-2, 4, 4);
							((Graphics2D)(g)).fill(bullet);
						}
					}
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
		
		public void addKeyEvent(GameKeyEvent ke)
		{
			switch(ke.getChar())
			{
			case 'a':
				addOrRemoveShipEvent(ke.getPressType(), ShipEvent.Left, ship1Events);
				break;
			case 'd':
				addOrRemoveShipEvent(ke.getPressType(), ShipEvent.Right, ship1Events);
				break;
			case 'w':
				addOrRemoveShipEvent(ke.getPressType(), ShipEvent.Boost, ship1Events);
				break;
			case 's':
				addOrRemoveShipEvent(ke.getPressType(), ShipEvent.Fire, ship1Events);
				break;
			}
			
			switch(ke.getChar())
			{
			case 'j':
				addOrRemoveShipEvent(ke.getPressType(), ShipEvent.Left, ship2Events);
				break;
			case 'l':
				addOrRemoveShipEvent(ke.getPressType(), ShipEvent.Right, ship2Events);
				break;
			case 'i':
				addOrRemoveShipEvent(ke.getPressType(), ShipEvent.Boost, ship2Events);
				break;
			case 'k':
				addOrRemoveShipEvent(ke.getPressType(), ShipEvent.Fire, ship2Events);
				break;
			}
		}
			
		private void addOrRemoveShipEvent(GameKeyEvent.PressType type, ShipEvent event, Set<ShipEvent> shipESet)
		{
			if(type == GameKeyEvent.PressType.DOWN)
			{
				if(!shipESet.contains(event))
					shipESet.add(event);
			}
			else
			{
				if(shipESet.contains(event))
					shipESet.remove(event);
			}
		}
}
