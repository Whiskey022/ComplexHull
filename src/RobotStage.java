
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class RobotStage {
	
	private static Obstacle[] obstacles;
	private static Point[] targetPoints;
	private static ArrayList<Point> upperBound;
	private static ArrayList<Point> lowerBound;
	
	public static void readFile(String fileName) throws IOException {
		BufferedReader file = new BufferedReader(new FileReader(fileName));
		StringTokenizer st = new StringTokenizer(file.readLine());
		
		//Get target points count
		int targetCount = Integer.parseInt(st.nextToken());
		targetPoints = new Point[targetCount];
		
		//Get target points coordinates
		for (int i=0; i<targetCount; i++) {
			st = new StringTokenizer(file.readLine());
			targetPoints[i] = new Point();
			targetPoints[i].x = Integer.parseInt(st.nextToken()); // Read X coordinate
			targetPoints[i].y = Integer.parseInt(st.nextToken()); // Read y coordinate
		}
		
		//Get obstacles count
		st = new StringTokenizer(file.readLine());
		int obstaclesCount = Integer.parseInt(st.nextToken());
		obstacles = new Obstacle[obstaclesCount];
		
		//Read each obstacle
		for (int i = 0; i < obstaclesCount; i++) {
			st = new StringTokenizer(file.readLine());
			
			//Get all coordinates for the obstacle
			Point[] p = new Point[Integer.parseInt(st.nextToken())];
			for (int j = 0; j < p.length; j++) {
				p[j] = new Point();
				p[j].x = Integer.parseInt(st.nextToken()); // Read X coordinate
				p[j].y = Integer.parseInt(st.nextToken()); // Read y coordinate
			}
			//Add points to the obstacles array
			obstacles[i] = new Obstacle(p);
		}
		
		file.close();
	}

	public static Obstacle[] getObstacles() {
		return obstacles;
	}
	
	public static Point[] getTargets() {
		return targetPoints;
	}
	
	public static Point[] getConvexHull(Point[] points) {
		Point[] hull = ConvexHull.convex_hull(points).clone();
		return hull;
	}
	
	public static void setBounds(Point[] hull, Point beginPoint, Point endPoint) {
		ArrayList<Point> upper = new ArrayList<Point>();
		ArrayList<Point> lower = new ArrayList<Point>();
		for (int i=0; i<hull.length; i++) {
			if (hull[i].x == beginPoint.x && hull[i].y == beginPoint.y) {
				for (int j = i; j < hull.length; j++) {
					upper.add(hull[j]);
					if (hull[j].x == endPoint.x && hull[j].y == endPoint.y) {
						break;
					}
				}
			}
			if (hull[i].x == endPoint.x && hull[i].y == endPoint.y) {
				for (int j = i; j < hull.length; j++) {
					lower.add(hull[j]);
				}
				lower.add(hull[0]);
			}
		}
		upperBound = upper;
		lowerBound = lower;
	}
	
	public static ArrayList<Point> getFastestPath(){
		if (getLength(lowerBound) < getLength(upperBound)) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	
	private static double getLength(ArrayList<Point> bound) {
		double length = 0.0;
		for (int i=0; i < bound.size() - 1; i++) {
			length += Math.sqrt((bound.get(i).x - bound.get(i+1).x)*(bound.get(i).x - bound.get(i+1).x)
					+ (bound.get(i).y - bound.get(i+1).y)*(bound.get(i).y - bound.get(i+1).y));
		}
		System.out.println(length);
		return length;
	}
}
