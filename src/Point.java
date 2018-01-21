
class Point implements Comparable<Point> {
		public int x, y;

		public int compareTo(Point p) {
			if (this.x == p.x) {
				return this.y - p.y;
			} else {
				return this.x - p.x;
			}
		}

		public String toString() {
			return "("+x + "," + y+")";
		}

	}