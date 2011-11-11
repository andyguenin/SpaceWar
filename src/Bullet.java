
public class Bullet implements SWObject{

	
	private static final int LIFESPAN = 200;
	private int life;
	private int[] position;
	private double[] velocity;
	private double[] acceleration;
	private static final int MASS = 0;
	
	public Bullet(int x, int y, double vx, double vy)
	{
		position = new int[2];
		velocity = new double[2];
		acceleration = new double[2];
		
		position[0] = x;
		position[1] = y;
		velocity[0] = vx;
		velocity[1] = vy;
	}
	

	public void externalForces(double x, double y) {
		acceleration[0] = x;
		acceleration[1] = y;
		
	}


	public double getMass() {
		return MASS;
	}

	public int[] getPosition() {
		return position;
	}


	public void step(double t) {
		velocity[0] += acceleration[0] * t;
		velocity[1] += acceleration[1] * t;
		
		position[0] += velocity[0] * t;
		position[1] += velocity[1] * t;
		
	}
	
	public boolean isDead()
	{
		return life > LIFESPAN;
	}

}

