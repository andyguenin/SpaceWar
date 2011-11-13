
public interface SWObject {
	
	public double getMass();
	
	public int[] getPosition();
	
	public void externalForces(double x, double y);
	
	public void step();
	


}
