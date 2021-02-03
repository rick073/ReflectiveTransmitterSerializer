
public class Object2
{
	public int x;
	public int y;
	public Object2 nextPoint;
	
	Object2(int inX, int inY, Object2 np)
	{
		x = inX;
		y = inY;
		nextPoint = np;
	}
	
	Object2()
	{
		nextPoint = null;
	}
}
