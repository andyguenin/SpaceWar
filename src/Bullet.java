
public class Bullet implements SWObject{

	
	private static final int LIFESPAN = 100;
	private int life;
	private double[] position;
	private double[] velocity;
	private double[] acceleration;
	private static final int MASS = 50;
	private double t;
	
	public Bullet(int x, int y, double vx, double vy, double fps)
	{
		position = new double[2];
		velocity = new double[2];
		acceleration = new double[2];
		
		position[0] = x;
		position[1] = y;
		velocity[0] = vx;
		velocity[1] = vy;
		t = 1/fps;
	}
	

	public void externalForces(double x, double y) {
		acceleration[0] = x;
		acceleration[1] = y;
		
	}


	public double getMass() {
		return MASS;
	}

	public int[] getPosition() {
		int[] ret = {(int)position[0], (int)position[1]};
		return ret;
	}


	public void step() {
		velocity[0] += acceleration[0] * t;
		velocity[1] += acceleration[1] * t;
		
		position[0] += velocity[0] * t;
		position[1] += velocity[1] * t;
		
		position[0] = Math.abs((position[0] + 1200) % 800) - 400;
		position[1] = Math.abs((position[1] + 1200) % 800) - 400;
		
		
		life++;
		
	}
	
	public boolean isDead()
	{
		return life > LIFESPAN;
	}

}

