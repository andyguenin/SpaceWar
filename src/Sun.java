
public class Sun implements SWObject {
	
	double mass;
	int position[];
	
	public Sun(double mass, int x, int y)
	{
		this.mass = mass;
		position = new int[2];
		position[0] = x;
		position[1] = y;
	}


	public double getMass() {
		return mass;
	}

	public int[] getPosition() {
		
		return position;
	}

	public void externalForces(double x, double y) {
		return;		
	}


	@Override
	public void step(double t) {
		return;
	}

	
	public boolean collision(int x, int y)
	{
		if(((x*x)/(100) + (y*y) / (100)) < 1)
			return true;
		return false;
		
	}
}
