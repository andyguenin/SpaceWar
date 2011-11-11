public class Ship implements SWObject{

	private static final int ROCKET_ACCEL = 90;
	private static final int BULLET_REL = 5;
	
	private double mass;
	public double[] position;
	public  double[] velocity;
	public double[] acceleration;
	private double orientation;
	private Bullet[] firedBullets;
	private boolean unlimitedFuel = false;
	private boolean unlimitedBullets = false;
	int fuel;
	int maxBullets;
	private boolean firingRocket;
	
	public Ship(double mass, int x, int y, double orientation, int maxBulletsAlive, int startFuel, int maxBullets)
	{
		position = new double[2];
		velocity = new double[2];
		acceleration = new double[2];
		position[0] = x;
		position[1] = y;
		acceleration[0] = 0;
		acceleration[0] = 0;
		this.mass = mass;
		if(maxBulletsAlive == 0)
			unlimitedBullets = true;
		if(startFuel == 0)
			unlimitedFuel = true;
		this.fuel = startFuel;
		firedBullets = new Bullet[maxBulletsAlive ==0? 1:maxBulletsAlive];
		firingRocket = false;
		this.maxBullets = maxBullets;
		this.orientation = orientation;
		
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
		int firstIndex = 0;
		for(int i = 0; i < firedBullets.length; i++)
		{
			if(firedBullets[i] != null)
				firstIndex++;
			else
				i = firedBullets.length;
		}
		if(firstIndex == firedBullets.length)
			if(firedBullets[firstIndex] != null)
				return;
		firedBullets[firstIndex] = new Bullet((int)(position[0]) + (int)Math.cos(orientation) * BULLET_REL , (int)(position[1]) + (int)Math.cos(orientation) * BULLET_REL, velocity[0] + (int)Math.cos(orientation) * BULLET_REL, velocity[1] + (int)Math.sin(orientation) * BULLET_REL);
		
	}
		
	private void accelerate()
	{
		double x = Math.cos(orientation)*ROCKET_ACCEL;
		double y = Math.sin(orientation)*ROCKET_ACCEL;
		acceleration[0] += x;
		acceleration[1] += y;
		firingRocket = false;
	}

	public void externalForces(double x, double y) {
		acceleration[0] = x;
		acceleration[1] = y;		
	}

	@Override
	public void step(double t) {
		accelerate();
		velocity[0] += acceleration[0] * t;
		velocity[1] += acceleration[1] * t;
		
		position[0] += velocity[0] * t;
		position[1] += velocity[1] * t;
		
		position[0] = Math.abs((position[0] + 1200) % 800) - 400;
		position[1] = Math.abs((position[1] + 1200) % 800) - 400;
		
	}
	
	public Bullet[] getBullets()
	{
		return firedBullets; 
	}
	
	public boolean collision(int x, int y)
	{
		if(((x*x)/(400.0) + (y*y) / (81.0)) < 1)
			return true;
		return false;
		
	}

}

