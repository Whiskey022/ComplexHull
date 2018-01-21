
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class Main extends Application {

	private Canvas canvas = new Canvas(500, 500);
	private Group canvasGroup = new Group();
	private GraphicsContext gc = canvas.getGraphicsContext2D();
	private Obstacle[] obstacles;
	private Point[] targetPoints;
	private Float radius = 2.0f;
	private double dotsDistance = 10;


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Convex hull task");

		BorderPane bp = new BorderPane();
		canvasGroup.getChildren().setAll(canvas);

		draw();

		bp.setCenter(canvasGroup);

		Scene scene = new Scene(bp, 800, 500);
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.show();

	}

	private void draw() throws IOException {
		//Read file
		RobotStage.readFile("task1.txt");
		
		//Get file data
		obstacles = RobotStage.getObstacles();
		targetPoints = RobotStage.getTargets();
		
		//Draw target points
		for (Point targetPoint : targetPoints) {
			drawTargetPoint(targetPoint);
		}
		
		//Draw obstacles
		for (Obstacle obstacle : obstacles) {
			drawObstacle(obstacle.points);
		}
		
		//Calculate and draw hull
		Point[] points = Stream.concat(Arrays.stream(targetPoints), Arrays.stream(obstacles[0].points))
                .toArray(Point[]::new);
		Point[] hull = RobotStage.getConvexHull(points);
		drawHull(hull);
		
		//Calculate fastest path and draw it
		RobotStage.setBounds(hull, targetPoints[0], targetPoints[1]);
		drawPath(RobotStage.getFastestPath());
	}
	
	private void drawTargetPoint(Point targetPoint) {
		Circle dot = new Circle (targetPoint.x * dotsDistance, targetPoint.y * dotsDistance, radius);
		dot.setFill(Color.BLUE);
		canvasGroup.getChildren().add(dot);
	}

	private void drawObstacle(Point[] obstacle) {
		Circle[] dots = createObstacleDots(obstacle);
		for (int i = 0; i < dots.length; i++) {
			Line line = new Line();
			line.startXProperty().bind(dots[i].centerXProperty().add(dots[i].translateXProperty()));
			line.startYProperty().bind(dots[i].centerYProperty().add(dots[i].translateYProperty()));
			if (i < dots.length - 1) {
				line.endXProperty().bind(dots[i + 1].centerXProperty().add(dots[i + 1].translateXProperty()));
				line.endYProperty().bind(dots[i + 1].centerYProperty().add(dots[i + 1].translateYProperty()));
			} else if (i == dots.length - 1) {
				line.endXProperty().bind(dots[0].centerXProperty().add(dots[0].translateXProperty()));
				line.endYProperty().bind(dots[0].centerYProperty().add(dots[0].translateYProperty()));
			}
			canvasGroup.getChildren().addAll(dots[i], line);
		}
	}
	
	private void drawHull(Point[] hull) {
		Circle[] dots = createHullDots(hull);
		for (int i = 0; i < dots.length; i++) {
			Line line = new Line();
			line.setStroke(Color.RED);
			line.startXProperty().bind(dots[i].centerXProperty().add(dots[i].translateXProperty()));
			line.startYProperty().bind(dots[i].centerYProperty().add(dots[i].translateYProperty()));
			if (i < dots.length - 1) {
				line.endXProperty().bind(dots[i + 1].centerXProperty().add(dots[i + 1].translateXProperty()));
				line.endYProperty().bind(dots[i + 1].centerYProperty().add(dots[i + 1].translateYProperty()));
			} else if (i == dots.length - 1) {
				line.endXProperty().bind(dots[0].centerXProperty().add(dots[0].translateXProperty()));
				line.endYProperty().bind(dots[0].centerYProperty().add(dots[0].translateYProperty()));
			}
			canvasGroup.getChildren().addAll(line);
		}
	}
	
	private void drawPath(ArrayList<Point> path) {
		Circle[] dots = createPathDots(path);
		for (int i = 0; i < dots.length - 1; i++) {
			Line line = new Line();
			line.setStroke(Color.GREEN);
			line.startXProperty().bind(dots[i].centerXProperty().add(dots[i].translateXProperty()));
			line.startYProperty().bind(dots[i].centerYProperty().add(dots[i].translateYProperty()));
			
			line.endXProperty().bind(dots[i + 1].centerXProperty().add(dots[i + 1].translateXProperty()));
			line.endYProperty().bind(dots[i + 1].centerYProperty().add(dots[i + 1].translateYProperty()));
			
			canvasGroup.getChildren().add(line);
		}
	}

	private Circle[] createObstacleDots(Point[] obstacle) {
		Circle[] dots = new Circle[obstacle.length];
		for (int i = 0; i < obstacle.length; i++) {
			dots[i] = new Circle(obstacle[i].x * dotsDistance, obstacle[i].y * dotsDistance, radius);
			dots[i].setFill(Color.BLACK);
		}
		return dots;
	}
	
	private Circle[] createHullDots(Point[] hull) {
		Circle[] dots = new Circle[hull.length];
		for (int i = 0; i < hull.length; i++) {
			dots[i] = new Circle(hull[i].x * dotsDistance, hull[i].y * dotsDistance, 0.5f);
			dots[i].setFill(Color.GREEN);
		}
		return dots;
	}
	
	private Circle[] createPathDots (ArrayList<Point> path) {
		Circle[] dots = new Circle[path.size()];
		for (int i = 0; i < path.size(); i++) {
			dots[i] = new Circle(path.get(i).x * dotsDistance, path.get(i).y * dotsDistance, 0.5f);
			dots[i].setFill(Color.GREEN);
		}
		return dots;
	}
}
