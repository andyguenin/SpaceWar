public class Ship implements SWObject{

	private static final int ROCKET_ACCEL = 150;
	private static final int BULLET_REL = 250;
	
	private double mass;
	public double[] position;
	public  double[] velocity;
	public double[] acceleration;
	private double orientation;
	private Bullet[] firedBullets;
	private boolean unlimitedFuel = false;
	int fuel;
	int maxBullets;
	private boolean firingRocket;
	private double t;
	private double fps;
	private int width;
	private int height;
	private int spacing = 10;
	private int sinceLastFired;
	
	public Ship(double mass, int x, int y, double orientation, int maxBulletsAlive, int startFuel, int maxBullets, double fps, int width, int height)
	{
		position = new double[2];
		velocity = new double[2];
		acceleration = new double[2];
		position[0] = x;
		position[1] = y;
		acceleration[0] = 0;
		acceleration[0] = 0;
		this.mass = mass;
		if(startFuel == 0)
			unlimitedFuel = true;
		this.fuel = startFuel;
		firedBullets = new Bullet[maxBulletsAlive ==0? 1:maxBulletsAlive];
		firingRocket = false;
		this.maxBullets = maxBullets;
		this.orientation = orientation;
		this.fps = fps;
		t = 1/fps;
		this.width = width;
		this.height = height;
		sinceLastFired = 0;
		
	}
	
	public double getMass() {
		return mass;
	}

	public double getOrientation(){
		return orientation;
	}
	public int[] getPosition() {
		int[] a = new int[2];
		a[0] = (int)(position[0]);
		a[1] = (int)(position[1]);
		return a;
	}
	
	public double[] getVelocity() {
		return velocity;
	}
	
	public void fireRockets()
	{
		if(fuel != 0 || unlimitedFuel)
		{
			firingRocket = true;
			if(!unlimitedFuel)
				fuel--;
		}
		firingRocket = true;
	}

	public void rotateLeft()
	{
		orientation -= Math.PI/180*10;
		orientation %= 2*Math.PI;
	}
	
	public void rotateRight()
	{
		orientation += Math.PI/180*10;
		orientation %= 2*Math.PI;
	}
	
	public void fireBullet()
	{
		if(sinceLastFired >= spacing)
		{
			int firstIndex = 0;
			for(int i = 0; i < firedBullets.length; i++)
			{
				if(firedBullets[i] != null)
					firstIndex++;
				else
					i = firedBullets.length;
			}
			if(firstIndex == firedBullets.length)
				if(firedBullets[firstIndex-1] != null)
					return;
			firedBullets[firstIndex] = new Bullet((int)(position[0] - Math.cos(orientation) * 20) , (int)(position[1] + Math.sin(orientation) * 20), (int)(velocity[0] - Math.cos(orientation) * BULLET_REL), (int)(velocity[1] + Math.sin(orientation) * BULLET_REL), fps);
			sinceLastFired = 0;
		}
	}
		
	private void accelerate()
	{
		double x = -Math.cos(orientation)*ROCKET_ACCEL*(firingRocket?1:0);
		double y = Math.sin(orientation)*ROCKET_ACCEL*(firingRocket?1:0);
		acceleration[0] += x;
		acceleration[1] += y;
		firingRocket = false;
	}

	public void externalForces(double x, double y) {
		acceleration[0] = x;
		acceleration[1] = y;		
	}

	@Override
	public void step() {
		accelerate();
		velocity[0] += acceleration[0] * t;
		velocity[1] += acceleration[1] * t;
		
		position[0] += velocity[0] * t;
		position[1] += velocity[1] * t;
		
		position[0] = Math.abs((position[0] + 1200) % 800) - 400;
		position[1] = Math.abs((position[1] + 1200) % 800) - 400;
		
		for(int i = 0; i < firedBullets.length; i++)
		{
			if(firedBullets[i] !=null && firedBullets[i].isDead())
				firedBullets[i] = null;
		}
		
		sinceLastFired++;
		
	}
	
	public Bullet[] getBullets()
	{
		return firedBullets; 
	}
	
	public boolean collision(int xo, int yo)
	{
		int x = (int)(position[0] - xo);
		int y = (int)(position[1] - yo);
		if(((x*x)/(81.0) + (y*y) / (400.0)) < 1)
			return true;
		return false;
		
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}

	public boolean isBoosting()
	{
		return firingRocket;
	}
}

